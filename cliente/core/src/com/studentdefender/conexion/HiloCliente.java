package com.studentdefender.conexion;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.studentdefender.utils.Global;

public class HiloCliente extends Thread {

    private DatagramSocket conexion;
    private boolean fin = false;
    private InetAddress ip;
    private int puerto = 9000;

    public HiloCliente() {
        try {
            conexion = new DatagramSocket();
            ip = InetAddress.getByName(new String("255.255.255.255"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        enviarMensaje("conexion");
    }

    @Override
    public void run() {
        do {
            esperarMensajes();
        } while (!fin);
    }

    private void esperarMensajes() {
        byte[] datos = new byte[2048];
        DatagramPacket paquete = new DatagramPacket(datos, datos.length);
        try {
            conexion.receive(paquete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        procesarMensaje(paquete);
    }

    private void procesarMensaje(DatagramPacket paquete) {
        ip = paquete.getAddress();
        Mensaje mensaje = (Mensaje) UtilConexion.bytesToObject(paquete);
        Global.mensaje.add(mensaje);
    }

    public void enviarMensaje(Object objeto) {
        byte[] datos = UtilConexion.objectToBytes(new Mensaje(Global.jugador, objeto));
        DatagramPacket paquete = new DatagramPacket(datos, datos.length, ip, puerto);
        try {
            conexion.send(paquete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
