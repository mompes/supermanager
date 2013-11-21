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
 * Corrige y elimina las etiquetas html de la página de las ligas. Ofreciendo
 * el resultado en forma de árbol XML.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public class PaginaLigas extends Pagina {

	public PaginaLigas(final String npagina) {
		super(npagina);
	}

	@Override
	public void limpiar() {
		super.limpiar();
		limpiarExtra();
	}

	@Override
	public NodeList toXML() {
		pagina = "<table>" + pagina + "</table>";
		InputSource is = null;
		try {
			is = new InputSource(new ByteArrayInputStream(
					pagina.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e1) {
			Log.e("PaginaLigas", "Codificaciï¿½n " + e1.getMessage());
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
		} catch (Exception e) {
			Log.e("PaginaLigas", "Parse " + e.getMessage());
			return null;
		}
		// Get a list of all elements in the document
		NodeList list = doc.getElementsByTagName("a");
		return list;
	}

	@Override
	protected void getTabla() {
		int inicio = pagina
				.lastIndexOf("<tr>\n  <td height=\"25\" background=\"gif/ligaprivada.gif\">");
		int fin = pagina.indexOf("</table>", inicio);
		pagina = pagina.substring(inicio, fin);
	}

	public final void limpiarExtra() {
		pagina = pagina
				.replace(
						"<img src=\"gif/pixel.gif\" width=\"10\" height=\"1\" border=\"0\"/>",
						"")
				.replace(
						"<img src=\"gif/ampliar.gif\" width=\"63\" height=\"18\" border=\"0\"/>",
						"");
	}
}
