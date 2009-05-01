package ca.uvic.portal.ecsPortlet.service;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uvic.portal.ecsPortlet.domain.EcsAlternateIdSoap;
import ca.uvic.portal.ecsPortlet.domain.EcsInboxMessageSoap;
import ca.uvic.portal.ecsPortlet.domain.EcsSoap;
import ca.uvic.portal.ecsPortlet.domain.InboxMessage;
import ca.uvic.portal.ecsPortlet.domain.ResponseMessage;
import ca.uvic.portal.ecsPortlet.domain.TransformInboxMessage;

/**The implementation class for InboxMessageService.  This class is responsible
 * for the implementation details of building up a queue of InboxMessage
 * domain objects.
 * @author Charles Frank
 * @version svn:$Id$
 */
public final class InboxMessageServiceImpl implements InboxMessageService {
    /**
     * private The InboxMessage limit.
     */
    private static int messageLimit;
    /**
     * private The Digest Rules file that dictate InboxMessage object creation.
     */
    private static String messageRulesFile;
    /**
     * private The Digest Rules file that dictate AlternateId object creation.
     */
    private static String alternateIdRulesFile;
    /**
     * private The exchange id type to change from.
     */
    private static String alternateIdFromIdType;
    /**
     * private The exchange id type to change to.
     */
    private static String alternateIdToIdType;
    /**
     * private The exchange domain, i.e. uvic or devad, etc.
     */
    private static String exchangeDomain;
    /**
     * private The exchange url,
     * example: https://serv.uvic.ca/EWS/Exchange.asmx .
     */
    private static String exchangeUrl;
    /**
     * private The mailbox domain, example '@uvic.ca' or '@devad.uvic.ca'.
     */
    private static String exchangeMailboxDomain;
    /**
     * private The queue of InboxMessage objects.
     */
    private ConcurrentLinkedQueue < InboxMessage > transformedMsgs
        = new ConcurrentLinkedQueue < InboxMessage >();
    /**
     * private Apache commons logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Constructor: use Spring Injection.
     * @param msgLimit The InboxMessage limit.
     * @param msgRulesFile The Digest Rules file that dictate InboxMessage
     * object creation.
     * @param altIdRulesFile The Digest Rules file that dictate AlternateId
     * object creation.
     * @param fromIdType The exchange id type to change from.
     * @param toIdType  The exchange id type to change to.
     * @param exchDomain The exchange domain to query with, example 'devad'.
     * @param exchUrl The url location of the exchange server to query.
     * @param exchMboxDomain The exhange mailbox domain, example '@uvic.ca'.
     */
    public InboxMessageServiceImpl(
            final int msgLimit,
            final String msgRulesFile,
            final String altIdRulesFile,
            final String fromIdType,
            final String toIdType,
            final String exchDomain,
            final String exchUrl,
            final String exchMboxDomain) {
        messageLimit              = msgLimit;
        messageRulesFile          = msgRulesFile;
        alternateIdRulesFile      = altIdRulesFile;
        alternateIdFromIdType     = fromIdType;
        alternateIdToIdType       = toIdType;
        exchangeDomain            = exchDomain;
        exchangeUrl               = exchUrl;
        exchangeMailboxDomain     = exchMboxDomain;
    }

    /**
     * Get the queue of InboxMessage objects assembled from Soap call.
     * @param user The user that will be used to query Exchange.
     * @param pass The password of the user that will be used to query Exchange.
     * @return The queue of InboxMessages.
     */
    public ConcurrentLinkedQueue < InboxMessage >
        getInboxMessages(final String user, final String pass) {

        String mailbox = user + exchangeMailboxDomain;

        //First do the InboxMessage Soap call, and build up message object
        EcsInboxMessageSoap inboxSoap = new EcsInboxMessageSoap(messageLimit);
        EcsSoap msgSoap =
            new EcsSoap(exchangeUrl, user, pass, exchangeDomain, inboxSoap,
                    messageRulesFile);
        try {
            msgSoap.queryExchange();
        } catch (Exception e) {
            String error = "Failed to assemble EcsInboxMessageSoap call: ";
            logger.error(error, e);
            e.printStackTrace();
        }
        //TODO think about processing when no messages are returned.
        ConcurrentLinkedQueue < Object > respMsgs =
            msgSoap.getExchangeObjects();
        Iterator < Object > respIter = respMsgs.iterator();
        ResponseMessage respMessage = (ResponseMessage) respIter.next();
        ConcurrentLinkedQueue < Object > inboxMessages =
            respMessage.getExchangeObjects();

        //Next do the AlternateId Soap call (pass in message object),
        //and get the AlternateId objects
        EcsAlternateIdSoap altIdSoap =
            new EcsAlternateIdSoap(alternateIdFromIdType, alternateIdToIdType,
                    mailbox, inboxMessages);
        EcsSoap idSoap = new EcsSoap(exchangeUrl, user, pass, exchangeDomain,
                altIdSoap, alternateIdRulesFile);
        try {
            idSoap.queryExchange();
        } catch (Exception e) {
            String error = "Failed to assemble EcsAlternateIdSoap call: ";
            logger.error(error, e);
            e.printStackTrace();
        }
        ConcurrentLinkedQueue < Object > ids = idSoap.getExchangeObjects();

        //Now transform the InboxMessage Objects to set the AlternateId,
        //for example, set the OwaId property of the InboxMessage object.
        try {
            TransformInboxMessage transIm =
                new TransformInboxMessage(inboxMessages, ids);
            transIm.transform();
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }

        //Transform the queue of Object into casts of InboxMessage
        Iterator < Object > msgIter = inboxMessages.iterator();
        //Have to ConcurrentLinkedQueue or message dupes will appear in the jsp.
        transformedMsgs.clear();
        while (msgIter.hasNext()) {
            InboxMessage msg = (InboxMessage) msgIter.next();
            //logger.debug("Checking iterator: " + msg.getOwaId());
            transformedMsgs.add(msg);
           //transformedMsgs.add((InboxMessage) msgIter.next());
        }
        return transformedMsgs;
    }

}
