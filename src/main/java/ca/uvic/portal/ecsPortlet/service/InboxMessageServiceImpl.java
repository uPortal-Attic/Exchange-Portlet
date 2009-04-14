package ca.uvic.portal.ecsPortlet.service;

import java.util.concurrent.ConcurrentLinkedQueue;

import ca.uvic.portal.ecsPortlet.domain.InboxMessage;
import ca.uvic.portal.ecsPortlet.domain.EcsInboxMessageSoap;
import ca.uvic.portal.ecsPortlet.domain.EcsSoap;
import ca.uvic.portal.ecsPortlet.domain.EcsAlternateIdSoap;
import ca.uvic.portal.ecsPortlet.domain.TransformInboxMessage;

import java.util.Properties;
import java.util.Iterator;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**The implementation class for InboxMessageService.  This class is responsible
 * for the implementation details of building up a queue of InboxMessage
 * domain objects.
 * @author Charles Frank
 * @version svn:$Id$
 */
public final class InboxMessageServiceImpl implements InboxMessageService {
    /**
     * private The application properties file.
     */
    private static String applicationPropertiesFile;
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
     * private Apache commons logger.
     */
    private final Log logger = LogFactory.getLog(getClass());
    /**
     * private The queue of InboxMessage objects.
     */
    private ConcurrentLinkedQueue < InboxMessage > transformedMsgs
        = new ConcurrentLinkedQueue < InboxMessage >();

    /**
     * Constructor: use Spring Injection.
     * @param appPropFile The applictionPropertyFile w/deployment specifics.
     * @param msgLimit The InboxMessage limit.
     * @param msgRulesFile The Digest Rules file that dictate InboxMessage
     * object creation.
     * @param altIdRulesFile The Digest Rules file that dictate AlternateId
     * object creation.
     * @param fromIdType The exchange id type to change from.
     * @param toIdType  The exchange id type to change to.
     */
    public InboxMessageServiceImpl(
            final String appPropFile,
            final int msgLimit,
            final String msgRulesFile,
            final String altIdRulesFile,
            final String fromIdType,
            final String toIdType) {

        applicationPropertiesFile = appPropFile;
        messageLimit              = msgLimit;
        messageRulesFile          = msgRulesFile;
        alternateIdRulesFile      = altIdRulesFile;
        alternateIdFromIdType     = fromIdType;
        alternateIdToIdType       = toIdType;
    }

    /**
     * Get the queue of InboxMessage objects assembled from Soap call.
     * @param user The user that will be used to query Exchange.
     * @param pass The password of the user that will be used to query Exchange.
     * @return The queue of InboxMessages.
     */
    public ConcurrentLinkedQueue < InboxMessage >
        getInboxMessages(final String user, final String pass) {

        Properties prop = new Properties();
        try {
            prop.load(
                    getClass().getResourceAsStream(applicationPropertiesFile));
        } catch (IOException e) {
            String error = "Failed to load ecs application properties file";
            logger.error(error, e);
            e.printStackTrace();
        }

        String domain  = prop.getProperty("ecs.domain");
        String url     = prop.getProperty("ecs.url");
        String mailbox = user + prop.getProperty("ecs.mailbox.domain");

        //First do the InboxMessage Soap call, and build up message object
        EcsInboxMessageSoap inboxSoap = new EcsInboxMessageSoap(messageLimit);
        EcsSoap msgSoap =
            new EcsSoap(url, user, pass, domain, inboxSoap, messageRulesFile);
        try {
            msgSoap.queryExchange();
        } catch (Exception e) {
            String error = "Failed to assemble EcsInboxMessageSoap call: ";
            logger.error(error, e);
            e.printStackTrace();
        }
        ConcurrentLinkedQueue < Object > msgs = msgSoap.getExchangeObjects();

        //Next do the AlternateId Soap call (pass in message object),
        //and get the AlternateId objects
        EcsAlternateIdSoap altIdSoap =
            new EcsAlternateIdSoap(alternateIdFromIdType, alternateIdToIdType,
                    mailbox, msgs);
        EcsSoap idSoap = new EcsSoap(
                url, user, pass, domain, altIdSoap, alternateIdRulesFile);
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
                new TransformInboxMessage(msgs, ids);
            transIm.transform();
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }

        //Transform the queue of Object into casts of InboxMessage
        Iterator < Object > msgIter = msgs.iterator();
        while (msgIter.hasNext()) {
            InboxMessage msg = (InboxMessage) msgIter.next();
            //logger.debug("Checking iterator: " + msg.getOwaId());
            transformedMsgs.add(msg);
           //transformedMsgs.add((InboxMessage) msgIter.next());
        }
        return transformedMsgs;
    }

}
