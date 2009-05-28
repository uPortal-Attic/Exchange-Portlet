package ca.uvic.portal.ecsPortlet.domain;


/**
 * A Domain class for an Exchange Channel Calendar List. This class works
 * closely * with a Digester file that parses a soap response envelope into
 * this * domain object.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public class CalendarList {

    /**
     * private message id.
     */
    private String id;
    /**
     * private message owaId.
     */
    private String owaId;
    /**
     * private message displayName.
     */
    private String displayName;
    /**
     * private message childFolderCount.
     */
    private int childFolderCount;

    /**
     * Constructor default.
     */
    public CalendarList() { }

    /**
     * @return the id
     */
    public final String getId() {
        return id;
    }
    /**
     * @param calId the id to set
     */
    public final void setId(final String calId) {
        this.id = calId;
    }
    /**
     * @return the owaId
     */
    public final String getOwaId() {
        return owaId;
    }
    /**
     * @param oId owaId to set
     */
    public final void setOwaId(final String oId) {
        this.owaId = oId;
    }

    /**
     * @return the displayName
     */
    public final String getDisplayName() {
        return displayName;
    }
    /**
     * @param calDisplayName the displayName to set
     */
    public final void setDisplayName(final String calDisplayName) {
        this.displayName = calDisplayName;
    }
    /**
     * @return the childFolderCount
     */
    public final int getChildFolderCount() {
        return childFolderCount;
    }
    /**
     * @param calChildFolderCount the childFolderCount to set
     */
    public final void setChildFolderCount(final int calChildFolderCount) {
        this.childFolderCount = calChildFolderCount;
    }

}
