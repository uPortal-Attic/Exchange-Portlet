package ca.uvic.portal.ecsPortlet.portlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.portlet.PortletRequest;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.web.portlet.ModelAndView;

/**
 * Test class to test the InboxMessageControllerTest.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public class InboxMessageControllerTest extends TestCase {

    /**
     * private The application properties file.
     */
    private static final String TESTPROPFILE = "/ecs.test.properties";
    /**
     * private The exchange user to test with.
     */
    private String exchangeUser;
    /**
     * private The exchange user password to test with.
     */
    private String exchangePassword;
    /**
     * private The PortletRequest USER_INFO login id to test with.
     */
    private String portletRequestUserInfoLoginId;
    /**
     * private The PortletRequest USER_INFO password to test with.
     */
    private String portletRequestUserInfoPassword;

    /**
     * private The application context.
     */
    private static final ApplicationContext APPCONTEXT;
    /**
     * private The portlet context.
     */
    private static final ApplicationContext ECSPORTLETCONTEXT;
    /**
     * private The commons logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    //When the class loads before any objects are created, load the context.
    static {
        try {
            APPCONTEXT = new FileSystemXmlApplicationContext(
              new String[]{
                  "src/main/webapp/WEB-INF/context/applicationContext.xml"});
            ECSPORTLETCONTEXT = new FileSystemXmlApplicationContext(
              new String[]{
                  "src/main/webapp/WEB-INF/context/portlet/ecs-portlet.xml"},
                  APPCONTEXT);
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * @param name The name of the test to run.
     */
    public InboxMessageControllerTest(final String name) {
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
            prop.load(
                    getClass().getResourceAsStream(TESTPROPFILE));
        } catch (IOException e) {
           logger.debug("Failed to load testing properties.");
           e.printStackTrace();
        }
        exchangeUser      = prop.getProperty("ecs.user");
        exchangePassword  = prop.getProperty("ecs.pass");
        portletRequestUserInfoLoginId =
            prop.getProperty("ecs.portletRequest.userInfo.loginId");
        portletRequestUserInfoPassword =
            prop.getProperty("ecs.portletRequest.userInfo.password");
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
     * Test method for {@link ca.uvic.portal.ecsPortlet.portlet.
     * InboxMessageController#handleRenderRequest()}.
     * @throws Exception controller exception.
     */
    public final void testGetInboxMessages() throws Exception {
        InboxMessageController controller = (InboxMessageController)
            ECSPORTLETCONTEXT.getBean("inboxMessageController");

        MockRenderRequest request = new MockRenderRequest();
        MockRenderResponse response = new MockRenderResponse();
        HashMap < String, String > userInfo = new HashMap < String, String >();
        userInfo.put(portletRequestUserInfoLoginId, exchangeUser);
        userInfo.put(portletRequestUserInfoPassword, exchangePassword);
        request.setAttribute(PortletRequest.USER_INFO, userInfo);

        ModelAndView mav = controller.handleRenderRequest(request, response);

        assertNotNull("Get model and view from controller", mav);
    }
}
