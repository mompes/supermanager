package es.mompes.supermanager.webservice;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.util.Log;
import es.mompes.supermanager.paginas.Pagina;
import es.mompes.supermanager.paginas.PaginaCrearEquipo;
import es.mompes.supermanager.paginas.PaginaEquipo;
import es.mompes.supermanager.paginas.PaginaEquipos;
import es.mompes.supermanager.util.Configuration;
import es.mompes.supermanager.util.EnumNacionalidad;
import es.mompes.supermanager.util.EnumPosicion;
import es.mompes.supermanager.util.EquipoACB;
import es.mompes.supermanager.util.EquipoSupermanager;
import es.mompes.supermanager.util.GetPageException;
import es.mompes.supermanager.util.SupermanagerPlayer;

public class WSEquipoSupermanager {
	private static final String TAG = WSEquipoSupermanager.class.getName();

	public static List<SupermanagerPlayer> getJugadoresVistaEquipo(
			int codigoEquipo) {
		List<SupermanagerPlayer> jugadores = new LinkedList<SupermanagerPlayer>();
		Pagina pagina = null;
		try {
			pagina = new PaginaEquipo(WebService.getPaginaLogueado(
					Configuration.getInstance().getProperty(
							Configuration.EQUIPO)
							+ codigoEquipo, null));
		} catch (GetPageException e) {
			Log.e(TAG, "Error descargando p醙ina de equipo: " + codigoEquipo
					+ ", error: " + e.getMessage());
			return jugadores;
		}
		pagina.limpiar();
		NodeList nodeList = pagina.toXML();
		if (nodeList != null) {
			EnumPosicion posicion;
			for (int i = 0; i < (nodeList.getLength() - 1); i++) {
				// Get element
				Element jugador = (Element) nodeList.item(i);
				if (i < 3) {
					posicion = EnumPosicion.BASE;
				} else if (i < 7) {
					posicion = EnumPosicion.ALERO;
				} else {
					posicion = EnumPosicion.PIVOT;
				}
				jugadores.add(extraerJugadorVistaEquipo(jugador, posicion));
			}
		}
		return jugadores;
	}

	public static List<SupermanagerPlayer> getJugadoresVistaCrearEquipo(
			StringBuilder nombre) {
		Pagina pagina = null;
		List<SupermanagerPlayer> jugadores = new LinkedList<SupermanagerPlayer>();
		try {
			pagina = new PaginaCrearEquipo(WebService.getPaginaLogueado(
					Configuration.getInstance().getProperty(
							Configuration.NUEVO_EQUIPO), null));
		} catch (GetPageException e) {
			Log.e(TAG,
					"Error descargando p醙ina crear equipo: " + e.getMessage());
			return jugadores;
		}
		// Recupera el nombre del equipo
		int index = pagina.getPagina().indexOf("name=\"nombre\"");
		index = pagina.getPagina().indexOf("value", index)
				+ "value=\"".length();
		nombre.append(pagina.getPagina().substring(index,
				pagina.getPagina().indexOf("\"", index)));
		pagina.limpiar();
		NodeList nodeList = pagina.toXML();
		if (nodeList != null) {
			EnumPosicion posicion;
			for (int i = 0; i < (nodeList.getLength() - 1); i++) {
				// Get element
				Element jugador = (Element) nodeList.item(i);
				if (i < 3) {
					posicion = EnumPosicion.BASE;
				} else if (i < 7) {
					posicion = EnumPosicion.ALERO;
				} else {
					posicion = EnumPosicion.PIVOT;
				}
				jugadores
						.add(extraerJugadorVistaCrearEquipo(jugador, posicion));
			}
		}
		return jugadores;
	}

	/**
	 * Comprueba si a un equipo le quedan cambios por realizar.
	 * 
	 * @param equipo
	 * @return
	 */
	public static boolean quedanCambios(EquipoSupermanager equipo) {
		String pagina = null;
		try {
			pagina = WebService.getPaginaLogueado(Configuration.getInstance()
					.getProperty(Configuration.EQUIPO) + equipo.getCodigo(),
					null);
		} catch (GetPageException e) {
			return false;
		}
		return pagina.contains("<a href=\"#\" onclick=\"comprueba2(\'");
	}

