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
 * Corrige y elimina las etiquetas html de la página del mercado. Ofreciendo
 * el resultado en forma de árbol XML.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public class PaginaMercado extends Pagina {

	public PaginaMercado(final String npagina) {
		super(npagina);
	}

	@Override
	protected final void getTabla() {
		int inicio = pagina.indexOf("<tr>\n  <td class=\"grisizqda\"");
		int fin = pagina.indexOf("<table width=\"841\"", inicio);
		pagina = pagina.substring(inicio, fin);
	}

	@Override
	public NodeList toXML() {
		pagina = "<table>" + pagina + "</table>";
		InputSource is = null;
		try {
			is = new InputSource(new ByteArrayInputStream(
					pagina.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e1) {
			Log.e("PaginaMercado", "Codificaciï¿½n " + e1.getMessage());
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
			Log.e("PaginaMercado", "Parse " + e.getMessage());
			return null;
		}
		// Get a list of all elements in the document
		NodeList list = doc.getElementsByTagName("tr");
		return list;
	}

}
