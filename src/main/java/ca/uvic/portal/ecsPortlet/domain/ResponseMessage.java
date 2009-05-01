package ca.uvic.portal.ecsPortlet.domain;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class is a domain class that will create message response object from
 * each of the soap responses.  The soap responses have properties in common,
 * though each soap request is unique.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public class ResponseMessage {
    /**
     * private The response indicator from a returned soap response, will be
     * Success or Error.
     */ 
    private String responseIndicator;
    /**
     * private The response code from a returned soap response.
     */
    private String responseCode;
    /**
     * private The response message text from a returned soap response.
     */
    private String responseText;

    private ConcurrentLinkedQueue < Object > exchangeObjects =
        new ConcurrentLinkedQueue < Object >();
    /**
     * Add a java object constructed of the soap elements returned to the queue.
     * @param obj parameter representing a record
     */
    public final void addExchangeObject(final Object obj) {
        exchangeObjects.add(obj);
    }
    /**
     * Get the queue of built up exchange domain objects.
     * @return queue of exchange objects.
     */
    public final ConcurrentLinkedQueue < Object > getExchangeObjects() {
        return exchangeObjects;
    }

    /**
     * @return the responseIndicator
     */
    public final String getResponseIndicator() {
        return responseIndicator;
    }
    /**
     * @param respIndicator the responseIndicator to set
     */
    public final void setResponseIndicator(String respIndicator) {
        this.responseIndicator = respIndicator;
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
    public final void setResponseCode(String respCode) {
        this.responseCode = respCode;
    }
    /**
     * @return the responseText
     */
    public final String getResponseText() {
        return responseText;
    }
    /**
     * @param respText the responseText to set
     */
    public final void setResponseText(String respText) {
        this.responseText = respText;
    }

}
