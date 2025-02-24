package pelea.pelien.network;

import pelea.pelien.Luchador;
import pelea.pelien.globals.GameData;
import pelea.pelien.globals.NetworkData;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServerThread extends Thread {

    private final int PORT = 9999;
    private DatagramSocket socket;
    private boolean end = false;
    private String specialChar = "!";
    private final int MAX_CLIENTS = 2;
    private int clientsConnected = 0;
    private Client[] clients = new Client[MAX_CLIENTS];

    public ServerThread() {
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
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
                throw new RuntimeException(e);
            }
        }
    }

    private void processMessage(DatagramPacket packet) {
        String message = new String(packet.getData()).trim();
        System.out.println("Mensaje recibido: " + message);
        String[] parts = message.split(specialChar);

        switch(parts[0]) {
            case "connect":
                boolean successfulConnection = connectClient(packet);
                if (successfulConnection && clientsConnected == MAX_CLIENTS) {
                    sendMessageToAll("startgame");
                    GameData.networkListener.startGame();
                }
                break;
            case "disconnect":
                GameData.networkListener.jugadorDesconectado();
                int numClient = Integer.parseInt(parts[1]);
                numClient = (numClient==1)?0:1;
                clientsConnected--;
                if(clientsConnected>0) {
                    this.sendMessage("terminarjuego!" + "JUGADOR DESCONECTADO", this.clients[numClient].getIp(), this.clients[numClient].getPort());
                    this.sendMessage("gameover", this.clients[numClient].getIp(), this.clients[numClient].getPort());
                }
                this.clearClients();
                break;
            case "animation":
                handleAnimation(parts);
                break;
        }
    }

    private void handleAnimation(String[] parts) {
        String entityId = parts[1];
        String animation = parts[2];
        float posX = Float.parseFloat(parts[3]);
        float posY = Float.parseFloat(parts[4]);
        sendMessageToAll("animation" + specialChar + entityId + specialChar + animation + specialChar + posX + specialChar + posY);
    }

    private boolean connectClient(DatagramPacket packet) {
        if(clientsConnected < MAX_CLIENTS){
            if(!clientExists(packet.getAddress(), packet.getPort())){
                addClient(packet);
                sendMessage("connection" + specialChar + "successful" + specialChar + (clientsConnected-1),  packet.getAddress(), packet.getPort());
                return true;
            }
        }
        return false;
    }

    private boolean clientExists(InetAddress address, int port) {
        for (int i = 0; i < clientsConnected; i++) {
            if(clients[i].getIp().equals(address) && clients[i].getPort() == port){
                return true;
            }
        }
        return false;
    }

    private void addClient(DatagramPacket packet) {
        clients[clientsConnected] = new Client(packet.getAddress(), packet.getPort(), clientsConnected);
        clientsConnected++;
        System.out.println("Cliente conectado");
    }

    public void sendMessage(String msg, InetAddress ip, int port){
        byte[] data = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
        }
    }

    public void sendMessageToAll(String msg){
        for (int i = 0; i < clientsConnected; i++) {
            sendMessage(msg, clients[i].getIp(), clients[i].getPort());
        }
    }

    public void clearClients() {
        clients = new Client[MAX_CLIENTS];
        clientsConnected = 0;
    }

    public void end(){
        this.end = true;
        this.socket.close();
    }
}
