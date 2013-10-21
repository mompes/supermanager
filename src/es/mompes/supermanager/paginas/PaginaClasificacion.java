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
 * Gestiona y procesa la p�gina de las clasificaciones. Ofreciendo el resultado
 * en forma de �rbol XML.
 * 
 * @author Juan Mompe�n Esteban
 * 
 */
public class PaginaClasificacion extends Pagina {

	/**
	 * Construye una nueva p�gina de clasificaciones.
	 * 
	 * @param npagina
	 *            La p�gina a analizar.
	 */
	public PaginaClasificacion(final String npagina) {
		super(npagina);
	}

	@Override
	public final NodeList toXML() {
		InputSource is = null;
		try {
			is = new InputSource(new ByteArrayInputStream(
					this.pagina.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e1) {
			Log.e("PaginaClasificacion", "Codificaci�n " + e1.getMessage());
			return null;
		}
		is.setEncoding("UTF-8");
		// Crea un factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(is);
		} catch (Exception e) {
			Log.e("PaginaClasificacion", "Parse " + e.getMessage());
			return null;
		}
		// Recupera la lista de todos los elementos del documento
		NodeList list = doc.getElementsByTagName("tr");
		return list;
	}

	@Override
	public final void limpiar() {
		super.limpiar();
		this.limpiarExtra();
	}

	/**
	 * Elimina los campos input no cerrados.
	 */
	private void limpiarExtra() {
		this.pagina = this.pagina.replaceAll("<input [^>]*>", "")
				.replaceAll("<form [^>]*>", "").replace("</form>", "")
				.replace("<font>", "").replaceAll("<font [^>]*>", "")
				.replace("</font>", "").replace("<b>", "").replace("</b>", "");
	}

	@Override
	protected final void getTabla() {
		this.pagina = this.pagina
				.substring(this.pagina
						.indexOf("<table width=\"609\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"));
		this.pagina = this.pagina
				.substring(
						0,
						this.pagina
								.indexOf("<table width=\"609\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n<tr>\n   <td background=\"gif/clasificacionpie.gif\" height=\"51\"><img src=\"gif/pixel.gif\" width=\"1\" height=\"1\" border=\"0\"></td>"));
		this.pagina = this.pagina.replaceAll("<table [^>]*>", "");
		this.pagina = "<table>" + this.pagina.replaceAll("</table>", "")
				+ "</table>";
	}

}