	/**
	 * Devuelve un array que muestra si un equipo gan贸 en las jornadas en las
	 * que se pregunta. Los resultados se dan en el mismo orden en el que se
	 * pregunt贸.
	 * 
	 * @param jornadas
	 *            Jornadas sobre las que se quiere saber si el equipo gan贸.
	 * @param Equipo
	 *            Equipo sobre el que se desea sabe si gan贸. El "nombre corto"
	 *            del equipo.
	 * @return Array que contiene true si el equipo gan贸 en la jornada
	 *         respectiva en el array de jornadas y false si perdi贸.
	 */
	public final static boolean[] getResultadosEquipo(final int[] jornadas,
			final String equipo) {
		boolean[] resultados = new boolean[jornadas.length];
		try {
			String pagina = WebService.getPaginaLogueado(
					Configuration.getInstance().getProperty(
							"resultadosEquipos1")
							+ ((jornadas[0] == 0) ? jornadas[1] : jornadas[0])
							+ Configuration.getInstance().getProperty(
									"resultadosEquipos2")
							+ jornadas[jornadas.length - 1]
							+ Configuration.getInstance().getProperty(
									"resultadosEquipos3"), null);
			for (int i = 0; i < jornadas.length; i++) {
				if (jornadas[i] == 0) {
					continue;
				}
				int posicionJornada = pagina.indexOf("JORNADA " + jornadas[i]
						+ "<");
				// Los nombres del equipo a lo largo de la temporada
				String[] nombres = EquipoACB.nombreLargo(equipo);
				// Almacena la posici贸n para esta jornada
				int minNombre = Integer.MAX_VALUE;
				// Almacena la posici贸n del nombre m谩s cercano
				int nombreCercano = 0;
				for (int j = 0; j < nombres.length; j++) {
					if (pagina.indexOf(nombres[j], posicionJornada) >= 0
							& pagina.indexOf(nombres[j], posicionJornada) < minNombre) {
						minNombre = pagina.indexOf(nombres[j], posicionJornada);
						nombreCercano = j;
					}
				}
				// Indica si un equipo jug贸 en casa o fuera
				boolean enCasa = false;
				// Comprueba si el equipo jug贸 en casa o fuera
				if (pagina.charAt(minNombre + nombres[nombreCercano].length()
						+ 1) == '-') {
					enCasa = true;
				}
				// Veamos qui茅n gan贸
				int blanco2 = pagina.indexOf("blanco2", minNombre);
				String puntos1 = "", puntos2 = "";
				int iterador = 2;
				while (pagina.charAt(blanco2 + "blanco2".length() + iterador) != ' ') {
					puntos1 += pagina.charAt(blanco2 + "blanco2".length()
							+ iterador);
					iterador++;
				}
				int guion = pagina.indexOf('-', blanco2);
				iterador = 2;
				while (pagina.charAt(guion + iterador) != '<') {
					puntos2 += pagina.charAt(guion + iterador);
					iterador++;
				}
				int resultado1, resultado2;
				resultado1 = Integer.parseInt(puntos1);
				resultado2 = Integer.parseInt(puntos2);
				resultados[i] = (enCasa & (resultado1 > resultado2))
						| (!enCasa & (resultado1 < resultado2));
			}
		} catch (Exception e) {
		}
		return resultados;
	}

	public static boolean ficharJugador(String idJugador, String posicion,
			String codigoEquipo) {
		List<NameValuePair> parametros = new LinkedList<NameValuePair>();
		parametros.add(new BasicNameValuePair("id_jug", idJugador));
		parametros.add(new BasicNameValuePair("posicion", posicion));
		parametros.add(new BasicNameValuePair("id_equ", codigoEquipo));
		parametros.add(new BasicNameValuePair("control", "1"));
		try {
			// Hace efectiva la compra
			WebService.getPaginaLogueado(Configuration.getInstance()
					.getProperty(Configuration.MERCADO), parametros);
		} catch (GetPageException e) {
			return false;
		}
		return true;
	}

	public static boolean venderJugador(String codigoEquipo,
			String codigoJugador) {
		try {
			// Vende al jugador
			WebService.getPaginaLogueado(
					Configuration.getInstance().getProperty(
							Configuration.VENDER1)
							+ codigoEquipo
							+ Configuration.getInstance().getProperty(
									Configuration.VENDER2) + codigoJugador,
					null);
		} catch (GetPageException e) {
			return false;
		}
		return true;
	}

