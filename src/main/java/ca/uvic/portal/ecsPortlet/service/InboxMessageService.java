package ca.uvic.portal.ecsPortlet.service;
import java.util.concurrent.ConcurrentLinkedQueue;
import ca.uvic.portal.ecsPortlet.domain.InboxMessage;

/**
 * The Service class for InboxMessageService.
 * @author Charles Frank
 * @version svn:$Id$
 * @see InboxMessageServiceImpl.java
 */
public interface InboxMessageService {

    /**
     * Get the queue of InboxMessage objects.
     * @param user The user that will be used to query Exchange.
     * @param pass The password of the user that will be used to query Exchange.
     * @return The queue of InboxMessages objects.
     */
    ConcurrentLinkedQueue < InboxMessage >
        getInboxMessages(String user, String pass);

}
