package ca.uvic.portal.ecsPortlet;

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
 * Unit test for simple App.
 */
public class InboxMessageTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public InboxMessageTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( InboxMessageTest.class );
    }
    

    /**
     * Rigourous Test :-)
     */
    public void testInboxMessage()
    {

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

        //For injection - with message limit 10 
        EcsInboxMessageSoap inboxSoap = new EcsInboxMessageSoap(10);

        //Handle properties usually injected via Spring
        //Exchange url, soap file for call to soap server/user/pass/domain,
        //EcsRemoteSoapCall type, digester rules in this constructor.
        EcsSoap soap = new EcsSoap( url, user, pass, domain, inboxSoap,
            "/ecs_inbox_msgs-rules.xml");
        try {
            soap.queryExchange();
        } catch(Exception e){
            assertNull("Got error " + e, e);
        }
        ConcurrentLinkedQueue <Object> messages = soap.getExchangeObjects();
        Iterator <Object> exchangeIterator = messages.iterator();
        assertNotNull("received messages back", exchangeIterator.hasNext()); 
        InboxMessage message = (InboxMessage) messages.iterator().next();
        System.out.println(message.getSubject());
        System.out.println(message.getDateTimeCreated("yyyy-MM-dd"));
        assertNotNull("got subject", message.getSubject());
        assertNotNull("got id", message.getId());
        assertNotNull("got sensitivity", message.getSensitivity());
        assertNotNull("got size", message.getSize());
        assertNotNull("got hasAttachments", message.getHasAttachments());
        assertNotNull("got isRead", message.getIsRead());
        assertNotNull("got fromMailboxName", message.getFromMailboxName());
        assertNotNull("got dateTimeSent", message.getDateTimeSent("yyyy-MM-dd"));
        assertNotNull("got dateTimeSent", message.getDateTimeCreated("yyyy-MM-dd"));
    }

}
