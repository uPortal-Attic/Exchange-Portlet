package ca.uvic.portal.ecsPortlet;

import junit.framework.TestCase;
import ca.uvic.portal.ecsPortlet.domain.EcsAlternateIdSoap;

/**
 * Unit test for AlternateIdSoap.
 * @author Charles Frank
 * @version svn:$Id$
 */
public class EcsAlternateIdSoapTest extends TestCase {

    /**
     * @param name test to run.
     */
    public EcsAlternateIdSoapTest(final String name) {
        super(name);
    }

    /**
     * private messageSoap hold the object.
     */
    private EcsAlternateIdSoap messageSoap;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected final void setUp() throws Exception {
        super.setUp();
        messageSoap = new EcsAlternateIdSoap("EwsLegacyId", "OwaId",
                "AAAPAGNwZnJhbmtAdXZpYy5jYQBGAAAAAAA3B2neddmORZ0j5Zf2ke02BwCMcjqBwhBGQb4UWQONLxSWAAAArrcpAAA0kpbnJiIdS6dumTrYTWoaAEQN3MlBAAA=",
                "cpfrank@uvic.ca");
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected final void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link ca.uvic.portal.ecsPortlet.domain.EcsAlternateIdSoap#getSoapCall()}.
     */
    public final void testGetSoapCall() {
        assertNotNull("getSoapCall()", messageSoap.getSoapCall());
    }

}
