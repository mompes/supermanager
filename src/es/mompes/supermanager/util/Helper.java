package es.mompes.supermanager.util;


import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;

/**
 * Provee mï¿½todos ï¿½tiles a otras clases.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public class Helper {

	/**
	 * Comprueba si hay internet.
	 * 
	 * @return True si hay internet y false en caso contrario.
	 */
	public static final boolean isThereInternet(final Context contexto) {
		ConnectivityManager cm = (ConnectivityManager) contexto
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isAvailable() && ni.isConnected();
	}

	/**
	 * Muestra un diï¿½logo con el tï¿½tulo y mensaje indicados.
	 * 
	 * @param titulo
	 *            Tï¿½tulo del diï¿½logo.
	 * @param mensaje
	 *            Mensaje del diï¿½logo.
	 * @param activity
	 *            La pantalla que quiere mostrar el diï¿½logo.
	 */
	public static final void showDialog(final Activity activity,
			final String titulo, final String mensaje) {
		Dialog d = new Dialog(activity);
		d.setTitle(titulo);
		TextView tv = new TextView(activity);
		tv.setText(mensaje);
		d.setContentView(tv);
		d.show();
	}

	/**
	 * Devuelve el contenido de un nodo.
	 * 
	 * @param node
	 *            El nodo del que se quiere conocer su contenido.
	 * @return El texto del nodo.
	 */
	public static String getTextContent(final Node node) {
		StringBuffer buffer = new StringBuffer();
		NodeList childList = node.getChildNodes();
		for (int i = 0; i < childList.getLength(); i++) {
			Node child = childList.item(i);
			if (child.getNodeType() == Node.TEXT_NODE)
				buffer.append(child.getNodeValue());
		}

		return buffer.toString();
	}
}
