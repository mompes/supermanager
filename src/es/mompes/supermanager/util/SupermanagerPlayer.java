package es.mompes.supermanager.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Representa a un jugador del supermanager.
 * 
 * @author Juan Mompe�n Esteban
 * 
 */
public class SupermanagerPlayer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8777931237539141394L;
	/**
	 * Este string se utiliza para valores desconocidos del nombre, del balance
	 * y de los minutos.
	 */
	public static final String datosDesconocidos = "-";
	/**
	 * El precio del jugador en el juego.
	 */
	private int precio;
	/**
	 * La valoración media del jugador.
	 */
	private double media;
	/**
	 * La última valoración del jugador.
	 */
	private double ultimaValoracion;
	/**
	 * La posición dónde el jugador juega.
	 */
	private EnumPosicion posicion;
	/**
	 * Valoración necesaria para subir un 15%.
	 */
	private double sube15;
	/**
	 * Valoración necesaria para mantener el precio.
	 */
	private double seMantiene;
	/**
	 * Valoración necesaria para bajar un 15%.
	 */
	private double baja15;
	/**
	 * La valoración media del jugador en los últimos 3 partidos.
	 */
	private double valoracionUltimos3Partidos;
	/**
	 * Minutos jugados por el jugador de media.
	 */
	private String minutos;
	/**
	 * Nombre del jugador.
	 */
	private String nombre;
	/**
	 * Equipo del jugador.
	 */
	private String equipo;
	/**
	 * Balance del equipo del jugador. (Victorias/Derrotas).
	 */
	private String balance;
	/**
	 * El código que la ACB utiliza para representar al jugador. Es un código
	 * formado por 3 letras y números.
	 */
	private String codigoACB;
	/**
	 * Este campo refleja si esta jugador ha sido vendido. Será true cuando el
	 * jugador haya sido vendido y anulará el valor del resto de campos,
	 * considerándose inválidos.
	 */
	private boolean libre;
	/**
	 * La nacionalidad del jugador.
	 */
	private EnumNacionalidad nacionalidad;
	/**
	 * Indica si se puede anular el cambio realizado en una posición que está
	 * libre o no.
	 */
	private boolean anular;
	/**
	 * Indica si un jugador se puede sustituir sin consumir cambios.
	 */
	private boolean sustituir;
	/**
	 * Indica si un jugador está lesionado.
	 */
	private boolean lesionado;
	/**
	 * Indica la jornada en la que se fichó al jugador.
	 */
	private int jornadaFichaje;

	/**
	 * Indica si se puede fichar el jugador. El jugador es no fichable si nos
	 * faltan españoles, no nos queda dinero...
	 */
	private boolean fichable;
	/**
	 * Mensaje que se muestra cuando un jugador está lesionado.
	 */
	private String lesionadoInfo;

	/**
	 * Construye un nuevo jugador del supermanager.
	 * 
	 * @param nnombre
	 *            El nombre del jugador.
	 * @param nequipo
	 *            El nombre del equipo.
	 * @param nprecio
	 *            El precio del jugador.
	 * @param nmedia
	 *            La valoración media.
	 * @param nultimaValoracion
	 *            La última valoración.
	 * @param nposicion
	 *            La posición en la que juega el jugador.
	 * @param nbaja15
	 *            La valoración necesaria para que el precio del jugador baje
	 *            un 15%.
	 * @param nbalance
	 *            El número de victorias y derrotas del jugador expresado en el
	 *            siguiente formato: (Victorias/Derrotas).
	 * @param nminutos
	 *            La media de minutos jugados por el jugador.
	 * @param nseMantiene
	 *            La valoración necesaria para que el precio del jugador se
	 *            mantenga.
	 * @param nsube15
	 *            La valoración necesaria para que el precio del jugador suba
	 *            un 15%.
	 * @param nvaloracionUltimos3Partidos
	 *            La valoración en los últimos 3 partidos del jugador.
	 */
	public SupermanagerPlayer(final String nnombre, final String nequipo,
			final int nprecio, final double nmedia,
			final double nultimaValoracion, final EnumPosicion nposicion,
			final double nbaja15, final String nbalance, final String nminutos,
			final double nseMantiene, final double nsube15,
			final double nvaloracionUltimos3Partidos, final String ncodigoACB,
			final EnumNacionalidad nnacionalidad) {
		this.baja15 = nbaja15;
		this.balance = nbalance;
		this.equipo = nequipo;
		this.media = nmedia;
		this.minutos = nminutos;
		this.nombre = nnombre;
		this.posicion = nposicion;
		this.precio = nprecio;
		this.seMantiene = nseMantiene;
		this.sube15 = nsube15;
		this.ultimaValoracion = nultimaValoracion;
		this.valoracionUltimos3Partidos = nvaloracionUltimos3Partidos;
		this.codigoACB = ncodigoACB;
		this.nacionalidad = nnacionalidad;
		this.libre = false;
		this.anular = false;
		this.sustituir = false;
		this.lesionado = false;
		this.jornadaFichaje = Integer.MAX_VALUE;
		this.fichable = true;
	}

	/**
	 * 
	 * @return Jornada en la que se fichó al jugador o infinito si se
	 *         desconoce.
	 */
	public int getJornadaFichaje() {
		return jornadaFichaje;
	}

	/**
	 * Actualiza la jornada en la que se fichó al jugador.
	 * 
	 * @param jornadaFichaje
	 *            La nueva jornada de fichaje.
	 */
	public void setJornadaFichaje(int jornadaFichaje) {
		this.jornadaFichaje = jornadaFichaje;
	}

	public SupermanagerPlayer() {
		this.libre = false;
		this.posicion = null;
		this.balance = SupermanagerPlayer.datosDesconocidos;
		this.minutos = SupermanagerPlayer.datosDesconocidos;
		this.valoracionUltimos3Partidos = Double.NEGATIVE_INFINITY;
		this.sube15 = Double.NEGATIVE_INFINITY;
		this.seMantiene = Double.NEGATIVE_INFINITY;
		this.baja15 = Double.NEGATIVE_INFINITY;
		this.equipo = null;
		this.media = Double.NEGATIVE_INFINITY;
		this.nombre = null;
		this.precio = 0;
		this.ultimaValoracion = Double.NEGATIVE_INFINITY;
		this.codigoACB = null;
		this.nacionalidad = null;
		this.anular = false;
		this.sustituir = false;
		this.lesionado = false;
		this.jornadaFichaje = Integer.MAX_VALUE;
		this.fichable = true;
	}

	public boolean isLesionado() {
		return lesionado;
	}

	public void setLesionado(boolean lesionado) {
		this.lesionado = lesionado;
	}

	public String getLesionadoInfo() {
		return lesionadoInfo;
	}

	public void setLesionadoInfo(String lesionadoInfo) {
		this.lesionadoInfo = lesionadoInfo;
	}

	public boolean isSustituir() {
		return sustituir;
	}

	public void setSustituir(boolean sustituir) {
		this.sustituir = sustituir;
	}

	/**
	 * Construye un nuevo jugador del supermanager.
	 * 
	 * @param nnombre
	 *            The name.
	 * @param nultimaValoracion
	 *            The last score.
	 */
	public SupermanagerPlayer(final String nnombre, final String nequipo,
			final double nultimaValoracion) {
		this.nombre = nnombre;
		this.equipo = nequipo;
		this.ultimaValoracion = nultimaValoracion;
		this.jornadaFichaje = Integer.MAX_VALUE;
		this.fichable = true;
	}

	public boolean isAnular() {
		return anular;
	}

	public void setAnular(boolean anular) {
		this.anular = anular;
	}

	public EnumNacionalidad getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(EnumNacionalidad nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public void setLibre(boolean libre) {
		this.libre = libre;
	}

	/**
	 * 
	 * @return La URI de la imagen del jugador.
	 */
	public URL getImagen() {
		try {
			return new URL("http://www.acb.com/fotos_cara/jugadores/J"
					+ this.codigoACB + "LACB56.jpg");
		} catch (MalformedURLException e) {
			return null;
		}

	}

	/**
	 * Indica si un jugador ha sido vendido. Si es true el resto de datos del
	 * jugador se consideran no válidos.
	 * 
	 * @return True si ha sido vendido y false en caso contrario.
	 */
	public boolean getLibre() {
		return libre;
	}

	/**
	 * 
	 * @return El código que representa al jugador en la página de la ACB.
	 */
	public String getCodigoACB() {
		return codigoACB;
	}

	/**
	 * Cambia el valor del código de la ACB del jugador.
	 * 
	 * @param ncodigoACB
	 *            El nuevo código.
	 */
	public void setCodigoACB(String ncodigoACB) {
		this.codigoACB = ncodigoACB;
	}

	/**
	 * @return the precio
	 */
	public final int getPrecio() {
		if (libre) {
			return 0;
		}
		return precio;
	}

	/**
	 * @param nprecio
	 *            the precio to set
	 */
	public final void setPrecio(final int nprecio) {
		this.precio = nprecio;
	}

	/**
	 * @return the media
	 */
	public final double getMedia() {
		return media;
	}

	/**
	 * @param nmedia
	 *            the media to set
	 */
	public final void setMedia(final double nmedia) {
		this.media = nmedia;
	}

	/**
	 * @return the ultimaValoracion
	 */
	public final double getUltimaValoracion() {
		return ultimaValoracion;
	}

	/**
	 * @param nultimaValoracion
	 *            the ultimaValoracion to set
	 */
	public final void setUltimaValoracion(final double nultimaValoracion) {
		this.ultimaValoracion = nultimaValoracion;
	}

	/**
	 * @return the posicion
	 */
	public final EnumPosicion getPosicion() {
		return posicion;
	}

	/**
	 * @param nposicion
	 *            the posicion to set
	 */
	public final void setPosicion(final EnumPosicion nposicion) {
		this.posicion = nposicion;
	}

	/**
	 * @return the sube15
	 */
	public final double getSube15() {
		return sube15;
	}

	/**
	 * @param nsube15
	 *            the sube15 to set
	 */
	public final void setSube15(final double nsube15) {
		this.sube15 = nsube15;
	}

	/**
	 * @return the seMantiene
	 */
	public final double getSeMantiene() {
		return seMantiene;
	}

	/**
	 * @param nseMantiene
	 *            the seMantiene to set
	 */
	public final void setSeMantiene(final double nseMantiene) {
		this.seMantiene = nseMantiene;
	}

	/**
	 * @return the baja15
	 */
	public final double getBaja15() {
		return baja15;
	}

	/**
	 * @param nbaja15
	 *            the baja15 to set
	 */
	public final void setBaja15(final double nbaja15) {
		this.baja15 = nbaja15;
	}

	/**
	 * @return the valoracionUltimos3Partidos
	 */
	public final double getValoracionUltimos3Partidos() {
		return valoracionUltimos3Partidos;
	}

	/**
	 * @param nvaloracionUltimos3Partidos
	 *            the valoracionUltimos3Partidos to set
	 */
	public final void setValoracionUltimos3Partidos(
			final double nvaloracionUltimos3Partidos) {
		this.valoracionUltimos3Partidos = nvaloracionUltimos3Partidos;
	}

	/**
	 * @return the minutos
	 */
	public final String getMinutos() {
		return minutos;
	}

	/**
	 * @param nminutos
	 *            the minutos to set
	 */
	public final void setMinutos(final String nminutos) {
		this.minutos = nminutos;
	}

	/**
	 * @return the nombre
	 */
	public final String getNombre() {
		return nombre;
	}

	/**
	 * @param nnombre
	 *            the nombre to set
	 */
	public final void setNombre(final String nnombre) {
		this.nombre = nnombre;
	}

	/**
	 * @return the equipo
	 */
	public final String getEquipo() {
		return equipo;
	}

	/**
	 * @param nequipo
	 *            the equipo to set
	 */
	public final void setEquipo(final String nequipo) {
		this.equipo = nequipo;
	}

	/**
	 * @return the balance
	 */
	public final String getBalance() {
		return balance;
	}

	/**
	 * @param nbalance
	 *            the balance to set
	 */
	public final void setBalance(final String nbalance) {
		this.balance = nbalance;
	}

	public boolean isFichable() {
		return fichable;
	}

	public void setFichable(boolean fichable) {
		this.fichable = fichable;
	}

	@Override
	public final boolean equals(final Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof SupermanagerPlayer)) {
			return false;
		}
		SupermanagerPlayer p = (SupermanagerPlayer) o;
		return nombre != null && p.nombre != null && nombre.equals(p.nombre)
				&& equipo != null && p.equipo != null
				&& equipo.equals(p.equipo);
	}

	@Override
	public final String toString() {
		return "Nombre: " + nombre + " Equipo: " + equipo + " Precio: "
				+ precio + "\nValoración media: " + media
				+ " Última Valoración: " + ultimaValoracion + " Posición: "
				+ posicion + " Minutos: " + minutos + " Balance: " + balance
				+ " Sube15: " + sube15 + " seMantiene: " + seMantiene
				+ " baja15: " + baja15 + " ValoraciónÚltimos3Partidos: "
				+ valoracionUltimos3Partidos;
	}

	private void readObject(ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		// always perform the default de-serialization first
		aInputStream.defaultReadObject();
	}

	private void writeObject(ObjectOutputStream aOutputStream)
			throws IOException {
		// perform the default serialization for all non-transient, non-static
		// fields
		aOutputStream.defaultWriteObject();
	}
}