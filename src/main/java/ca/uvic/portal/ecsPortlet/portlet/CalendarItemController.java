package ca.uvic.portal.ecsPortlet.portlet;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.portlet.ModelAndView;

import ca.uvic.portal.ecsPortlet.domain.CalendarList;
import ca.uvic.portal.ecsPortlet.domain.EcsCalendarItemSoap;
import ca.uvic.portal.ecsPortlet.domain.EcsCalendarItemSoap.DayTense;
import ca.uvic.portal.ecsPortlet.service.CalendarItemService;
import ca.uvic.portal.ecsPortlet.service.CalendarListService;

/**
 * This is a simple Controller subclass of MowaController which delegates to the
 * {@link CalendarItemService CalendarItemService} and then populates the model
 * with all returned calendar items.  Implements the controller interface.
 * @author Charles Frank
 * @see CalendarItemService
 * @see MowaController
 */
public class CalendarItemController extends MowaController {


    /**
     * private The calendar item service.
     */
    private CalendarItemService calendarItemService;
    /**
     * private The calendar list service.
     */
    private CalendarListService calendarListService;

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
        } else {
           logger.debug("actionRequest calId was not null '"
                   + request.getParameter("calId") + "'");
           response.setRenderParameter("calId", request.getParameter("calId"));
        }
        //response.setRenderParameter("action", "calendarView");
    }

    /**
     * Method to list exchange calendar items.
     * @param request The request object.
     * @param response The response object.
     * @return ModelAndView return the model and view.
     * @throws Exception Throws exceptions related to message retrieval.  This
     * generic exception should catch most everything that is thrown in the
     * process of the soap request/response cycle.  The applicationContext.xml
     * is setup to catch the generic error and display the correct error
     * template in the portlet.
     */
    @Override
    public final ModelAndView handleRenderRequestInternal(
            final RenderRequest request, final RenderResponse response) throws
            Exception {
        //For testing applicationContext.xml exceptionMappings
        //This will catch all errors as they are all derived from Exception
        //if( 1 == 1) throw new Exception("Testing Exception");

        //Check the special cases where initial login or no mowa account
        //return the appropriate view as needed
        //This method also sets the user and pass attributes.
        String initialMowaView = this.initialMowaView(request);
        if (initialMowaView.length() != 0) {
            return new ModelAndView(initialMowaView);
        }

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
        /*
        if(logger.isDebugEnabled()) {
            logger.debug("REQUEST PROPERTY calId: "
                + request.getProperty("calId"));
            Map <?, ?> params = request.getParameterMap();
            Iterator paramIt = params.entrySet().iterator();
            while(paramIt.hasNext()){
                Map.Entry pairs = (Map.Entry) paramIt.next();
                logger.debug("REQUEST PARAM KEY: '" + pairs.getKey() + "'");
                logger.debug("REQUEST PARAM VAL: '"
                    + request.getParameter(pairs.getKey().toString()));
            }
        }
        */
        String calId = "calendar";
        if (!(request.getParameter("calId") == null)) {
          calId = request.getParameter("calId");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("calId is: '" +  calId + "'");
        }

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

        //Create the URL base to escape portlet context on the link generation
        //in view.
        URL ssoUrl = new URL(singleSignOnUrl);
        URL oUrl   = new URL(owaUrl);

        //logical view name        => calendarItems
        //variable holding objects => calItems
        return new ModelAndView("calendarItems", model).addObject(
                "ssoUrl", ssoUrl.toString()).addObject("oUrl", oUrl.toString());

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

}
