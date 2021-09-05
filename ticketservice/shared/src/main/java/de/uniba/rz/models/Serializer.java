package de.uniba.rz.models;

import de.uniba.rz.entities.Ticket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Serializer {

    public byte[] ticketData;

    public byte[] serialize(Ticket ticket) {

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); ObjectOutputStream os = new ObjectOutputStream(outputStream)) {
            os.writeObject(ticket);
            ticketData = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ticketData;
    }
}
