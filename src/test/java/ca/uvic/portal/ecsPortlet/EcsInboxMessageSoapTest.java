/**
 * 
 */
package ca.uvic.portal.ecsPortlet;

import junit.framework.TestCase;
import ca.uvic.portal.ecsPortlet.domain.EcsInboxMessageSoap;

/**
 * @author Charles Frank
 * @version $svn:Id$
 *
 */
public class EcsInboxMessageSoapTest extends TestCase {
    

    /**
     * @param name
     */
    public EcsInboxMessageSoapTest(String name) {
        super(name);
    }

    protected EcsInboxMessageSoap messageSoap;
    protected String lineEnding;
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        messageSoap = new EcsInboxMessageSoap(10);
        lineEnding  = messageSoap.getLineEnding();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link ca.uvic.portal.ecsPortlet.domain.EcsInboxMessageSoap#getSoapCall()}.
     */
    public void testGetSoapCall() {
        assertNotNull("getSoapCall()", messageSoap.getSoapCall());
    }

}
