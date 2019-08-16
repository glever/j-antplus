package be.glever.ant.usb;

import be.glever.ant.message.AntBlockingMessage;
import be.glever.ant.message.AntMessage;
import be.glever.ant.message.control.RequestMessage;

public class RequestMatcher {

    public static boolean isMatchingResponse(AntBlockingMessage message, AntMessage response) {
        if (message instanceof RequestMessage) {
            return response.getMessageId() == ((RequestMessage) message).getMsgIdRequested();
        }

        return response.getMessageId() == message.getResponseMessageId();
    }

    public static boolean isMatchingResponse(AntMessage request, AntMessage response) {
        if (request instanceof AntBlockingMessage) {
            return isMatchingResponse((AntBlockingMessage) request, response);
        }

        return false;
    }

}
