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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
     * private Apache Commons logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

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
        //Get the first, and in this case, only ResponseMessage back.
        ConcurrentLinkedQueue < Object > respMessages =
            soap.getExchangeObjects();
        Iterator < Object > respIterator = respMessages.iterator();
        assertNotNull("received response messages back",
                respIterator.hasNext());
        ResponseMessage respMessage = (ResponseMessage) respIterator.next();

        //Get the first InboxMessage back
        ConcurrentLinkedQueue < Object > inboxMessages =
            respMessage.getExchangeObjects();
        if (logger.isDebugEnabled()) {
            logger.debug("We have this many InboxMessage objects: "
                    + inboxMessages.size());
        }
        Iterator < Object > inboxIter = inboxMessages.iterator();
        assertNotNull("received inbox message back", inboxIter.hasNext());
        InboxMessage message = (InboxMessage) inboxIter.next();

        if (respMessage.getResponseIndicator().equals("Success")) {
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
            assertEquals("Success", respMessage.getResponseIndicator());
            assertEquals("NoError", respMessage.getResponseCode());
            assertNull("error message text should be null",
                    respMessage.getResponseText());
        } else {
            assertEquals("Error", respMessage.getResponseIndicator());
            assertNotNull("got error response code",
                    respMessage.getResponseCode());
            assertNotNull("got error message text",
                    respMessage.getResponseText());
        }
    }

}
