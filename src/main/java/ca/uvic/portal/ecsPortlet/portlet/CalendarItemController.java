package ca.uvic.portal.ecsPortlet.portlet;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import ca.uvic.portal.ecsPortlet.domain.CalendarList;
import ca.uvic.portal.ecsPortlet.domain.EcsCalendarItemSoap;
import ca.uvic.portal.ecsPortlet.domain.EcsCalendarItemSoap.DayTense;
import ca.uvic.portal.ecsPortlet.service.CalendarItemService;
import ca.uvic.portal.ecsPortlet.service.CalendarListService;

/**
 * This is a simple Controller which delegates to the
 * {@link CalendarItemService CalendarItemService} and then populates the model
 * with all returned calendar items.  Implements the controller interface.
 * @author Charles Frank
 * @see CalendarItemService
 */
public class CalendarItemController extends AbstractController {

    /**
     * private Apache commons logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * private The calendar item service.
     */
    private CalendarItemService calendarItemService;
    /**
     * private The calendar list service.
     */
    private CalendarListService calendarListService;
    /**
     * private The login id portlet.xml property, depends on portal used.  This
     * property will come from an application properties file, through the
     * applicationContext and portletContext.
     */
    private String loginIdPortletParam;
    /**
     * private The password portlet.xml property, depends on portal used.  This
     * property will come from an application properties file, through the
     * applicationContext and portletContext.
     */
    private String passwordPortletParam;

    /**
     * Method to prepare some required request parameters before render.
     * @param request The action request object.
     * @param response The action response object.
     */
    @Override
    public final void handleActionRequestInternal(
            final ActionRequest request, final ActionResponse response) {
        if (request.getParameter("calId") == null) {
           logger.debug("actionRequest calId was null");
           response.setRenderParameter("calId", "calendar");
        } else {
           //logger.debug("actionRequest calId was '"
           //        + request.getParameter("calId") + "'");
           response.setRenderParameter("calId", request.getParameter("calId"));
        }
        response.setRenderParameter("action", "calendarView");
    }

    /**
     * Method to list exchange calendar items.
     * @param request The request object.
     * @param response The response object.
     * @return ModelAndView return the model and view.
     */
    @Override
    public final ModelAndView handleRenderRequestInternal(
            final RenderRequest request, final RenderResponse response) {
        //Default to today.
        DayTense dayTense = null;
        //Get the USER_INFO from portlet.xml, which gets it from personDirs.xml
        if (request.getParameter("dayTense") == null) {
            dayTense = EcsCalendarItemSoap.DayTense.TODAY;
        } else if (request.getParameter("dayTense").equals("today")) {
            dayTense = EcsCalendarItemSoap.DayTense.TODAY;
        } else if (request.getParameter("dayTense").equals("tomorrow")) {
            dayTense = EcsCalendarItemSoap.DayTense.TOMORROW;
        } else if (request.getParameter("dayTense").equals("yesterday")) {
            dayTense = EcsCalendarItemSoap.DayTense.YESTERDAY;
        }
        String calId = request.getParameter("calId");
        logger.debug("calId is: '" +  calId + "'");

        Map  userInfo =
            (Map) request.getAttribute(PortletRequest.USER_INFO);
        if (logger.isDebugEnabled()) {
           logger.debug("loginIdPortletParam: '" + loginIdPortletParam + "'");
           logger.debug("passwordPortletParam: '" + passwordPortletParam + "'");
        }
        String user  = (String) userInfo.get(loginIdPortletParam);
        String pass  = (String) userInfo.get(passwordPortletParam);
        /*
        if(logger.isDebugEnabled()) {
           logger.debug("USER: '" + user + "'");
           logger.debug("PASSWORD: '" + pass + "'");
        }
        */
        Map < String, Object > model = new HashMap < String, Object >();
        model.put("calItems",
                calendarItemService.getCalendarItems(user, pass,
                                                     dayTense, calId));
        ConcurrentLinkedQueue < CalendarList > listQueue =
            calendarListService.getCalendarListItems(user, pass);
        model.put("calList",
                calendarListService.getCalendarListItems(user, pass));

        //Load up the
        for (CalendarList cal : listQueue) {
            if (cal.getId().equals(calId)) {
                model.put("calendarId", cal);
                //logger.debug("MAJOR DEBUG: " + cal.getOwaId());
                //logger.debug("MAJOR DEBUG: " + cal.getDisplayName());
            }
         }

        //logical view name        => calendarItems
        //variable holding objects => calItems
        return new ModelAndView("calendarItems", model);
    }

    /**
     * Set the CalendarItemService.
     * @param calItemService The CalendarItemService to set.
     */
    @Required
    public final void setCalendarItemService(
            final CalendarItemService calItemService) {
        this.calendarItemService = calItemService;
    }

    /**
     * Set the CalendarListService.
     * @param calListService The CalendarListService to set.
     */
    @Required
    public final void setCalendarListService(
            final CalendarListService calListService) {
        this.calendarListService = calListService;
    }

    /**
     * Set the loginId portlet param.  This is closely tied with parameters
     * made accessible via the portlet.xml context file.  This parameter is
     * also specific to the portal that the portlet will be deployed to.  For
     * example it might be user.login.id for uPortal, but
     * urn:sungardhe:dir:loginId for Luminis Portal.  This parameter is used
     * with the JSR-168 portlet specification USER_INFO information hash.
     * @param loginIdParam The login id portlet param.
     * @see portlet.xml, applicationContext.xml for more information on this
     * deployment specific property.
     */
    @Required
    public final void setLoginIdPortletParam(final String loginIdParam) {
        this.loginIdPortletParam = loginIdParam;
    }

    /**
     * Set the password portlet param.  This is closely tied with parameters
     * made accessible via the portlet.xml context file.  This parameter is
     * also specific to the portal that the portlet will be deployed to.
     * This parameter is used with the JSR-168 portlet specification USER_INFO
     * information hash.
     * @param passwordParam The password portlet param.
     * @see portlet.xml, applicationContext.xml for more information on this
     * deployment specific property.
     */
    @Required
    public final void setPasswordPortletParam(final String passwordParam) {
        this.passwordPortletParam = passwordParam;
    }
}
