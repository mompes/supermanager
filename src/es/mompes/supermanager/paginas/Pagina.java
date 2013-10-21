package es.mompes.supermanager.paginas;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

import org.w3c.dom.*;

import android.util.Log;

/**
 * 
 * Elimina o corrige algunas etiquetas html. Ofreciendo el resultado en forma de
 * �rbol XML.
 * 
 * @author Juan Mompe�n Esteban
 * 
 */
public abstract class Pagina {
	protected String pagina;

	/**
	 * Nueva p�gina.
	 * 
	 * @param npagina
	 *            Tiene que ser diferente de null.
	 */
	public Pagina(final String npagina) {
		Assert.assertNotNull(npagina);
		pagina = npagina;
	}

	public void limpiar() {
		Assert.assertNotNull(this.pagina);
		try {
			getTabla();
			limpiarOtrasCosas();
			cerrarImg();
			borrarStyles();
			borrarPrimeraCelda();
			// deleteLinks();
			borrarFont();
		} catch (Exception e) {
			Log.e("Pagina", "Limpiando " + e.getMessage());
		}
	}

	protected void limpiarOtrasCosas() {
		Assert.assertNotNull(this.pagina);
		StringBuilder builder = new StringBuilder(pagina);
		reemplazar("<br>", "", builder);
		reemplazar("<br />", "", builder);
		reemplazar("\";", "\"", builder);
		reemplazar("&", "&amp;", builder);
		reemplazar("&nbsp;", "", builder);
		reemplazar("&amp;amp;", "&amp;", builder);
		reemplazar("&amp;nbsp;", "&nbsp;", builder);
		reemplazar("©", "&copy;", builder);
		reemplazar(" align=\"right\"", "", builder);
		reemplazar(" align=\"center\"", "", builder);
		reemplazar("</font>", "", builder);
		pagina = builder.toString();
	}

	private void reemplazar(String viejo, String nuevo, StringBuilder builder) {
		int index = 0;
		while ((index = builder.indexOf(viejo, index)) != -1) {
			builder.replace(index, index + viejo.length(), nuevo);
			index += nuevo.length();
		}
	}

	protected void cerrarImg() {
		Assert.assertNotNull(this.pagina);
		StringBuilder sb = new StringBuilder();

		Boolean isInside = false;
		String imgStr = "";
		char ch = ' ';
		for (int i = 0; i < pagina.length(); i++) {
			ch = pagina.charAt(i);
			switch (ch) {
			case '<':
				imgStr = "<";
				break;
			case 'i':
				imgStr = (imgStr == "<") ? "<i" : "";
				break;
			case 'm':
				imgStr = (imgStr == "<i") ? "<im" : "";
				break;
			case 'g':
				if (imgStr == "<im") {
					isInside = true;
				}
				imgStr = "";
				break;
			case '/':
				if (isInside && pagina.charAt(i + 1) == '>') {
					isInside = false;
				}
				imgStr = "";
				break;
			case '>':
				if (isInside) {
					sb.append("/");
					isInside = false;
				}
				break;
			default:
				imgStr = "";
				break;
			}

			sb.append(ch);
		}
		pagina = sb.toString();
	}

	protected void borrarStyles() {
		Assert.assertNotNull(this.pagina);
		Pattern linkRegex = Pattern.compile(" class=\"[^\"]*\"");
		Matcher matcher = linkRegex.matcher(pagina);
		pagina = matcher.replaceAll("");
	}

	protected void borrarPrimeraCelda() {
		Assert.assertNotNull(this.pagina);
		pagina = pagina.replace("\n  <td height=\"24\"></td>", "");
	}

	protected void borrarLinks() {
		Assert.assertNotNull(this.pagina);
		Pattern linkRegex = Pattern.compile("(?i)<a([^>]+)>");
		Matcher matcher = linkRegex.matcher(pagina);
		pagina = matcher.replaceAll("");
		linkRegex = Pattern.compile("(?i)</a>");
		matcher = linkRegex.matcher(pagina);
		pagina = matcher.replaceAll("");
	}

	protected void borrarFont() {
		Assert.assertNotNull(this.pagina);
		pagina = pagina.replaceAll("<font [^>]*>", "");
		// page = page.replace("<font color=\"black\">", "")
		// .replace("</font>", "");
	}

	public abstract NodeList toXML();

	protected abstract void getTabla();

	public String toString() {
		return pagina;
	}

	public String getPagina() {
		return pagina;
	}
}
