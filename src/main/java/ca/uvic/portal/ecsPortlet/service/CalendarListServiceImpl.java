package ca.uvic.portal.ecsPortlet.service;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uvic.portal.ecsPortlet.domain.CalendarList;
import ca.uvic.portal.ecsPortlet.domain.EcsAlternateIdSoap;
import ca.uvic.portal.ecsPortlet.domain.EcsCalendarListSoap;
import ca.uvic.portal.ecsPortlet.domain.EcsSoap;
import ca.uvic.portal.ecsPortlet.domain.ResponseMessage;
import ca.uvic.portal.ecsPortlet.domain.TransformCalendarList;

/**The implementation class for CalendarListService.  This class is responsible
 * for the implementation details of building up a queue of CalendarList
 * domain objects.
 * @author Charles Frank
 * @version svn:$Id$
 */
public final class CalendarListServiceImpl implements CalendarListService {
    /**
     * private The CalendarList limit.
     */
    private static String calendarParentFolderId;
    /**
     * private The Digest Rules file that dictate CalendarList object creation.
     */
    private static String calendarListRulesFile;
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
     * private The queue of CalendarList objects.
     */
    private ConcurrentLinkedQueue < CalendarList > transformedCalItems
        = new ConcurrentLinkedQueue < CalendarList >();
    /**
     * private Apache commons logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Constructor: use Spring Injection.
     * @param calParentFolderId The CalendarList limit.
     * @param calListRulesFile The Digest Rules file that dictate CalendarList
     * object creation.
     * @param altIdRulesFile The Digest Rules file that dictate AlternateId
     * object creation.
     * @param fromIdType The exchange id type to change from.
     * @param toIdType  The exchange id type to change to.
     * @param exchDomain The exchange domain to query with, example 'devad'.
     * @param exchUrl The url location of the exchange server to query.
     * @param exchMboxDomain The exhange mailbox domain, example '@uvic.ca'.
     */
    public CalendarListServiceImpl(
            final String calParentFolderId,
            final String calListRulesFile,
            final String altIdRulesFile,
            final String fromIdType,
            final String toIdType,
            final String exchDomain,
            final String exchUrl,
            final String exchMboxDomain) {
        calendarParentFolderId    = calParentFolderId;
        calendarListRulesFile     = calListRulesFile;
        alternateIdRulesFile      = altIdRulesFile;
        alternateIdFromIdType     = fromIdType;
        alternateIdToIdType       = toIdType;
        exchangeDomain            = exchDomain;
        exchangeUrl               = exchUrl;
        exchangeMailboxDomain     = exchMboxDomain;
    }

    /**
     * Get the queue of CalendarList objects assembled from Soap call.
     * @param user The user that will be used to query Exchange.
     * @param pass The password of the user that will be used to query Exchange.
     * @return The queue of CalendarLists.
     */
    public ConcurrentLinkedQueue < CalendarList >
        getCalendarListItems(final String user, final String pass) {

        String mailbox = user + exchangeMailboxDomain;

        //First do the CalendarList Soap call, and build up message object
        EcsCalendarListSoap calendarListSoap =
            new EcsCalendarListSoap(calendarParentFolderId);
        EcsSoap calItemSoap =
            new EcsSoap(exchangeUrl, user, pass, exchangeDomain, calendarListSoap,
                    calendarListRulesFile);
        try {
            calItemSoap.queryExchange();
        } catch (Exception e) {
            String error = "Failed to assemble EcsCalendarListSoap call: ";
            logger.error(error, e);
            e.printStackTrace();
        }
        //TODO think about processing when no messages are returned.
        ConcurrentLinkedQueue < Object > respMsgs =
            calItemSoap.getExchangeObjects();
        Iterator < Object > respIter = respMsgs.iterator();
        ResponseMessage respMessage = (ResponseMessage) respIter.next();
        ConcurrentLinkedQueue < Object > calendarListItems =
            respMessage.getExchangeObjects();

        //Next do the AlternateId Soap call (pass in message object),
        //and get the AlternateId objects
        EcsAlternateIdSoap altIdSoap =
            new EcsAlternateIdSoap(alternateIdFromIdType, alternateIdToIdType,
                    mailbox, calendarListItems);
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

        //Now transform the CalendarList Objects to set the AlternateId,
        //for example, set the OwaId property of the CalendarList object.
        try {
            TransformCalendarList transIm =
                new TransformCalendarList(calendarListItems, ids);
            transIm.transform();
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }

        //Transform the queue of Object into casts of CalendarList
        Iterator < Object > calListIter = calendarListItems.iterator();
        //Have to ConcurrentLinkedQueue or message dupes will appear in the jsp.
        transformedCalItems.clear();
        while (calListIter.hasNext()) {
            CalendarList calItem = (CalendarList) calListIter.next();
            //logger.debug("Checking iterator: " + calItem.getOwaId());
            transformedCalItems.add(calItem);
           //transformedCalItems.add((CalendarList) calListIter.next());
        }
        return transformedCalItems;
    }

}
