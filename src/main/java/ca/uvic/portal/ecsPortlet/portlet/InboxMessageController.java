package ca.uvic.portal.ecsPortlet.portlet;

import java.net.URL;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.portlet.ModelAndView;

import ca.uvic.portal.ecsPortlet.service.InboxMessageService;

/**
 * This is a simple Controller subclass of MowaController which delegates to the
 * {@link InboxMessageService InboxMessageService} and then populates the model
 * with all returned inbox messages. Implements the controller interface.
 *
 * @author Charles Frank
 * @see InboxMessageService
 * @see MowaController
 */
public class InboxMessageController extends MowaController {


    /**
     * private The inbox message service.
     */
    private InboxMessageService inboxMessageService;


    /**
     * Method to list exchange inbox messages.
     *
     * @param request
     *            The request object.
     * @param response
     *            The response object.
     * @return ModelAndView return the model and view.
     * @throws Exception
     *             Throws exceptions related to message retrieval. This generic
     *             exception should catch most everything that is thrown in the
     *             process of the soap request/response cycle. The
     *             applicationContext.xml is setup to catch the generic error
     *             and display the correct error template in the portlet.
     */
    @Override
    public final ModelAndView handleRenderRequestInternal(
            final RenderRequest request, final RenderResponse response)
            throws Exception {

        //Check the special cases where initial login or no mowa account
        //return the appropriate view as needed
        //This method also sets the user and pass attributes.
        String initialMowaView = this.initialMowaView(request);
        if (initialMowaView.length() != 0) {
            return new ModelAndView(initialMowaView);
        }

        /*
        if(logger.isDebugEnabled() && user.equals("cpfrank")) {
            logger.debug("USER: '" + user + "'");
            if(pass != null) {
                logger.debug("We have a password.");
            } else {
                logger.debug("Password is null.");
            }
        }
        */
        // Create the URL base to escape portlet context on the link generation
        // in view.
        URL gcfUrl = new URL(request.getScheme(), request.getServerName(),
                request.getServerPort(), singleSignOnServletContextPath);

        // logical view name => inboxMessages
        // variable holding objects => messages
        //user and pass are set in checkOnMowaUser in super class.
        return new ModelAndView("inboxMessages", "messages",
                inboxMessageService.getInboxMessages(user, pass)).addObject(
                "gcfUrl", gcfUrl.toString());
    }

    /**
     * Set the InboxMessageService.
     *
     * @param inboxMsgService
     *            The InboxMessageService to set.
     */
    @Required
    public final void setInboxMessageService(
            final InboxMessageService inboxMsgService) {
        this.inboxMessageService = inboxMsgService;
    }

}
