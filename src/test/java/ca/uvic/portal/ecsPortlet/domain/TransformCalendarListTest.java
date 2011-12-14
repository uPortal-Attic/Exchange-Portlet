package ca.uvic.portal.ecsPortlet.domain;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/** Test Class to test the setting of the CalenderList items owaId attribute.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public class TransformCalendarListTest extends TestCase {

    /**
     * private Set the TESTPROPFILE constant for testing.
     */
    private static final String TESTPROPFILE = "/ecs.test.properties";
    /**
     * private Set the fake response success.
     */
    private static final String  ITEMFAKERESPONSESUCCESS =
        "/ecs-calendar-list-success.xml";
    /**
     * private Set the transform fake response success.
     */
    private static final String  TRANSFAKERESPONSESUCCESS =
        "/ecs-calendarlist-transform.xml";

    /**
     * private messages Queue of CalenderItemss.
     */
    private static ConcurrentLinkedQueue < Object > calendarListItems;

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
    public TransformCalendarListTest(final String name) {
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
        String calRulesFile = prop.getProperty("ecs.calendarListRulesFile");
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

        ConcurrentLinkedQueue < Object > calListItems =
            message.getExchangeObjects();

        assertNotNull("received cal list items back", calListItems.size());

        EcsSoapMock idSoap =
            new EcsSoapMock(TRANSFAKERESPONSESUCCESS, altIdRulesFile);
        try {
            idSoap.queryExchange();
        } catch (Exception e) {
            assertNull("Got error " + e, e);
        }
        ConcurrentLinkedQueue < Object > ids = idSoap.getExchangeObjects();
        calendarListItems = calListItems;
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
        Iterator < Object > calIter = calendarListItems.iterator();
        CalendarList item = (CalendarList) calIter.next();
        assertNull("check OwaId null", item.getOwaId());
        TransformCalendarList transIm =
            new TransformCalendarList(calendarListItems, altIds);
        try {
            transIm.transform();
        } catch (Exception e) {
            assertNull("Got transform error: " + e, e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("check getOwaId: " + item.getOwaId());
        }
        String testTransformedId =
            "LgAAAABdzdUlySKYR7cfsFfqRBrYAQC8rpjAVPufTZLJvZq3X9vIAN3cyjUNAAAB";
        assertEquals(testTransformedId, item.getOwaId());
    }

    /**
     * Teset the error generated from an failed transform.
     */
    public final void testTransformError() {
        //Un-balance the queues and test for thrown error.
        //NOTE This will cause the logger in the TransformCalendarItem
        //class to produce an error message in log4j.
        calendarListItems.remove();
        TransformCalendarList transIm =
            new TransformCalendarList(calendarListItems, altIds);
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
