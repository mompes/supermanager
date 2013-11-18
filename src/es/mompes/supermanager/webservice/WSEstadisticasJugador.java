package es.mompes.supermanager.webservice;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import es.mompes.supermanager.paginas.PaginaJugador;
import es.mompes.supermanager.util.Configuration;
import es.mompes.supermanager.util.EstadisticasJugador;
import es.mompes.supermanager.util.GetPageException;

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
				Element fila = (Element) nodo.item(i);
				EstadisticasJugador filaEstadisticas = extraerFilaEstadisticas(fila);
				if (filaEstadisticas != null) {
					estadisticas.add(filaEstadisticas);
				}
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
		// Este número se utiliza para corregir la diferencia de índices
		// entre los valores normales de las estadísticas y los valores de
		// los totales y los promedios.
		int extra = 0;
		if (hijos.item(1) != null
				&& (hijos.item(1).getTextContent().trim().equals("Totales") | hijos
						.item(1).getTextContent().trim().equals("Promedios"))) {
			extra = -4;
			jornada = -1;
		} else {
			try {
				jornada = Integer.parseInt(hijos.item(3).getTextContent());
			} catch (Exception e) {
				return null;
			}
		}
		// Si falla parseando algún dato devuelve null
		try {
			minutos = hijos.item(7 + extra).getTextContent();
			puntos = Integer.parseInt(hijos.item(9 + extra).getTextContent());
			t2 = hijos.item(11 + extra).getTextContent();
			t3 = hijos.item(13 + extra).getTextContent();
			t1 = hijos.item(15 + extra).getTextContent();
			rebotes = hijos.item(17 + extra).getTextContent();
			asistencias = Integer.parseInt(hijos.item(19 + extra)
					.getTextContent());
			recuperaciones = Integer.parseInt(hijos.item(21 + extra)
					.getTextContent());
			perdidas = Integer
					.parseInt(hijos.item(23 + extra).getTextContent());
			contraataques = Integer.parseInt(hijos.item(25 + extra)
					.getTextContent());
			tapones = hijos.item(27 + extra).getTextContent();
			mates = Integer.parseInt(hijos.item(29 + extra).getTextContent());
			faltaAFavor = Integer.parseInt(hijos.item(31 + extra)
					.getTextContent());
			faltaEnContra = Integer.parseInt(hijos.item(33 + extra)
					.getTextContent());
			masMenos = Integer
					.parseInt(hijos.item(35 + extra).getTextContent());
			valoracion = Integer.parseInt(hijos.item(37 + extra)
					.getTextContent());
		} catch (Exception e) {
			return null;
		}
		return new EstadisticasJugador(jornada, minutos, puntos, t1, t2, t3,
				rebotes, asistencias, recuperaciones, perdidas, contraataques,
				tapones, mates, faltaAFavor, faltaEnContra, masMenos,
				valoracion);
	}
}
