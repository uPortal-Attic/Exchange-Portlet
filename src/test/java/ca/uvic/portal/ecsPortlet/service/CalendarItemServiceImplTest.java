package ca.uvic.portal.ecsPortlet.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import junit.framework.TestCase;
import ca.uvic.portal.ecsPortlet.domain.CalendarItem;
import ca.uvic.portal.ecsPortlet.domain.EcsCalendarItemSoap;

/**
 * Test class for testing the CalendarItemServiceImpl implementation.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public class CalendarItemServiceImplTest extends TestCase {

    /**
     * private The application properties file.
     */
    private static final String TESTPROPFILE = "/ecs.test.properties";

    /**
     * private The exchange id type to change from.
     */
    private static final String FROMIDTYPE = "EwsId";
    /**
     * private The exchange id type to change to.
     */
    private static final String TOIDTYPE = "OwaId";
    /**
     * private The queue of CalendarItem objects.
     */
    private ConcurrentLinkedQueue < CalendarItem > calendarItems;
    /**
     * private The CalendarItemServiceImpl object.
     */
    private CalendarItemServiceImpl calItemImpl;
    /**
     * private The exchange user to test with.
     */
    private String exchangeUser;
    /**
     * private The exchange user password to test with.
     */
    private String exchangePassword;

    /**
     * @param name The name of the test to run.
     */
    public CalendarItemServiceImplTest(final String name) {
        super(name);
    }

    /**
     * Setup CalendarItemServiceImpl for testing.
     * @throws Exception Standard JUnit exception.
     */
    @Override
    protected final void setUp() throws Exception {
        super.setUp();

        Properties prop = new Properties();
        try {
            prop.load(
                    getClass().getResourceAsStream(TESTPROPFILE));
        } catch (IOException e) {
           e.printStackTrace();
        }
        exchangeUser     = prop.getProperty("ecs.user");
        exchangePassword  = prop.getProperty("ecs.pass");
        String msgRulesFile =
            prop.getProperty("ecs.calendarItemRulesFile");
        String altIdRulesFile =
            prop.getProperty("ecs.alternateIdRulesFile");
        int calItemLimit = Integer.parseInt(
                prop.getProperty("ecs.calendarEventLimit").substring(0));
        String exchDomain = prop.getProperty("ecs.domain");
        String exchUrl = prop.getProperty("ecs.url");
        String exchMboxDomain = prop.getProperty("ecs.mailbox.domain");

        calItemImpl = new CalendarItemServiceImpl(
                calItemLimit,
                msgRulesFile,
                altIdRulesFile,
                FROMIDTYPE,
                TOIDTYPE,
                exchDomain,
                exchUrl,
                exchMboxDomain);
    }

    /**
     * Teardown CalendarItemServiceImpl for testing.
     * @throws Exception Standard JUnit exception.
     */
    @Override
    protected final void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link ca.uvic.portal.ecsPortlet.
     * service.CalendarItemServiceImpl#getCalendarItems(
     * java.lang.String, java.lang.String)}.
     */
    public final void testGetCalendarItems() {
        //Note this is a live test and the calendar entry must be in
        //your exchange test server.
        String calStart = "2009-05-12T00:00:00Z";
        String calEnd   = "2009-05-12T16:00:00Z";
        try {
            calendarItems = calItemImpl.getCalendarItems(exchangeUser,
                                                         exchangePassword,
                                                         calStart,
                                                         calEnd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator < CalendarItem > calIter = calendarItems.iterator();
        CalendarItem calItem = calIter.next();
        //System.out.println(calItem.getOwaId());
        assertNotNull("check getOrganizerMailboxName",
                calItem.getOrganizerMailboxName());
        assertNotNull("check getOwaId",
                calItem.getOwaId());

    }
    /* This requires an entry for today, was passing when event is entered
     * today
    public final void testGetCalendarItemsToday() {
        calendarItems = calItemImpl.getCalendarItems(
                exchangeUser,
                exchangePassword, 
                EcsCalendarItemSoap.DayTense.TODAY);
        Iterator < CalendarItem > calIter = calendarItems.iterator();
        CalendarItem calItem = calIter.next();
        //System.out.println(calItem.getOwaId());
        assertNotNull("check getOrganizerMailboxName",
                calItem.getOrganizerMailboxName());
        assertNotNull("check getOwaId",
                calItem.getOwaId());
    }
    */

}
