package es.mompes.supermanager.paginas;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Procesa la página con las estadï¿½sticas de un jugador.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public class PaginaJugador extends Pagina {

	/**
	 * Construye un nuevo objeto para analizar la página del jugador.
	 * 
	 * @param npage
	 *            La página a analizar.
	 */
	public PaginaJugador(final String npage) {
		super(npage);
	}

	@Override
	public NodeList toXML() {

		pagina = "<table>" + pagina + "</table>";
		InputSource is = null;
		try {
			is = new InputSource(new ByteArrayInputStream(
					pagina.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e1) {
			return null;
		}
		is.setEncoding("UTF-8");
		// Create a factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// Use the factory to create a builder
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(is);
			// doc = builder.parse(file);
		} catch (ParserConfigurationException e) {
			return null;
		} catch (SAXException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		NodeList list = null;
		try {
			// Get a list of all elements in the document
			list = doc.getElementsByTagName("tr");
		} catch (Exception e) {
		}
		return list;
	}

	@Override
	protected void getTabla() {
		int inicio = pagina.indexOf("<tr>", pagina
				.indexOf("<table class=\"estadisticas2\" cellspacing=\"0\">"));
		int fin = pagina.indexOf("</table>", inicio);

		this.pagina = this.pagina.substring(inicio, fin);
	}

}
