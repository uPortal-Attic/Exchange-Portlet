package ca.uvic.portal.ecsPortlet;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import junit.framework.TestCase;
import ca.uvic.portal.ecsPortlet.domain.EcsAlternateIdSoap;
import ca.uvic.portal.ecsPortlet.domain.EcsInboxMessageSoap;
import ca.uvic.portal.ecsPortlet.domain.EcsSoap;

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

    /**
     * private Set the messageLimit.
     */
    private static final int MSGLIMIT = 10;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected final void setUp() throws Exception {
        super.setUp();
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("/ecs.test.properties"));
        } catch (IOException e) {
           e.printStackTrace();
        }

        String user   = prop.getProperty("ecs.test.user");
        String pass   = prop.getProperty("ecs.test.pass");
        String domain = prop.getProperty("ecs.test.domain");
        String url    = prop.getProperty("ecs.test.url");
        String mlbox  = prop.getProperty("ecs.test.mailbox");

        //For injection - with message limit 10
        EcsInboxMessageSoap inboxSoap = new EcsInboxMessageSoap(MSGLIMIT);

        //Handle properties usually injected via Spring
        //Exchange url, soap file for call to soap server/user/pass/domain,
        //EcsRemoteSoapCall type, digester rules in this constructor.
        EcsSoap soap = new EcsSoap(url, user, pass, domain, inboxSoap,
            "/ecs_inbox_msgs-rules.xml");
        try {
            soap.queryExchange();
        } catch (Exception e) {
            assertNull("Got error " + e, e);
        }
        ConcurrentLinkedQueue < Object > messages = soap.getExchangeObjects();
        messageSoap =
            new EcsAlternateIdSoap("EwsLegacyId", "OwaId", mlbox, messages);
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
        //System.out.println(messageSoap.getSoapCall());
        assertNotNull("getSoapCall()", messageSoap.getSoapCall());
    }

}
