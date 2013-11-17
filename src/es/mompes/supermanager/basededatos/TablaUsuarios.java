package es.mompes.supermanager.basededatos;

import java.util.LinkedList;
import java.util.List;

import es.mompes.supermanager.util.Contenedor;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;

/**
 * Tabla de usuarios, contienen sus nicknames y contraseñas.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public class TablaUsuarios {

	/**
	 * El nombre de la columna que almacena los nombres.
	 */
	public static final String KEY_NAME = "Nombre";
	/**
	 * El nombre de la columna que almacena las claves.
	 */
	public static final String KEY_PASSWORD = "Clave";
	/**
	 * Nombre de la tabla en la base de datos.
	 */
	public static final String DATABASE_TABLE = "Usuarios";
	/**
	 * Comando de creacción de la tabla.
	 */
	public static final String CREATE_TABLE = "CREATE TABLE " + DATABASE_TABLE
			+ " (" + KEY_NAME + " TEXT PRIMARY KEY, " + KEY_PASSWORD
			+ " TEXT NOT NULL)";

	/**
	 * Inserta una nueva fila en la tabla de usuarios.
	 * 
	 * @param nnombre
	 *            El nuevo nombre.
	 * @param nclave
	 *            La nueva clave.
	 * @return El id de la fila insertada o -1 en caso de error.
	 */
	public static final long createEntry(BaseDeDatos bd, final String nnombre,
			final String nclave) {
		if (!bd.isOpen()) {
			bd.open();
		}
		// Almacena los valores a insertar en un objeto
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, nnombre);
		cv.put(KEY_PASSWORD, nclave);
		// Inserta los nuevos datos en la base de datos
		long resultado = bd.database.insert(DATABASE_TABLE, null, cv);
		bd.close();
		return resultado;
	}

	/**
	 * Devuelve la contraseña del usuario consultado.
	 * 
	 * @param nombre
	 *            Usuario sobre el que se realiza la consulta.
	 * @return La contraseña del usuario o null si no se encuentra el usuario
	 *         indicado.
	 */
	public static final String getPassword(BaseDeDatos bd, final String nombre) {
		if (!bd.isOpen()) {
			bd.open();
		}
		String resultado = null;
		String[] columnas = new String[] { KEY_PASSWORD };
		Cursor c = bd.database.query(DATABASE_TABLE, columnas, KEY_NAME + "=?",
				new String[] { nombre }, null, null, null);
		if (c != null && c.moveToFirst()
				&& !c.isNull(c.getColumnIndex(KEY_PASSWORD))) {
			resultado = c.getString(c.getColumnIndex(KEY_PASSWORD));
			c.close();
		}
		bd.close();
		return resultado;
	}

	/**
	 * Comprueba si una entrada ya está en la base de datos.
	 * 
	 * @param nombre
	 *            Nombre a comprobar.
	 * @return True si la entrada ya está y false en caso contrario.
	 */
	public static final boolean exists(BaseDeDatos bd, final String nombre) {
		if (!bd.isOpen()) {
			bd.open();
		}
		String[] columnas = new String[] { KEY_NAME };
		Cursor c = bd.database.query(DATABASE_TABLE, columnas,
				KEY_NAME + " =?", new String[] { nombre }, null, null, null);
		boolean resultado = c != null && c.getCount() > 0;
		c.close();
		bd.close();
		return resultado;
	}

	/**
	 * Actualiza el valor de una entrada de la base de datos.
	 * 
	 * @param nnombre
	 *            Nombre a actualizar.
	 * @param nclave
	 *            Clave a actualizar.
	 */
	public static final void updateEntry(BaseDeDatos bd, final String nnombre,
			final String nclave) {
		if (!bd.isOpen()) {
			bd.open();
		}
		ContentValues cv = new ContentValues();
		cv.put(KEY_PASSWORD, nclave);
		bd.database.update(DATABASE_TABLE, cv, KEY_NAME + "=?",
				new String[] { nnombre });
		bd.close();
	}

	/**
	 * Borra una entrada de la base de datos.
	 * 
	 * @param nnombre
	 *            Nombre de la entrada a borrar.
	 */
	public static final void deleteEntry(BaseDeDatos bd, final String nnombre) {
		if (!bd.isOpen()) {
			bd.open();
		}
		bd.database.delete(DATABASE_TABLE, KEY_NAME + "=?",
				new String[] { nnombre });
		bd.close();
	}

	/**
	 * Devuelve una lista con los nombres de todos los usuarios en la base de
	 * datos.
	 * 
	 * @return Una lista con los nombres de todos los usuarios en la base de
	 *         datos.
	 */
	public static final List<String> getAll(BaseDeDatos bd) {
		if (!bd.isOpen()) {
			bd.open();
		}
		String[] columnas = new String[] { KEY_NAME };
		// Recibe todos los datos almacenados en la base de datos
		Cursor c = bd.database.query(DATABASE_TABLE, columnas, null, null,
				null, null, null);
		List<String> lista = new LinkedList<String>();
		int index = c.getColumnIndex(KEY_NAME);
		// Los almacena en una lista
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			lista.add(c.getString(index));
		}

		c.close();
		bd.close();
		return lista;
	}
}
