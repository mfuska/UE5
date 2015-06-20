package de.jojoob.dh;

import de.jojoob.clientserver.InputProcessor;
import de.jojoob.clientserver.Server;

import java.io.IOException;
import java.math.BigInteger;

public class MainDHServer {
	private static Server server;
	private static DHHost dhServer;

	private static long startDH;
	private static long endDH;

	public static void main(String[] args) throws IOException {

		server = new Server(31337);

		server.continuousRead(new ServerInputProcessor());

		int pLength = 2048;

		System.out.println("start DH...");
		startDH = System.currentTimeMillis();
		dhServer = new DHHost();

//		dhServer.setP(DHParams.RFC3562_16_p);
//		dhServer.setG(BigInteger.valueOf(2));

		System.out.println("generate P and G...");
		long startPG = System.currentTimeMillis();

//		dhServer.generateGPRandom(pLength);
		dhServer.generateGPDSALike(pLength, 256);
//		dhServer.generateGPsavePrime(pLength);

		long endPG = System.currentTimeMillis();
		System.out.println("P and G generation finished");
		long durationPG = endPG - startPG;
		System.out.println("duration: " + durationPG + " milliseconds");

		dhServer.generateA();

		server.writeLine(dhServer.getP().toString());
		server.writeLine(dhServer.getG().toString());
		server.writeLine(dhServer.getA().toString());

	}

	private static class ServerInputProcessor implements InputProcessor {
		@Override
		public void input(String input) {
//			System.out.println(input);
			BigInteger bigInteger = new BigInteger(input);

			dhServer.setB(bigInteger);

			dhServer.generateK();

			System.out.println(dhServer.getK().toString(16));

			endDH = System.currentTimeMillis();

			long durationDH = endDH - startDH;
			System.out.println("DH finished");
			System.out.println("duration: " + durationDH + " milliseconds");
		}
	}
}
