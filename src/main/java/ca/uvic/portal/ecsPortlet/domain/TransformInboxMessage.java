package ca.uvic.portal.ecsPortlet.domain;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is responsible for processing InboxMessage domain objects,
 * updating them with a particular ID request. NOTE: it expects the two queues
 * of the domain objects to be in the same order so that the method setting
 * mechanism will align well, i.e. the right transformed id will be set in the
 * message.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public class TransformInboxMessage {
    /**
     * private Queue of InboxMessage objects to transform.
     */
    private ConcurrentLinkedQueue < Object > msgs;
    /**
     * private Queue of AlternateIds to use in transformation.
     */
    private ConcurrentLinkedQueue < Object > altIds;
    /**
     * private commons logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Constructor.
     * @param messages Queue of InboxMessage objects to transfor.
     * @param alternateIds Queue of AlternateIds to use in transformation.
     */
    public TransformInboxMessage(
            final ConcurrentLinkedQueue < Object > messages,
            final ConcurrentLinkedQueue < Object > alternateIds) {
        this.msgs = messages;
        this.altIds = alternateIds;
    }

    /**
     * Transform the InboxMessages by inserting the alternateIds.
     * @return Queue of InboxMessages.
     * @throws Exception Can throw an exception if the size of the queue of
     * InboxMessages does not match size of the queue of AlternateIds.  Also
     * throws an exception if the AlternateId format is not a recognized format.
     */
    public final ConcurrentLinkedQueue < Object > transform() throws Exception {
        Iterator < Object > msgIter = msgs.iterator();
        Iterator < Object > altIter = altIds.iterator();
        if (msgs.size() != altIds.size()) {
            String errMsg = "The count of InboxMessage objects does not match"
                + " does not match the count of AlteranteId objects";
            logger.error(errMsg);
            throw new Exception(errMsg);

        }
        while (msgIter.hasNext() && altIter.hasNext()) {
            InboxMessage msg = (InboxMessage) msgIter.next();
            AlternateId id   = (AlternateId)  altIter.next();
            if (id.getFormat().equals("OwaId")) {
                msg.setOwaId(id.getId());
            } else {
                String errMsg = "Transform is only possible from ItemId"
                    + " EwsId or EwsLegacyId) to OwaId";
                logger.error(errMsg);
                throw new Exception(errMsg);
            }
        }
        return msgs;
    }
}
