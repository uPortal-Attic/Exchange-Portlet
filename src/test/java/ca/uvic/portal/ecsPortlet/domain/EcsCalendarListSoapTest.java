package ca.uvic.portal.ecsPortlet.domain;

import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Unit test for EcsInboxMessageSoap.
 * @author Charles Frank
 * @version svn:$Id$
 */
public class EcsCalendarListSoapTest extends TestCase {

    /**
     * private Set the TESTPROPFILE constant for testing.
     */
    private static final String TESTPROPFILE = "/ecs.test.properties";
    /**
     * private messageSoap hold the object.
     */
    private EcsCalendarListSoap calendarListSoap;
    
    /**
     * private Commons Logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Create the test case.
     * @param name the name of the test to run.
     */
    public EcsCalendarListSoapTest(final String name) {
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
        String calendarParentFolderId =  
            prop.getProperty("ecs.calendarParentFolderId").substring(0);
        calendarListSoap = new EcsCalendarListSoap(calendarParentFolderId);
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
     * Test method
     * {@link ca.uvic.portal.ecsPortlet.domain.
     * EcsInboxMessageSoap#getSoapCall()}.
     */
    public final void testGetSoapCall() {
        logger.debug("calendarListSoap.getSoapCall(): "
                + calendarListSoap.getSoapCall());
        assertNotNull("getSoapCall()", calendarListSoap.getSoapCall());
    }
    /**
     * Test method
     * {@link ca.uvic.portal.ecsPortlet.domain.
     * EcsInboxMessageSoap#getLineEnding()}.
     */
    public final void testGetLineEnding() {
        assertEquals("\r\n", calendarListSoap.getLineEnding());
    }

}
