package ca.uvic.portal.ecsPortlet.domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * This class is responsible for returning the soap xml body for locating
 * calendar events.
 * @author Charles Frank
 * @version svn:$Id$
 *
 */
public final class EcsCalendarItemSoap extends EcsRemoteSoapCall {

    /**
     * private Soap date format receiving from exchange server.
     */
    private static final String SOAPDATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    /**
     * private The hour of the start of a business day.
     */
    private static final int BUSINESSDAYSTART = 7;
    /**
     * private The hour of the end of a business day.
     */
    private static final int BUSINESSDAYEND = 18;
    /**
     * private The integer value needed for calculating one day to add in
     * the past.
     */
    private static final int ONEDAYPAST = -1;
    /**
     * private The integer value needed for calculating one day to add in
     * the future.
     */
    private static final int ONEDAYFUTURE = 1;

    /**
     * private eventLimit Event limit property for the soap envelope.
     */
    private static int eventLimit;

    /**
     * Set the DayTense to past or present.  This enum represents either
     * yesterday to today,today to tomorrow, or only today. It will either be
     * past or future tense, combined with today's events, or just today's
     * events.
     */
    public enum DayTense {
        /**
         * The enum value representing yesterday to today.
         */
        YESTERDAY,
        /**
         * The enum value representing today to tomorrow.
         */
        TOMORROW,
        /**
         * The enum value representing ONLY today.
         */
        TODAY
    }
    /**
     * private dayTense One the possible enum values YESTERDAY, or TODAY.
     */
     private DayTense dayTense;

    /**
     * Constructor uses Spring injection.
     * @param evtLimit inbox message limit for the soap envelope
     * @param tense The one day past/future tense for calendar event retrieval.
     */
    public EcsCalendarItemSoap(final int evtLimit, final DayTense tense) {
        super();
        eventLimit = evtLimit;
        //Internal typing for Enum will help with this at compile time check.
        this.dayTense = tense;
        this.setXMLBody();
    }

    /**
     * Set the soap xml body of the soap envelope (decorated by super class).
     * Query for calendar events one day past through today, or query for today
     * through tomorrow.
     * @see EcsSoap
     * @see dayTense
     */
    protected void setXMLBody() {
      //Calculate the start and end dates
      SimpleDateFormat dateFormat = new SimpleDateFormat(SOAPDATEFORMAT);
      String dayStart;
      String dayEnd;
      switch (this.dayTense) {
          case YESTERDAY:
              dayStart = dateFormat.format(
                      this.getDayPastOrFuture(ONEDAYPAST, BUSINESSDAYSTART));
              dayEnd   = dateFormat.format(
                      this.getToday(BUSINESSDAYEND));
              break;
          case TOMORROW:
              dayStart = dateFormat.format(this.getToday(BUSINESSDAYSTART));
              dayEnd   = dateFormat.format(
                      this.getDayPastOrFuture(ONEDAYFUTURE, BUSINESSDAYEND));
              break;
          case TODAY:
              dayStart = dateFormat.format(this.getToday(BUSINESSDAYSTART));
              dayEnd   = dateFormat.format(this.getToday(BUSINESSDAYEND));
              break;
          default:
              //NOTE: we should never get here cause the constructor
              //requires the Enum DayTense type, and will fail to compile
              //if it doesn't get a valid value.
              dayStart = dateFormat.format(this.getToday(BUSINESSDAYSTART));
              dayEnd   = dateFormat.format(this.getToday(BUSINESSDAYEND));
      }

        String xmlBody =
        "<FindItem xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\" xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\" Traversal=\"Shallow\">" + this.getLineEnding()
      +   "<ItemShape>" + this.getLineEnding()
      +     "<t:BaseShape>Default</t:BaseShape>" + this.getLineEnding()
      +   "</ItemShape>" + this.getLineEnding()
      +   "<CalendarView MaxEntriesReturned=\"" + eventLimit + "\" StartDate=\"" + dayStart + "\" EndDate=\"" + dayEnd + "\" />" + this.getLineEnding()
      +   "<ParentFolderIds>" + this.getLineEnding()
      +     "<t:DistinguishedFolderId Id=\"calendar\"/>" + this.getLineEnding()
      +   "</ParentFolderIds>" + this.getLineEnding()
      + "</FindItem>" + this.getLineEnding();
        super.setXMLBody(xmlBody);
    }
    /**
     * private method for Getting today with specific hour settings.
     * @param hour Pass in the hour to set for today.
     * @return Date Give back a Date object with minute and seconds zeroed out.
     */
    private Date getToday(final int hour) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * private method for getting one day in the future/past, relative to now
     * with specific hour settings.
     * @param dayPastOrFuture Pass in an integer representing 1 day in the past
     * or 1 day in the future.
     * @param hour Pass in the hour to set for the day.
     * @return Date Give back a Date object with minute and seconds zeroed out.
     */
    private Date getDayPastOrFuture(final int dayPastOrFuture, final int hour) {
        Calendar cal = Calendar.getInstance();
        //Take us 1 day into the past or future.
        cal.add(Calendar.DATE, dayPastOrFuture);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

}
