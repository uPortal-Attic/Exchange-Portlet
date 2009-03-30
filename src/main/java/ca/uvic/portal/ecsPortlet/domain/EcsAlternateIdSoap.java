package ca.uvic.portal.ecsPortlet.domain;

import ca.uvic.portal.ecsPortlet.domain.EcsRemoteSoapCall;

public final class EcsAlternateIdSoap extends EcsRemoteSoapCall {
    
    private static String fromIdType;
    private static String toIdType;
    private static String referenceId; 
    private static String userMailBox;

    //Constructor Injection
    public EcsAlternateIdSoap(String fromType, String toType, String refId, String mailBox) {
        super();
        fromIdType  = fromType;
        toIdType    = toType;
        referenceId = refId;
        userMailBox = mailBox;
        this.setXMLBody();
    }

    protected void setXMLBody() {
        String xmlBody = 
     "<ConvertId xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\" xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\" DestinationFormat=\"" + toIdType + "\">" + this.getLineEnding() +
       "<SourceIds>" + this.getLineEnding() +
         "<t:AlternateId Format=\"" + fromIdType + "\"" + " Id=\"" + referenceId + "\""  + " Mailbox=\"" + userMailBox + "\"/>" + this.getLineEnding() +
       "</SourceIds>" + this.getLineEnding() +
     "</ConvertId>" + this.getLineEnding();
        super.setXMLBody(xmlBody);    
    }

}
