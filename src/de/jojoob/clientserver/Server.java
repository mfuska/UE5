package de.jojoob.clientserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by joo on 20.02.15.
 */
public class Server {

	private ServerSocket serverSocket;
	private Socket socket;
	private DataOutputStream output;
//	private DataInputStream dataInputStream;
	private BufferedReader input;

	private Runnable continuousReader;

	public Server(int port) throws IOException {
		this.serverSocket = new ServerSocket(31337);
		this.socket = serverSocket.accept();
		this.output = new DataOutputStream(this.socket.getOutputStream());
//		DataInputStream dataInputStream = new DataInputStream(this.socket.getInputStream());
		this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
	}

	public String readLine() throws IOException {
		return this.input.readLine();
	}

	public void writeLine(String line) throws IOException {
		this.output.writeBytes(line + "\n");
	}

	public void continuousRead(final InputProcessor inputProcessor) {
		this.continuousReader = new Thread (new ContinuousReader(this.input, inputProcessor));
		((Thread)this.continuousReader).start();
	}

	public void close() throws IOException {
		socket.close();
//		serverSocket.close();
	}
}
