package es.mompes.supermanager.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import es.mompes.supermanager.util.GetPageException;

/**
 * Permite descargar páginas web.
 * 
 * @author Juan Mompeán Esteban
 * 
 */
public class WebService {
	private static final String TAG = WebService.class.getName();
	private static DefaultHttpClient clienteLogueado;
	private static DefaultHttpClient clienteNoLogueado;

	/**
	 * Devuelve un DefaultHttpClient que permite la creación de hilos que
	 * trabajen con diferentes clientes simultáneamente. Este método debe ser
	 * usado siempre que se vaya a requerir el uso de hilos.
	 * 
	 * @return Un cliente que permite el uso de varios clientes simultáneamente.
	 */
	private static DefaultHttpClient getThreadSafeClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		ClientConnectionManager mgr = client.getConnectionManager();
		HttpParams params = client.getParams();

		client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
				mgr.getSchemeRegistry()), params);

		return client;
	}

	public static DefaultHttpClient getClienteLogueado() {
		if (clienteLogueado == null) {
			clienteLogueado = getThreadSafeClient();
		}
		return clienteLogueado;
	}

	public static DefaultHttpClient getClienteNoLogueado() {
		if (clienteNoLogueado == null) {
			clienteNoLogueado = getThreadSafeClient();
		}
		return clienteNoLogueado;
	}

	/**
	 * Descarga la página que se le indique en la url.
	 * 
	 * @param url
	 *            Página a descargar.
	 * @return La página descargada si hubo éxito.
	 * @throws GetPageException
	 *             Si hubo algún problema descargando la página se lanza esta
	 *             excepción.
	 */
	public final static String getPaginaLogueado(final String url,
			List<? extends NameValuePair> parametros) throws GetPageException {
		Log.d(TAG, "Descargando página: " + url + " con parámetros "
				+ parametros);
		if (parametros == null) {
			parametros = new LinkedList<NameValuePair>();
		}
		try {
			HttpPost post = new HttpPost(url);
			post.setEntity(new UrlEncodedFormEntity(parametros));
			ResponseHandler<String> res = new BasicResponseHandler();
			// Descarga la página y la almacena en respuesta
			String respuesta = getClienteLogueado().execute(post, res);
			return respuesta;
		} catch (Exception e) {
			throw new GetPageException(
					"Hubo un problema descargando la página web: " + e.getMessage());
		}
	}

	public static String getPagina(String url) throws GetPageException {
		try {
			HttpGet get = new HttpGet(url);
			get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
			get.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			get.addHeader("Accept-Language", "en,es;q=0.8,en-GB;q=0.6,ca;q=0.4");
			get.addHeader("Cache-Control", "max-age=0");
			ResponseHandler<String> res = new BasicResponseHandler();
			// Descarga la página y la almacena en respuesta
			String respuesta = getClienteNoLogueado().execute(get, res);
			//Log.d(TAG, respuesta);
			return respuesta;
		} catch (Exception e) {
			throw new GetPageException(
					"Hubo un problema descargando la página web: " + e.getMessage());
		}
	}

	/**
	 * Descarga la imagen que se le indica en la URL.
	 * 
	 * @param direccion
	 *            Dirección desde la que se descarga la imagen.
	 * @return La imagen descargada o null si se produce algún error.
	 */
	public static final Bitmap descargarImagen(URL direccion) {
		try {
			HttpURLConnection conn = (HttpURLConnection) direccion
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();

			return BitmapFactory.decodeStream(is);
		} catch (IOException e) {
			return null;
		}
	}
}
