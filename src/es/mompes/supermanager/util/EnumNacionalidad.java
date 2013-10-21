package es.mompes.supermanager.util;


/**
 * Representa la nacionalidad de los jugadores.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public enum EnumNacionalidad {
	/**
	 * Jugadores de fuera de Europa.
	 */
	EXTRACOMUNITARIO,
	/**
	 * Jugadores europeos.
	 */
	COMUNITARIO,
	/**
	 * Jugadores con pasaporte espaï¿½ol.
	 */
	ESPAÑOL;
	/**
	 * El mï¿½ximo de jugadores extracomunitarios que se puede tener.
	 */
	public static int MAX_EXTRACOMUNITARIO = 2;
	/**
	 * El mï¿½nimo de jugadores espaï¿½oles que se puede tener.
	 */
	public static int MIN_ESPAÑOLES = 4;

	@Override
	public String toString() {
		switch (this) {
		case EXTRACOMUNITARIO:
			return "Extracomunitario";
		case COMUNITARIO:
			return "Comunitario";
		case ESPAÑOL:
			return "Espaï¿½ol";
		default:
			return "";
		}
	}

	public static EnumNacionalidad fromStringToNacionalidad(String nacionalidad) {
		for (EnumNacionalidad enumNacionalidad : EnumNacionalidad.values()) {
			if (enumNacionalidad.toString().toUpperCase()
					.equals(nacionalidad.toUpperCase())) {
				return enumNacionalidad;
			}
		}
		return null;
	}

	public static EnumNacionalidad fromStringHTMLToNacionalidad(
			String nacionalidad) {
		if (nacionalidad.equals("Jugador Extracomunitario")) {
			return EXTRACOMUNITARIO;
		} else if (nacionalidad.equals("Jugador Seleccionable")
				|| nacionalidad.equals("Jugador Nacional")) {
			return ESPAÑOL;
		} else if (nacionalidad.equals("")) {
			return COMUNITARIO;
		}
		return null;
	}

}
