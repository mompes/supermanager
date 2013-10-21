package es.mompes.supermanager.util;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import es.mompes.supermanager.util.R;

/**
 * Contiene y maneja las estadísticas de una jornada de un jugador.
 * 
 * @author Juan Mompe�n Esteban
 * 
 */
public class EstadisticasJugador implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7382683524005397637L;
	private int jornada;
	private String minutos;
	private int puntos;
	private String t1;
	private String t2;
	private String t3;
	private String rebotes;
	private int asistencias;
	private int recuperaciones;
	private int perdidas;
	private int contraataques;
	private String tapones;
	private int mates;
	private int faltaAFavor;
	private int faltaEnContra;
	private int masMenos;
	/**
	 * Almacena la valoracion del jugador en esa jornada sin sumar el bonus.
	 */
	private int valoracion;

	public EstadisticasJugador(int njornada, final String nminutos,
			int npuntos, final String nt1, final String nt2, final String nt3,
			final String nrebotes, int nasistencias, int nrecuperaciones,
			int nperdidas, int ncontraataques, final String ntapones,
			int nmates, int nfaltaAFavor, int nfaltaEnContra, int nmasMenos,
			int nvaloracion) {
		super();
		this.jornada = njornada;
		this.minutos = nminutos;
		this.puntos = npuntos;
		this.t1 = nt1;
		this.t2 = nt2;
		this.t3 = nt3;
		this.rebotes = nrebotes;
		this.asistencias = nasistencias;
		this.recuperaciones = nrecuperaciones;
		this.perdidas = nperdidas;
		this.contraataques = ncontraataques;
		this.tapones = ntapones;
		this.mates = nmates;
		this.faltaAFavor = nfaltaAFavor;
		this.faltaEnContra = nfaltaEnContra;
		this.masMenos = nmasMenos;
		this.valoracion = nvaloracion;
	}
	
	public int getJornada() {
		return jornada;
	}

	public String getMinutos() {
		return minutos;
	}

	public int getPuntos() {
		return puntos;
	}

	public String getT1() {
		return t1;
	}

	public String getT2() {
		return t2;
	}

	public String getT3() {
		return t3;
	}

	public String getRebotes() {
		return rebotes;
	}

	public int getAsistencias() {
		return asistencias;
	}

	public int getRecuperaciones() {
		return recuperaciones;
	}

	public int getPerdidas() {
		return perdidas;
	}

	public int getContraataques() {
		return contraataques;
	}

	public String getTapones() {
		return tapones;
	}

	public int getMates() {
		return mates;
	}

	public int getFaltaAFavor() {
		return faltaAFavor;
	}

	public int getFaltaEnContra() {
		return faltaEnContra;
	}

	public int getMasMenos() {
		return masMenos;
	}

	public int getValoracion() {
		return valoracion;
	}

	/**
	 * Devuelve el número de partidos jugados por un jugador a partir de sus
	 * estadísticas. Se considera que las estadísticas que se le pasan han sido
	 * obtenidas de la función @see
	 * EstadisticasJugador.estadisticasJugadorFromXML por lo que se le restará
	 * dos al número de jornadas almacenadas. Esto se debe a que las dos últimas
	 * posiciones de la lista contienen el total y la media de las estadísticas
	 * respectivamente.
	 * 
	 * @param estadisticas
	 *            Las estadísticas del jugador a lo largo de la temporada.
	 * @return El número de partidos que el jugador ha jugado.
	 */
	public final static int numeroPartidosJugados(
			final List<EstadisticasJugador> estadisticas) {
		int total = 0;
		for (EstadisticasJugador stats : estadisticas) {
			if (stats.getMinutos() != null && !stats.getMinutos().equals("")) {
				total++;
			}
		}
		return (total >= 2) ? total - 2 : 0;
	}

	/**
	 * Devuelve la valoración que un jugador necesita para variar su broker el
	 * porcentaje indicado.
	 * 
	 * @param broker
	 *            El broker actual del jugador.
	 * @param mediaAntigua
	 *            La media del jugador hasta ahora.
	 * @param numeroPartidos
	 *            El número de partidos que ha jugado el jugador
	 * @param variacion
	 *            La variación en el broker que se quiere obtener. 1.15 para un
	 *            15%, 1.00 para no variación o 0.85 para un -15%.
	 * @return La valoración necesaria.
	 */
	public final static double necesarioVariar(final int broker,
			final double mediaAntigua, final int numeroPartidos,
			final double variacion) {
		double media = (broker * variacion) / 70000;
		return media * (numeroPartidos + 1) - mediaAntigua * numeroPartidos;
	}

	public final static int[] historicoBroker(
			final List<EstadisticasJugador> estadisticas,
			final int brokerInicial, final boolean[] resultadosEquipo) {
		List<EstadisticasJugador> stats = estadisticas.subList(0, (estadisticas
				.size() >= 2) ? estadisticas.size() - 2 : estadisticas.size());
		int[] historico = new int[stats.size() + 1];
		double[] medias = EstadisticasJugador.media(stats, resultadosEquipo,
				true);
		historico[0] = brokerInicial;
		for (int i = 1; i <= stats.size(); i++) {
			if (medias[i] * 70000 * 0.99 > historico[i - 1] * 1.15) {
				historico[i] = (int) (historico[i - 1] * 1.15);
			} else if (medias[i] * 70000 * 1.01 < historico[i - 1] * 0.85) {
				historico[i] = (int) (historico[i - 1] * 0.85);
			} else {
				historico[i] = (int) (medias[i] * 70000);
			}
		}
		return historico;
	}

	/**
	 * Reconstruye la evolución del broker del jugador desde la jornada 0 hasta
	 * la actual a partir de su valor actual.
	 * 
	 * @param estadisticas
	 *            Las estadísticas del jugador a lo largo de la temporada.
	 * @param brokerActual
	 *            El broker actual del jugador.
	 * @param resultadosEquipo
	 *            Contiene true si el equipo ganó en la jornada correspondiente
	 *            y false en caso contrario.
	 * @return Un arrary con el broker del jugador a lo largo de la temporada.
	 */
	public final static int[] historicoBrokerHeuristica(
			final List<EstadisticasJugador> estadisticas,
			final int brokerActual, final boolean[] resultadosEquipo) {
		// Elimina los totales y los promedios
		List<EstadisticasJugador> stats = estadisticas.subList(0, (estadisticas
				.size() >= 2) ? estadisticas.size() - 2 : estadisticas.size());
		int[] historico = new int[stats.size() + 1];
		double[] medias = EstadisticasJugador.media(stats, resultadosEquipo,
				false);
		historico[stats.size()] = brokerActual;
		for (int i = stats.size() - 1; i > 0; i--) {
			// El jugador está infravalorado
			if (medias[i + 1] * 70000 * 0.99 > historico[i + 1]) {
				historico[i] = (int) (historico[i + 1] / 1.15);
				// while (i > 0) {
				// historico[i] = 0;
				// i--;
				// }
			}
			// El jugador está sobrevalorado
			else if (medias[i + 1] * 70000 * 1.01 < historico[i + 1]) {
				historico[i] = (int) (historico[i + 1] / 0.85);
				// while (i > 0) {
				// historico[i] = 0;
				// i--;
				// }
			} else {
				historico[i] = (int) (historico[i + 1] / Math.min(1.15,
						Math.max(0.85, (medias[i + 1] / medias[i]))));
			}
		}
		for (int i = 0; i < stats.size(); i++) {
			historico[i] = historico[i + 1];
		}
		return historico;
	}

	/**
	 * Filtra las estadísticas que le pasamos eliminando las jornadas que un
	 * equipo no contabilizó en el supermanager.
	 * 
	 * @param estadisticas
	 *            Las estadísticas del jugador a lo largo de la temporada que
	 *            queremos que sean filtradas.
	 * @param equipo
	 *            El equipo en el que juega el jugador.
	 * @return La lista de estadísticas sin las jornadas que el equipo no
	 *         contabilizó en el supermanager.
	 */
	public final static List<EstadisticasJugador> filtrarJornadas(
			final List<EstadisticasJugador> estadisticas, final String equipo) {
		List<EstadisticasJugador> filtradas = new LinkedList<EstadisticasJugador>();
		List<Integer> jornadasAEliminar = new LinkedList<Integer>();
		if (equipo.equals("BRV")) {
			jornadasAEliminar.add(2);
		} else if (equipo.equals("RMA")) {
			jornadasAEliminar.add(2);
		}
		filtradas.addAll(estadisticas);
		for (int jornada : jornadasAEliminar) {
			for (int i = 0; i < filtradas.size(); i++) {
				if (filtradas.get(i).getJornada() == jornada) {
					filtradas.remove(i);
					i--;
				}
			}
		}
		return filtradas;
	}

	/**
	 * Calcula la media para cada jornada de la valoración de un jugador.
	 * 
	 * @param estadisticas
	 *            Estadísticas con las valoraciones de cada partido.
	 * @param resultadosEquipo
	 *            Contiene true si el jugador ganó es jornada y false en otro
	 *            caso. Puede contener un valor no válido en la posición 0 si el
	 *            precio inicial del jugador es conocido. En tal caso se indica
	 *            en el parámetro jornada0.
	 * @param jornada0
	 *            Es true si la primera posición del parámetro resultadosEquipo
	 *            contiene un valor no válido y false en otro caso.
	 * @return Un array de doubles con la media para cada jornada.
	 */
	private final static double[] media(
			final List<EstadisticasJugador> estadisticas,
			final boolean[] resultadosEquipo, final boolean jornada0) {
		double[] medias = new double[estadisticas.size() + 1];
		medias[0] = 0;
		int offset = 1;
		if (jornada0) {
			offset = 0;
		}
		for (int i = 1; i <= estadisticas.size(); i++) {
			medias[i] = (medias[i - 1] * (i - 1) + estadisticas.get(i - 1)
					.getValoracion() * (resultadosEquipo[i - offset] ? 1.2 : 1))
					/ i;
		}
		return medias;
	}

	/**
	 * Devuelve el broker inicial de un jugador.
	 * 
	 * @param nombre
	 *            El nombre del jugador sobre el que se pregunta.
	 * @param posicion
	 *            Posición en la que juega el jugador.
	 * @return El broker inicial del jugador o 0 si no hay datos sobre él.
	 */
	public final static int brokerInicial(final String nombre,
			final EnumPosicion posicion, final Activity activity) {
		XmlResourceParser parser = null;
		if (posicion == EnumPosicion.BASE) {
			parser = activity.getResources().getXml(R.xml.bases);
		} else if (posicion == EnumPosicion.ALERO) {
			parser = activity.getResources().getXml(R.xml.aleros);
		} else if (posicion == EnumPosicion.PIVOT) {
			parser = activity.getResources().getXml(R.xml.pivots);
		}
		try {
			int eventType = parser.getEventType();
			boolean este = false;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String name = null;

				switch (eventType) {
				case XmlPullParser.START_TAG:
					name = parser.getName().toLowerCase();

					if (name.equals("j")) {
						for (int i = 0; i < parser.getAttributeCount(); i++) {
							String attribute = parser.getAttributeName(i)
									.toLowerCase();
							if (attribute.equals("n")) {
								String value = parser.getAttributeValue(i);
								if (value.equals(nombre)) {
									este = true;
								}
							}

						}
					}

					break;
				case XmlPullParser.TEXT:
					if (este) {
						int broker = 0;
						try {
							broker = Integer.parseInt(parser.getText());
						} catch (Exception e) {
							return 0;
						}
						parser.close();
						return broker;
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			throw new RuntimeException("Cannot parse XML");
		} catch (IOException e) {
			throw new RuntimeException("Cannot parse XML");
		} finally {
			if (parser != null) {
				parser.close();
			}
		}
		return 0;
	}

	protected void readObject(ObjectInputStream aInputStream)
			throws ClassNotFoundException, IOException {
		// always perform the default de-serialization first
		aInputStream.defaultReadObject();
	}

	protected void writeObject(ObjectOutputStream aOutputStream)
			throws IOException {
		// perform the default serialization for all non-transient, non-static
		// fields
		aOutputStream.defaultWriteObject();
	}
}