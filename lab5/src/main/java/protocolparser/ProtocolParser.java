package protocolparser;

import jakarta.xml.bind.*;
import logger.ChatLogger;
import protocolparser.Serial.command.Command;
import protocolparser.Serial.response.Event;
import protocolparser.Serial.response.Response;

import java.io.StringReader;
import java.io.StringWriter;

public class ProtocolParser {
    public static String createXMLClientMessage(Command cmd) {
        if (cmd!=null) {
            try {
                JAXBContext context = JAXBContext.newInstance(Command.class);
                Marshaller marshaller = context.createMarshaller();

                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

                StringWriter writer = new StringWriter();
                marshaller.marshal(cmd, writer);
                return writer.toString();
            }
            catch (JAXBException e) {
                System.out.println("Error creating xml object");
            }
        }
        return null;
    }

    public static Command unpackXMLClientMessage(String message) {
        try {
            System.out.println("Unpacking message: " + message);
            JAXBContext context = JAXBContext.newInstance(Command.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Command) unmarshaller.unmarshal(new StringReader(message));
        }
        catch (JAXBException e) {
            ChatLogger.logger.error("Error unmarshalling message");
        }
        return null;
    }

    public static String createXMLResponseMessage(Response response) {
        try {
            JAXBContext context = JAXBContext.newInstance(Response.class);
            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

            StringWriter writer = new StringWriter();
            marshaller.marshal(response, writer);
            return writer.toString();
        } catch (JAXBException e) {
            System.out.println("Error creating xml object");
        }
        return null;
    }

    public static Response unpackXMLResponseMessage(String message) {
        try {
            JAXBContext context = JAXBContext.newInstance(Response.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Response) unmarshaller.unmarshal(new StringReader(message));


        } catch (JAXBException e) {
            ChatLogger.logger.error("Error unmarshalling message", e);
        }

        return null;
    }



}