	public static boolean sustituirJugador(String codigoEquipo,
			String codigoJugador) {
		try {
			WebService.getPaginaLogueado(
					Configuration.getInstance().getProperty(
							Configuration.SUSTITUIR1)
							+ codigoEquipo
							+ Configuration.getInstance().getProperty(
									Configuration.SUSTITUIR2) + codigoJugador,
					null);
			// Recarga la p谩gina del equipo, si no se recarga dicha p谩gina
			// no es posible hacer m谩s de una anulaci贸n seguidas
			WebService.getPaginaLogueado(Configuration.getInstance()
					.getProperty(Configuration.EQUIPO) + codigoEquipo, null);
		} catch (GetPageException e) {
			return false;
		}
		return true;
	}

	public static boolean anularCambio(String codigoEquipo, String codigoJugador) {
		// Vende al jugador
		try {
			WebService.getPaginaLogueado(
					Configuration.getInstance().getProperty(
							Configuration.ANULAR1)
							+ codigoEquipo
							+ Configuration.getInstance().getProperty(
									Configuration.ANULAR2) + codigoJugador,
					null);
			List<NameValuePair> parametros = new LinkedList<NameValuePair>();
			parametros.add(new BasicNameValuePair("control", "1"));
			parametros.add(new BasicNameValuePair("id_equ", codigoEquipo));
			parametros.add(new BasicNameValuePair("posicion", codigoJugador));

			WebService.getPaginaLogueado(Configuration.getInstance()
					.getProperty(Configuration.ANULAR3), parametros);
			// Recarga la p醙ina del equipo, si no se recarga dicha p醙ina
			// no es posible hacer m醩 de una anulaci髇 seguidas
			WebService.getPaginaLogueado(Configuration.getInstance()
					.getProperty(Configuration.EQUIPO) + codigoEquipo, null);
		} catch (GetPageException e) {
			return false;
		}
		return true;
	}

	public static boolean anularTodosLosCambios(String codigoEquipo) {
		try {
			WebService.getPaginaLogueado(Configuration.getInstance()
					.getProperty(Configuration.ANULARTODOS) + codigoEquipo,
					null);
			List<NameValuePair> parametros = new LinkedList<NameValuePair>();
			parametros.add(new BasicNameValuePair("control", "1"));
			parametros.add(new BasicNameValuePair("id_equ", codigoEquipo));
			WebService.getPaginaLogueado(Configuration.getInstance()
					.getProperty(Configuration.ANULARTODOS2), parametros);
		} catch (GetPageException e) {
			return false;
		}
		return true;
	}

	/**
	 * Recupera todos los equipos del jugador, si se produjera alg煤n problema
	 * recuperando los equipos devuelve null.
	 * 
	 * @return Una lista de los equipos si todo fue bien y null si hubo alg煤n
	 *         problema.
	 */
	public static List<EquipoSupermanager> getTodosEquipos() {
		List<EquipoSupermanager> equipos = new LinkedList<EquipoSupermanager>();
		PaginaEquipos teamsPage = null;
		try {
			teamsPage = new PaginaEquipos(WebService.getPaginaLogueado(
					Configuration.getInstance().getProperty(
							Configuration.EQUIPOSURL), null));
		} catch (Exception e) {
			Log.e(TAG, "Error descargando p醙ina: " + e.getMessage());
			return null;
		}
		teamsPage.limpiar();
		NodeList nodeList = teamsPage.toXML();
		if (nodeList != null) {
			for (int i = 1; i < nodeList.getLength(); i++) {
				equipos.add(extraerEquipo((Element) nodeList.item(i)));
			}
		}
		return equipos;
	}

	public static boolean crearEquipo(final String nombre) {
		List<NameValuePair> parametros = new LinkedList<NameValuePair>();
		parametros.add(new BasicNameValuePair("nombre", nombre));
		parametros.add(new BasicNameValuePair("CMD", "1"));
		try {
			WebService.getPaginaLogueado(Configuration.getInstance()
					.getProperty(Configuration.NUEVO_EQUIPO), parametros);
		} catch (GetPageException e) {
			Log.e(TAG, "Error creando equipo: " + e.getMessage());
			return false;
		}
		parametros = new LinkedList<NameValuePair>();
		parametros.add(new BasicNameValuePair("COMPLETO", "1"));
		try {
			WebService.getPaginaLogueado(Configuration.getInstance()
					.getProperty(Configuration.NUEVO_EQUIPO), parametros);
		} catch (GetPageException e) {
			Log.e(TAG, "Error creando equipo: " + e.getMessage());
			return false;
		}
		return true;
	}

