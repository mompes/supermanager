package es.mompes.supermanager.basededatos;

import java.util.LinkedList;
import java.util.List;

import es.mompes.supermanager.util.Contenedor;

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
	public static final long createEntry(final String nnombre,
			final String nclave) {
		// Almacena los valores a insertar en un objeto
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, nnombre);
		cv.put(KEY_PASSWORD, nclave);
		// Inserta los nuevos datos en la base de datos
		return Contenedor.getInstance().baseDeDatos.database.insert(
				DATABASE_TABLE, null, cv);
	}

	/**
	 * Devuelve la contraseña del usuario consultado.
	 * 
	 * @param nombre
	 *            Usuario sobre el que se realiza la consulta.
	 * @return La contraseña del usuario o null si no se encuentra el usuario
	 *         indicado.
	 */
	public static final String getPassword(final String nombre) {
		String resultado = null;
		String[] columnas = new String[] { KEY_PASSWORD };
		Cursor c = Contenedor.getInstance().baseDeDatos.database.query(
				DATABASE_TABLE, columnas, KEY_NAME + "=?",
				new String[] { nombre }, null, null, null);
		if (c != null && c.moveToFirst()
				&& !c.isNull(c.getColumnIndex(KEY_PASSWORD))) {
			resultado = c.getString(c.getColumnIndex(KEY_PASSWORD));
			c.close();
		}
		return resultado;
	}

	/**
	 * Comprueba si una entrada ya está en la base de datos.
	 * 
	 * @param nombre
	 *            Nombre a comprobar.
	 * @return True si la entrada ya está y false en caso contrario.
	 */
	public static final boolean exists(final String nombre) {
		String[] columnas = new String[] { KEY_NAME };
		Cursor c = Contenedor.getInstance().baseDeDatos.database.query(
				DATABASE_TABLE, columnas, KEY_NAME + " =?",
				new String[] { nombre }, null, null, null);
		boolean resultado = c != null && c.getCount() > 0;
		c.close();
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
	public static final void updateEntry(final String nnombre,
			final String nclave) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_PASSWORD, nclave);
		Contenedor.getInstance().baseDeDatos.database.update(DATABASE_TABLE,
				cv, KEY_NAME + "=?", new String[] { nnombre });
	}

	/**
	 * Borra una entrada de la base de datos.
	 * 
	 * @param nnombre
	 *            Nombre de la entrada a borrar.
	 */
	public static final void deleteEntry(final String nnombre) {
		Contenedor.getInstance().baseDeDatos.database.delete(DATABASE_TABLE,
				KEY_NAME + "=?", new String[] { nnombre });
	}

	/**
	 * Devuelve una lista con los nombres de todos los usuarios en la base de
	 * datos.
	 * 
	 * @return Una lista con los nombres de todos los usuarios en la base de
	 *         datos.
	 */
	public static final List<String> getAll() {
		String[] columnas = new String[] { KEY_NAME };
		// Recibe todos los datos almacenados en la base de datos
		Cursor c = Contenedor.getInstance().baseDeDatos.database.query(
				DATABASE_TABLE, columnas, null, null, null, null, null);
		List<String> lista = new LinkedList<String>();
		int index = c.getColumnIndex(KEY_NAME);
		// Los almacena en una lista
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			lista.add(c.getString(index));
		}

		c.close();

		return lista;
	}
}
