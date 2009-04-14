package ca.uvic.portal.ecsPortlet.domain;

import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;
import ca.uvic.portal.ecsPortlet.domain.EcsInboxMessageSoap;

/**
 * Unit test for EcsInboxMessageSoap.
 * @author Charles Frank
 * @version svn:$Id$
 */
public class EcsInboxMessageSoapTest extends TestCase {

    /**
     * private Set the TESTPROPFILE constant for testing.
     */
    private static final String TESTPROPFILE = "/ecs.test.properties";
    /**
     * private messageSoap hold the object.
     */
    private EcsInboxMessageSoap messageSoap;

    /**
     * Create the test case.
     * @param name the name of the test to run.
     */
    public EcsInboxMessageSoapTest(final String name) {
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
        int msgLimit = Integer.parseInt(
                prop.getProperty("ecs.messageLimit").substring(0));
        messageSoap = new EcsInboxMessageSoap(msgLimit);
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
        assertNotNull("getSoapCall()", messageSoap.getSoapCall());
    }
    /**
     * Test method
     * {@link ca.uvic.portal.ecsPortlet.domain.
     * EcsInboxMessageSoap#getLineEnding()}.
     */
    public final void testGetLineEnding() {
        assertEquals("\r\n", messageSoap.getLineEnding());
    }

}
