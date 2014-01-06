package es.mompes.supermanager.paginas;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.util.Log;

public class PaginaMercadoCrearEquipo extends Pagina {
	private static final String TAG = PaginaMercadoCrearEquipo.class.getName();

	public PaginaMercadoCrearEquipo(String npagina) {
		super(npagina);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void limpiar() {
		super.limpiar();
		pagina = pagina.replaceAll("<input [^>]*>", "").replace("</input>", "")
				.replace("</table>", "");
	}

	@Override
	public NodeList toXML() {
		pagina = "<table>" + pagina + "</table>";
		InputSource is = null;
		try {
			is = new InputSource(new ByteArrayInputStream(
					pagina.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e1) {
			Log.e(TAG, "Codificación " + e1.getMessage());
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
			Log.e(TAG, "Parse " + e.getMessage());
			return null;
		}
		// Get a list of all elements in the document
		NodeList list = doc.getElementsByTagName("tr");
		return list;
	}

	@Override
	protected void getTabla() {
		int inicio = pagina.indexOf("<tr>\n   <td class=\"grisizqda\"");
		int fin = pagina
				.indexOf(
						"<table width=\"450\" cellpadding=\"0\" cellspacing=\"0\" background=\"gif/fondomercadoabajo.gif\" align=\"center\">",
						inicio);
		pagina = pagina.substring(inicio, fin);
	}
}
