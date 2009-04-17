package ca.uvic.portal.ecsPortlet.portlet;

import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import ca.uvic.portal.ecsPortlet.service.InboxMessageService;

/**
 * This is a simple Controller which delegates to the
 * {@link InboxMessageService InboxMessageService} and then populates the model
 * with all returned inbox messages.  Implements the controller interface.
 * @author Charles Frank
 * @see InboxMessageService
 */
public class InboxMessageController extends AbstractController {
    /**
     * private The inbox message service.
     */
    private InboxMessageService inboxMessageService;

    /**
     * Method to list exchange inbox messages.
     * @param request The request object.
     * @param response The response object.
     * @return ModelAndView return the model and view.
     */
    @Override
    public final ModelAndView handleRenderRequestInternal(
            final RenderRequest request, final RenderResponse response) {
        //Get the USER_INFO from portlet.xml, which gets it from personDirs.xml
        Map userInfo =
            (Map) request.getAttribute(PortletRequest.USER_INFO);
        String user  = (String) userInfo.get("urn:sungardhe:dir:loginId");
        String pass  = (String) userInfo.get("password");
        //logical view name = inboxMessages|variable holding objects is messages
        return new ModelAndView("inboxMessages", "messages",
                inboxMessageService.getInboxMessages(user, pass));
    }

    /**
     * Set the InboxMessageService.
     * @param inboxMsgService The InboxMessageService to set.
     */
    @Required
    public final void setInboxMessageService(
            final InboxMessageService inboxMsgService) {
        this.inboxMessageService = inboxMsgService;
    }

}
