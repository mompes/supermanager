package es.mompes.supermanager.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Un equipo que contiene toda la informaciï¿½n de un equipo del
 * supermanager.acb.com. Incluyendo el cï¿½digo, el nombre, el broker, el valor
 * total de los jugadores, la posiciï¿½n en la general,...
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public class EquipoSupermanager extends Equipo implements Serializable {
	private final static String TAG = EquipoSupermanager.class.getName();
	/**
	 * 
	 */
	private static final long serialVersionUID = -5882867069916634421L;
	/**
	 * El cï¿½digo ï¿½nico del equipo.
	 */
	private final int codigo;
	/**
	 * La cantidad de dinero que tiene el equipo.
	 */
	private int broker;
	/**
	 * La posiciï¿½n en la clasificaciï¿½n general.
	 */
	private int posicionGeneral;
	/**
	 * La posiciï¿½n en la clasificaciï¿½n del broker.
	 */
	private int posicionBroker;
	/**
	 * La valoraciï¿½n total del equipo a lo largo de la temporada.
	 */
	private float totalValoracion;
	/**
	 * La ï¿½ltima valoraciï¿½n del equipo. Su valor es -Float.MAX_VALUE si no
	 * contiene un valor vï¿½lido.
	 */
	private float ultimaValoracion;
	/**
	 * Posiciï¿½n en la clasificaciï¿½n de la jornada.
	 */
	private int posicionJornada;
	/**
	 * Indica si hay algï¿½n jugador en el equipo que ha sido dado de baja en el
	 * juego.
	 */
	private boolean baja;
	/**
	 * Indica si hay algï¿½n jugador lesionado en el equipo.
	 */
	private boolean lesion;
	/**
	 * Contiene el string diciendo el nï¿½mero de jugadores del equipo o una
	 * cadena vacï¿½a si el equipo estï¿½ completo.
	 */
	private String pocosJugadores;

	/**
	 * Construye un nuevo equipo
	 * 
	 * @param ncodigo
	 *            El cï¿½digo del equipo en supermanager.acb.com.
	 * @param nnombre
	 *            El nombre del equipo.
	 * @param nbroker
	 *            El broker total del equipo.
	 * @param nposicionGeneral
	 *            La posiciï¿½n en la clasificaciï¿½n general.
	 * @param nposicionBroker
	 *            La posiciï¿½n en la clasificaciï¿½n del broker.
	 * @param nvaloracion
	 *            La valoraciï¿½n total del equipo a lo largo de la temporada.
	 * @param nultimaValoracion
	 *            La ï¿½ltima valoraciï¿½n del equipo.
	 * @param nposicionJornada
	 *            La posiciï¿½n del equipo en la clasificaciï¿½n de la ï¿½ltima
	 *            jornada.
	 */
	public EquipoSupermanager(final int ncodigo, final String nnombre,
			final int nbroker, final int nposicionGeneral,
			final int nposicionBroker, final float nvaloracion,
			final float nultimaValoracion, final int nposicionJornada,
			final boolean nbaja, final boolean nlesion) {
		super(nnombre);
		this.codigo = ncodigo;
		this.broker = nbroker;
		this.posicionGeneral = nposicionGeneral;
		this.posicionBroker = nposicionBroker;
		this.totalValoracion = nvaloracion;
		this.ultimaValoracion = nultimaValoracion;
		this.posicionJornada = nposicionJornada;
		this.baja = nbaja;
		this.lesion = nlesion;
		this.pocosJugadores = "";
	}

	public EquipoSupermanager(String nnombre, int ncodigo) {
		super(nnombre);
		codigo = ncodigo;
	}

	public int getPosicionJornada() {
		return posicionJornada;
	}

	public void setPosicionJornada(int posicionJornada) {
		this.posicionJornada = posicionJornada;
	}

	/**
	 * Si se establece el valor de ultimaValoraciï¿½n en Double.NEGATIVE_INFINITY
	 * este campo pasa a actuar como la suma de la valoraciï¿½n actual de los
	 * jugadores del equipo.
	 * 
	 * @param ultimaValoracion
	 *            La nueva valoraciï¿½n.
	 */
	public void setUltimaValoracion(float ultimaValoracion) {
		this.ultimaValoracion = ultimaValoracion;
	}

	/**
	 * Recupera la valoraciï¿½n total del equipo en la ï¿½ltima jornada.
	 * 
	 * @return La valoraciï¿½n del equipo en la ï¿½ltima jornada.
	 */
	public final float getUltimaValoracion() {
		if (this.ultimaValoracion > 0) {
			return this.ultimaValoracion;
		}
		float valoracion = 0;
		for (int i = 0; i < MAXIMUM_NUMBER_OF_PLAYERS && i < jugadores.size(); i++) {
			if (jugadores.get(i) != null
					&& jugadores.get(i).getUltimaValoracion() != Double.NEGATIVE_INFINITY) {
				valoracion += jugadores.get(i).getUltimaValoracion();
			}
		}
		return valoracion;
	}

	/**
	 * La posiciï¿½n en la clasificaciï¿½n general.
	 * 
	 * @return La posiciï¿½n en la clasificaciï¿½n general.
	 */
	public final int getPosicionGeneral() {
		return posicionGeneral;
	}

	/**
	 * Establece la posiciï¿½n en la general.
	 * 
	 * @param nposicionGeneral
	 *            La nueva posiciï¿½n en la general.
	 */
	public final void setPosicionGeneral(final int nposicionGeneral) {
		posicionGeneral = nposicionGeneral;
	}

	/**
	 * Recupera la posiciï¿½n en la clasificaciï¿½n del broker.
	 * 
	 * @return La posiciï¿½n en la clasificaciï¿½n del broker.
	 */
	public final int getPosicionBroker() {
		return posicionBroker;
	}

	/**
	 * Establece la posiciï¿½n en la clasificaciï¿½n del broker.
	 * 
	 * @param nposicionBroker
	 *            La nueva posiciï¿½n en la clasificaciï¿½n del broker.
	 */
	public final void setPosicionBroker(final int nposicionBroker) {
		posicionBroker = nposicionBroker;
	}

	/**
	 * Recupera el cï¿½digo del equipo en supermanager.acb.com.
	 * 
	 * @return El cï¿½digo del equipo en supermanager.acb.com.
	 */
	public final int getCodigo() {
		return codigo;
	}

	public boolean isLesion() {
		return lesion;
	}

	/**
	 * Estable el broker total del equipo.
	 * 
	 * @param nbroker
	 *            El broker total del equipo.
	 */
	public final void setBroker(final int nbroker) {
		this.broker = nbroker;
	}

	/**
	 * Recupera la cantidad total de puntos que el equipo ha conseguido hasta
	 * ahora.
	 * 
	 * @return La cantidad total de puntos del equipo.
	 */
	public final float getValoracionTotal() {
		return totalValoracion;
	}

	/**
	 * El valor total de los jugadores del equipo.
	 * 
	 * @return Valor de los jugadores.
	 */
	public final int getValorTotalJugadores() {
		int value = 0;
		for (int i = 0; i < Equipo.MAXIMUM_NUMBER_OF_PLAYERS
				& i < this.jugadores.size(); i++) {
			if (jugadores.get(i) != null && !jugadores.get(i).getLibre()
					&& jugadores.get(i).getPrecio() > 0) {
				value += jugadores.get(i).getPrecio();
			}
		}
		return value;
	}

	/**
	 * El broker total del equipo. Incluyendo el dinero no invertido en
	 * jugadores.
	 * 
	 * @return El broker total del equipo.
	 */
	public final int getBroker() {
		return broker;
	}

	/**
	 * El dinero que no estï¿½ invertido en jugadores.
	 * 
	 * @return El dinero no invertido en jugadores.
	 */
	public final int getDineroCaja() {
		int dineroCaja = this.broker;
		for (SupermanagerPlayer jugador : this.jugadores) {
			if (!jugador.getLibre()) {
				dineroCaja -= jugador.getPrecio();
			}
		}
		return dineroCaja;
	}

	/**
	 * 
	 * @return El nï¿½mero de jugadores extracomunitarios en el equipo.
	 */
	public final int getNumeroDeExtracomunitarios() {
		return getNumeroDe(EnumNacionalidad.EXTRACOMUNITARIO);
	}

	/**
	 * 
	 * @return El nï¿½mero de jugadores comunitarios en el equipo.
	 */
	public final int getNumeroDeComunitarios() {
		return getNumeroDe(EnumNacionalidad.COMUNITARIO);
	}

	/**
	 * 
	 * @return El nï¿½mero de jugadores espaï¿½oles en el equipo.
	 */
	public final int getNumeroDeEspañoles() {
		return getNumeroDe(EnumNacionalidad.ESPAÑOL);
	}

	/**
	 * 
	 * @param nacionalidad
	 *            La nacionalidad consultada.
	 * @return Cuï¿½ntos jugadores de esa nacionalidad hay en el equipo.
	 */
	private int getNumeroDe(EnumNacionalidad nacionalidad) {
		int resultado = 0;
		for (SupermanagerPlayer jugador : this.jugadores) {
			if (jugador.getNacionalidad() == nacionalidad) {
				resultado++;
			}
		}
		return resultado;
	}

	public String getPocosJugadores() {
		return pocosJugadores;
	}

	public void setPocosJugadores(final String npocosJugadores) {
		this.pocosJugadores = npocosJugadores;
	}

	public boolean isBaja() {
		return baja;
	}

	/**
	 * Busca un equipo con un cï¿½digo en una lista de equipos.
	 * 
	 * @param equipos
	 *            La lista con todos los equipos.
	 * @param codigo
	 *            El cï¿½digo del equipo que estamos buscando.
	 * @return El equipo que estaba buscando o null si no se encuentra.
	 */
	public static EquipoSupermanager getEquipo(
			final List<EquipoSupermanager> equipos, final long codigo) {
		EquipoSupermanager equipo = null;
		for (int i = 0; i < equipos.size(); i++) {
			if (equipos.get(i).getCodigo() == codigo) {
				equipo = equipos.get(i);
				break;
			}
		}
		return equipo;
	}

	@Override
	public final String toString() {
		String equipo = "Code: " + codigo + "\n\tName: " + getNombre()
				+ "\n\tPoints: " + totalValoracion + "\n\tMoney: " + broker
				+ "\n\tPositionBroker: " + posicionBroker
				+ "\n\tPositionGeneral: " + posicionGeneral;
		StringBuilder teamBuilder = new StringBuilder();
		teamBuilder.append(equipo);
		for (int i = 0; i < getNumeroDeJugadores(); i++) {
			teamBuilder.append("\n" + jugadores.get(i).toString());
		}
		return teamBuilder.toString();
	}

	/**
	 * Actualiza los valores de los jugadores del equipo con los obtenidos de la
	 * jornada virtual.
	 */
	public final void actualizarJornadaVirtual() {
		for (int i = 0; i < getNumeroDeJugadores(); i++) {
			for (int j = 0; j < PartidosJornadaVirtual.getNumeroDePartidos(); j++) {
				EquipoACB[] teams = PartidosJornadaVirtual.partidos[j]
						.getEquipos();
				if (jugadores.get(i) != null
						&& jugadores.get(i).getEquipo() != null
						&& teams[0] != null
						&& jugadores.get(i).getEquipo()
								.equals(teams[0].getNombre())
						&& teams[0].getJugador(jugadores.get(i).getNombre()) != null) {
					jugadores.get(i).setUltimaValoracion(
							teams[0].getJugador(jugadores.get(i).getNombre())
									.getUltimaValoracion());
				} else if (jugadores.get(i) != null
						&& jugadores.get(i).getEquipo() != null
						&& teams[1] != null
						&& jugadores.get(i).getEquipo()
								.equals(teams[1].getNombre())
						&& teams[1].getJugador(jugadores.get(i).getNombre()) != null) {
					if (teams[1].getJugador(jugadores.get(i).getNombre()) != null) {
						jugadores.get(i).setUltimaValoracion(
								teams[1].getJugador(
										jugadores.get(i).getNombre())
										.getUltimaValoracion());
					}
				}
			}
		}
	}

	protected void readObject(final ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		super.readObject(aInputStream);
		// always perform the default de-serialization first
		aInputStream.defaultReadObject();
	}

	protected void writeObject(final ObjectOutputStream aOutputStream)
			throws IOException {
		super.writeObject(aOutputStream);
		// perform the default serialization for all non-transient, non-static
		// fields
		aOutputStream.defaultWriteObject();
	}
}
