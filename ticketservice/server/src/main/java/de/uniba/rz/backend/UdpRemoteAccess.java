package de.uniba.rz.backend;

import java.io.IOException;
import java.net.*;

public class UdpRemoteAccess implements RemoteAccess {
    private boolean active = true;
    int port;
    DatagramSocket serverSocket;
    TicketStore ticketStore;

    public UdpRemoteAccess(String ipAddress, String port) {
        this.port = Integer.parseInt(port);
    }

    @Override
    public void prepareStartup(TicketStore ticketStore) {
        try {
            serverSocket = new DatagramSocket(this.port);
            serverSocket.setSoTimeout(5000);
            this.ticketStore = ticketStore;
        } catch (SocketException e){
            e.printStackTrace();
            shutdown();
        }
    }

    @Override
    public void shutdown() {
        active = false;
        System.out.println("\t [SERVER]: Stopping to listen for messages.");
    }

    @Override
    public void run() {
        while (active) {
            try {
                // create a packet to store received data
                // max udp packet size is 65'507 bytes, see https://en.wikipedia.org/wiki/User_Datagram_Protocol#UDP_datagram_structure
                byte[] buffer = new byte[65500];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try{
                    // wait for packet
                    serverSocket.receive(packet);
                    // process received packet
                    // TODO other way to ensure that there is only one TicketStore
                    new PacketHandler(packet, serverSocket, ticketStore).start();

                } catch (SocketTimeoutException e){
                    // swallow timeout
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        serverSocket.close();
        System.out.println("\t [SERVER]: Stopped.");
    }
}
