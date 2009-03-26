/**
 * 
 */
package ca.uvic.portal.ecsPortlet.domain;

/**
 * @author Charles Frank
 * @version $svn:Id$
 *
 */
public final class EcsInboxMessageSoap extends EcsRemoteSoapCall {

    protected int messageLimit;

    /**
     * 
     */
    public EcsInboxMessageSoap(int messageLimit) {
        super();
        this.messageLimit = messageLimit;
        this.setXMLBody();
    }

    protected void setXMLBody() {
        String xmlBody = 
        "<FindItem xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\" xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\" Traversal=\"Shallow\">" + this.getLineEnding() +
          "<ItemShape>" + this.getLineEnding() +
            "<t:BaseShape>Default</t:BaseShape>" + this.getLineEnding() +
          "</ItemShape>" + this.getLineEnding() +
          "<IndexedPageItemView MaxEntriesReturned=\"" + messageLimit + "\" BasePoint=\"Beginning\" Offset=\"0\" />" + this.getLineEnding() +
            "<ParentFolderIds>" + this.getLineEnding() +
              "<t:DistinguishedFolderId Id=\"inbox\"/>" + this.getLineEnding() +
           "</ParentFolderIds>" + this.getLineEnding() +
        "</FindItem>" + this.getLineEnding();
        super.setXMLBody(xmlBody);
    }

}
