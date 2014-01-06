package es.mompes.supermanager.paginas;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.util.Log;

public class PaginaCrearEquipo extends Pagina {
	private static String TAG = PaginaCrearEquipo.class.getName();

	public PaginaCrearEquipo(String npagina) {
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
			Log.e("PaginaEquipo", "Codificación " + e1.getMessage());
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
			Log.e("PaginaEquipo", "Parse " + e.getMessage());
			return null;
		}
		// Get a list of all elements in the document
		NodeList list = doc.getElementsByTagName("tr");
		return list;
	}

	@Override
	protected void getTabla() {
		int inicio = pagina
				.indexOf("<td width=\"21\" rowspan=\"3\" background=\"gif/tablabases.gif\">") - 5;
		int fin = pagina
				.indexOf(
						"</table>\n<table width=\"608\" cellpadding=\"0\" cellspacing=\"0\" background=\"gif/fondotablaabajo.gif\">",
						inicio);
		pagina = pagina.substring(inicio, fin);
	}

	public void limpiarExtra() {
		StringBuilder sb = new StringBuilder(pagina);
		reemplazar("&nbsp;", "", sb);
		reemplazar("nowrap", "", sb);
		reemplazar("&ordf;", "", sb);
		reemplazar("<font color=\"navy\">", "", sb);
		reemplazar(
				"<td width=\"21\" rowspan=\"3\" background=\"gif/tablabases.gif\"></td>",
				"", sb);
		reemplazar(
				"<td width=\"21\" rowspan=\"4\" background=\"gif/tablaaleros.gif\"></td>",
				"", sb);
		reemplazar(
				"<td width=\"21\" rowspan=\"4\" background=\"gif/tablapivots.gif\"></td>",
				"", sb);
		pagina = sb.toString();

	}
}
