package es.mompes.supermanager.webservice;

import es.mompes.supermanager.util.Configuration;

public class WSSupermanagerJugador {

	/**
	 * Recupera el id de un jugador en la ventana de fichar.
	 * 
	 * @param nombre
	 * @param acceso
	 * @return El id del jugador si fue posible encontrarlo o null si no fue
	 *         posible.
	 */
	public static final String getIdJugadorFichar(String nombre,
			String idEquipo, String posicion) {
		String pagina = null;
		try {
			pagina = WebService.getPaginaLogueado(
					Configuration.getInstance().getProperty(
							Configuration.FICHAR1)
							+ idEquipo
							+ Configuration.getInstance().getProperty(
									Configuration.FICHAR2) + posicion, null);
		} catch (Exception e) {
			return null;
		}
		try {
			pagina = pagina.substring(pagina.indexOf(nombre));
			pagina = pagina.substring(pagina.indexOf("value=") + 7);
			pagina = pagina.substring(0, pagina.indexOf("\""));
		} catch (Exception e) {
			return null;
		}
		return pagina;
	}

}
