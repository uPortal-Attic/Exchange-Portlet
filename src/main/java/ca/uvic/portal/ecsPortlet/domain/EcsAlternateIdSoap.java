package ca.uvic.portal.ecsPortlet.domain;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Iterator;

/**
 * This class is responsible for returning the soap xml body for locating
 * AlternateIds given a queue of inbox messages.  For example you might want
 * to convert the EwsId or EwsLegacyId to an OwaId.
 * @author Charles Frank
 * @version svn:$Id$
 */
public final class EcsAlternateIdSoap extends EcsRemoteSoapCall {

    /**
     * private fromIdType The id format to convert from.
     */
    private static String fromIdType;
    /**
     * private toIdType The id format to convert to.
     */
    private static String toIdType;
    /**
     * private userMailBox The mailbox of the user, ex. "user@mail.exchange.ca".
     */
    private static String userMailBox;
    /**
     * private msgs The InboxMessage domain objects needed for the from id
     * from id conversion.
     */
    private ConcurrentLinkedQueue < Object > msgs;

    //Constructor Injection
    /**
     * Constructor uses Spring Injection.
     * @param fromType The id format to convert from.
     * @param toType The id format to convert to.
     * @param mailBox The mailbox of the user, ex. "user@mail.exchange.ca".
     * @param messages The InboxMessage domain objects needed for the from id.
     */
    public EcsAlternateIdSoap(final String fromType,
                              final String toType,
                              final String mailBox,
                              final ConcurrentLinkedQueue < Object > messages) {
        super();
        fromIdType  = fromType;
        toIdType    = toType;
        msgs        = messages;
        userMailBox = mailBox;
        this.setXMLBody();
    }

    /**
     * Set the soap xml body of the soap envelope (decorated by super class).
     * @see EcsSoap
     */
    protected void setXMLBody() {
        Iterator < Object > msgIter = msgs.iterator();
        String xmlBody =
     "<ConvertId xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\" xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\" DestinationFormat=\"" + toIdType + "\">" + this.getLineEnding()
   +   "<SourceIds>" + this.getLineEnding();
        while (msgIter.hasNext()) {
            InboxMessage message = (InboxMessage) msgIter.next();
            String referenceId = message.getId();
            xmlBody = xmlBody
   +     "<t:AlternateId Format=\"" + fromIdType + "\"" + " Id=\"" + referenceId + "\""  + " Mailbox=\"" + userMailBox + "\"/>" + this.getLineEnding();
        }
        xmlBody = xmlBody
   +   "</SourceIds>" + this.getLineEnding()
   + "</ConvertId>" + this.getLineEnding();
        super.setXMLBody(xmlBody);
    }

}
