package es.mompes.supermanager.webservice;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import es.mompes.supermanager.paginas.PaginaClasificacion;
import es.mompes.supermanager.util.Configuration;
import es.mompes.supermanager.util.EnumClasificacion;
import es.mompes.supermanager.util.EnumZona;
import es.mompes.supermanager.util.EquipoLiga;
import es.mompes.supermanager.util.GetPageException;

/**
 * Gestiona el acceso a los datos de las clasificaciones.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public class WSClasificacion {
	/**
	 * Contiene el nombre del radio para indicar la zona seleccionado.
	 */
	private static final String RADIO_AMBITO = "ambito";
	/**
	 * Contiene el nombre del radio para indicar el tipo seleccionado.
	 */
	private static final String RADIO_TIPO = "tipo";

	/**
	 * Devuelve una lista con todos los equipos en el top para esa zona y ese
	 * tipo de clasificación.
	 * 
	 * @param zona
	 *            La zona sobre la que se consulta.
	 * @param clasificacion
	 *            El tipo de clasificación sobre el que se consulta.
	 * @return Una lista con todos los equipos según los datos introducidos.
	 * @throws GetPageException
	 *             Si no se puede descargar la página se genera un error.
	 */
	public static final List<EquipoLiga> getTop(final EnumZona zona,
			final EnumClasificacion clasificacion) throws GetPageException {
		// Añade a la petición de página la opción seleccionada
		List<NameValuePair> parametros = new LinkedList<NameValuePair>();
		parametros.add(new BasicNameValuePair(WSClasificacion.RADIO_AMBITO,
				zona.toTopRadio()));
		parametros.add(new BasicNameValuePair(WSClasificacion.RADIO_TIPO,
				clasificacion.toTopRadio()));
		// Descarga la página
		PaginaClasificacion pageTop = new PaginaClasificacion(
				WebService.getPaginaLogueado(Configuration.getInstance()
						.getProperty(Configuration.CLASIFICACIONESURL),
						parametros));
		// Limpia la página
		pageTop.limpiar();

		return WSClasificacion.xMLToTeams(pageTop.toXML(), clasificacion);
	}

	/**
	 * Analiza el árbol XML y extrae los equipos.
	 * 
	 * @param nodeList
	 *            árbol XML.
	 * @param clasificacion
	 *            Que tipo de clasificación se va a mostrar.
	 * @return Equipos extraídos.
	 */
	private static List<EquipoLiga> xMLToTeams(final NodeList nodeList,
			final EnumClasificacion clasificacion) {
		List<EquipoLiga> teamList = new LinkedList<EquipoLiga>();
		String nname, nuser, nvalue;
		EquipoLiga teamLeague;
		if (nodeList == null) {
			return teamList;
		}
		// Recorre la lista de equipos
		int inicio = 17;
		for (int i = inicio; i < nodeList.getLength() && i < (inicio + 25); i++) {
			// Recupera el elemento
			Element element = (Element) nodeList.item(i);
			NodeList team = element.getChildNodes();
			if (clasificacion != EnumClasificacion.HISTORICO) {
				nname = team.item(3).getTextContent();
				nuser = team.item(5).getTextContent();
				nvalue = team.item(7).getTextContent().replace(',', '.')
						.replace("\"", "");
			} else {
				nname = team.item(3).getTextContent();
				nuser = team.item(7).getTextContent();
				nvalue = team.item(5).getTextContent().replace(',', '.')
						.replace("\"", "");
			}
			teamLeague = new EquipoLiga(nname, nuser, nvalue);
			teamList.add(teamLeague);
		}
		return teamList;
	}
}
