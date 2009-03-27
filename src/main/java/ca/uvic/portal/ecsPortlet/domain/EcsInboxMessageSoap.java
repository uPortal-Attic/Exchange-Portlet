package ca.uvic.portal.ecsPortlet.domain;

/**
 * This class is responsible for returning the soap xml body for locating
 * inbox messages.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public final class EcsInboxMessageSoap extends EcsRemoteSoapCall {

    /**
     * private message limit property for the soap envelope.
     */
    private static int messageLimit;

    /**
     * Constructor uses Spring injection.
     * @param msgLimit inbox message limit for the soap envelope
     */
    public EcsInboxMessageSoap(final int msgLimit) {
        super();
        messageLimit = msgLimit;
        this.setXMLBody();
    }

    /**
     * Set the soap xml body of the soap envelope (decorated by super class).
     * @see EcsSoap
     */
    protected void setXMLBody() {
        String xmlBody =
        "<FindItem xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\" xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\" Traversal=\"Shallow\">" + this.getLineEnding()
      +   "<ItemShape>" + this.getLineEnding()
      +     "<t:BaseShape>Default</t:BaseShape>" + this.getLineEnding()
      +   "</ItemShape>" + this.getLineEnding()
      +   "<IndexedPageItemView MaxEntriesReturned=\"" + messageLimit + "\" BasePoint=\"Beginning\" Offset=\"0\" />" + this.getLineEnding()
      +   "<ParentFolderIds>" + this.getLineEnding()
      +     "<t:DistinguishedFolderId Id=\"inbox\"/>" + this.getLineEnding()
      +   "</ParentFolderIds>" + this.getLineEnding()
      + "</FindItem>" + this.getLineEnding();
        super.setXMLBody(xmlBody);
    }

}