	private static EquipoSupermanager extraerEquipo(Element elemento) {
		int codigo, broker, posicionGeneral, posicionBroker, posicionJornada;
		float valoracionTotal, ultimaValoracion;
		boolean baja, lesion;
		NodeList nodoEquipo;
		String nombre, pocosJugadores = "";
		baja = false;
		lesion = false;
		pocosJugadores = "";
		// Get element
		nodoEquipo = elemento.getChildNodes();
		codigo = Integer.parseInt(nodoEquipo.item(1).getTextContent());
		nombre = nodoEquipo.item(3).getChildNodes().item(1).getTextContent();
		if (nodoEquipo.item(3).getChildNodes().getLength() > 2
				&& nodoEquipo.item(3).getChildNodes().item(2).getNodeValue()
						.contains("jug")) {
			try {
				pocosJugadores = nodoEquipo.item(3).getChildNodes().item(2)
						.getNodeValue().trim();
			} catch (Exception e) {
				pocosJugadores = "";
			}
		}
		if (nodoEquipo.item(3).getChildNodes().getLength() > 3
				&& nodoEquipo.item(3).getChildNodes().item(3).getAttributes()
						.getNamedItem("src").getNodeValue()
						.equals("gif/lesion.gif")) {
			lesion = true;
			if (nodoEquipo.item(3).getChildNodes().getLength() > 4
					&& nodoEquipo.item(3).getChildNodes().item(4)
							.getAttributes().getNamedItem("src").getNodeValue()
							.equals("gif/baja.gif")) {
				baja = true;
			}
		} else if (nodoEquipo.item(3).getChildNodes().getLength() > 3
				&& nodoEquipo.item(3).getChildNodes().item(3).getAttributes()
						.getNamedItem("src").getNodeValue()
						.equals("gif/baja.gif")) {
			baja = true;
		}
		posicionJornada = Integer.parseInt(helperParentheses2(nodoEquipo
				.item(5).getTextContent()));
		posicionGeneral = Integer.parseInt(helperParentheses2(nodoEquipo
				.item(7).getTextContent()));
		posicionBroker = Integer.parseInt(helperParentheses2(nodoEquipo.item(9)
				.getTextContent()));
		ultimaValoracion = Float.parseFloat(helperParentheses1(nodoEquipo.item(
				5).getTextContent()));
		broker = Integer.parseInt(helperParentheses1(nodoEquipo.item(9)
				.getTextContent()));
		valoracionTotal = Float.parseFloat(helperParentheses1(nodoEquipo
				.item(7).getTextContent()));
		EquipoSupermanager equipo = new EquipoSupermanager(codigo, nombre,
				broker, posicionGeneral, posicionBroker, valoracionTotal,
				ultimaValoracion, posicionJornada, baja, lesion);
		if (!pocosJugadores.equals("")) {
			equipo.setPocosJugadores(pocosJugadores);
		}
		return equipo;
	}

	/**
	 * Extrae el valor de antes del primer espacio y elimina los "." y las ",".
	 * 
	 * @param cadena
	 *            La cadena a parsear.
	 * @return El resultado.
	 */
	private static String helperParentheses1(final String cadena) {
		return cadena.substring(0, cadena.indexOf(' ')).replace(".", "")
				.replace(",", ".");
	}

	/**
	 * Extrae el valor entre los par锟絥tesis.
	 * 
	 * @param cadena
	 *            La cadena a analizar.
	 * @return La nueva cadena.
	 */
	private static String helperParentheses2(final String cadena) {
		return cadena.substring(cadena.indexOf('(') + 1,
				cadena.indexOf(')') - 1);
	}

	private static SupermanagerPlayer extraerJugadorVistaCrearEquipo(
			Element elemento, EnumPosicion posicion) {
		SupermanagerPlayer j = new SupermanagerPlayer();
		j.setPosicion(posicion);
		NodeList jugador = elemento.getChildNodes();
		if (jugador.item(1).getChildNodes().item(0) instanceof Element
				&& jugador.item(1).getChildNodes().item(0).getTextContent()
						.equals("F I C H AL I B R E")) {
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
			j.setNombre(jugador.item(5).getChildNodes().item(0)
					.getTextContent());
		} catch (Exception e) {
			j.setNombre(SupermanagerPlayer.datosDesconocidos);
		}
		try {
			j.setEquipo(jugador.item(7).getTextContent());
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
			if (jugador.item(11).getTextContent().contains("j&ordf;")) {
				String jornada = jugador.item(11).getTextContent();
				int inicio = jornada.indexOf(';') + 1;
				int fin = jornada.indexOf(')');
				j.setJornadaFichaje(Integer.parseInt(jornada.substring(inicio,
						fin)));
			}
		} catch (Exception e) {
			j.setJornadaFichaje(Integer.MAX_VALUE);
		}
		try {
			j.setMedia(Double.parseDouble(jugador.item(13).getTextContent()
					.replace(',', '.')));
		} catch (Exception e) {
			j.setMedia(Double.NEGATIVE_INFINITY);
		}
		try {
			j.setPrecio(Integer.parseInt(jugador.item(9).getTextContent()
					.replace(".", "")));
		} catch (Exception e) {
			j.setPrecio(0);
		}
		return j;
	}

