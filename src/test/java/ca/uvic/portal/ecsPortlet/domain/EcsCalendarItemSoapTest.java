package ca.uvic.portal.ecsPortlet.domain;

import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Unit test for EcsCalendarItemSoap.
 * @author Charles Frank
 * @version svn:$Id$
 */
public class EcsCalendarItemSoapTest extends TestCase {
    /**
     * private Set the TESTPROPFILE constant for testing.
     */
    private static final String TESTPROPFILE = "/ecs.test.properties";
    /**
     * private calendarSoap A private object to hold the calendar soap object.
     */
    private EcsCalendarItemSoap calendarSoap;
    /**
     * private eventLimit The eventLimit to set.
     */
    private static int eventLimit;
    /**
     * private Commons Logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Create the test case.
     * @param name the name of the test to run.
     */
    public EcsCalendarItemSoapTest(final String name) {
        super(name);
    }

    /**
     * Setup for testing.
     * @throws Exception Standard JUnit exception.
     */
    @Override
    protected final void setUp() throws Exception {
        super.setUp();
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream(TESTPROPFILE));
        } catch (IOException e) {
           e.printStackTrace();
        }
        eventLimit = Integer.parseInt(
                prop.getProperty("ecs.calendarEventLimit").substring(0));
    }

    /**
     * Teardown for testing.
     * @throws Exception Standard JUnit exception.
     */
    @Override
    protected final void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method, with input for yesterday
     * {@link ca.uvic.portal.ecsPortlet.domain.
     * EcsCalendarItemSoap#getSoapCall()}.
     */
    public final void testGetSoapCallYesterday() {
        calendarSoap = new EcsCalendarItemSoap(
                eventLimit, EcsCalendarItemSoap.DayTense.YESTERDAY);
        if (logger.isDebugEnabled()) {
            logger.debug("getSoapCall yesterday is: "
                    + calendarSoap.getSoapCall());
        }
        assertNotNull("getSoapCall()", calendarSoap.getSoapCall());
    }
    /**
     * Test method, with input for tomorrow
     * {@link ca.uvic.portal.ecsPortlet.domain.
     * EcsCalendarItemSoap#getSoapCall()}.
     */
    public final void testGetSoapCallTomorrow() {
        calendarSoap = new EcsCalendarItemSoap(
                eventLimit, EcsCalendarItemSoap.DayTense.TOMORROW);
        if (logger.isDebugEnabled()) {
            logger.debug("getSoapCall tomorrow is: "
                    + calendarSoap.getSoapCall());
        }
        assertNotNull("getSoapCall()", calendarSoap.getSoapCall());
    }
    /**
     * Test method, with input for today
     * {@link ca.uvic.portal.ecsPortlet.domain.
     * EcsCalendarItemSoap#getSoapCall()}.
     */
    public final void testGetSoapCallDefault() {
        calendarSoap = new EcsCalendarItemSoap(
                eventLimit, EcsCalendarItemSoap.DayTense.TODAY);
        if (logger.isDebugEnabled()) {
            logger.debug("getSoapCall today is: "
                    + calendarSoap.getSoapCall());
        }
        assertNotNull("getSoapCall()", calendarSoap.getSoapCall());
    }
}
