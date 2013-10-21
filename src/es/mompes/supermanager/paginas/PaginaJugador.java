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
 * Procesa la p�gina con las estad�sticas de un jugador.
 * 
 * @author Juan Mompe�n Esteban
 * 
 */
public class PaginaJugador extends Pagina {

	/**
	 * Construye un nuevo objeto para analizar la p�gina del jugador.
	 * 
	 * @param npage
	 *            La p�gina a analizar.
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
		this.pagina = this.pagina.substring(this.pagina
				.indexOf("<table class=\"estadisticas2\" cellspacing=\"0\">"));
		this.pagina = this.pagina.substring(0, this.pagina.indexOf("</table>"));
		this.pagina = this.pagina.substring(this.pagina.indexOf("<tr>"));
	}

}
