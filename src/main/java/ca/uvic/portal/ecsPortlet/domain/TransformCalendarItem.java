package ca.uvic.portal.ecsPortlet.domain;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is responsible for processing CalendarItem domain objects,
 * updating them with a particular ID request. NOTE: it expects the two queues
 * of the domain objects to be in the same order so that the method setting
 * mechanism will align well, i.e. the right transformed id will be set in the
 * calendar item.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public class TransformCalendarItem {
    /**
     * private Queue of CalendarItem objects to transform.
     */
    private ConcurrentLinkedQueue < Object > calItems;
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
     * @param calendarItems Queue of CalendarItem objects to transfor.
     * @param alternateIds Queue of AlternateIds to use in transformation.
     */
    public TransformCalendarItem(
            final ConcurrentLinkedQueue < Object > calendarItems,
            final ConcurrentLinkedQueue < Object > alternateIds) {
        this.calItems = calendarItems;
        this.altIds = alternateIds;
    }

    /**
     * Transform the CalendarItems by inserting the alternateIds.
     * @return Queue of CalendarItems.
     * @throws Exception Can throw an exception if the size of the queue of
     * CalendarItems does not match size of the queue of AlternateIds.  Also
     * throws an exception if the AlternateId format is not a recognized format.
     */
    public final ConcurrentLinkedQueue < Object > transform() throws Exception {
        Iterator < Object > calIter = calItems.iterator();
        Iterator < Object > altIter = altIds.iterator();
        if (calItems.size() != altIds.size()) {
            String errMsg = "The count of CalendarItem objects"
                + " does not match the count of AlternateId objects";
            logger.error(errMsg);
            throw new Exception(errMsg);

        }
        while (calIter.hasNext() && altIter.hasNext()) {
            CalendarItem cal = (CalendarItem) calIter.next();
            AlternateId id   = (AlternateId)  altIter.next();
            if (id.getFormat().equals("OwaId")) {
                cal.setOwaId(id.getId());
            } else {
                String errMsg = "Transform is only possible from ItemId"
                    + " EwsId or EwsLegacyId to OwaId";
                logger.error(errMsg);
                throw new Exception(errMsg);
            }
        }
        return calItems;
    }
}
