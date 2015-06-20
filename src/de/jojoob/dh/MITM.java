package de.jojoob.dh;


import de.jojoob.clientserver.Client;
import de.jojoob.clientserver.InputProcessor;
import de.jojoob.clientserver.Server;

import java.io.IOException;
import java.math.BigInteger;

public class MITM {

    private static Server server;
    private static DHHost dhServer;

    private static Client client;
    private static DHHost dhClient;

    private static long startDH;
    private static long endDH;

    public static void main(String[] args) throws IOException {

        server = new Server(31336);

        server.continuousRead(new ServerInputProcessor());

        int pLength = 2048;

        System.out.println("start DH...");
        startDH = System.currentTimeMillis();
        dhServer = new DHHost();

        System.out.println("generate P and G...");
        long startPG = System.currentTimeMillis();
        dhServer.generateGPDSALike(pLength, 256);

        dhClient = new DHHost();
        client = new Client("localhost", 31337);
        client.continuousRead(new ClientInputProcessor());


        long endPG = System.currentTimeMillis();
        System.out.println("P and G generation finished");
        long durationPG = endPG - startPG;
        System.out.println("duration: " + durationPG + " milliseconds");

        dhServer.generateA();
        System.out.println("write P");
        server.writeLine(dhServer.getP().toString());
        System.out.println("write G");
        server.writeLine(dhServer.getG().toString());
        System.out.println("write A");
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

