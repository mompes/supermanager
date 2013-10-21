package es.mompes.supermanager.util;

import java.util.List;

import android.os.Handler;
import android.os.Message;

import es.mompes.supermanager.webservice.WSEstadisticasJugador;

public class HelperEstadisticasJugador extends Thread {
	private final static String TAG = HelperEstadisticasJugador.class.getName();
	private SupermanagerPlayer jugador;
	private Handler handlerPadre;

	public HelperEstadisticasJugador(final SupermanagerPlayer njugador,
			final Handler nhandlerPadre) {
		super();
		this.jugador = njugador;
		this.handlerPadre = nhandlerPadre;
	}

	@Override
	public final void run() {
		Message msg = new Message();
		msg.what = 1;
		List<EstadisticasJugador> estadisticas = WSEstadisticasJugador
				.getEstadisticas(jugador.getCodigoACB());
		if (estadisticas != null && !estadisticas.isEmpty()) {
			jugador.setMinutos(estadisticas.get(estadisticas.size() - 1)
					.getMinutos());
			jugador.setBaja15(EstadisticasJugador.necesarioVariar(
					jugador.getPrecio(), jugador.getMedia(),
					EstadisticasJugador.numeroPartidosJugados(estadisticas),
					0.85));
			jugador.setSube15(EstadisticasJugador.necesarioVariar(
					jugador.getPrecio(), jugador.getMedia(),
					EstadisticasJugador.numeroPartidosJugados(estadisticas),
					1.15));
			jugador.setSeMantiene(EstadisticasJugador.necesarioVariar(
					jugador.getPrecio(), jugador.getMedia(),
					EstadisticasJugador.numeroPartidosJugados(estadisticas),
					1.00));
		}
		this.handlerPadre.sendMessage(msg);
	}
}
