package es.mompes.supermanager.util;


/**
 * Representa los distintos tipos de clasificaci�n que se pueden seleccionar
 * para organizar distintas vistas.
 * 
 * @author Juan Mompe�n Esteban
 * 
 */
public enum EnumClasificacion {
	/**
	 * La vista de jornada.
	 */
	JORNADA,
	/**
	 * La vista de la clasificaci�n general.
	 */
	GENERAL,
	/**
	 * Clasificaci�n por broker.
	 */
	BROKERBASKET,
	/**
	 * Clasificaci�n por puntos anotados.
	 */
	ANOTACION,
	/**
	 * Clasificaci�n por asistencias.
	 */
	ASISTENCIAS,
	/**
	 * Clasificaci�n por rebotes.
	 */
	REBOTES,
	/**
	 * Clasificaci�n por el hist�rico de jornadas.
	 */
	HISTORICO,
	/**
	 * Clasificaci�n por triples.
	 */
	TRIPLES;

	@Override
	public String toString() {
		switch (this) {
		case JORNADA:
			return "Jornada";
		case GENERAL:
			return "General";
		case BROKERBASKET:
			return "BrokerBasket";
		case ANOTACION:
			return "Anotación";
		case ASISTENCIAS:
			return "Asistencias";
		case REBOTES:
			return "Rebotes";
		case HISTORICO:
			return "Histórico";
		case TRIPLES:
			return "Triples";
		default:
			return "";
		}
	}

	/**
	 * Devuelve una representaci�n apta para usar en los t�tulos de las columnas
	 * de una tabla.
	 * 
	 * @return T�tulos para columnas.
	 */
	public String toColumnsTitle() {
		switch (this) {
		case JORNADA:
			return "Puntos";
		case GENERAL:
			return "Puntos";
		case BROKERBASKET:
			return "Activo";
		case ANOTACION:
			return "Puntos";
		case ASISTENCIAS:
			return "Asistencias";
		case REBOTES:
			return "Rebotes";
		case HISTORICO:
			return "Puntos";
		case TRIPLES:
			return "Triples";
		default:
			return "";
		}
	}

	/**
	 * Devuelve una representaci�n para usar en columnas del enumerado.
	 * 
	 * @return Representaci�n del enumerado.
	 */
	public String toFirstColumn() {
		switch (this) {
		case HISTORICO:
			return "Jornada";
		default:
			return "Posición";
		}
	}

	/**
	 * Devuelve el valor del enumerado que se utiliza en la p�gina de las
	 * clasificaciones.
	 * 
	 * @return La representaci�n del valor del enumerado.
	 */
	public String toTopRadio() {
		switch (this) {
		case JORNADA:
			return "jornada";
		case GENERAL:
			return "posicion";
		case BROKERBASKET:
			return "posbroker";
		case ANOTACION:
			return "pospuntos";
		case ASISTENCIAS:
			return "posasistencias";
		case REBOTES:
			return "posrebotes";
		case HISTORICO:
			return "historico";
		case TRIPLES:
			return "postriples";
		default:
			return "";
		}
	}

	/**
	 * Devuelve el valor del enumerado que se utiliza en la p�gina de las ligas
	 * privadas.
	 * 
	 * @return La representaci�n del valor del enumerado.
	 */
	public String toPrivadasRadio() {
		switch (this) {
		case GENERAL:
			return "valoracion";
		case BROKERBASKET:
			return "broker";
		case ANOTACION:
			return "puntos";
		case ASISTENCIAS:
			return "asistencias";
		case REBOTES:
			return "rebotes";
		case HISTORICO:
			return "historico";
		case TRIPLES:
			return "triples";
		default:
			return "";
		}
	}

	/**
	 * Devuelve el equivalente en Enumzona del string introducido.
	 * 
	 * @param clasificacion
	 *            String a evaluar.
	 * @return El valor del enumerado equivalente o null si ninguno coincide.
	 */
	public static final EnumClasificacion stringToEnumClasificacion(
			final String clasificacion) {
		for (EnumClasificacion enumClasificacion : EnumClasificacion.values()) {
			if (enumClasificacion.toString().toUpperCase()
					.equals(clasificacion.toUpperCase())) {
				return enumClasificacion;
			}
		}
		return null;
	}
}
