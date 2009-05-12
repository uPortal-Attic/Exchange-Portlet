package ca.uvic.portal.ecsPortlet.domain;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is responsible for returning the soap xml body for locating
 * AlternateIds given a queue of Domain objects.  For example you might want
 * to convert the EwsId or EwsLegacyId to an OwaId.  Each object in the Domain
 * queue must implement a getId() method.
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
     * private exchangeObjects The domain objects needed for the from id soap
     * generation.
     */
    private ConcurrentLinkedQueue < Object > exchangeObjects;

    /**
     * private Commons Logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    //Constructor Injection
    /**
     * Constructor uses Spring Injection.
     * @param fromType The id format to convert from.
     * @param toType The id format to convert to.
     * @param mailBox The mailbox of the user, ex. "user@mail.exchange.ca".
     * @param exchObjects The domain objects needed for the from id conversion.
     */
    public EcsAlternateIdSoap(
            final String fromType,
            final String toType,
            final String mailBox,
            final ConcurrentLinkedQueue < Object > exchObjects) {
        super();
        fromIdType      = fromType;
        toIdType        = toType;
        exchangeObjects = exchObjects;
        userMailBox     = mailBox;
        this.setXMLBody();
    }

    /**
     * Set the soap xml body of the soap envelope (decorated by super class).
     * This method uses inspection to look at the class name of the each object
     * in the queue of Domain objects to Cast accordingly, and iterate through
     * the objects to generate the Id attribute of the soap request.  Each
     * exchange object domain object must implement a getId() method.
     * @see EcsSoap
     */
    protected void setXMLBody() {
        Iterator < Object > msgIter = exchangeObjects.iterator();
        String xmlBody =
     "<ConvertId xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\" xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\" DestinationFormat=\"" + toIdType + "\">" + this.getLineEnding()
   +   "<SourceIds>" + this.getLineEnding();
        while (msgIter.hasNext()) {
            String referenceId = "";
            Object exchangeObject = msgIter.next();
            if (exchangeObject instanceof CalendarItem) {
                if (logger.isDebugEnabled()) {
                    logger.debug("DEBUG instanceof CalendarItem");
                }
                CalendarItem message = (CalendarItem) exchangeObject;
                referenceId = message.getId();
            } else if (exchangeObject instanceof InboxMessage) {
                if (logger.isDebugEnabled()) {
                    logger.debug("DEBUG instanceof InboxMessage");
                }
                InboxMessage message = (InboxMessage) exchangeObject;
                referenceId = message.getId();
            }
            //String referenceId = message.getId();
            xmlBody = xmlBody
   +     "<t:AlternateId Format=\"" + fromIdType + "\"" + " Id=\"" + referenceId + "\""  + " Mailbox=\"" + userMailBox + "\"/>" + this.getLineEnding();
        }
        xmlBody = xmlBody
   +   "</SourceIds>" + this.getLineEnding()
   + "</ConvertId>" + this.getLineEnding();
        super.setXMLBody(xmlBody);
    }

}
