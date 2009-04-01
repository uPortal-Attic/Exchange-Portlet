package ca.uvic.portal.ecsPortlet;

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
     * private Set the messageLimit.
     */
    private static final int MSGLIMIT = 10;
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

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
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
        EcsSoap idSoap =
            new EcsSoap(url, user, pass, domain, altIdSoap, ALTIDRULESFILE);
        try {
            idSoap.queryExchange();
        } catch (Exception e) {
            assertNull("Got error " + e, e);
        }
        ConcurrentLinkedQueue < Object > ids = idSoap.getExchangeObjects();
        messages = msgs;
        altIds   = ids;

    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link ca.uvic.portal.ecsPortlet.domain.TransformInboxMessage#transform()}.
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
