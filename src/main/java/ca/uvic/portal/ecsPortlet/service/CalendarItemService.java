package ca.uvic.portal.ecsPortlet.service;
import java.util.concurrent.ConcurrentLinkedQueue;

import ca.uvic.portal.ecsPortlet.domain.CalendarItem;
import ca.uvic.portal.ecsPortlet.domain.EcsCalendarItemSoap.DayTense;

/**
 * The Service class for CalendarItemService.
 * @author Charles Frank
 * @version svn:$Id$
 * @see CalendarItemServiceImpl.java
 */
public interface CalendarItemService {

    /**
     * Get the queue of CalendarItem objects.
     * @param user The user that will be used to query Exchange.
     * @param pass The password of the user that will be used to query Exchange.
     * @param dayTense The enum value representing present, past, or future 
     * tense.
     * @return The queue of CalendarItem objects.
     * @see ca.uvic.portal.ecsPortlet.domain.EcsCalendarItemSoap.DayTense.
     */
    ConcurrentLinkedQueue < CalendarItem >
        getCalendarItems(String user, String pass, DayTense dayTense);

}
