package ca.uvic.portal.ecsPortlet.domain;

/**
 * Superclass for all Domain classes where the *ResponseMessage is a one to one
 * with the object returned, for example a returned envelope for the ConvertId
 * process soap request.  You would not use this class with a soap response that
 * returned only one *ResponseMessage, for example the FindItem soap request.
 * You would use the ResponseMessage Domain class for a soap response with only
 * one *ResponseMessage item in the envelope.
 * @author Charles Frank
 * @version svn:$Id$
 * @see ResponseMessage.java
 *
 */
public class Domain {
    /**
     * private The ResponseClass element from soap call. Is usually 'Success'
     * or 'Error'.
     */
    private String responseIndicator;
    /**
     * private The MessageText associated with the error text passed back
     * after a problematic soap exchange.
     */
    private String responseText;
    /**
     * private The ResponseCode element from soap call. Is usually 'NoError'
     * or 'Error*'.
     */
    private String responseCode;

    /**
     * Default constructor.
     */
    public Domain() { }

    /**
     * @return the responseIndicator
     */
    public final String getResponseIndicator() {
        return responseIndicator;
    }

    /**
     * @param respIndicator the responseIndicator to set
     */
    public final void setResponseIndicator(final String respIndicator) {
        this.responseIndicator = respIndicator;
    }

    /**
     * @return the responseText
     */
    public final String getResponseText() {
        return responseText;
    }

    /**
     * @param respTxt the errorMessageText to set
     */
    public final void setResponseText(final String respTxt) {
        this.responseText = respTxt;
    }

    /**
     * @return the responseCode
     */
    public final String getResponseCode() {
        return responseCode;
    }

    /**
     * @param respCode the responseCode to set
     */
    public final void setResponseCode(final String respCode) {
        this.responseCode = respCode;
    }

}
