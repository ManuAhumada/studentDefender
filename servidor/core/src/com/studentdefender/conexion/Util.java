package com.studentdefender.conexion;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Util {

	public static Scanner s = new Scanner(System.in);

	public static Random r = new Random();

	public static int ingresarEntero(int min, int max) {
		int opc = 0;
		boolean error = false;

		do {
			error = false;
			try {
				opc = s.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Error. Tipo de dato mal ingresado");
				error = true;
				s.nextLine();
			} catch (Exception e) {
				System.out.println("Error inesperado");
				e.printStackTrace();
				error = true;
			}

			if ((opc < min) || (opc > max)) {
				System.out.println("Respuesta mal ingresada. Fuera de rango");
				error = true;
			}

		} while (error);

		return opc;
	}

	public static float ingresarFloat(float min, float max) {
		float opc = 0;
		boolean error = false;

		do {
			error = false;
			try {
				opc = s.nextFloat();
			} catch (InputMismatchException e) {
				System.out.println("Error. Tipo de dato mal ingresado");
				error = true;
				s.nextLine();
			} catch (Exception e) {
				System.out.println("Error inesperado");
				e.printStackTrace();
				error = true;
			}

			if ((opc < min) || (opc > max)) {
				System.out.println("Respuesta mal ingresada. Fuera de rango");
				error = true;
			}

		} while (error);

		return opc;
	}

	public static void esperar(long milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
