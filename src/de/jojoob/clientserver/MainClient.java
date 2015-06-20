package de.jojoob.clientserver;

import java.io.IOException;

/**
 * Created by joo on 20.02.15.
 */
public class MainClient {
	public static void main(String[] args) throws IOException {

		Client client = new Client("localhost", 31337);

		client.continuousRead(new ClientInputProcessor());

		client.writeLine("Hello Server!");
//		System.out.println(client.readLine());

		client.close();

	}

	private static class ClientInputProcessor implements InputProcessor {
		@Override
		public void input(String input) {
			System.out.println(input);
		}
	}
}
