package es.mompes.supermanager.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class EquipoLiga extends Equipo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7124616986548595817L;
	private String usuario;
	private String valor;

	public EquipoLiga(final String nnombre, final String nusuario, final String nvalor) {
		super(nnombre);
		valor = nvalor;
		usuario = nusuario;
	}

	public String getValor() {
		return valor;
	}

	public String getUsuario() {
		return usuario;
	}

	@Override
	protected void readObject(ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		super.readObject(aInputStream);
	}

	@Override
	protected void writeObject(ObjectOutputStream aOutputStream)
			throws IOException {
		// TODO Auto-generated method stub
		super.writeObject(aOutputStream);
	}
}
