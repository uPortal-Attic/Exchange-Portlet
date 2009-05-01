package ca.uvic.portal.ecsPortlet.service;

import java.io.IOException;
import java.util.Properties;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import ca.uvic.portal.ecsPortlet.domain.InboxMessage;

import junit.framework.TestCase;

/**
 * Test class for testing the InboxMessageServiceImpl implementation.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public class InboxMessageServiceImplTest extends TestCase {

    /**
     * private The application properties file.
     */
    private static final String TESTPROPFILE = "/ecs.test.properties";

    /**
     * private The exchange id type to change from.
     */
    private static final String FROMIDTYPE = "EwsId";
    /**
     * private The exchange id type to change to.
     */
    private static final String TOIDTYPE = "OwaId";
    /**
     * private The queue of InboxMessage objects.
     */
    private ConcurrentLinkedQueue < InboxMessage > messages;
    /**
     * private The InboxMessageServiceImpl object.
     */
    private InboxMessageServiceImpl inboxMsgImpl;
    /**
     * private The exchange user to test with.
     */
    private String exchangeUser;
    /**
     * private The exchange user password to test with.
     */
    private String exchangePassword;

    /**
     * @param name The name of the test to run.
     */
    public InboxMessageServiceImplTest(final String name) {
        super(name);
    }

    /**
     * Setup InboxMessageServiceImpl for testing.
     * @throws Exception Standard JUnit exception.
     */
    @Override
    protected final void setUp() throws Exception {
        super.setUp();

        Properties prop = new Properties();
        try {
            prop.load(
                    getClass().getResourceAsStream(TESTPROPFILE));
        } catch (IOException e) {
           e.printStackTrace();
        }
        exchangeUser     = prop.getProperty("ecs.user");
        exchangePassword  = prop.getProperty("ecs.pass");
        String msgRulesFile =
            prop.getProperty("ecs.messageRulesFile");
        String altIdRulesFile =
            prop.getProperty("ecs.alternateIdRulesFile");
        int msgLimit = Integer.parseInt(
                prop.getProperty("ecs.messageLimit").substring(0));
        String exchDomain = prop.getProperty("ecs.domain");
        String exchUrl = prop.getProperty("ecs.url");
        String exchMboxDomain = prop.getProperty("ecs.mailbox.domain");

        inboxMsgImpl = new InboxMessageServiceImpl(
                msgLimit,
                msgRulesFile,
                altIdRulesFile,
                FROMIDTYPE,
                TOIDTYPE,
                exchDomain,
                exchUrl,
                exchMboxDomain);
    }

    /**
     * Teardown InboxMessageServiceImpl for testing.
     * @throws Exception Standard JUnit exception.
     */
    @Override
    protected final void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link ca.uvic.portal.ecsPortlet.
     * service.InboxMessageServiceImpl#getInboxMessages(
     * java.lang.String, java.lang.String)}.
     */
    public final void testGetInboxMessages() {
        messages =
            inboxMsgImpl.getInboxMessages(exchangeUser, exchangePassword);
        Iterator < InboxMessage > msgIter = messages.iterator();
        InboxMessage msg = msgIter.next();
        //System.out.println(msg.getOwaId());
        assertNotNull("check getFromMailboxName",
                msg.getFromMailboxName());
        assertNotNull("check getOwaId",
                msg.getOwaId());

    }

}
