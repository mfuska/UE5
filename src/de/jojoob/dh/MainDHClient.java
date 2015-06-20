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

		client = new Client("localhost", 31337);

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
					break;
				case 1:
					dhClient.setG(bigInteger);
					dhClient.generateA();
					try {
						client.writeLine(dhClient.getA().toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				case 2:
					dhClient.setB(bigInteger);
					dhClient.generateK();
					System.out.println(dhClient.getK().toString(16));
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
