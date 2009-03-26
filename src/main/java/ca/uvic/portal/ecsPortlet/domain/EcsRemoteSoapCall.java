/**
 * 
 */
package ca.uvic.portal.ecsPortlet.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Charles Frank
 * @version $svn:Id$
 *
 */
public class EcsRemoteSoapCall {
    
    private static String lineEnding = "\r\n";
    
    private static String xmlHeader = 
        "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + lineEnding;

    private static String soapEnvelopeOpen =
        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\">" + lineEnding;
    private static String soapEnvelopeClose =
        "</soap:Envelope>" + lineEnding;

    private static String soapHeader =
        "<soap:Header>\r\n" +
          "<t:RequestServerVersion Version=\"Exchange2007_SP1\"/>" + lineEnding +
        "</soap:Header>" + lineEnding;

    private static String soapBodyOpen = 
        "<soap:Body>" + lineEnding;

    private static String soapBodyClose = 
        "</soap:Body>" + lineEnding;

    private String soapXMLBody;
    private String soapXMLOpen;
    private String soapXMLClose;

    private final Log logger = LogFactory.getLog(getClass());

    public EcsRemoteSoapCall(){
        this.soapXMLOpen = xmlHeader + soapEnvelopeOpen + soapHeader + soapBodyOpen;
        this.soapXMLClose = soapBodyClose + soapEnvelopeClose;
    }

    public String getLineEnding() {
        return lineEnding;
    }

    protected void setXMLBody(String xml) {
        this.soapXMLBody = xml; 
    }

    public String getSoapCall() {
        //decorate the xml body w/the the soap headers/footers
        String soapXML = this.soapXMLOpen + this.soapXMLBody + this.soapXMLClose;
        if(logger.isDebugEnabled()){
            logger.debug("Assembled xml is: " + soapXML);
        }
        return soapXML;
    }

}
