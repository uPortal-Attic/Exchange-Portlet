package ca.uvic.portal.ecsPortlet;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import ca.uvic.portal.ecsPortlet.domain.EcsSoap;
import ca.uvic.portal.ecsPortlet.domain.AlternateId;
import ca.uvic.portal.ecsPortlet.domain.EcsAlternateIdSoap;
import java.util.Properties;
import java.io.IOException;

/**
 * Unit testing for AlternateId.
 */
public class AlternateIdTest extends TestCase {

    /**
     * private Set the FROMIDTYPE constant.
     */
    private static final String FROMIDTYPE = "EwsLegacyId";
    /**
     * private Set the TOIDTYPE constant.
     */
    private static final String TOIDTYPE = "OwaId";
    /**
     * private Set the REFID constant.
     */
    private static final String REFID =
        "AAAVAGNwZnJhbmtAZGV2YWQudXZpYy5jYQBGAAAAAABdzdUlySKYR7cfsFfqRBrYBwA2F2fSOmT1Sp6451umElC1AAAA8DtdAAA2F2fSOmT1Sp6451umElC1AAAA8ESQAAA=";
    
     /**
      * private Set the Digester RULESFILE constant.
      */
    private static final String RULESFILE = "/ecs_alternate_id-rules.xml";
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
        EcsAlternateIdSoap altIdSoap =
            new EcsAlternateIdSoap(FROMIDTYPE, TOIDTYPE, REFID, mailbox);

        //Handle properties usually injected via Spring
        //Exchange url, soap file for call to soap server/user/pass/domain,
        //EcsRemoteSoapCall type, digester rules in this constructor.
        EcsSoap soap =
            new EcsSoap(url, user, pass, domain, altIdSoap, RULESFILE);
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
