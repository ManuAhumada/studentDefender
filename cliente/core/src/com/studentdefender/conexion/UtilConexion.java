package com.studentdefender.conexion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;

public class UtilConexion {
	public static Object bytesToObject(DatagramPacket paquete) {
		byte[] data = paquete.getData();
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] objectToBytes(Object objeto) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(bytes);
			os.writeObject(objeto);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes.toByteArray();
	}
}
