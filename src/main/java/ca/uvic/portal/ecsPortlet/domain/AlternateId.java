package ca.uvic.portal.ecsPortlet.domain;

/**
 * A domain class for Exchange Alternate ID.  This class works closely with
 * a Digester file that parses a soap response envelope into this domain object.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public class AlternateId extends Domain {
    /**
     * private format of the id, EwsLegacyId, OwaId, etc.
     */
    private String format;
    /**
     * private id the requested alternate id of the exchange message.
     */
    private String id;
    /**
     * mailbox the user mailbox queried against.
     */
    private String mailbox;

    /**
     * Constructor default.
     */
    public AlternateId() {
        super();
    }

    /**
     * @return the format
     */
    public final String getFormat() {
        return format;
    }

    /**
     * @param idFormat the format to set
     */
    public final void setFormat(final String idFormat) {
        this.format = idFormat;
    }

    /**
     * @return the id
     */
    public final String getId() {
        return id;
    }

    /**
     * @param requestedId the id to set
     */
    public final void setId(final String requestedId) {
        this.id = requestedId;
    }

    /**
     * @return the mailbox
     */
    public final String getMailbox() {
        return mailbox;
    }

    /**
     * @param mailBox the mailbox to set
     */
    public final void setMailbox(final String mailBox) {
        this.mailbox = mailBox;
    };

}
