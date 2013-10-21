package es.mompes.supermanager.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import es.mompes.supermanager.webservice.WebService;

/**
 * Maneja la información de login y hace el login en el supermanager.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public class Acceso implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1101450454641897811L;
	/**
	 * Nombre del campo del nombre de usuario en la página html.
	 */
	private static final String USER = "usuario";
	/**
	 * Nombre del campo de la contraseña en la página html.
	 */
	private static final String PASSWORD = "clave";
	/**
	 * Nombre del campo de control en la página html.
	 */
	private static final String CONTROL = "control";
	/**
	 * String que aparece en la página de error devuelta por el servidor.
	 */
	private final static String ERROR_LOGIN = "No existe ningún socio con ese nombre de usuario";
	/**
	 * String que aparece en el supermanager cuando está cerrado por los
	 * cálculos previos a la jornada.
	 */
	private final static String ERROR_CERRADO = "SUPERMANAGER CERRADO TEMPORALMENTE";
	/**
	 * User dónde se contienen el nombre de usuario y la contraseña.
	 */
	private Usuario usuario;
	/**
	 * Jornada que se disputó la semana pasada.
	 */
	private String jornada;
	/**
	 * Indica si el usuario se conectado haciendo login en la página del
	 * supermanager.
	 */
	private boolean conectado;

	private void readObject(ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		String nick = (String) aInputStream.readObject();
		String clave = (String) aInputStream.readObject();
		usuario = new Usuario(nick, clave);
		jornada = (String) aInputStream.readObject();
		conectado = aInputStream.readBoolean();
	}

	private void writeObject(ObjectOutputStream aOutputStream)
			throws IOException {
		aOutputStream.writeObject(usuario.getNick());
		aOutputStream.writeObject(usuario.getClave());
		aOutputStream.writeObject(jornada);
		aOutputStream.writeBoolean(false);
		aOutputStream.flush();
		aOutputStream.close();
	}

	/**
	 * Construye un nuevo objeto Access.
	 * 
	 * @param nnombre
	 *            El nombre del usuario que se va a conectar.
	 * @param nclave
	 *            La contraseña del usuario que se va a conectar.
	 */
	public Acceso(final String nnombre, final String nclave) {
		this.setUsuario(new Usuario(nnombre, nclave));
		this.conectado = false;
		this.jornada = "-1";
	}

	/**
	 * Se conecta al supermanager.
	 * 
	 * @return True si el login fue correcto y false en caso contrario.
	 */
	public final EnumResultadoLogin connect() {
		this.conectado = false;
		List<NameValuePair> parametros = new LinkedList<NameValuePair>();
		// El resultado del login a devolver
		EnumResultadoLogin resultado = EnumResultadoLogin.ERROR_INTERNET;
		try {
			/*
			 * WebService .getPaginaLogueado(
			 * Configuration.getInstance().getProperty( Configuration.LOGINURL),
			 * parametros);
			 */
			parametros.add(new BasicNameValuePair(Acceso.USER, getUsuario()
					.getNick()));
			parametros.add(new BasicNameValuePair(Acceso.PASSWORD, getUsuario()
					.getClave()));
			parametros.add(new BasicNameValuePair(Acceso.CONTROL, "1"));
			String pagina = WebService.getPaginaLogueado(Configuration
					.getInstance().getProperty(Configuration.LOGINURL),
					parametros);
			// Comprueba si el login se ha llevado a cabo satisfactoriamente
			if (pagina.contains(Acceso.ERROR_LOGIN)) {
				resultado = EnumResultadoLogin.ERROR_DATOS;
			} else if (pagina.contains(Acceso.ERROR_CERRADO)) {
				resultado = EnumResultadoLogin.CERRADO;
			} else {
				this.establecerJornada(pagina);
				this.conectado = true;
				resultado = EnumResultadoLogin.CORRECTO;
			}
		} catch (GetPageException e) {
			resultado = EnumResultadoLogin.ERROR_INTERNET;
		}
		return resultado;
	}

	private void establecerJornada(final String codigo) {
		int index = codigo.indexOf("Jornada ");
		index += "Jornada ".length();
		this.setJornada("");
		while (codigo.charAt(index) != '<') {
			this.setJornada(this.getJornada() + codigo.charAt(index));
			index++;
		}
	}

	/**
	 * @return El usuario
	 */
	public final Usuario getUsuario() {
		return this.usuario;
	}

	/**
	 * @param nusuario
	 *            El nuevo usuario.
	 */
	public final void setUsuario(final Usuario nusuario) {
		this.usuario = nusuario;
	}

	/**
	 * @return La jornada.
	 */
	public final String getJornada() {
		return this.jornada;
	}

	/**
	 * @param njornada
	 *            La nueva jornada.
	 */
	private void setJornada(final String njornada) {
		this.jornada = njornada;
	}

	/**
	 * @return True si está conectado al supermanager y false en caso contrario.
	 */
	public final boolean isConectado() {
		return conectado;
	}
}
