package de.uniba.rz.backend;

import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;
import javassist.bytecode.ByteArray;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PacketHandler extends Thread {

    private final DatagramPacket packet;
    private final DatagramSocket socket;
    TicketStore ticketStore;


    public PacketHandler(DatagramPacket packet, DatagramSocket socket, TicketStore ticketStore) {
        this.packet = packet;
        this.socket = socket;
        this.ticketStore = ticketStore;
    }

    public void run() {
        System.out.println("\n[PacketHandler [id:"+this.getId()+"]: Handling received packet.");
        // process received packet
        byte[] receivedData = packet.getData();

        try {
            // we find out what was requested by deserializing the request-message into an object and checking its type
            ByteArrayInputStream in = new ByteArrayInputStream(receivedData);
            ObjectInputStream is = new ObjectInputStream(in);
            Object obj = is.readObject();

            byte[] responseData = null;

            // an object of type Ticket means a request for ticket creation or modification
            if (obj instanceof Ticket)
            {
                Ticket receivedTicket = (Ticket) obj;
                if (receivedTicket.getStatus() == Status.NEW)
                {
                    System.out.println("REQUEST to create new ticket");
                    Ticket storedTicket = ticketStore.storeNewTicket(receivedTicket.getReporter(),
                            receivedTicket.getTopic(), receivedTicket.getDescription(),
                            receivedTicket.getType(), receivedTicket.getPriority());
                    System.out.println("SUCCESS - New Ticket " + storedTicket);
                    // serialize storedTicket
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ObjectOutputStream os = new ObjectOutputStream(outputStream);
                    os.writeObject(storedTicket);
                    responseData = outputStream.toByteArray();

                }
                else
                {
                    try {
                        ticketStore.updateTicketStatus(receivedTicket.getId(), receivedTicket.getStatus());
                        // serialize and send a copy of the changed ticket, so that the client can compare
                        // the change it requested with the one that actually happened inside TicketStore
                        Ticket modifiedTicket = ticketStore.getTicket(receivedTicket.getId());
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        ObjectOutputStream os = new ObjectOutputStream(outputStream);
                        os.writeObject(modifiedTicket);
                        responseData = outputStream.toByteArray();
                    } catch (UnknownTicketException | IllegalStateException e) {
                        e.printStackTrace();;
                    }
                }
            }
            // an object not of type Ticket means a request for all tickets
            else
            {
                System.out.println("REQUEST to get all tickets");
                List<Ticket> ticketList = ticketStore.getAllTickets();
                // serialize ticketList
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(outputStream);
                os.writeObject(ticketList);
                responseData = outputStream.toByteArray();
                System.out.println(ticketList.size() + " sent to client");
            }
            
            DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, packet.getAddress(), packet.getPort());
            socket.send(responsePacket);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
