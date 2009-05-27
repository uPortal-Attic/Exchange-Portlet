package ca.uvic.portal.ecsPortlet.domain;

/**
 * This class is responsible for returning the soap xml body for locating
 * calendars that are sub calendars of the main calendar folder.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public final class EcsCalendarListSoap extends EcsRemoteSoapCall {

    /**
     * private message limit property for the soap envelope.
     */
    private static String calendarParentFolderId;

    /**
     * Constructor uses Spring injection.
     * @param msgLimit inbox message limit for the soap envelope
     */
    public EcsCalendarListSoap(final String calParentFolderId) {
        super();
        calendarParentFolderId = calParentFolderId;
        this.setXMLBody();
    }

    /**
     * Set the soap xml body of the soap envelope (decorated by super class).
     * @see EcsSoap
     */
    protected void setXMLBody() {
        String xmlBody =
        "<FindFolder xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\" Traversal=\"Shallow\">" + this.getLineEnding()
      +   "<FolderShape>" + this.getLineEnding()
      +     "<t:BaseShape>Default</t:BaseShape>" + this.getLineEnding()
      +   "</FolderShape>" + this.getLineEnding()
      +   "<ParentFolderIds>" + this.getLineEnding()
      +     "<t:DistinguishedFolderId Id=\"" + calendarParentFolderId + "\"/>" + this.getLineEnding()
      +   "</ParentFolderIds>" + this.getLineEnding()
      + "</FindFolder>" + this.getLineEnding();
        super.setXMLBody(xmlBody);
    }

}
