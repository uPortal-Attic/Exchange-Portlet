package ca.uvic.portal.ecsPortlet.domain;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import ca.uvic.portal.ecsPortlet.domain.EcsSoap;
import ca.uvic.portal.ecsPortlet.domain.AlternateId;
import ca.uvic.portal.ecsPortlet.domain.EcsAlternateIdSoap;
import ca.uvic.portal.ecsPortlet.domain.EcsInboxMessageSoap;
import java.util.Properties;
import java.io.IOException;

/**
 * Unit testing for AlternateId.
 */
public class AlternateIdTest extends TestCase {

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
      * private Set the Digester ALTIDRULESFILE constant.
      */
    private static final String ALTIDRULESFILE = "/ecs_alternate_id-rules.xml";
     /**
      * private Set the Digester MSGRULESFILE constant.
      */
    private static final String MSGRULESFILE = "/ecs_inbox_msgs-rules.xml";
    /**
     * private Set the messageLimit.
     */
    private static final int MSGLIMIT = 10;

    /**
     * Create the test case.
     * @param testName name of the test case
     */
    public AlternateIdTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AlternateIdTest.class);
    }

    /**
     * Test the getter methods for the domain class.
     */
    public final void testAlternateId() {

        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("/ecs.test.properties"));
        } catch (IOException e) {
           e.printStackTrace();
        }

        String user    = prop.getProperty("ecs.test.user");
        String pass    = prop.getProperty("ecs.test.pass");
        String domain  = prop.getProperty("ecs.test.domain");
        String url     = prop.getProperty("ecs.test.url");
        String mailbox = prop.getProperty("ecs.test.mailbox");

        //For injection - with message limit 10
        EcsInboxMessageSoap inboxSoap = new EcsInboxMessageSoap(MSGLIMIT);
        EcsSoap msgSoap =
            new EcsSoap(url, user, pass, domain, inboxSoap, MSGRULESFILE);
        try {
            msgSoap.queryExchange();
        } catch (Exception e) {
            assertNull("Got error " + e, e);
        }
        ConcurrentLinkedQueue < Object > msgs = msgSoap.getExchangeObjects();

        EcsAlternateIdSoap altIdSoap =
            new EcsAlternateIdSoap(FROMIDTYPE, TOIDTYPE, mailbox, msgs);

        //Handle properties usually injected via Spring
        //Exchange url, soap file for call to soap server/user/pass/domain,
        //EcsRemoteSoapCall type, digester rules in this constructor.
        EcsSoap soap =
            new EcsSoap(url, user, pass, domain, altIdSoap, ALTIDRULESFILE);
        try {
            soap.queryExchange();
        } catch (Exception e) {
            assertNull("Got error " + e, e);
        }
        ConcurrentLinkedQueue < Object > altIds = soap.getExchangeObjects();
        Iterator < Object > exchangeIterator = altIds.iterator();
        assertNotNull("received altId back", exchangeIterator.hasNext());
        AlternateId altId = (AlternateId) exchangeIterator.next();
        //System.out.println(altId.getId());
        //System.out.println(altId.getMailbox());
        assertNotNull("got id", altId.getId());
        assertNotNull("got format", altId.getFormat());
        assertNotNull("got mailbox", altId.getMailbox());
    }

}
