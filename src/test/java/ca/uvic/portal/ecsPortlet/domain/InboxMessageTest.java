package ca.uvic.portal.ecsPortlet.domain;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import ca.uvic.portal.ecsPortlet.domain.EcsSoap;
import ca.uvic.portal.ecsPortlet.domain.InboxMessage;
import ca.uvic.portal.ecsPortlet.domain.EcsInboxMessageSoap;
import java.util.Properties;
import java.io.IOException;

/**
 * Unit testing for InboxMessage.
 */
public class InboxMessageTest extends TestCase {

    /**
     * private Set the TESTPROPFILE constant for testing.
     */
    private static final String TESTPROPFILE = "/ecs.test.properties";

    /**
     * Create the test case.
     * @param testName name of the test case
     */
    public InboxMessageTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(InboxMessageTest.class);
    }

    /**
     * Test the getter methods for the domain class.
     */
    public final void testInboxMessage() {

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
        ConcurrentLinkedQueue < Object > messages = soap.getExchangeObjects();
        Iterator < Object > exchangeIterator = messages.iterator();
        assertNotNull("received messages back", exchangeIterator.hasNext());
        InboxMessage message = (InboxMessage) messages.iterator().next();
        //System.out.println(message.getSubject());
        //System.out.println(message.getDateTimeCreated("yyyy-MM-dd"));
        assertNotNull("got subject", message.getSubject());
        assertNotNull("got id", message.getId());
        assertNotNull("got sensitivity", message.getSensitivity());
        assertNotNull("got size", message.getSize());
        assertNotNull("got hasAttachments", message.getHasAttachments());
        assertNotNull("got isRead", message.getIsRead());
        assertNotNull("got fromMailboxName", message.getFromMailboxName());
        assertNotNull("got dateTimeSent",
                message.getDateTimeSent("yyyy-MM-dd"));
        assertNotNull("got dateTimeSent",
                message.getDateTimeCreated("yyyy-MM-dd"));
    }

}
