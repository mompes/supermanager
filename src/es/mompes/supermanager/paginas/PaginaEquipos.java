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
 * Elimina y corrige el html de la página de los equipos. Ofreciendo el
 * resultado en forma de árbol XML.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public class PaginaEquipos extends Pagina {
	public PaginaEquipos(final String npagina) {
		super(npagina);
	}

	@Override
	public void limpiar() {
		super.limpiar();
		limpiaExtra();
	}

	@Override
	protected void getTabla() {
		int inicio = pagina
				.indexOf("<tr>\n  <td height=\"25\" class=\"grisizqda\" style=\"text-align:right;\">");
		int fin = pagina
				.indexOf(
						"<tr>\n   <td colspan=\"6\" background=\"gif/tusequiposabajo2.gif\" height=\"14\">",
						inicio);
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
			Log.e("PaginaEquipos", "Codificaciï¿½n " + e1.getMessage());
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
			Log.e("PaginaEquipos", "Parse " + e.getMessage());
			return null;
		}
		// Get a list of all elements in the document
		NodeList list = doc.getElementsByTagName("tr");
		return list;

	}

	public void limpiaExtra() {
		pagina = pagina.replace("&nbsp;", "");
	}
}
