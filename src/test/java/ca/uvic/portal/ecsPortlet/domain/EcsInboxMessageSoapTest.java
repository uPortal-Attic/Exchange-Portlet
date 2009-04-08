package ca.uvic.portal.ecsPortlet.domain;

import junit.framework.TestCase;
import ca.uvic.portal.ecsPortlet.domain.EcsInboxMessageSoap;

/**
 * Unit test for EcsInboxMessageSoap.
 * @author Charles Frank
 * @version svn:$Id$
 */
public class EcsInboxMessageSoapTest extends TestCase {

    /**
     * private messageSoap hold the object.
     */
    private EcsInboxMessageSoap messageSoap;
    /**
     * private Set the messageLimit.
     */
    private static final int MSGLIMIT = 10;

    /**
     * Create the test case.
     * @param name the name of the test to run.
     */
    public EcsInboxMessageSoapTest(final String name) {
        super(name);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected final void setUp() throws Exception {
        super.setUp();
        messageSoap = new EcsInboxMessageSoap(MSGLIMIT);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected final void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method
     * {@link ca.uvic.portal.ecsPortlet.domain.EcsInboxMessageSoap#getSoapCall()}.
     */
    public final void testGetSoapCall() {
        assertNotNull("getSoapCall()", messageSoap.getSoapCall());
    }
    /**
     * Test method
     * {@link ca.uvic.portal.ecsPortlet.domain.EcsInboxMessageSoap#getLineEnding()}.
     */
    public final void testGetLineEnding() {
        assertEquals("\r\n", messageSoap.getLineEnding());
    }

}
