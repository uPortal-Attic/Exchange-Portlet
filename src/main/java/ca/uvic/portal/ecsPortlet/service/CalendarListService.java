package ca.uvic.portal.ecsPortlet.service;
import java.util.concurrent.ConcurrentLinkedQueue;

import ca.uvic.portal.ecsPortlet.domain.CalendarList;

/**
 * The Service class for CalendarList.
 * @author Charles Frank
 * @version svn:$Id$
 * @see CalendarServiceImpl.java
 */
public interface CalendarListService {

    /**
     * Get the queue of CalendarList objects.
     * @param user The user that will be used to query Exchange.
     * @param pass The password of the user that will be used to query Exchange.
     * @return The queue of CalendarList objects.
     */
    ConcurrentLinkedQueue < CalendarList >
        getCalendarListItems(String user, String pass);

}
