package ca.uvic.portal.ecsPortlet.domain;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/** Test Class to test the setting of the CalenderItems owaId attribute.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public class TransformCalendarItemTest extends TestCase {

    /**
     * private Set the TESTPROPFILE constant for testing.
     */
    private static final String TESTPROPFILE = "/ecs.test.properties";
    /**
     * private Set the fake response success
     */
    private static final String  ITEMFAKERESPONSESUCCESS =
        "/ecs-calendar-item.xml";
    /**
     * private Set the fake response success
     */
    private static final String  TRANSFAKERESPONSESUCCESS =
        "/ecs-calendar-transform.xml";

    /**
     * private messages Queue of CalenderItemss.
     */
    private static ConcurrentLinkedQueue < Object > calendarItems;

    /**
     * private alternate ids Queue of AlternateIds.
     */
    private static ConcurrentLinkedQueue < Object > altIds;
    
    /**
     * private Commons Logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * @param name The name of the test to run.
     */
    public TransformCalendarItemTest(final String name) {
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
        String calRulesFile = prop.getProperty("ecs.calendarItemRulesFile");
        String altIdRulesFile =
            prop.getProperty("ecs.alternateIdRulesFile");

        EcsSoapMock calSoap =
            new EcsSoapMock(ITEMFAKERESPONSESUCCESS, calRulesFile);
        try {
            calSoap.queryExchange();
        } catch (Exception e) {
            assertNull("Got error " + e, e);
        }

        ConcurrentLinkedQueue < Object > respMessage =
            calSoap.getExchangeObjects();

        //Make sure we actually got some msgs back
        Iterator < Object > respIterator = respMessage.iterator();
        assertNotNull("received response msg back", respIterator.hasNext());
        ResponseMessage message = (ResponseMessage) respIterator.next();
        if (message.getResponseIndicator().equals("Error")) {
            //Fail this test as we can can't get alternateId back if we don't
            //have messages.
            fail("Forcing failure as we returned no messages from exchange.");
        }

        ConcurrentLinkedQueue < Object > calItems =
            message.getExchangeObjects();

        assertNotNull("received inbox message back", calItems.size());

        EcsSoapMock idSoap =
            new EcsSoapMock(TRANSFAKERESPONSESUCCESS, altIdRulesFile);
        try {
            idSoap.queryExchange();
        } catch (Exception e) {
            assertNull("Got error " + e, e);
        }
        ConcurrentLinkedQueue < Object > ids = idSoap.getExchangeObjects();
        calendarItems = calItems;
        altIds        = ids;

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
     * TransformCalenderItems#transform()}.
     */
    public final void testTransformSuccess() {
        Iterator < Object > calIter = calendarItems.iterator();
        CalendarItem item = (CalendarItem) calIter.next();
        assertNull("check OwaId null", item.getOwaId());
        TransformCalendarItem transIm =
            new TransformCalendarItem(calendarItems, altIds);
        try {
            transIm.transform();
        } catch (Exception e) {
            assertNull("Got transform error: " + e, e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("check getOwaId: " + item.getOwaId());
        }
        String testTransformedId =
            "CIjLnC9oH9gARgAAAAA3B2neddmORZ0j5Zf2ke02BwCMcjqBwhBGQb4"
          + "UWQONLxSWAAAArpDFAACMcjqBwhBGQb4UWQONLxSWAA3PRG6wAAAQ";
        assertEquals(testTransformedId, item.getOwaId());
    }

    public final void testTransformError() {
        //Un-balance the queues and test for thrown error.
        calendarItems.remove();
        TransformCalendarItem transIm =
            new TransformCalendarItem(calendarItems, altIds);
        try {
            transIm.transform();
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("check for errant transform thrown error: "
                        + "'" + e + "'");
            }
            assertNotNull("Check for thrown error on mismatched queues", e);
        }

    }

}
