package ca.uvic.portal.ecsPortlet.domain;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * A Domain class for Exchange Channel Calendar Items. This class works closely
 * with a Digester file that parses a soap response envelope into this
 * domain object.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public class CalendarItem {

    /**
     * private CalendarItem id.
     */
    private String id;
    /**
     * private CalendarItem owaId.
     */
    private String owaId;
    /**
     * private CalendarItem subject.
     */
    private String subject;
    /**
     * private CalendarItem has attachments.
     */
    private Boolean hasAttachments;
    /**
     * private CalendarItem start.
     */
    private Date start;
    /**
     * private CalendarItem end.
     */
    private Date end;
    /**
     * private CalendarItem legacyFreeBusyStatus.
     */
    private String legacyFreeBusyStatus;
    /**
     * private CalendarItem location.
     */
    private String location;
    /**
     * private CalendarItem calendarItemType.
     */
    private String calendarItemType;
    /**
     * private CalendarItem organizerMailboxName.
     */
    private String organizerMailboxName;
    /**
     * private soap date format receiving from exchange server.
     */
    private static final String SOAPDATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * Constructor default.
     */
    public CalendarItem() { }

    /**
     * @return the id
     */
    public final String getId() {
        return id;
    }
    /**
     * @param calItemId the id to set
     */
    public final void setId(final String calItemId) {
        this.id = calItemId;
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
     * @return the subject
     */
    public final String getSubject() {
        return subject;
    }
    /**
     * @param calItemSubject the subject to set
     */
    public final void setSubject(final String calItemSubject) {
        this.subject = calItemSubject;
    }

    /**
     * @return the attachments flag
     */
    public final Boolean getHasAttachments() {
        return hasAttachments;
    }
    /**
     * @param attachments the attachments flag to set
     */
    public final void setHasAttachments(final Boolean attachments) {
        this.hasAttachments = attachments;
    }

    /**
     * @param format a string in the format required by SimpleDateFormat
     * @return the SimpleDateFormat manipulated string
     */
    public final String getStart(final String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(this.start);
    }
    /**
     * @param timeStart string to create a Date object with
     * @throws ParseException a parse exception
     */
    public final void setStart(final String timeStart)
        throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(SOAPDATEFORMAT);
        Date calendarItemDate = sdf.parse(timeStart);
        this.start = calendarItemDate;
    }

    /**
     * @param format a string in the format required by SimpleDateFormat
     * @return the SimpleDateFormat manipulated string
     */
    public final String getEnd(final String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(this.end);
    }
    /**
     * @param timeEnd string to create Date object with
     * @throws ParseException a parse exception
     */
    public final void setEnd(final String timeEnd)
        throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(SOAPDATEFORMAT);
        Date calendarItemDate = sdf.parse(timeEnd);
        this.end = calendarItemDate;
    }

    /**
     * @return the location
     */
    public final String getLocation() {
        return location;
    }
    /**
     * @param loc the location to set.
     */
    public final void setLocation(final String loc) {
        this.location = loc;
    }

    /**
     * @return the calendarItemType
     */
    public final String getCalendarItemType() {
        return calendarItemType;
    }

    /**
     * @param calItemType the calItemType to set.
     */
    public final void setCalendarItemType(final String calItemType) {
        this.calendarItemType = calItemType;
    }

    /**
     * @return the organizerMailboxName
     */
    public final String getOrganizerMailboxName() {
        return organizerMailboxName;
    }
    /**
     * @param organizerMboxName the organizerMailboxName to set
     */
    public final void setOrganizerMailboxName(final String organizerMboxName) {
        this.organizerMailboxName = organizerMboxName;
    }

    /**
     * @return the legacyFreeBusyStatus
     */
    public final String getLegacyFreeBusyStatus() {
        return legacyFreeBusyStatus;
    }
    /**
     * @param legFreeBusyStat the legacyFreeBusyStatus to set
     */
    public final void setLegacyFreeBusyStatus(final String legFreeBusyStat) {
        this.legacyFreeBusyStatus = legFreeBusyStat;
    }


}
