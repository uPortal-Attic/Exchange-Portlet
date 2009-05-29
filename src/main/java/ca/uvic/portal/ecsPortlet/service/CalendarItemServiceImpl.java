package ca.uvic.portal.ecsPortlet.service;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uvic.portal.ecsPortlet.domain.CalendarItem;
import ca.uvic.portal.ecsPortlet.domain.EcsAlternateIdSoap;
import ca.uvic.portal.ecsPortlet.domain.EcsCalendarItemSoap;
import ca.uvic.portal.ecsPortlet.domain.EcsSoap;
import ca.uvic.portal.ecsPortlet.domain.ResponseMessage;
import ca.uvic.portal.ecsPortlet.domain.TransformCalendarItem;
import ca.uvic.portal.ecsPortlet.domain.EcsCalendarItemSoap.DayTense;

/**The implementation class for CalendarItemService.  This class is responsible
 * for the implementation details of building up a queue of CalendarItem
 * domain objects.  The getCalendarItems method is overloaded, and it has
 * a few signatures based on a variety of different input for the soap request,
 * including time ranges, simple enum YESTERDAY, TOMORROW, TODAY time ranges,
 * and calendar id inputs.
 * @author Charles Frank
 * @version svn:$Id$
 */
public final class CalendarItemServiceImpl implements CalendarItemService {
    /**
     * private The CalendarItem event limit.
     */
    private static int eventLimit;
    /**
     * private The Digest Rules file that dictate CalendarItem object creation.
     */
    private static String calItemRulesFile;
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
     * private The queue of CalendarItem objects.
     */
    private ConcurrentLinkedQueue < CalendarItem > transformedCalItems
        = new ConcurrentLinkedQueue < CalendarItem >();
    /**
     * private Apache commons logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Constructor: use Spring Injection.
     * @param evtLimit The CalendarItem limit.
     * @param calRulesFile The Digest Rules file that dictate CalendarItem
     * object creation.
     * @param altIdRulesFile The Digest Rules file that dictate AlternateId
     * object creation.
     * @param fromIdType The exchange id type to change from.
     * @param toIdType  The exchange id type to change to.
     * @param exchDomain The exchange domain to query with, example 'devad'.
     * @param exchUrl The URL location of the exchange server to query.
     * @param exchMboxDomain The exchange mailbox domain, example '@uvic.ca'.
     */
    public CalendarItemServiceImpl(
            final int evtLimit,
            final String calRulesFile,
            final String altIdRulesFile,
            final String fromIdType,
            final String toIdType,
            final String exchDomain,
            final String exchUrl,
            final String exchMboxDomain) {
        eventLimit                = evtLimit;
        calItemRulesFile          = calRulesFile;
        alternateIdRulesFile      = altIdRulesFile;
        alternateIdFromIdType     = fromIdType;
        alternateIdToIdType       = toIdType;
        exchangeDomain            = exchDomain;
        exchangeUrl               = exchUrl;
        exchangeMailboxDomain     = exchMboxDomain;
    }
    /**
     * Get the queue of CalendarItem objects assembled from Soap call.
     * @param user The user that will be used to query Exchange.
     * @param pass The password of the user that will be used to query Exchange.
     * @param calStart The calendar start date for events.
     * @param calEnd The calendar end date for events.
     * @return The queue of CalendarItems.
     * @throws Exception Throws exception if calStart is not before calEnd.
     */
    public ConcurrentLinkedQueue < CalendarItem >
        getCalendarItems(final String user, final String pass,
                         final String calStart, final String calEnd)
                         throws Exception {

        //String mailbox = user + exchangeMailboxDomain;

        //First do the CalendarItem Soap call, and build up message object
        EcsCalendarItemSoap calItemSoap =
            new EcsCalendarItemSoap(eventLimit, calStart, calEnd);

        return processCalendarItems(user, pass, calItemSoap);
    }
    /**
     * Get the queue of CalendarItem objects assembled from Soap call.
     * @param user The user that will be used to query Exchange.
     * @param pass The password of the user that will be used to query Exchange.
     * @param calStart The calendar start date for events.
     * @param calEnd The calendar end date for events.
     * @param calId The calendar id to set for the soap envelope.
     * @return The queue of CalendarItems.
     * @throws Exception Throws exception if calStart is not before calEnd.
     */
    public ConcurrentLinkedQueue < CalendarItem >
        getCalendarItems(final String user, final String pass,
                         final String calStart, final String calEnd,
                         final String calId)
                         throws Exception {

        //String mailbox = user + exchangeMailboxDomain;

        //First do the CalendarItem Soap call, and build up message object
        EcsCalendarItemSoap calItemSoap =
            new EcsCalendarItemSoap(eventLimit, calStart, calEnd, calId);

        return processCalendarItems(user, pass, calItemSoap);
    }

