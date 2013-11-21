package es.mompes.supermanager.paginas;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.util.Log;

/**
 * 
 * Corrige y elimina etiquetas html de la página de una liga. Ofreciendo el
 * resultado en forma de árbol XML.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public class PaginaLiga extends Pagina {

	public PaginaLiga(final String npagina) {
		super(npagina);
	}

	@Override
	public void limpiar() {
		super.limpiar();
	}

	@Override
	public NodeList toXML() {
		pagina = "<table>" + pagina + "</table>";
		InputSource is = null;
		try {
			is = new InputSource(new ByteArrayInputStream(
					pagina.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e1) {
			Log.e("PaginaLiga", "Codificaciï¿½n " + e1.getMessage());
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
		} catch (Exception e) {
			Log.e("PaginaLiga", "Parse " + e.getMessage());
			return null;
		}
		// Get a list of all elements in the document
		NodeList list = doc.getElementsByTagName("tr");
		return list;
	}

	@Override
	protected void getTabla() {
		int inicio = pagina.indexOf("<tr>", pagina.indexOf("marengoabajoder"));
		int fin = pagina.indexOf("</table>", inicio);
		pagina = pagina.substring(inicio, fin);
	}

}
