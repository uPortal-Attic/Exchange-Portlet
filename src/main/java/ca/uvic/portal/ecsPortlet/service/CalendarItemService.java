package ca.uvic.portal.ecsPortlet.service;
import java.util.concurrent.ConcurrentLinkedQueue;

import ca.uvic.portal.ecsPortlet.domain.CalendarItem;
import ca.uvic.portal.ecsPortlet.domain.EcsCalendarItemSoap.DayTense;

/**
 * The Service class for CalendarItemService.  The getCalendarItems method is
 * overloaded, and it has a few signatures based on a variety of different
 * input for the soap request, including time ranges, simple enum YESTERDAY,
 * TOMORROW, TODAY time ranges, and calendar id inputs.
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

    /**
     * Get the queue of CalendarItem objects.
     * @param user The user that will be used to query Exchange.
     * @param pass The password of the user that will be used to query Exchange.
     * @param dayTense The enum value representing present, past, or future
     * tense.
     * @param calId The calendar id to query Exchange with.
     * @return The queue of CalendarItem objects.
     * @see ca.uvic.portal.ecsPortlet.domain.EcsCalendarItemSoap.DayTense.
     */
    ConcurrentLinkedQueue < CalendarItem >
        getCalendarItems(String user, String pass, DayTense dayTense,
                         String calId);

    /**
     * Get the queue of CalendarItem objects.
     * @param user The user that will be used to query Exchange.
     * @param pass The password of the user that will be used to query Exchange.
     * @param calStart The calendar start date for events.
     * @param calEnd The calendar end date for events.
     * @throws Exception Throws exception if calStart is not before calEnd.
     * @return The queue of CalendarItem objects.
     */
    ConcurrentLinkedQueue < CalendarItem >
        getCalendarItems(String user, String pass, String calStart,
                         String calEnd) throws Exception;

    /**
     * Get the queue of CalendarItem objects.
     * @param user The user that will be used to query Exchange.
     * @param pass The password of the user that will be used to query Exchange.
     * @param calStart The calendar start date for events.
     * @param calEnd The calendar end date for events.
     * @param calId The calendar id to query Exchange with.
     * @throws Exception Throws exception if calStart is not before calEnd.
     * @return The queue of CalendarItem objects.
     */
    ConcurrentLinkedQueue < CalendarItem >
        getCalendarItems(String user, String pass, String calStart,
                         String calEnd, String calId) throws Exception;
}
