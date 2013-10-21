package es.mompes.supermanager.basededatos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Esta clase aglutina cierta funcionalidad que todas las bases de datos deben
 * implementar.
 * 
 * @author Juan Mompe�n Esteban
 * 
 */
public class BaseDeDatos {

	/**
	 * El nombre de la base de datos d�nde se almacena toda la informaci�n.
	 */
	protected static final String DATABASE_NAME = "Supermanager";
	/**
	 * El contexto.
	 */
	protected Context context;
	/**
	 * La base de datos.
	 */
	public SQLiteDatabase database;
	/**
	 * Versi�n de la base de datos.
	 */
	private static final int DATABASE_VERSION = 1;
	/**
	 * Clase que ayuda con la gesti�n de la base de datos.
	 */
	private DbHelper helper;

	/**
	 * Facilita el trabajo con la base de datos.
	 * 
	 * @author Juan Mompe�n Esteban
	 * 
	 */
	static class DbHelper extends SQLiteOpenHelper {
		public DbHelper(final Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(final SQLiteDatabase db) {
			db.execSQL(TablaJugadores.CREATE_TABLE);
			db.execSQL(TablaJugadoresPuntuaciones.CREATE_TABLE);
			db.execSQL(TablaEquipos.CREATE_TABLE);
			db.execSQL(TablaEquiposPuntuaciones.CREATE_TABLE);
			db.execSQL(TablaUsuarios.CREATE_TABLE);

		}

		@Override
		public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
				final int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TablaJugadores.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS "
					+ TablaJugadoresPuntuaciones.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + TablaEquipos.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS "
					+ TablaEquiposPuntuaciones.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + TablaUsuarios.DATABASE_TABLE);
			this.onCreate(db);
		}
	}

	/**
	 * Construye una nueva base de datos.
	 * 
	 * @param ncontext
	 *            Contexto.
	 */
	public BaseDeDatos(final Context ncontext) {
		this.context = ncontext;
	}

	/**
	 * Abre la base de datos en modo escritura y devuelve una instancia de ella.
	 * 
	 * @return Una instancia de la base de datos.
	 */
	public final BaseDeDatos open() {
		this.helper = new DbHelper(this.context);
		this.database = this.helper.getWritableDatabase();
		return this;
	}

	/**
	 * Cierra la base de datos.
	 */
	public final void close() {
		this.database.close();
	}

	/**
	 * Consulta si la base de datos est� abierta.
	 * 
	 * @return true si la base de datos est� abierta, false en otro caso.
	 */
	public final Boolean isOpen() {
		return this.database != null && this.database.isOpen();
	}

	/**
	 * Recupera el valor de una columna en base a unas condiciones. Se compara
	 * el valor de una columna y el valor de otra debe ser el m�ximo de todas
	 * las coincidencias con la otra condici�n.
	 * 
	 * @param databaseTable
	 *            Nombre de la tabla a consultar.
	 * @param nombreColumna
	 *            Nombre de la columna a comparar.
	 * @param valorColumna
	 *            Valor de la columna a comparar.
	 * @param nombreColumnaMax
	 *            Columna cuyo valor debe ser el m�ximo.
	 * @param columnaConsultada
	 *            Columna consultada.
	 * @return Valor de la columna consultada o null si no hay coincidencias.
	 */
	protected final String getAlgo(final String databaseTable,
			final String nombreColumna, final String valorColumna,
			final String nombreColumnaMax, final String columnaConsultada) {
		String resultado = null;
		Cursor c = this.database.rawQuery("SELECT " + columnaConsultada
				+ ", MAX(" + nombreColumnaMax + ") FROM " + databaseTable
				+ " WHERE " + nombreColumna + "=?",
				new String[] { valorColumna });
		if (c == null) {
			return resultado;
		}
		if (c.moveToFirst()) {
			resultado = c.getString(c.getColumnIndex(columnaConsultada));
		}
		c.close();
		return resultado;
	}

	/**
	 * Recupera la columna consultada en funci�n de dos par�metros.
	 * 
	 * @param databaseTable
	 *            Tabla sobre la que se hace la consulta.
	 * @param nombreColumnaUno
	 *            Nombre de la primera columna sobre la que se hace la consulta.
	 * @param valorColumnaUno
	 *            Valor que se compara con la primera columna.
	 * @param nombreColumnaDos
	 *            Nombre de la segunda columna sobre la que se hace la consulta.
	 * @param valorColumnaDos
	 *            Valor que se compara con la segunda columna.
	 * @param columnaConsultada
	 *            Columna consultada.
	 * @return El valor de la columna consultada o null si no hay coincidencias.
	 */
	protected final String getAlgo(final String databaseTable,
			final String nombreColumnaUno, final String valorColumnaUno,
			final String nombreColumnaDos, final String valorColumnaDos,
			final String columnaConsultada) {
		String resultado = null;
		String[] columnas = new String[] { columnaConsultada };
		Cursor c = this.database.query(databaseTable, columnas,
				nombreColumnaUno + "=? " + " AND " + nombreColumnaDos + "=? ",
				new String[] { valorColumnaUno, valorColumnaDos }, null, null,
				null);
		if (c == null) {
			return resultado;
		}
		if (c.moveToFirst()) {
			resultado = c.getString(c.getColumnIndex(columnaConsultada));
		}
		c.close();
		return resultado;
	}
}