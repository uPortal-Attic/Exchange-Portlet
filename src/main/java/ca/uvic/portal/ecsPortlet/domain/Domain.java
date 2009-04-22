package ca.uvic.portal.ecsPortlet.domain;

/**
 * Superclass for all Domain classes.  Basically a wrapper for common
 * soap elements that need to be set in the Domain classes.  For example,
 * error responses on faulty soap exchange.
 * @author Charles Frank
 * @version svn:$Id$
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
    private String errorMessageText;
    /**
     * private The ResponseCode element from soap call. Is usually 'NoError'
     * or 'Error*'.
     */
    private String errorResponseCode;

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
     * @return the errorMessageText
     */
    public final String getErrorMessageText() {
        return errorMessageText;
    }

    /**
     * @param errMsgTxt the errorMessageText to set
     */
    public final void setErrorMessageText(final String errMsgTxt) {
        this.errorMessageText = errMsgTxt;
    }

    /**
     * @return the errorResponseCode
     */
    public final String getErrorResponseCode() {
        return errorResponseCode;
    }

    /**
     * @param errRespCode the errorResponseCode to set
     */
    public final void setErrorResponseCode(final String errRespCode) {
        this.errorResponseCode = errRespCode;
    }

}
