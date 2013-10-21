package es.mompes.supermanager.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementa la funcionalidad necesaria para gestionar el mercado.
 * 
 * @author Juan Mompe�n Esteban
 * 
 */
public class Mercado implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5609286500292122040L;
	/**
	 * Contiene a los bases del mercado.
	 */
	public List<SupermanagerPlayer> bases;
	/**
	 * Contiene a los escoltas del mercado.
	 */
	public List<SupermanagerPlayer> aleros;
	/**
	 * Contiene a los p�vots del mercado.
	 */
	public List<SupermanagerPlayer> pivots;

	/**
	 * Construye un nuevo mercado.
	 */
	public Mercado() {
		this.bases = new LinkedList<SupermanagerPlayer>();
		this.aleros = new LinkedList<SupermanagerPlayer>();
		this.pivots = new LinkedList<SupermanagerPlayer>();
	}

	/**
	 * Asigna la List de jugadores a la posici�n indicada.
	 * 
	 * @param posicion
	 *            Posici�n en la que se asignar�n los jugadores.
	 * @param jugadores
	 *            Jugadores a a�adir.
	 */
	public final void rellenarMercado(final EnumPosicion posicion,
			final List<SupermanagerPlayer> jugadores) {
		switch (posicion) {
		case BASE:
			this.bases = new LinkedList<SupermanagerPlayer>(jugadores);
			break;
		case ALERO:
			this.aleros = new LinkedList<SupermanagerPlayer>(jugadores);
			break;
		case PIVOT:
			this.pivots = new LinkedList<SupermanagerPlayer>(jugadores);
			break;
		default:
			break;
		}
	}

	/**
	 * Devuelve una List con los jugadores de la posici�n indicada o null si la
	 * posici�n no existe. Si no se ha rellenado previamente esa posici�n el
	 * resultado ser� una List vac�a.
	 * 
	 * @param posicion
	 *            La posici�n de la que se piden los jugadores.
	 * @return Una List con jugadores.
	 */
	public final List<SupermanagerPlayer> getJugadores(EnumPosicion posicion) {
		List<SupermanagerPlayer> List = new LinkedList<SupermanagerPlayer>();
		switch (posicion) {
		case BASE:
			List.addAll(this.bases);
			break;
		case ALERO:
			List.addAll(this.aleros);
			break;
		case PIVOT:
			List.addAll(this.pivots);
			break;
		default:
			List = null;
		}
		return List;
	}

	/**
	 * 
	 * @return Una List con los bases del mercado.
	 */
	public final List<SupermanagerPlayer> getBases() {
		return this.bases;
	}

	/**
	 * 
	 * @return Una List con los aleros del mercado.
	 */
	public final List<SupermanagerPlayer> getAleros() {
		return this.aleros;
	}

	/**
	 * 
	 * @return Una List con los pivots del mercado.
	 */
	public final List<SupermanagerPlayer> getPivots() {
		return this.pivots;
	}

	/**
	 * 
	 * @return Comprueba si el mercado est� vac�o. Ser� false siempre que alguna
	 *         de las Lists de jugadores est� vac�a y solamente true cuando
	 *         todas las Lists contengan informaci�n.
	 */
	public final boolean isEmpty() {
		return this.bases.isEmpty() && this.aleros.isEmpty()
				&& this.pivots.isEmpty();
	}

	/**
	 * Ordena las Lists de jugadores en funci�n del precio de los mismos.
	 */
	public final void ordenar() {
		Comparator<SupermanagerPlayer> comparador = new Comparator<SupermanagerPlayer>() {
			public int compare(SupermanagerPlayer jugador1,
					SupermanagerPlayer jugador2) {
				if (jugador1.getPrecio() < jugador2.getPrecio()) {
					return 1;
				} else if (jugador1.getPrecio() > jugador2.getPrecio()) {
					return -1;
				} else {
					return 0;
				}
			}
		};
		Collections.sort(this.bases, comparador);
		Collections.sort(this.aleros, comparador);
		Collections.sort(this.pivots, comparador);
	}

	private void readObject(ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		this.bases = (List<SupermanagerPlayer>) aInputStream.readObject();
		this.aleros = (List<SupermanagerPlayer>) aInputStream.readObject();
		this.pivots = (List<SupermanagerPlayer>) aInputStream.readObject();
	}

	private void writeObject(ObjectOutputStream aOutputStream)
			throws IOException {
		aOutputStream.writeObject(this.bases);
		aOutputStream.writeObject(this.aleros);
		aOutputStream.writeObject(this.pivots);
		aOutputStream.flush();
		aOutputStream.close();
	}
}
