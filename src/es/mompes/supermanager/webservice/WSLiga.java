package es.mompes.supermanager.webservice;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import es.mompes.supermanager.paginas.PaginaComentariosLiga;
import es.mompes.supermanager.paginas.PaginaLiga;
import es.mompes.supermanager.paginas.PaginaLigas;
import es.mompes.supermanager.util.Comentario;
import es.mompes.supermanager.util.Configuration;
import es.mompes.supermanager.util.EquipoLiga;
import es.mompes.supermanager.util.GetPageException;
import es.mompes.supermanager.util.Helper;
import es.mompes.supermanager.util.Liga;

public class WSLiga {
	private static final String RADIO = "tipo";
	private static final String COMENTARIO_USUARIO = "<td class=\"tagboardfecha\">";
	private static final String COMENTARIO_FECHA = "<td class=\"tagboardfecha\" align=\"right\">";
	private static final String COMENTARIO_TEXTO = "<td class=\"tagboard\" colspan=\"2\">";
	private static final String ENVIAR_TEXTO_NAME = "texto";
	private static final String ENVIAR_BOTON_NAME = "comentario";
	private static final String ENVIAR_BOTON_VALUE = "Añadir";

	/**
	 * Recupera todos los equipos de una liga y seg�n un tipo de
	 * clasificaci�n.
	 * 
	 * @param codigo
	 *            El c�digo de la liga a recuperar.
	 * @param valueRadio
	 *            El tipo de clasificaci�n que se solicita.
	 * @return Todos los equipos de una liga.
	 * @throws GetPageException
	 */
	public static final List<EquipoLiga> getEquiposLiga(final String codigo,
			final String valueRadio) throws GetPageException {
		List<NameValuePair> parametros = new LinkedList<NameValuePair>();
		parametros.add(new BasicNameValuePair(WSLiga.RADIO, valueRadio));
		parametros.add(new BasicNameValuePair("id_pri", codigo));
		PaginaLiga paginaLiga = new PaginaLiga(WebService.getPaginaLogueado(
				Configuration.getInstance().getProperty(
						Configuration.PRIVADAURL)
						+ "?id_pri=" + codigo, parametros));
		paginaLiga.limpiar();

		List<EquipoLiga> lista = xMLToEquipos(paginaLiga.toXML());
		return lista;
	}

	private static final List<EquipoLiga> xMLToEquipos(final NodeList nodeList) {
		if (nodeList == null) {
			return null;
		}
		List<EquipoLiga> listaEquipos = new LinkedList<EquipoLiga>();
		String nnombre, nusuario, nvaloracion;
		EquipoLiga equipoLiga;

		for (int i = 0; i < nodeList.getLength(); i++) {
			// Get element
			Element element = (Element) nodeList.item(i);
			NodeList equipo = element.getChildNodes();
			nnombre = Helper.getTextContent(equipo.item(3));
			nusuario = Helper.getTextContent(equipo.item(5));
			nvaloracion = Helper.getTextContent(equipo.item(7))
					.replace(',', '.').replace("\"", "");
			equipoLiga = new EquipoLiga(nnombre, nusuario, nvaloracion);
			listaEquipos.add(equipoLiga);
		}
		return listaEquipos;
	}

	public static final List<Liga> getTodasLigas() throws GetPageException {
		PaginaLigas paginaLigas = new PaginaLigas(WebService.getPaginaLogueado(
				Configuration.getInstance().getProperty(
						Configuration.PRIVADASURL), null));

		paginaLigas.limpiar();
		return WSLiga.xMLToLigas(paginaLigas.toXML());
	}

	private static final List<Liga> xMLToLigas(NodeList nodeList) {
		List<Liga> listaLigas = new LinkedList<Liga>();
		if (nodeList == null) {
			return listaLigas;
		}
		String nnombre;
		Long ncodigo;
		Liga liga;
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (Helper.getTextContent(nodeList.item(i)).equals("")
					| (nodeList.item(i).getChildNodes().getLength() > 0 && nodeList
							.item(i).getChildNodes().item(0).getNodeName()
							.equals("img"))) {
				continue;
			}
			// Get element
			nnombre = Helper.getTextContent(nodeList.item(i));
			ncodigo = Long.parseLong(nodeList.item(i).getAttributes()
					.getNamedItem("href").getNodeValue().substring(22));
			liga = new Liga(nnombre, ncodigo);
			listaLigas.add(liga);
		}
		return listaLigas;
	}

	/**
	 * Recupera los comentarios de una liga privada.
	 * 
	 * @param codigo
	 *            El código de la liga.
	 * @param p
	 *            La página de comentarios que se solicita.
	 * @return Una lista con los comentarios de la liga, la lista estará vacía
	 *         si no habían comentarios.
	 * @throws GetPageException
	 *             Si se produce algún error descargando los datos se lanza
	 *             esta excepción.
	 */
	public static final List<Comentario> getComentarios(long codigo,
			Integer... p) throws GetPageException {
		int pag = 1;
		if (p != null && p.length > 0) {
			pag = p[0];
		}
		// Descarga la página
		PaginaComentariosLiga pagina = new PaginaComentariosLiga(
				WebService.getPaginaLogueado(
						Configuration.getInstance().getProperty(
								Configuration.COMENTARIOS_LIGA)
								+ Long.toString(codigo) + "&p=" + pag, null));
		List<Comentario> comentarios = new LinkedList<Comentario>();
		// Analiza la página
		// Primero busca al usuario porque debería aparecer el primero
		int inicio = 0;
		while ((inicio = pagina.getPagina().indexOf(COMENTARIO_USUARIO, inicio)) != -1) {
			// Saca al usuario
			inicio += COMENTARIO_USUARIO.length();
			// Rezar para que '<' no esté contenido en el nombre del usuario
			int fin = pagina.getPagina().indexOf('<', inicio);
			String usuario = pagina.getPagina().substring(inicio, fin);
			// Busca la fecha
			inicio = pagina.getPagina().indexOf(COMENTARIO_FECHA, inicio);
			inicio += COMENTARIO_FECHA.length();
			fin = pagina.getPagina().indexOf('<', inicio);
			String fecha = pagina.getPagina().substring(inicio, fin);
			inicio = pagina.getPagina().indexOf(COMENTARIO_TEXTO, inicio);
			inicio += COMENTARIO_TEXTO.length();
			fin = pagina.getPagina().indexOf('<', inicio);
			String texto = pagina.getPagina().substring(inicio, fin);
			comentarios.add(new Comentario(texto, usuario, fecha));
		}
		return comentarios;
	}

	/**
	 * Envía un comentario a una liga privada.
	 * 
	 * @param comentario
	 *            Comentario a enviar.
	 * @param codigo
	 *            El código de la liga en la que se quiere comentar.
	 * @throws GetPageException
	 */
	public static void enviarComentario(String comentario, long codigo)
			throws GetPageException {
		// A�ade los datos
		List<NameValuePair> parametros = new LinkedList<NameValuePair>();
		parametros.add(new BasicNameValuePair(ENVIAR_TEXTO_NAME, comentario));
		parametros.add(new BasicNameValuePair(ENVIAR_BOTON_NAME, ENVIAR_BOTON_VALUE));
		// Env�a el comentario
		WebService.getPaginaLogueado(Configuration.getInstance().getProperty(
				Configuration.A�ADIR_COMENTARIO_LIGA)
				+ Long.toString(codigo), parametros);
	}
}
