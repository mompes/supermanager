package es.mompes.supermanager.util;

import java.io.Serializable;

/**
 * Clase que contiene un comentario de una liga privada.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public class Comentario implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 805973846601246131L;
	private String texto;
	private String usuario;
	private String fecha;

	public Comentario(String texto, String usuario, String fecha) {
		super();
		this.texto = texto;
		this.usuario = usuario;
		this.fecha = fecha;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
}
