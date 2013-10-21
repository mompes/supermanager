package es.mompes.supermanager.util;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Representa una liga privada del supermanager.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public class Liga implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2014792269645821133L;
	private String nombre;
	private long codigo;

	/**
	 * Construye una nueva liga con un nombre y un cï¿½digo.
	 * 
	 * @param nnombre
	 *            El nombre de la liga.
	 * @param ncodigo
	 *            El cï¿½digo de la liga.
	 */
	public Liga(final String nnombre, final long ncodigo) {
		nombre = nnombre;
		codigo = ncodigo;
	}

	public long getCodigo() {
		return codigo;
	}

	public String getNombre() {
		return nombre;
	}

	private void readObject(ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		// always perform the default de-serialization first
		aInputStream.defaultReadObject();
	}

	private void writeObject(ObjectOutputStream aOutputStream)
			throws IOException {
		// perform the default serialization for all non-transient, non-static
		// fields
		aOutputStream.defaultWriteObject();
	}
}
