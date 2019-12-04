package com.studentdefender.conexion;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.studentdefender.utils.Etapas;
import com.studentdefender.utils.Global;

public class HiloServidor extends Thread {

	private DatagramSocket conexion;
	public boolean fin = false;
	private int cantMax = 2;
	private InetAddress[] ips = new InetAddress[cantMax];
	private int[] puertos = new int[cantMax];

	public HiloServidor() {
		try {
			conexion = new DatagramSocket(9000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		do {
			esperarMensajes();
		} while (!fin);
	}

	private void esperarMensajes() {
		byte[] datos = new byte[4096];
		DatagramPacket paquete = new DatagramPacket(datos, datos.length);
		try {
			conexion.receive(paquete);
		} catch (IOException e) {
			e.printStackTrace();
		}
		procesarMensaje(paquete);
	}

	private void procesarMensaje(DatagramPacket paquete) {
		Object objeto = UtilConexion.bytesToObject(paquete);

		Mensaje mensaje = (Mensaje) objeto;

		if (mensaje.jugador != -1) {
			Global.mensajesJugadores[mensaje.jugador] = mensaje.mensaje;
		}

		if (Global.etapa == Etapas.CONEXION && mensaje.mensaje.equals("conexion")) {
			boolean jugadorExiste = false;
			for (int i = 0; i < Global.cantJugadores; i++) {
				if (paquete.getAddress().equals(ips[i]) && paquete.getPort() == puertos[i])
					jugadorExiste = true;
			}
			if (!jugadorExiste) {
				if (Global.cantJugadores < cantMax) {
					ips[Global.cantJugadores] = paquete.getAddress();
					puertos[Global.cantJugadores] = paquete.getPort();
					System.out.println("Cliente " + ips[Global.cantJugadores] + " puerto "
							+ puertos[Global.cantJugadores] + " conectado");
					enviarMensaje("conectado", Global.cantJugadores);
					Global.cantJugadores++;
					System.out.println(Global.cantJugadores);
				} else {
					enviarMensaje("Server lleno", paquete.getAddress(), paquete.getPort());
				}
			} else {
				enviarMensaje("Ya se encuentra conectado", paquete.getAddress(), paquete.getPort());
			}
		}
	}

	public void enviarMensaje(Object mensaje) {
		byte[] datos = UtilConexion.objectToBytes(new Mensaje(mensaje));
		for (int i = 0; i < Global.cantJugadores; i++) {
			DatagramPacket paquete = new DatagramPacket(datos, datos.length, ips[i], puertos[i]);
			try {
				conexion.send(paquete);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void enviarMensaje(Object mensaje, int jugador) {
		enviarMensaje(mensaje, ips[jugador], puertos[jugador], jugador);
	}

	private void enviarMensaje(Object mensaje, InetAddress ip, int puerto) {
		enviarMensaje(mensaje, ip, puerto, -1);
	}

	private void enviarMensaje(Object mensaje, InetAddress ip, int puerto, int jugador) {
		byte[] datos = UtilConexion.objectToBytes(new Mensaje(jugador, mensaje));
		DatagramPacket paquete = new DatagramPacket(datos, datos.length, ip, puerto);
		try {
			conexion.send(paquete);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reiniciar() {
		Global.etapa = Etapas.CONEXION;
		Global.cantJugadores = 0;
		ips = new InetAddress[cantMax];
		puertos = new int[cantMax];
	}
}