	private static SupermanagerPlayer extraerJugadorVistaEquipo(
			Element elemento, EnumPosicion posicion) {
		SupermanagerPlayer j = new SupermanagerPlayer();
		j.setPosicion(posicion);
		NodeList jugador = elemento.getChildNodes();
		if (jugador.item(1).getChildNodes().item(0) instanceof Element
				&& jugador.item(1).getChildNodes().item(0).getTextContent()
						.equals("F I C H AL I B R E")) {
			j.setLibre(true);
			// No se puede terminar aqu铆 porque tiene que comprobar si se puede
			// anular el cambio
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
			j.setNombre(jugador.item(5).getChildNodes().item(0)
					.getTextContent());
		} catch (Exception e) {
			j.setNombre(SupermanagerPlayer.datosDesconocidos);
		}
		try {
			j.setEquipo(jugador.item(9).getTextContent());
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
			if (jugador.item(11).getTextContent().contains("j&ordf;")) {
				String jornada = jugador.item(11).getTextContent();
				int inicio = jornada.indexOf(';') + 1;
				int fin = jornada.indexOf(')');
				j.setJornadaFichaje(Integer.parseInt(jornada.substring(inicio,
						fin)));
			}
		} catch (Exception e) {
			j.setJornadaFichaje(Integer.MAX_VALUE);
		}
		try {
			j.setMedia(Double.parseDouble(jugador.item(19).getTextContent()
					.replace(',', '.')));
		} catch (Exception e) {
			j.setMedia(Double.NEGATIVE_INFINITY);
		}
		try {
			j.setPrecio(Integer.parseInt(jugador.item(13).getTextContent()
					.replace(".", "")));
		} catch (Exception e) {
			j.setPrecio(Integer.MIN_VALUE);
		}
		try {
			j.setUltimaValoracion(Double.parseDouble(jugador.item(15)
					.getTextContent().replace(',', '.')));
		} catch (Exception e) {
			j.setUltimaValoracion(Double.NEGATIVE_INFINITY);
		}
		try {
			if (jugador.item(21) != null && jugador.item(21) instanceof Element) {
				NodeList hijos1 = jugador.item(21).getChildNodes();
				if (hijos1.item(0) != null && hijos1.item(0) instanceof Element) {
					NodeList hijos2 = hijos1.item(0).getChildNodes();
					if (hijos2.item(0) != null
							&& hijos2.item(0) instanceof Element) {
						j.setSustituir(((Element) hijos2.item(0)).getAttribute(
								"src").equals("gif/sustituir.gif"));
					}
				}
			}
			if (jugador.item(23) != null && jugador.item(23) instanceof Element) {
				NodeList hijos1 = jugador.item(23).getChildNodes();
				if (hijos1.item(0) != null && hijos1.item(0) instanceof Element) {
					NodeList hijos2 = hijos1.item(0).getChildNodes();
					if (hijos2.item(0) != null
							&& hijos2.item(0) instanceof Element) {
						j.setAnular(((Element) hijos2.item(0)).getAttribute(
								"src").equals("gif/anular.gif"));
					}
				}
			} else if (jugador.item(5) != null
					&& jugador.item(5) instanceof Element) {
				NodeList hijos1 = jugador.item(5).getChildNodes();
				if (hijos1.item(0) != null && hijos1.item(0) instanceof Element) {
					NodeList hijos2 = hijos1.item(0).getChildNodes();
					if (hijos2.item(0) != null
							&& hijos2.item(0) instanceof Element) {
						j.setAnular(((Element) hijos2.item(0)).getAttribute(
								"src").equals("gif/anular.gif"));
					}
				}
			}
		} catch (Exception e) {

		}
		return j;
	}
}
