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
            prop.load(getClass().getResourceAsStream(TESTPROPFILE));
        } catch (IOException e) {
           e.printStackTrace();
        }

        String user    = prop.getProperty("ecs.user");
        String pass    = prop.getProperty("ecs.pass");
        String domain  = prop.getProperty("ecs.domain");
        String url     = prop.getProperty("ecs.url");
        String mailbox = prop.getProperty("ecs.mailbox");
        String altIdRulesFile =
            prop.getProperty("ecs.alternateIdRulesFile");
        String msgRulesFile =
            prop.getProperty("ecs.messageRulesFile");
        int msgLimit = Integer.parseInt(
                prop.getProperty("ecs.messageLimit").substring(0));

        //For injection - with message limit 10
        EcsInboxMessageSoap inboxSoap = new EcsInboxMessageSoap(msgLimit);
        EcsSoap msgSoap =
            new EcsSoap(url, user, pass, domain, inboxSoap, msgRulesFile);
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
            new EcsSoap(url, user, pass, domain, altIdSoap, altIdRulesFile);
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
