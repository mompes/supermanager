package es.mompes.supermanager.util;


/**
 * Representa las distintas zonas en las que se pueden consultar las
 * clasificaciones.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public enum EnumZona {
	/**
	 * Todo el juego.
	 */
	ABSOLUTO,
	/**
	 * Sï¿½lo tu provincia.
	 */
	PROVINCIAL,
	/**
	 * Sï¿½lo tu localidad.
	 */
	LOCAL;

	@Override
	public String toString() {
		switch (this) {
		case ABSOLUTO:
			return "Absoluto";
		case PROVINCIAL:
			return "Provincial";
		case LOCAL:
			return "Local";
		default:
			return "";
		}
	}

	/**
	 * Devuelve el valor del enumerado que se utiliza en la pï¿½gina de las
	 * clasificaciones.
	 * 
	 * @return La representaciï¿½n del valor del enumerado.
	 */
	public final String toTopRadio() {
		switch (this) {
		case ABSOLUTO:
			return "Absoluta";
		case PROVINCIAL:
			return "Provincial";
		case LOCAL:
			return "Local";
		default:
			return "";
		}
	}

	/**
	 * Devuelve el equivalente en Enumzona del string introducido.
	 * 
	 * @param zona
	 *            String a evaluar.
	 * @return El valor del enumerado equivalente o null si ninguno coincide.
	 */
	public static final EnumZona stringToEnumZona(final String zona) {
		for (EnumZona enumZona : EnumZona.values()) {
			if (enumZona.toString().toUpperCase().equals(zona.toUpperCase())) {
				return enumZona;
			}
		}
		return null;
	}
}
