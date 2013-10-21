package es.mompes.supermanager.util;

import java.io.IOException;
import java.util.Properties;

/**
 * 
 * @author Juan Mompeán Esteban
 */
public class Configuration {

	Properties properties = null;

	/** Configuration file name. */
	public final static String CONFIG_FILE_NAME = "Configuration.properties";

	/** URL del mercado de los bases. */
	public static final String BASESURL = "bases";
	/** URL del mercado de los aleros. */
	public static final String ALEROSURL = "aleros";
	/** URL del mercado de los pï¿½vots. */
	public static final String PIVOTSURL = "pivots";
	/** URL del mercado de los bases. */
	public static final String BASESURLC = "basesC";
	/** URL del mercado de los aleros. */
	public static final String ALEROSURLC = "alerosC";
	/** URL del mercado de los pï¿½vots. */
	public static final String PIVOTSURLC = "pivotsC";
	/** URL para hacer el login. */
	public static final String LOGINURL = "login";
	/** URL a los equipos. */
	public static final String EQUIPOSURL = "equipos";
	/** URL a las clasificaciones. */
	public static final String CLASIFICACIONESURL = "clasificaciones";
	/** URL a las ligas privadas. */
	public static final String PRIVADASURL = "privadas";
	/** URL a una liga privada. */
	public static final String PRIVADAURL = "privada";
	/** URL a un equipo. */
	public static final String EQUIPO = "equipo";
	/** URL a la jornada virtual. */
	public static final String VIRTUALMATCHES = "virtualMatches";
	/** URL a las estadï¿½sticas de la jornada virtual */
	public static final String VIRTUALSTATISTICS = "virtualStatistics";
	/** Primera parte de la URL de las estadï¿½sticas de un jugador. */
	public static final String JUGADORESTADISTICAS1 = "jugadorEstadisticas1";
	/** Segunda parte de la URL de las estadï¿½sticas de un jugador. */
	public static final String JUGADORESTADISTICAS2 = "jugadorEstadisticas2";
	/** URL para vender un jugador. */
	public static final String VENDER1 = "vender1";
	/** URL para vender un jugador. */
	public static final String VENDER2 = "vender2";
	/** URL para anular la venta de un jugador. */
	public static final String ANULAR1 = "anular1";
	/** URL para anular la venta de un jugador. */
	public static final String ANULAR2 = "anular2";
	/** URL para anular la venta de un jugador. Esta es la segunda fase. */
	public static final String ANULAR3 = "anular3";
	/** URL para fichar a un jugador. */
	public static final String FICHAR1 = "fichar1";
	/** URL para fichar a un jugador. */
	public static final String FICHAR2 = "fichar2";
	/** URL para finalizar el fichaje de un jugador. */
	public static final String MERCADO = "mercado";
	/** URL para sustituir a un jugador. */
	public static final String SUSTITUIR1 = "sustituir1";
	/** URL para sustituir a un jugador. */
	public static final String SUSTITUIR2 = "sustituir2";
	/** URL para anular todos los cambios. */
	public static final String ANULARTODOS = "anularTodos";
	/** URL para anular todos los cambios fase 2. */
	public static final String ANULARTODOS2 = "anularTodos2";
	/** URL para ver los resultados de los equipos. */
	public static final String RESULTADOSEQUIPOS1 = "resultadosEquipos1";
	public static final String RESULTADOSEQUIPOS2 = "resultadosEquipos2";
	public static final String RESULTADOSEQUIPOS3 = "resultadosEquipos3";
	/** URL para nuevo equipo */
	public static final String NUEVO_EQUIPO = "crearEquipo";
	/** URL para acceder a los comentarios de una liga privada */
	public static final String COMENTARIOS_LIGA = "comentariosLiga";
	/** URL para aÃ±adir un comentario a una liga privada */
	public static final String AÑADIR_COMENTARIO_LIGA = "añadirComentario";

	private Configuration() {
		this.properties = new Properties();
		try {
			properties.load(Configuration.class.getClassLoader()
					.getResourceAsStream(CONFIG_FILE_NAME));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}// Configuration

	/**
	 * Implementando Singleton
	 * 
	 * @return
	 */
	public static Configuration getInstance() {
		return ConfigurationHolder.INSTANCE;
	}

	private static class ConfigurationHolder {

		private static final Configuration INSTANCE = new Configuration();
	}

	/**
	 * Retorna la propiedad de configuraciï¿½n solicitada
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return this.properties.getProperty(key);
	}// getProperty
}