package de.jojoob.dh;

import de.jojoob.clientserver.Client;
import de.jojoob.clientserver.InputProcessor;

import java.io.IOException;
import java.math.BigInteger;

public class MainDHClient {
	private static Client client;
	private static DHHost dhClient;

	public static void main(String[] args) throws IOException {

		dhClient = new DHHost();

		client = new Client("localhost", 31336);

		client.continuousRead(new ClientInputProcessor());

	}

	private static class ClientInputProcessor implements InputProcessor {
		private int state = 0;

		@Override
		public void input(String input) {
//			System.out.println(input);
			BigInteger bigInteger = new BigInteger(input);

			switch (state) {
				case 0:
					dhClient.setP(bigInteger);
					System.out.println("state:0");
					System.out.println("getP:" + bigInteger.toString());
					break;
				case 1:
					System.out.println("state:1");
					System.out.println("getG:" + bigInteger.toString());
					dhClient.setG(bigInteger);
					dhClient.generateA();
					try {
						System.out.println("state:1 before write A");
						client.writeLine(dhClient.getA().toString());
						System.out.println("state:1 after write A" + dhClient.getA().toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				case 2:
					System.out.println("state:2");
					System.out.println("getB:" + bigInteger.toString());
					dhClient.setB(bigInteger);

					dhClient.generateK();
					System.out.println("K:" + dhClient.getK().toString(16));
					try {
						client.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				default:
					System.out.println("unkown state");
					break;
			}
			state++;
		}
	}
}
