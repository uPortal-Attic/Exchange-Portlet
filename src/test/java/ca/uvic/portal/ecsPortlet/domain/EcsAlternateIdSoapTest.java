package ca.uvic.portal.ecsPortlet.domain;

import java.io.IOException;
import java.util.Iterator;
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
     * private Set the TESTPROPFILE constant for testing.
     */
    private static final String TESTPROPFILE = "/ecs.test.properties";
    /**
     * private Set the FROMIDTYPE constant, in test environment this is EwsId
     * in production it is EwsLegacyId.
     */
    private static final String FROMIDTYPE = "EwsId";
    /**
     * private Set the TOIDTYPE constant.
     */
    private static final String TOIDTYPE = "OwaId";
    /**
     * private messageSoap hold the object.
     */
    private EcsAlternateIdSoap messageSoap;

    /**
     * @param name test to run.
     */
    public EcsAlternateIdSoapTest(final String name) {
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

        String user   = prop.getProperty("ecs.user");
        String pass   = prop.getProperty("ecs.pass");
        String domain = prop.getProperty("ecs.domain");
        String url    = prop.getProperty("ecs.url");
        String mlbox  = prop.getProperty("ecs.mailbox");
        String msgRulesFile =
            prop.getProperty("ecs.messageRulesFile");
        int msgLimit = Integer.parseInt(
                prop.getProperty("ecs.messageLimit").substring(0));
        //For injection - with message limit 10
        EcsInboxMessageSoap inboxSoap = new EcsInboxMessageSoap(msgLimit);

        //Handle properties usually injected via Spring
        //Exchange url, soap file for call to soap server/user/pass/domain,
        //EcsRemoteSoapCall type, digester rules in this constructor.
        EcsSoap soap = new EcsSoap(url, user, pass, domain, inboxSoap,
            msgRulesFile);
        try {
            soap.queryExchange();
        } catch (Exception e) {
            assertNull("Got error " + e, e);
        }
        ConcurrentLinkedQueue < Object > respMessages =
            soap.getExchangeObjects();

        //Make sure we actually got some msgs back
        Iterator < Object > respIterator = respMessages.iterator();
        assertNotNull("received response msg back", respIterator.hasNext());
        ResponseMessage respMessage = (ResponseMessage) respIterator.next();
        if (respMessage.getResponseIndicator().equals("Error")) {
            //Fail this test as we can can't get alternateId back if we don't
            //have messages.
            fail("Forcing failure as we returned no messages from exchange.");
        }
        ConcurrentLinkedQueue < Object > inboxMessages =
            respMessage.getExchangeObjects();
        messageSoap =
            new EcsAlternateIdSoap(FROMIDTYPE, TOIDTYPE, mlbox, inboxMessages);
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
     * Test method for
     * {@link ca.uvic.portal.ecsPortlet.domain.
     * EcsAlternateIdSoap#getSoapCall()}.
     */
    public final void testGetSoapCall() {
        //System.out.println(messageSoap.getSoapCall());
        assertNotNull("getSoapCall()", messageSoap.getSoapCall());
    }

}
