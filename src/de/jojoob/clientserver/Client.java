package de.jojoob.clientserver;

import java.io.*;
import java.net.Socket;

/**
 * Created by joo on 20.02.15.
 */
public class Client {

	private Socket socket;
	private DataOutputStream output;
	//	private DataInputStream dataInputStream;
	private BufferedReader input;

	private Runnable continuousReader;

	public Client(String host, int port) throws IOException {
		this.socket = new Socket(host, port);
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
		this.socket.close();
	}
}
