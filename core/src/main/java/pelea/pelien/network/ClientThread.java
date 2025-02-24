package pelea.pelien.network;

import pelea.pelien.globals.GameData;
import pelea.pelien.globals.NetworkData;

import java.io.IOException;
import java.net.*;

public class ClientThread extends Thread {

    private InetAddress serverIp;
    private final int SERVER_PORT = 9999;
    private DatagramSocket socket;
    private boolean end = false;
    private String specialChar = "!";

    public ClientThread() {

        try {
            serverIp = InetAddress.getByName("255.255.255.255");
            socket = new DatagramSocket();
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while(!end){
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            try {
                socket.receive(packet);
                processMessage(packet);
            } catch (IOException e) {

            }
        }
    }

    private void processMessage(DatagramPacket packet) {
        String message = new String(packet.getData()).trim();
        System.out.println("Mensaje recibido: " + message);
        String[] parts = message.split(specialChar);

        switch(parts[0]){
            case "connection":
                manageConnection(parts[1], Integer.parseInt(parts[2]), packet.getAddress());
                break;
            case "startgame":
                GameData.networkListener.startGame();
                break;
            case "spawnentity":
                GameData.networkListener.spawnEntity(Float.parseFloat(parts[1]),Float.parseFloat(parts[2]), parts[3]);
                break;
            case "moveentity":
                GameData.networkListener.moveEntity(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), parts[3]);
                break;
            case "flip":
                GameData.networkListener.flipEntity(parts[1],parts[2]);
                break;
            case "barra":
                GameData.networkListener.barraVida(parts[1] , Float.parseFloat(parts[2]) , parts[3]);
                break;
            case "timervalor":
                GameData.networkListener.timerValor(Integer.parseInt(parts[1]));
                break;
            case "terminarjuego":
                GameData.networkListener.terminarJuego(parts[1]);
                break;
        }


    }

   private void handleObstacle(String[] parts) {
        switch(parts[1]){

        }
    }

    private void handleEnemy(String[] parts) {
        switch(parts[1]){

        }
    }

    private void manageConnection(String state, int clientNumber, InetAddress serverIp) {
        this.serverIp = serverIp;
        switch(state){
            case "successful":
                GameData.clientNumber = clientNumber;
                break;
        }
    }

    public void sendMessage(String msg){
        byte[] data = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, serverIp, SERVER_PORT);
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void end(){
        end = true;
        socket.close();
    }

}
