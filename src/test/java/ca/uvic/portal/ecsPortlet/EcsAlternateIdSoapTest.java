/**
 * 
 */
package ca.uvic.portal.ecsPortlet;

import junit.framework.TestCase;
import ca.uvic.portal.ecsPortlet.domain.EcsAlternateIdSoap;

/**
 * @author Charles Frank
 * @version $svn:Id$
 *
 */
public class EcsAlternateIdSoapTest extends TestCase {
    

    /**
     * @param name
     */
    public EcsAlternateIdSoapTest(String name) {
        super(name);
    }

    protected EcsAlternateIdSoap messageSoap;
    protected String lineEnding;
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        messageSoap = new EcsAlternateIdSoap("EwsLegacyId", "OwaId",
                "AAAPAGNwZnJhbmtAdXZpYy5jYQBGAAAAAAA3B2neddmORZ0j5Zf2ke02BwCMcjqBwhBGQb4UWQONLxSWAAAArrcpAAA0kpbnJiIdS6dumTrYTWoaAEQN3MlBAAA=",
                "cpfrank@uvic.ca");
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link ca.uvic.portal.ecsPortlet.domain.EcsAlternateIdSoap#getSoapCall()}.
     */
    public void testGetSoapCall() {
        assertNotNull("getSoapCall()", messageSoap.getSoapCall());
    }

}
