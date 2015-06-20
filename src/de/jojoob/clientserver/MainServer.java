package de.jojoob.clientserver;

import java.io.IOException;

/**
 * Created by joo on 20.02.15.
 */
public class MainServer {
	static Server server;

	public static void main(String[] args) throws IOException {

		server = new Server(31337);

		server.continuousRead(new ServerInputProcessor());

//		System.out.println(server.readLine());
		server.writeLine("Hello Client!");

//		server.close();

	}

	private static class ServerInputProcessor implements InputProcessor {
		@Override
		public void input(String input) {
			System.out.println(input);
//			try {
//				server.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
	}
}
