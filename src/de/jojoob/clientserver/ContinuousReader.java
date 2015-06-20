package de.jojoob.clientserver;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by joo on 05.03.15.
 */
public class ContinuousReader implements Runnable {
	private BufferedReader input;
	private InputProcessor inputProcessor;

	public ContinuousReader(BufferedReader input, InputProcessor inputProcessor) {
		this.input = input;
		this.inputProcessor = inputProcessor;
	}

	@Override
	public void run() {
		String string = "";
		while (string != null) {
			try {
				string = this.input.readLine();
				if (string != null) {
					this.inputProcessor.input(string);
				}
			} catch (IOException e) {
				string = null;
			}
		}
	}
}