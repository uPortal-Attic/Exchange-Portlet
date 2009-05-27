package ca.uvic.portal.ecsPortlet.domain;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is responsible for processing CalendarList domain objects,
 * updating them with a particular ID request. NOTE: it expects the two queues
 * of the domain objects to be in the same order so that the method setting
 * mechanism will align well, i.e. the right transformed id will be set for the
 * calendar folder.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public class TransformCalendarList {
    /**
     * private Queue of CalendarList objects to transform.
     */
    private ConcurrentLinkedQueue < Object > calListItems;
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
     * @param calenderListItems Queue of CalendarList objects to transform.
     * @param alternateIds Queue of AlternateIds to use in transformation.
     */
    public TransformCalendarList(
            final ConcurrentLinkedQueue < Object > calenderListItems,
            final ConcurrentLinkedQueue < Object > alternateIds) {
        this.calListItems = calenderListItems;
        this.altIds = alternateIds;
    }

    /**
     * Transform the CalendarList items by inserting the alternateIds.
     * @return Queue of CalendarList items.
     * @throws Exception Can throw an exception if the size of the queue of
     * CalendarList items does not match size of the queue of AlternateIds.
     * Also throws an exception if the AlternateId format is not a recognized
     * format.
     */
    public final ConcurrentLinkedQueue < Object > transform() throws Exception {
        Iterator < Object > calListIter = calListItems.iterator();
        Iterator < Object > altIter = altIds.iterator();
        if (calListItems.size() != altIds.size()) {
            String errMsg = "The count of CalendarList objects"
                + " does not match the count of AlternateId objects";
            logger.error(errMsg);
            throw new Exception(errMsg);

        }
        while (calListIter.hasNext() && altIter.hasNext()) {
            CalendarList calList = (CalendarList) calListIter.next();
            AlternateId id   = (AlternateId)  altIter.next();
            if (id.getFormat().equals("OwaId")) {
                calList.setOwaId(id.getId());
            } else {
                String errMsg = "Transform is only possible from ItemId"
                    + " EwsId or EwsLegacyId to OwaId";
                logger.error(errMsg);
                throw new Exception(errMsg);
            }
        }
        return calListItems;
    }
}
