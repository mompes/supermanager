package es.mompes.supermanager.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

/**
 * Un equipo de baloncesto.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public class Equipo extends Observable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5940526194275833888L;
	/**
	 * Nï¿½mero mï¿½ximo de jugadores en un equipo.
	 */
	public static final int MAXIMUM_NUMBER_OF_PLAYERS = 11;
	/**
	 * Nombre del equipo.
	 */
	private String nombre;
	/**
	 * Array de jugadores.
	 */
	protected List<SupermanagerPlayer> jugadores;

	/**
	 * Construye un nuevo equipo.
	 * 
	 * @param nnombre
	 *            NOmbre del equipo.
	 */
	public Equipo(final String nnombre) {
		nombre = nnombre;
		jugadores = new LinkedList<SupermanagerPlayer>();
	}

	/**
	 * Recupera los jugadores del equipo.
	 * 
	 * @return Una lista con los jugadores del equipo.
	 */
	public final List<SupermanagerPlayer> getJugadores() {
		return this.jugadores;
	}

	public void setJugadores(List<SupermanagerPlayer> jugadores) {
		this.jugadores = jugadores;
	}

	/**
	 * Una lista con los jugadores del equipo que juegan en una posiciï¿½n
	 * concreta.
	 * 
	 * @param posicion
	 *            La posiciï¿½n de la que se quieren los jugadores.
	 * @return Los jugadores del equipo en esa posiciï¿½n.
	 */
	public final List<SupermanagerPlayer> getJugadores(
			final EnumPosicion posicion) {
		LinkedList<SupermanagerPlayer> list = new LinkedList<SupermanagerPlayer>();
		for (int i = 0; i < this.jugadores.size(); i++) {
			if (jugadores.get(i) != null
					&& jugadores.get(i).getPosicion() == posicion) {
				list.add(jugadores.get(i));
			}
		}
		return list;
	}

	/**
	 * El nï¿½mero de jugadores del equipo.
	 * 
	 * @return El nï¿½mero de jugadores del equipo.
	 */
	public final int getNumeroDeJugadores() {
		int contador = 0;
		for (int i = 0; i < jugadores.size(); i++) {
			if (jugadores.get(i) != null && !jugadores.get(i).getLibre()) {
				contador++;
			}
		}
		return contador;
	}

	/**
	 * El nombre del equipo.
	 * 
	 * @return El nombre del equipo.
	 */
	public final String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Aï¿½ade un jugador al equipo.
	 * 
	 * @param njugador
	 *            El nuevo jugador a aï¿½adir.
	 * 
	 */
	public final void addJugador(final SupermanagerPlayer njugador) {
		jugadores.add(njugador);
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * Elimina un jugador del equipo.
	 * 
	 * @param jugador
	 *            El jugador a eliminar.
	 */
	public final void removeJugador(final SupermanagerPlayer jugador) {

		for (int i = 0; i < jugadores.size(); i++) {
			if (jugadores.get(i) != null && jugador.equals(jugadores.get(i))) {
				this.jugadores.remove(i);
			}
		}
		this.setChanged();
		this.notifyObservers();
	}
	
	public final void replaceJugador(final int posicion, final SupermanagerPlayer jugador) {
		jugadores.remove(posicion);
		jugadores.add(posicion, jugador);
		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public String toString() {
		String team = "Name: " + nombre;
		StringBuilder teamBuilder = new StringBuilder();
		teamBuilder.append(team);
		for (int i = 0; i < this.jugadores.size(); i++) {
			if (jugadores.get(i) != null) {
				teamBuilder.append("\n" + jugadores.get(i).toString());
			}
		}
		return teamBuilder.toString();
	}

	/**
	 * Transforma el nombre largo de un equipo en un nombre corto. Este mï¿½todo
	 * es un mï¿½todo auxiliar necesario para contrastar datos obtenidos de la
	 * pï¿½gina de la ACB.
	 * 
	 * @param nombreLargo
	 *            El nombre largo del equipo.
	 * @return La abreviatura equivalente. Si no se encuentra el nombre del
	 *         equipo devuelve "".
	 */
	public static final String nombreCorto(final String nombreLargo) {
		if (nombreLargo.equals("MAD-CROC FUENLABRADA")) {
			return "MCF";
		} else if (nombreLargo.equals("FC BARCELONA REGAL")) {
			return "FCB";
		} else if (nombreLargo.equals("FIATC MUTUA JOVENTUT")) {
			return "FIATC";
		} else if (nombreLargo.equals("ASEFA ESTUDIANTES")) {
			return "ASE";
		} else if (nombreLargo.equals("GESCRAP BIZKAIA")) {
			return "GBB";
		} else if (nombreLargo.equals("UCAM MURCIA")) {
			return "UCAM";
		} else if (nombreLargo.equals("BANCA CIVICA")) {
			return "BCS";
		} else if (nombreLargo.equals("ASSIGNIA MANRESA")) {
			return "ASS";
		} else if (nombreLargo.equals("GRAN CANARIA 2014")) {
			return "GCA";
		} else if (nombreLargo.equals("REAL MADRID")) {
			return "RMA";
		} else if (nombreLargo.equals("BLUSENS MONBUS")) {
			return "BSM";
		} else if (nombreLargo.equals("LUCENTUM ALICANTE")) {
			return "ALI";
		} else if (nombreLargo.equals("BLANCOS DE RUEDA VALLADOLID")) {
			return "BRV";
		} else if (nombreLargo.equals("CAJA LABORAL")) {
			return "CLA";
		} else if (nombreLargo.equals("VALENCIA BASKET")) {
			return "VBC";
		} else if (nombreLargo.equals("UNICAJA")) {
			return "UNI";
		} else if (nombreLargo.equals("CAI ZARAGOZA")) {
			return "CAI";
		} else if (nombreLargo.equals("LAGUN ARO GBC")) {
			return "GBC";
		}
		return "";
	}

	/**
	 * Transforma la abreviatura de un equipo en el nombre largo.
	 * 
	 * @param nombreCorto
	 *            El nombre corto del equipo.
	 * @return Un array con los diferentes nombres largos que ha tenido el
	 *         equipo a lo largo de la temporada. Si no se encuentra el equipo
	 *         devuelve un array de longitud 0.
	 */
	public static final String[] nombreLargo(final String nombreCorto) {
		String[] nombres = new String[0];
		if (nombreCorto.equals("MCF")) {
			nombres = new String[2];
			nombres[0] = "Baloncesto Fuenlabrada";
			nombres[1] = "Mad-Croc Fuenlabrada";
			return nombres;
		} else if (nombreCorto.equals("FCB")) {
			nombres = new String[1];
			nombres[0] = "FC Barcelona Regal";
			return nombres;
		} else if (nombreCorto.equals("FIATC")) {
			nombres = new String[1];
			nombres[0] = "FIATC Mutua Joventut";
			return nombres;
		} else if (nombreCorto.equals("ASE")) {
			nombres = new String[1];
			nombres[0] = "Asefa Estudiantes";
			return nombres;
		} else if (nombreCorto.equals("GBB")) {
			nombres = new String[1];
			nombres[0] = "Gescrap Bizkaia";
			return nombres;
		} else if (nombreCorto.equals("UCAM")) {
			nombres = new String[1];
			nombres[0] = "UCAM Murcia";
			return nombres;
		} else if (nombreCorto.equals("BCS")) {
			nombres = new String[2];
			nombres[0] = "Cajasol Banca Civica";
			nombres[1] = "Banca Civica";
			return nombres;
		} else if (nombreCorto.equals("ASS")) {
			nombres = new String[1];
			nombres[0] = "Assignia Manresa";
			return nombres;
		} else if (nombreCorto.equals("GCA")) {
			nombres = new String[1];
			nombres[0] = "Gran Canaria 2014";
			return nombres;
		} else if (nombreCorto.equals("RMA")) {
			nombres = new String[1];
			nombres[0] = "Real Madrid";
			return nombres;
		} else if (nombreCorto.equals("BSM")) {
			nombres = new String[1];
			nombres[0] = "Blusens Monbus";
			return nombres;
		} else if (nombreCorto.equals("ALI")) {
			nombres = new String[1];
			nombres[0] = "Lucentum Alicante";
			return nombres;
		} else if (nombreCorto.equals("BRV")) {
			nombres = new String[1];
			nombres[0] = "Blancos de Rueda Valladolid";
			return nombres;
		} else if (nombreCorto.equals("CLA")) {
			nombres = new String[1];
			nombres[0] = "Caja Laboral";
			return nombres;
		} else if (nombreCorto.equals("VBC")) {
			nombres = new String[1];
			nombres[0] = "Valencia Basket";
			return nombres;
		} else if (nombreCorto.equals("UNI")) {
			nombres = new String[1];
			nombres[0] = "Unicaja";
			return nombres;
		} else if (nombreCorto.equals("CAI")) {
			nombres = new String[1];
			nombres[0] = "CAI Zaragoza";
			return nombres;
		} else if (nombreCorto.equals("GBC")) {
			nombres = new String[1];
			nombres[0] = "Lagun Aro GBC";
			return nombres;
		}
		return nombres;
	}

	/**
	 * Busca un jugador con el nombre indicado en el equipo.
	 * 
	 * @param nombre
	 *            El nombre del jugador a buscar.
	 * @return El jugador solicitado o null si no se encuentra.
	 */
	public final SupermanagerPlayer getJugador(final String nombre) {
		for (int i = 0; i < getNumeroDeJugadores(); i++) {
			int coma = jugadores.get(i).getNombre().indexOf(',');
			int coma2 = nombre.indexOf(',');
			if (jugadores
					.get(i)
					.getNombre()
					.substring(
							0,
							(coma > 0) ? coma : jugadores.get(i).getNombre()
									.length())
					.toUpperCase()
					.equals(nombre.substring(0,
							(coma2 > 0) ? coma2 : nombre.length())
							.toUpperCase())) {
				return jugadores.get(i);
			}
		}
		return null;
	}

	protected void readObject(final ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		// always perform the default de-serialization first
		aInputStream.defaultReadObject();
	}

	protected void writeObject(final ObjectOutputStream aOutputStream)
			throws IOException {
		// perform the default serialization for all non-transient, non-static
		// fields
		aOutputStream.defaultWriteObject();
	}
}
