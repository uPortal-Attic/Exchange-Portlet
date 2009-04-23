package ca.uvic.portal.ecsPortlet.domain;

import junit.framework.TestCase;
import ca.uvic.portal.ecsPortlet.domain.EcsSoap;
import ca.uvic.portal.ecsPortlet.domain.InboxMessage;
import ca.uvic.portal.ecsPortlet.domain.EcsInboxMessageSoap;
import ca.uvic.portal.ecsPortlet.domain.EcsAlternateIdSoap;
import ca.uvic.portal.ecsPortlet.domain.TransformInboxMessage;
import java.util.Properties;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.IOException;


/** Test Class to test the setting of the InboxMessage owaId attribute.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public class TransformInboxMessageTest extends TestCase {

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
     * private messages Queue of InboxMessages.
     */
    private static ConcurrentLinkedQueue < Object > messages;

    /**
     * private alternate ids Queue of AlternateIds.
     */
    private static ConcurrentLinkedQueue < Object > altIds;

    /**
     * @param name The name of the test to run.
     */
    public TransformInboxMessageTest(final String name) {
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

        String user    = prop.getProperty("ecs.user");
        String pass    = prop.getProperty("ecs.pass");
        String domain  = prop.getProperty("ecs.domain");
        String url     = prop.getProperty("ecs.url");
        String mailbox = prop.getProperty("ecs.mailbox");
        String msgRulesFile =
            prop.getProperty("ecs.messageRulesFile");
        String altIdRulesFile =
            prop.getProperty("ecs.alternateIdRulesFile");
        int msgLimit = Integer.parseInt(
                prop.getProperty("ecs.messageLimit").substring(0));

        EcsInboxMessageSoap inboxSoap = new EcsInboxMessageSoap(msgLimit);
        EcsSoap msgSoap =
            new EcsSoap(url, user, pass, domain, inboxSoap, msgRulesFile);
        try {
            msgSoap.queryExchange();
        } catch (Exception e) {
            assertNull("Got error " + e, e);
        }
        ConcurrentLinkedQueue < Object > msgs = msgSoap.getExchangeObjects();
        //Make sure we actually got some msgs back
        Iterator < Object > msgIterator = msgs.iterator();
        assertNotNull("received msgs back", msgIterator.hasNext());
        InboxMessage singleMsg = (InboxMessage) msgIterator.next();
        if(singleMsg.getResponseIndicator().equals("Error")) {
            //Fail this test as we can can't get alternateId back if we don't
            //have messages.
            fail("Forcing failure as we returned no messages from exchange.");
        }

        EcsAlternateIdSoap altIdSoap =
            new EcsAlternateIdSoap(FROMIDTYPE, TOIDTYPE, mailbox, msgs);
        EcsSoap idSoap =
            new EcsSoap(url, user, pass, domain, altIdSoap, altIdRulesFile);
        try {
            idSoap.queryExchange();
        } catch (Exception e) {
            assertNull("Got error " + e, e);
        }
        ConcurrentLinkedQueue < Object > ids = idSoap.getExchangeObjects();
        messages = msgs;
        altIds   = ids;

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
     * Test method for {@link ca.uvic.portal.ecsPortlet.domain.
     * TransformInboxMessage#transform()}.
     */
    public final void testTransform() {
        Iterator < Object > msgIter = messages.iterator();
        InboxMessage message = (InboxMessage) msgIter.next();
        assertNull("check OwaId null", message.getOwaId());
        TransformInboxMessage transIm =
            new TransformInboxMessage(messages, altIds);
        try {
            transIm.transform();
        } catch (Exception e) {
            assertNull("Got transform error: " + e, e);
        }
        //System.out.println("check getOwaId: " + message.getOwaId());
        assertNotNull("check OwaId", message.getOwaId());
    }

}
