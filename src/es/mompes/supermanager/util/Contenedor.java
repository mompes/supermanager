package es.mompes.supermanager.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
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
	public BaseDeDatos baseDeDatos = null;
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
	/**
	 * Contiene una lista con todas las actividades activas de la app. Esta
	 * lista es necesaria para el control de las bases de datos.
	 */
	public ArrayList<Activity> listaActivities = new ArrayList<Activity>();

	private static Contenedor contenedor;

	public static Contenedor getInstance() {
		if (contenedor == null) {
			contenedor = new Contenedor();
		}
		return contenedor;
	}

	public static void setInstance(final Contenedor ncontenedor) {
		contenedor = ncontenedor;
	}

	private void readObject(ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		this.acceso = (Acceso) aInputStream.readObject();
		this.equipos = (List<EquipoSupermanager>) aInputStream.readObject();
		this.mercado = (Mercado) aInputStream.readObject();
		this.listaActivities = new ArrayList<Activity>();
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
