package ca.uvic.portal.ecsPortlet.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is responsible for wrapping each soap body with the appropriate
 * soap xml declaration, envelope, header, and body tags.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public class EcsRemoteSoapCall {
    /**
     * private line ending to use with each soap element line.
     */
    private static String lineEnding = "\r\n";
    /**
     * private proper xml header.
     */
    private static String xmlHeader =
        "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + lineEnding;
    /**
     * private soap envelope open tag.
     */
    private static String soapEnvelopeOpen =
        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\">" + lineEnding;
    /**
     * private soap envelope close tag.
     */
    private static String soapEnvelopeClose =
        "</soap:Envelope>" + lineEnding;
    /**
     * private soap header tag.
     */
    private static String soapHeader =
        "<soap:Header>\r\n"
      +   "<t:RequestServerVersion Version=\"Exchange2007_SP1\"/>" + lineEnding
      + "</soap:Header>" + lineEnding;
    /**
     * private soap body open tag.
     */
    private static String soapBodyOpen =
        "<soap:Body>" + lineEnding;
    /**
     * private soap body close tag.
     */
    private static String soapBodyClose =
        "</soap:Body>" + lineEnding;

    /**
     * private soap xml body passed in from sub class.
     */
    private String soapXMLBody;
    /**
     * private string holds the entire wrapping header applied before soap body.
     */
    private String soapXMLOpen;
    /**
     * private string holds the entire wrapping footer applied after soap body.
     */
    private String soapXMLClose;
    /**
     * private commons logger.
     */
    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Constructor sets up the soap opening and closing wrappers that enclose
     * the soap body received from the subclass.
     */
    public EcsRemoteSoapCall() {
        this.soapXMLOpen = xmlHeader + soapEnvelopeOpen + soapHeader
            + soapBodyOpen;
        this.soapXMLClose = soapBodyClose + soapEnvelopeClose;
    }

    /**
     * Get the appropriate line ending for the end of an xml element line.
     * @return lineEnding
     */
    public final String getLineEnding() {
        return lineEnding;
    }
    /**
     * Set the soap xml body to be wrapped by the header and footer.
     * @param xmlBody the body of the soap request sent in from the subclass.
     */
    protected final void setXMLBody(final String xmlBody) {
        this.soapXMLBody = xmlBody;
    }
    /**
     * Get the entire soap call.
     * @return soapXML the decorated soap call, in its entirety.
     */
    public final String getSoapCall() {
        //decorate the xml body w/the the soap headers/footers
        String soapXML = this.soapXMLOpen
            + this.soapXMLBody
            + this.soapXMLClose;
        if (logger.isDebugEnabled()) {
            logger.debug("Assembled xml is: " + soapXML);
        }
        return soapXML;
    }

}
