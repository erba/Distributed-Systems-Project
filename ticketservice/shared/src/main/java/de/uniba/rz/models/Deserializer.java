package de.uniba.rz.models;

import de.uniba.rz.entities.Ticket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Deserializer {

    Ticket deserializedTicket;

    public Ticket deserialize(byte[] ticketData) {

        try(ByteArrayInputStream in = new ByteArrayInputStream(ticketData);
            ObjectInputStream is = new ObjectInputStream(in)) {

            deserializedTicket = (Ticket) is.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return deserializedTicket;
    }

}
