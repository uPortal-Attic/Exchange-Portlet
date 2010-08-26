package ca.uvic.portal.ecsPortlet.portlet;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import ca.uvic.portal.ecsPortlet.service.InboxMessageService;

/**
 * This is a simple Controller which delegates to the
 * {@link InboxMessageService InboxMessageService} and then populates the model
 * with all returned inbox messages. Implements the controller interface.
 *
 * @author Charles Frank
 * @see InboxMessageService
 */
public class InboxMessageController extends AbstractController {

    /**
     * private Apache commons logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * private The inbox message service.
     */
    private InboxMessageService inboxMessageService;
    /**
     * private The login id portlet.xml property, depends on portal used. This
     * property will come from an application properties file, through the
     * applicationContext and portletContext.
     */
    private String loginIdPortletParam;
    /**
     * private The password portlet.xml property, depends on portal used. This
     * property will come from an application properties file, through the
     * applicationContext and portletContext.
     */
    private String passwordPortletParam;
    /**
     * private The single sign on servlet portion of a URL.
     */
    private String singleSignOnServletContextPath = "/cp/ip/login";
    /**
     * private The login id backup field to use if loginIdPortletParam is not
     * available. This property will come from an application properties file,
     * through the applicationContext and portletContext.
     */
    private String loginIdPortletParamBackup;
    /**
     * private The mowa entitlement attribute name (likely from LDAP).  This
     * attribute's value determines if a portal user is a mowa user.
     */
    private String mowaEntitlementAttributeName;
    /**
     * private The mowa entitlement attribute value (likely from LDAP).  This
     * value determines if a portal user is a mowa user.
     */
    private String mowaEntitlementAttributeValue;
    /**
     * private The boolean value that performs a switch to turn on/off
     * a check to see if the portal user has a mowa account (useful for
     * displaying a nice page for any user w/out a mowa account).
     */
    public boolean checkMowaUser;

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
        // Get the USER_INFO from portlet.xml, which gets it from personDirs.xml
        Map<String,String> userInfo = (Map<String,String>)
            request.getAttribute(PortletRequest.USER_INFO);