    /**
     * Get the queue of CalendarItem objects assembled from Soap call.
     * @param user The user that will be used to query Exchange.
     * @param pass The password of the user that will be used to query Exchange.
     * @param dayTense The enum value TODAY, TOMORROW, YESTERDAY, see
     * EcsCalendarItemSoap.DayTense.
     * @return The queue of CalendarItems.
     */
    public ConcurrentLinkedQueue < CalendarItem >
        getCalendarItems(final String user, final String pass,
                         final DayTense dayTense) {

        //First do the CalendarItem Soap call, and build up message object
        EcsCalendarItemSoap calItemSoap =
            new EcsCalendarItemSoap(eventLimit, dayTense);

        return processCalendarItems(user, pass, calItemSoap);
    }

    /**
     * Get the queue of CalendarItem objects assembled from Soap call.
     * @param user The user that will be used to query Exchange.
     * @param pass The password of the user that will be used to query Exchange.
     * @param dayTense The enum value TODAY, TOMORROW, YESTERDAY, see
     * EcsCalendarItemSoap.DayTense.
     * @param calId The calendar id to set for the soap envelope.
     * @return The queue of CalendarItems.
     */
    public ConcurrentLinkedQueue < CalendarItem >
        getCalendarItems(final String user, final String pass,
                         final DayTense dayTense, final String calId) {

        //First do the CalendarItem Soap call, and build up message object
        EcsCalendarItemSoap calItemSoap =
            new EcsCalendarItemSoap(eventLimit, dayTense, calId);

        return processCalendarItems(user, pass, calItemSoap);
    }

    /**
     * private The main processing for returning the queue of Calendar items.
     * This private method is used with either of the overloaded methods
     * getCalendarItems.
     * @param user The user that will be used to query Exchange.
     * @param pass The password of the user that will be used to query Exchange.
     * @param calItemSoap The initial soap cal to get the Calendar Items.
     * @return The queue of CalendarItems.
     */
    private ConcurrentLinkedQueue < CalendarItem >
        processCalendarItems(final String user, final String pass,
                             final EcsCalendarItemSoap calItemSoap) {
        EcsSoap calSoap =
            new EcsSoap(exchangeUrl, user, pass, exchangeDomain, calItemSoap,
                    calItemRulesFile);
        try {
            calSoap.queryExchange();
        } catch (Exception e) {
            String error = "Failed to assemble EcsCalendarItemSoap call: ";
            logger.error(error, e);
            e.printStackTrace();
        }
        //TODO think about processing when no messages are returned.
        ConcurrentLinkedQueue < Object > respMsgs =
            calSoap.getExchangeObjects();
        Iterator < Object > respIter = respMsgs.iterator();
        ResponseMessage respMessage = (ResponseMessage) respIter.next();
        ConcurrentLinkedQueue < Object > calendarItems =
            respMessage.getExchangeObjects();

        String mailbox = user + exchangeMailboxDomain;
        //Next do the AlternateId Soap call (pass in message object),
        //and get the AlternateId objects
        EcsAlternateIdSoap altIdSoap =
            new EcsAlternateIdSoap(alternateIdFromIdType, alternateIdToIdType,
                    mailbox, calendarItems);
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

        //Now transform the CalendarItem Objects to set the AlternateId,
        //for example, set the OwaId property of the CalendarItem object.
        try {
            TransformCalendarItem transIm =
                new TransformCalendarItem(calendarItems, ids);
            transIm.transform();
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }

        //Clear ConcurrentLinkedQueue or message dupes will appear in the jsp.
        transformedCalItems.clear();
        //Transform the queue of Object into casts of CalendarItem
        for (Object cal : calendarItems) {
           transformedCalItems.add((CalendarItem) cal);
        }

        return transformedCalItems;
    }

}
