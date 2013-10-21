package es.mompes.supermanager.util;


/**
 * Representa un equipo de la ACB. Se utiliza para la jornada virtual, d�nde es
 * interesante a�adir la puntuaci�n al equipo.
 * 
 * @author Juan Mompe�n Esteban
 * 
 */
public class EquipoACB extends Equipo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8187481995850466865L;
	/**
	 * Los puntos de un equipo en una jornada.
	 */
	private int puntuacion;

	/**
	 * Construye un nuevo equipo con un nombre.
	 * 
	 * @param nnombre
	 *            Nombre del equipo.
	 */
	public EquipoACB(final String nnombre) {
		super(nnombre);
		puntuacion = 0;
	}

	public final void setPuntuacion(final int npuntuacion) {
		puntuacion = npuntuacion;
	}

	public final int getPuntuacion() {
		return puntuacion;
	}

}
