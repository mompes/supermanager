package es.mompes.supermanager.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import es.mompes.supermanager.basededatos.BaseDeDatos;

/**
 * Aquï¿½ se almacenan todos los objetos que necesitan ser compartidos entre
 * diferentes clases, activities de la app.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public class Contenedor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5485983888000234530L;
	/**
	 * La base de datos.
	 */
	private BaseDeDatos baseDeDatos = null;
	/**
	 * Contiene el login al supermanager y permite la navegaciï¿½n por el mismo.
	 */
	public Acceso acceso = null;
	/**
	 * Una lista de todos nuestros equipos.
	 */
	public List<EquipoSupermanager> equipos = new LinkedList<EquipoSupermanager>();
	/**
	 * El mercado.
	 */
	public Mercado mercado = new Mercado();

	private static Contenedor contenedor;

	public static Contenedor getInstance() {
		if (contenedor == null) {
			contenedor = new Contenedor();
		}
		return contenedor;
	}

	public BaseDeDatos getBaseDeDatos(Activity activity) {
		if (baseDeDatos == null) {
			baseDeDatos = new BaseDeDatos(activity);
		}
		return baseDeDatos;
	}

	public static void setInstance(final Contenedor ncontenedor) {
		contenedor = ncontenedor;
	}

	private void readObject(ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		this.acceso = (Acceso) aInputStream.readObject();
		this.equipos = (List<EquipoSupermanager>) aInputStream.readObject();
		this.mercado = (Mercado) aInputStream.readObject();
	}

	private void writeObject(ObjectOutputStream aOutputStream)
			throws IOException {
		aOutputStream.writeObject(this.acceso);
		aOutputStream.writeObject(this.equipos);
		aOutputStream.writeObject(this.mercado);
		aOutputStream.flush();
		aOutputStream.close();
	}
}
