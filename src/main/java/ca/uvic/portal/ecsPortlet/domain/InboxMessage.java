/**
 * A domain class for Exchange Channel Inbox Messages
 */
package ca.uvic.portal.ecsPortlet.domain;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * @author Charles Frank
 * @version $svn:Id$
 *
 */
public class InboxMessage {

    public String id;
    public String subject;
    public String sensitivity;
    public int size;
    public Date dateTimeCreated;
    public Date dateTimeSent;
    public Boolean hasAttachments;
    public String fromMailboxName;
    public Boolean isRead;
    public static final String SOAPDATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    
    
    public InboxMessage() {}
    
    public String toString() {
        return "Subject: " + subject;
    }
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }
    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
    /**
     * @return the sensitivity
     */
    public String getSensitivity() {
        return sensitivity;
    }
    /**
     * @param sensitivity the sensitivity to set
     */
    public void setSensitivity(String sensitivity) {
        this.sensitivity = sensitivity;
    }
    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }
    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }
    /**
     * @param A string in the format required by SimpleDateFormat
     * @return the SimpleDateFormat manipulated string
     */
    public String getDateTimeSent(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(this.dateTimeSent);
    }
    /**
     * @param dateTimeSent string to create a Date object with 
     */
    public void setDateTimeSent(String timeSent) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(SOAPDATEFORMAT);
        Date messageDate = sdf.parse(timeSent);
        this.dateTimeSent = messageDate;
    }
    /**
     * @param A string in the format required by SimpleDateFormat
     * @return the SimpleDateFormat manipulated string
     */
    public String getDateTimeCreated(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(this.dateTimeCreated);
    }
    /**
     * @param dateTimeCreated string to create Date object with
     */
    public void setDateTimeCreated(String timeCreated) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(SOAPDATEFORMAT);
        Date createdDate = sdf.parse(timeCreated);
        this.dateTimeCreated = createdDate;
    }
    /**
     * @return the hasAttachements
     */
    public Boolean getHasAttachments() {
        return hasAttachments;
    }
    /**
     * @param hasAttachements the hasAttachements to set
     */
    public void setHasAttachments(Boolean hasAttachments) {
        this.hasAttachments = hasAttachments;
    }
    /**
     * @return the fromMailboxName
     */
    public String getFromMailboxName() {
        return fromMailboxName;
    }
    /**
     * @param fromMailboxName the fromMailboxName to set
     */
    public void setFromMailboxName(String fromMailboxName) {
        this.fromMailboxName = fromMailboxName;
    }
    /**
     * @return the isRead
     */
    public Boolean getIsRead() {
        return isRead;
    }
    /**
     * @param isRead the isRead to set
     */
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

}
