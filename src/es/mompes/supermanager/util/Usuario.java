package es.mompes.supermanager.util;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Representa a un usuario del supermanager con un nombre de usuario y una
 * clave.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public class Usuario implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2959978002694788756L;
	private String nick, clave;

	/**
	 * Construye un nuevo usuario con los datos indicados.
	 * 
	 * @param nnick
	 *            Nombre de usuario.
	 * @param nclave
	 *            Clave del usuario.
	 */
	public Usuario(final String nnick, final String nclave) {
		nick = nnick;
		clave = nclave;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(final String nnick) {
		this.nick = nnick;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(final String nclave) {
		this.clave = nclave;
	}

	private void readObject(final ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		// always perform the default de-serialization first
		aInputStream.defaultReadObject();
	}

	private void writeObject(final ObjectOutputStream aOutputStream)
			throws IOException {
		// perform the default serialization for all non-transient, non-static
		// fields
		aOutputStream.defaultWriteObject();
	}
}
