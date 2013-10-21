package es.mompes.supermanager.webservice;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import es.mompes.supermanager.paginas.PaginaJugador;
import es.mompes.supermanager.util.Configuration;
import es.mompes.supermanager.util.EstadisticasJugador;
import es.mompes.supermanager.util.GetPageException;
import es.mompes.supermanager.util.Helper;

public class WSEstadisticasJugador {
	public static List<EstadisticasJugador> getEstadisticas(String codigoJugador) {
		List<EstadisticasJugador> estadisticas = new LinkedList<EstadisticasJugador>();
		PaginaJugador paginaJugador;
		try {
			paginaJugador = new PaginaJugador(WebService.getPaginaLogueado(
					Configuration.getInstance().getProperty(
							Configuration.JUGADORESTADISTICAS1)
							+ codigoJugador
							+ Configuration.getInstance().getProperty(
									Configuration.JUGADORESTADISTICAS2), null));
		} catch (GetPageException e) {
			return estadisticas;
		}
		paginaJugador.limpiar();
		NodeList nodo = paginaJugador.toXML();
		if (nodo != null) {
			for (int i = 0; i < nodo.getLength(); i++) {
				Element jugador = (Element) nodo.item(i);
				estadisticas.add(extraerFilaEstadisticas(jugador));
			}
		}
		return estadisticas;
	}

	private static EstadisticasJugador extraerFilaEstadisticas(
			final Element jugador) {
		int jornada = 0, puntos = 0, asistencias = 0, recuperaciones = 0;
		int perdidas = 0, contraataques = 0, mates = 0, faltaAFavor = 0;
		int faltaEnContra = 0, masMenos = 0, valoracion = 0;
		String minutos = "", t1 = "", t2 = "", t3 = "", rebotes = "", tapones = "";
		NodeList hijos = jugador.getChildNodes();
		// Esta número se utiliza para corregir la diferencia de índices
		// entre los valores normales de las estadísticas y los valores de
		// los totales y los promedios.
		int extra = 0;
		if (Helper.getTextContent(hijos.item(1)).trim().equals("Totales")
				| Helper.getTextContent(hijos.item(1)).trim()
						.equals("Promedios")) {
			extra = -4;
			jornada = -1;
		} else {
			jornada = Integer.parseInt(Helper.getTextContent(hijos.item(3)));
		}
		minutos = Helper.getTextContent(hijos.item(7 + extra));
		try {
			puntos = Integer.parseInt(Helper.getTextContent(hijos
					.item(9 + extra)));
		} catch (Exception e) {
		}
		t2 = Helper.getTextContent(hijos.item(11 + extra));
		t3 = Helper.getTextContent(hijos.item(13 + extra));
		t1 = Helper.getTextContent(hijos.item(15 + extra));
		rebotes = Helper.getTextContent(hijos.item(17 + extra));
		asistencias = Integer.parseInt(Helper.getTextContent(hijos
				.item(19 + extra)));
		recuperaciones = Integer.parseInt(Helper.getTextContent(hijos
				.item(21 + extra)));
		perdidas = Integer.parseInt(Helper.getTextContent(hijos
				.item(23 + extra)));
		contraataques = Integer.parseInt(Helper.getTextContent(hijos
				.item(25 + extra)));
		tapones = Helper.getTextContent(hijos.item(27 + extra));
		mates = Integer.parseInt(Helper.getTextContent(hijos.item(29 + extra)));
		faltaAFavor = Integer.parseInt(Helper.getTextContent(hijos
				.item(31 + extra)));
		faltaEnContra = Integer.parseInt(Helper.getTextContent(hijos
				.item(33 + extra)));
		masMenos = Integer.parseInt(Helper.getTextContent(hijos
				.item(35 + extra)));
		valoracion = Integer.parseInt(Helper.getTextContent(hijos
				.item(37 + extra)));
		return new EstadisticasJugador(jornada, minutos, puntos, t1, t2, t3,
				rebotes, asistencias, recuperaciones, perdidas, contraataques,
				tapones, mates, faltaAFavor, faltaEnContra, masMenos,
				valoracion);
	}
}
