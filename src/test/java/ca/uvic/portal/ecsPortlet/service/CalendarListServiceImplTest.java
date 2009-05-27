package ca.uvic.portal.ecsPortlet.service;

import java.io.IOException;
import java.util.Properties;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import ca.uvic.portal.ecsPortlet.domain.CalendarList;

import junit.framework.TestCase;

/**
 * Test class for testing the CalendarListServiceImpl implementation.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public class CalendarListServiceImplTest extends TestCase {

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
     * private The queue of CalendarList objects.
     */
    private ConcurrentLinkedQueue < CalendarList > calendarListItems;
    /**
     * private The CalendarListServiceImpl object.
     */
    private CalendarListServiceImpl calListImpl;
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
    public CalendarListServiceImplTest(final String name) {
        super(name);
    }

    /**
     * Setup CalendarListServiceImpl for testing.
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
        String calListRulesFile =
            prop.getProperty("ecs.calendarListRulesFile");
        String altIdRulesFile =
            prop.getProperty("ecs.alternateIdRulesFile");
        String calParentFolderId =
            prop.getProperty("ecs.calendarParentFolderId");
        String exchDomain = prop.getProperty("ecs.domain");
        String exchUrl = prop.getProperty("ecs.url");
        String exchMboxDomain = prop.getProperty("ecs.mailbox.domain");

        calListImpl = new CalendarListServiceImpl(
                calParentFolderId,
                calListRulesFile,
                altIdRulesFile,
                FROMIDTYPE,
                TOIDTYPE,
                exchDomain,
                exchUrl,
                exchMboxDomain);
    }

    /**
     * Teardown CalendarListServiceImpl for testing.
     * @throws Exception Standard JUnit exception.
     */
    @Override
    protected final void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link ca.uvic.portal.ecsPortlet.
     * service.CalendarListServiceImpl#getCalendarLists(
     * java.lang.String, java.lang.String)}.
     */
    public final void testGetCalendarLists() {
        calendarListItems =
            calListImpl.getCalendarListItems(exchangeUser, exchangePassword);
        Iterator < CalendarList > calItemIter = calendarListItems.iterator();
        CalendarList calItem = calItemIter.next();
        //System.out.println("DEBUG THIS: " + calItem.getId());
        assertNotNull("check getId",
                calItem.getId());
        assertNotNull("check getOwaId",
                calItem.getOwaId());

    }

}
