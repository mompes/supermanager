package es.mompes.supermanager.paginas;

import java.util.regex.Pattern;

import junit.framework.Assert;

import org.w3c.dom.*;

import android.util.Log;

/**
 * 
 * Elimina o corrige algunas etiquetas html. Ofreciendo el resultado en forma de
 * árbol XML.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public abstract class Pagina {
	protected String pagina;

	/**
	 * Nueva página.
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

	protected void reemplazar(String viejo, String nuevo, StringBuilder builder) {
		int index = 0;
		while ((index = builder.indexOf(viejo, index)) != -1) {
			builder.replace(index, index + viejo.length(), nuevo);
			index += nuevo.length();
		}
	}

	protected void cerrarImg() {
		Assert.assertNotNull(this.pagina);
		pagina = pagina.replaceAll("<img([^>]*)[^/]>", "<img $1\"/>");
	}

	protected void borrarStyles() {
		Assert.assertNotNull(this.pagina);
		pagina = Pattern.compile(" class=\"[^\"]*\"").matcher(pagina).replaceAll("");
	}

	protected void borrarPrimeraCelda() {
		Assert.assertNotNull(this.pagina);
		pagina = pagina.replace("\n  <td height=\"24\"></td>", "");
	}

	protected void borrarFont() {
		Assert.assertNotNull(this.pagina);
		pagina = pagina.replaceAll("<font [^>]*>", "");
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