        if (checkMowaUser) {
            //This is a uPortal specific way to get around using the USER_INFO
            //issue of a Map(<String,String>), which for an attribute from the
            //datasource that has multiple values, eduPersonAffiliation or
            //eduPersonEntitlement, gets only the very first value as a String.
            //Here we can pull out a List of possible values.  This method
            //also keeps compliance with JSR-168 spec.
            Map<String, List<Object>> userInfoMulti =
                (Map <String, List<Object>>) request.
                        getAttribute("org.jasig.portlet.USER_INFO_MULTIVALUED");

            //Check if the user is mowa enabled
            boolean mowaEntitlement = false;
            Iterator <String> it = userInfoMulti.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                Object val =  (Object) userInfoMulti.get(key);
                if (key.toString().equals(mowaEntitlementAttributeName)) {
                    /*
                    if(logger.isDebugEnabled()) {
                        logger.debug("key: " + key.toString());
                        logger.debug("val: " + val.getClass());
                        logger.debug("val: " + val.toString());
                    }
                    */
                    List <Object> entitlements = (List<Object>) val;
                    Iterator <Object> eduPerIter = entitlements.iterator();
                    while (eduPerIter.hasNext()) {
                        String entitlement = eduPerIter.next().toString();
                        if (entitlement.equals(mowaEntitlementAttributeValue)) {
                            logger.debug(mowaEntitlementAttributeName + ": "
                                    + entitlement);
                            mowaEntitlement = true;
                        }
                    }
                }
            }
            //Handle the situation where there is no mowa entitlement
            if (!mowaEntitlement) {
                return new ModelAndView("ecsNoMowa");
            }
        }

        /*
         * if (logger.isDebugEnabled()) { logger.debug("loginIdPortletParam: '"
         * + loginIdPortletParam + "'"); logger.debug("passwordPortletParam: '"
         * + passwordPortletParam + "'"); }
         */
        String user = (String) userInfo.get(loginIdPortletParam);
        String pass = (String) userInfo.get(passwordPortletParam);

        // Handle the case where the user has just added the portlet, but
        // portlet won't work until next portal login.
        if (user == null || user.length() == 0) {
            if (logger.isWarnEnabled()) {
                logger.warn("We have no user, trying "
                        + loginIdPortletParamBackup);
            }
            // If the login id is not available try the backup field.
            user = (String) userInfo.get(loginIdPortletParamBackup);
            if (user == null || user.length() == 0) {
                if (logger.isWarnEnabled()) {
                    logger.warn("We have no user or uid.");
                }
                return new ModelAndView("ecsFirstTime");
            }
        }
        /*
         * if(logger.isDebugEnabled()) { logger.debug("USER: '" + user + "'");
         * logger.debug("PASSWORD: '" + pass + "'"); }
         */
        // Create the URL base to escape portlet context on the link generation
        // in view.
        URL gcfUrl = new URL(request.getScheme(), request.getServerName(),
                request.getServerPort(), singleSignOnServletContextPath);

        // logical view name => inboxMessages
        // variable holding objects => messages
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

    /**
     * Set the loginId portlet param. This is closely tied with parameters made
     * accessible via the portlet.xml context file. This parameter is also
     * specific to the portal that the portlet will be deployed to. For example
     * it might be user.login.id for uPortal, but urn:sungardhe:dir:loginId for
     * Luminis Portal. This parameter is used with the JSR-168 portlet
     * specification USER_INFO information hash.
     *
     * @param loginIdParam
     *            The login id portlet param.
     * @see portlet.xml, applicationContext.xml for more information on this
     *      deployment specific property.
     */
    @Required
    public final void setLoginIdPortletParam(final String loginIdParam) {
        this.loginIdPortletParam = loginIdParam;
    }

    /**
     * Set the loginId portlet param backup field, it will be used if the
     * loginIdPortletParam is not available. This field should something similar
     * to "uid" that is sure to be populated in personDirectoryContext.xml. This
     * field is tied with parameters made accessible via the portlet.xml context
     * file. This parameter is used with the JSR-168 portlet specification
     * USER_INFO information hash.
     *
     * @param loginIdParamBackup
     *            The login id portlet param backup field.
     * @see portlet.xml, applicationContext.xml for more information on this
     *      deployment specific property.
     */
    @Required
    public final void setLoginIdPortletParamBackup(
            final String loginIdParamBackup) {
        this.loginIdPortletParamBackup = loginIdParamBackup;
    }

    /**
     * Set the password portlet param. This is closely tied with parameters made
     * accessible via the portlet.xml context file. This parameter is also
     * specific to the portal that the portlet will be deployed to. This
     * parameter is used with the JSR-168 portlet specification USER_INFO
     * information hash.
     *
     * @param passwordParam
     *            The password portlet param.
     * @see portlet.xml, applicationContext.xml for more information on this
     *      deployment specific property.
     */
    @Required
    public final void setPasswordPortletParam(final String passwordParam) {
        this.passwordPortletParam = passwordParam;
    }

    /**
     * Set the mowa entitlement attribute name (likely from LDAP).  This param
     * corresponds to a setting in portlet.xml context file
     * (example, eduPersonEntitlement).
     *
     * @param attributeName
     *          The mowa entitlement attribute value.
     */
    public final void setMowaEntitlementAttributeName(
            final String attributeName) {
        this.mowaEntitlementAttributeName = attributeName;
    }

    /**
     * Set the mowa entitlement attribute value (likely from LDAP).  This param
     * corresponds to a setting in portlet.xml context file
     * (example, urn:mace:uvic.ca:university:usource:mowa_user).
     *
     * @param attributeValue
     *          The mowa attribute value.
     *
     * @see mowaEntitlementAttributeName
     */
    public final void setMowaEntitlementAttributeValue(
            final String attributeValue) {
        this.mowaEntitlementAttributeValue = attributeValue;
    }

    /**
     * Set the checkMowaUser boolean value.  If true this will check to see if
     * the portal user has a mowa account, and if not, display a nice view
     * informing the user that they need a mowa account.
     *
     * @param checkAccount
     *          The boolean flag to enable a check on a users mowa account.
     */
    @Required
    public final void setCheckMowaUser(final boolean checkAccount) {
        this.checkMowaUser = checkAccount;
    }
}
