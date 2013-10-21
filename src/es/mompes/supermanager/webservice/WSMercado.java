package es.mompes.supermanager.webservice;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.util.Log;
import es.mompes.supermanager.paginas.Pagina;
import es.mompes.supermanager.paginas.PaginaMercado;
import es.mompes.supermanager.paginas.PaginaMercadoCrearEquipo;
import es.mompes.supermanager.util.Configuration;
import es.mompes.supermanager.util.EnumNacionalidad;
import es.mompes.supermanager.util.EnumPosicion;
import es.mompes.supermanager.util.GetPageException;
import es.mompes.supermanager.util.Helper;
import es.mompes.supermanager.util.SupermanagerPlayer;

public class WSMercado {
	private final static String TAG = WSMercado.class.getName();

	/**
	 * Extrae los jugadores de una posición del mercado.
	 * 
	 * @param posicion
	 *            Posición de la que se quieren conocer los jugadores.
	 * @return Una lista de los jugadores de la posición solicitada del
	 *         mercado. Si se produjo algún error devolverá una lista vacía.
	 */
	public static List<SupermanagerPlayer> getJugadoresVistaMercado(
			EnumPosicion posicion) {
		Pagina pagina = null;
		List<SupermanagerPlayer> jugadores = new LinkedList<SupermanagerPlayer>();
		String url = null;
		switch (posicion) {
		case BASE:
			url = Configuration.getInstance().getProperty(
					Configuration.BASESURL);
			break;
		case ALERO:
			url = Configuration.getInstance().getProperty(
					Configuration.ALEROSURL);
			break;
		case PIVOT:
			url = Configuration.getInstance().getProperty(
					Configuration.PIVOTSURL);
			break;
		default:
			break;
		}
		try {
			pagina = new PaginaMercado(WebService.getPaginaLogueado(url, null));
		} catch (GetPageException e) {
			Log.e(TAG,
					"Error descargando página de mercado: " + e.getMessage());
			return jugadores;
		}
		pagina.limpiar();
		NodeList nodeList = pagina.toXML();
		if (nodeList != null) {
			// Recorre la tabla de jugadores analizando y a�adiendo nuevos
			// jugadores.
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element jugador = (Element) nodeList.item(i);
				SupermanagerPlayer j = null;
				j = extraerJugadorDeMercado(jugador, posicion);
				jugadores.add(j);
			}
		}
		return jugadores;
	}

	public static List<SupermanagerPlayer> getJugadoresVistaMercadoCrearEquipo(
			EnumPosicion posicion, int numero) {
		Pagina pagina = null;
		List<SupermanagerPlayer> jugadores = new LinkedList<SupermanagerPlayer>();
		String url = null;
		switch (posicion) {
		case BASE:
			url = Configuration.getInstance().getProperty(
					Configuration.BASESURLC)
					+ "b" + (numero + 1);
			break;
		case ALERO:
			url = Configuration.getInstance().getProperty(
					Configuration.ALEROSURLC)
					+ "a" + (numero + 1);
			break;
		case PIVOT:
			url = Configuration.getInstance().getProperty(
					Configuration.PIVOTSURLC)
					+ "p" + (numero + 1);
			break;
		default:
			break;
		}
		try {
			pagina = new PaginaMercadoCrearEquipo(WebService.getPaginaLogueado(
					url, null));
		} catch (GetPageException e) {
			Log.e(TAG, "Error descargando p�gina de mercado: " + e.getMessage());
			return jugadores;
		}
		int inicio = pagina.getPagina().indexOf("black") + "black".length();
		// Es el segundo black
		inicio = pagina.getPagina().indexOf("black", inicio)
				+ "black>".length() + 1;
		int fin = pagina.getPagina().indexOf("<", inicio);
		String primero = pagina.getPagina().substring(inicio, fin);
		pagina.limpiar();
		NodeList nodeList = pagina.toXML();
		if (nodeList != null) {
			// Recorre la tabla de jugadores analizando y a�adiendo nuevos
			// jugadores.
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element jugador = (Element) nodeList.item(i);
				jugadores.add(extraerJugadorMercadoCrearEquipo(jugador,
						posicion));
			}
			// Pone los jugadores como fichables a partir de la posición
			// correcta
			boolean fichable = false;
			for (SupermanagerPlayer jugador : jugadores) {
				if (jugador.getNombre().equals(primero)) {
					fichable = true;
				}
				jugador.setFichable(fichable);
			}
		}

		return jugadores;
	}

	/**
	 * Extrae los datos de un jugador desde la vista de mercado.
	 * 
	 * @param nodo
	 * @param posicion
	 * @return
	 */
	private static SupermanagerPlayer extraerJugadorDeMercado(Element nodo,
			EnumPosicion posicion) {
		SupermanagerPlayer j = new SupermanagerPlayer();
		j.setPosicion(posicion);
		NodeList jugador = nodo.getChildNodes();
		if (jugador.item(1).getChildNodes().item(0) instanceof Element
				&& Helper.getTextContent(
						jugador.item(1).getChildNodes().item(0)).equals(
						"F I C H AL I B R E")) {
			j.setLibre(true);
			// Puede terminar puesto que no va a obtener nada más
			return j;
		}
		try {
			if (((Element) jugador.item(1).getChildNodes().item(0)) == null) {
				j.setLesionado(false);
			} else {
				j.setLesionado(((Element) jugador.item(1).getChildNodes()
						.item(0)).getAttribute("src").equals("gif/lesion.gif"));
			}
		} catch (Exception e) {

		}
		try {
			if (((Element) jugador.item(3).getChildNodes().item(0)) == null) {
				j.setNacionalidad(EnumNacionalidad.COMUNITARIO);
			} else {
				j.setNacionalidad(EnumNacionalidad
						.fromStringHTMLToNacionalidad(((Element) jugador
								.item(3).getChildNodes().item(0))
								.getAttribute("alt")));
			}
		} catch (Exception e) {

		}
		try {
			j.setNombre(Helper.getTextContent(jugador.item(5).getChildNodes()
					.item(0)));
		} catch (Exception e) {
			j.setNombre(SupermanagerPlayer.datosDesconocidos);
		}
		try {
			j.setEquipo(Helper.getTextContent(jugador.item(9)));
		} catch (Exception e) {
			j.setEquipo(SupermanagerPlayer.datosDesconocidos);
		}
		try {
			String aux = ((Element) jugador.item(5).getChildNodes().item(0))
					.getAttribute("href");
			j.setCodigoACB(aux.substring(aux.indexOf('=') + 1));
		} catch (Exception e) {
			j.setCodigoACB(SupermanagerPlayer.datosDesconocidos);
		}
		try {
			j.setMedia(Double.parseDouble(Helper.getTextContent(
					jugador.item(13)).replace(',', '.')));
		} catch (Exception e) {
			j.setMedia(Double.NEGATIVE_INFINITY);
		}
		try {
			j.setPrecio(Integer.parseInt(Helper
					.getTextContent(jugador.item(15)).replace(".", "")));
		} catch (Exception e) {
			j.setPrecio(Integer.MIN_VALUE);
		}
		try {
			j.setUltimaValoracion(Double.parseDouble(Helper.getTextContent(
					jugador.item(19)).replace(',', '.')));
		} catch (Exception e) {
			j.setUltimaValoracion(Double.NEGATIVE_INFINITY);
		}
		try {
			j.setBalance("(" + Helper.getTextContent(jugador.item(11)) + ")");
		} catch (Exception e) {
		}
		try {
			j.setMinutos(Helper.getTextContent(jugador.item(17)));
		} catch (Exception e) {
		}
		try {
			j.setValoracionUltimos3Partidos(Double.parseDouble(Helper
					.getTextContent(jugador.item(21)).replace(',', '.')));
		} catch (Exception e) {
		}
		try {
			j.setSube15(Double.parseDouble(Helper.getTextContent(
					jugador.item(23)).replace(',', '.')));
		} catch (Exception e) {
		}
		try {
			j.setSeMantiene(Double.parseDouble(Helper.getTextContent(
					jugador.item(25)).replace(',', '.')));
		} catch (Exception e) {
		}
		try {
			j.setBaja15(Double.parseDouble(Helper.getTextContent(
					jugador.item(27)).replace(',', '.')));
		} catch (Exception e) {
		}
		return j;
	}

	public static SupermanagerPlayer extraerJugadorMercadoCrearEquipo(
			Element elemento, EnumPosicion posicion) {
		// Jugador del mercado de crear equipo
		SupermanagerPlayer j = new SupermanagerPlayer();
		j.setPosicion(posicion);
		NodeList jugador = elemento.getChildNodes();
		if (jugador.item(1).getChildNodes().item(0) instanceof Element
				&& Helper.getTextContent(
						jugador.item(1).getChildNodes().item(0)).equals(
						"F I C H AL I B R E")) {
			j.setLibre(true);
			return j;
		}
		try {
			if (((Element) jugador.item(1).getChildNodes().item(0)) == null) {
				j.setLesionado(false);
			} else {
				j.setLesionado(((Element) jugador.item(1).getChildNodes()
						.item(0)).getAttribute("src").equals("gif/lesion.gif"));
			}
		} catch (Exception e) {

		}
		try {
			if (((Element) jugador.item(5).getChildNodes().item(0)) == null) {
				j.setNacionalidad(EnumNacionalidad.COMUNITARIO);
			} else {
				j.setNacionalidad(EnumNacionalidad
						.fromStringHTMLToNacionalidad(((Element) jugador
								.item(5).getChildNodes().item(0))
								.getAttribute("alt")));
			}
		} catch (Exception e) {

		}
		try {
			j.setNombre(Helper.getTextContent(jugador.item(7).getChildNodes()
					.item(0)));
		} catch (Exception e) {
			j.setNombre(SupermanagerPlayer.datosDesconocidos);
		}
		try {
			j.setEquipo(Helper.getTextContent(jugador.item(11)));
		} catch (Exception e) {
			j.setEquipo(SupermanagerPlayer.datosDesconocidos);
		}
		try {
			String aux = ((Element) jugador.item(7).getChildNodes().item(0))
					.getAttribute("href");
			j.setCodigoACB(aux.substring(aux.indexOf('=') + 1));
		} catch (Exception e) {
			j.setCodigoACB(SupermanagerPlayer.datosDesconocidos);
		}
		try {
			if (Helper.getTextContent(jugador.item(11)).contains("j&ordf;")) {
				String jornada = Helper.getTextContent(jugador.item(11));
				int inicio = jornada.indexOf(';') + 1;
				int fin = jornada.indexOf(')');
				j.setJornadaFichaje(Integer.parseInt(jornada.substring(inicio,
						fin)));
			}
		} catch (Exception e) {
			j.setJornadaFichaje(Integer.MAX_VALUE);
		}
		try {
			j.setMedia(Double.parseDouble(Helper.getTextContent(
					jugador.item(15)).replace(',', '.')));
		} catch (Exception e) {
			j.setMedia(Double.NEGATIVE_INFINITY);
		}
		try {
			j.setPrecio(Integer.parseInt(Helper
					.getTextContent(jugador.item(17)).replace(".", "")));
		} catch (Exception e) {
			j.setPrecio(Integer.MIN_VALUE);
		}
		return j;
	}

}
