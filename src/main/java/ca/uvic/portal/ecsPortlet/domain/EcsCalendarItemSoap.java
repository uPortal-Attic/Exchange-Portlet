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
    private static final int BUSINESSDAYSTART = 6;
    /**
     * private The hour of the end of a business day.
     */
    private static final int BUSINESSDAYEND = 21;
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
     * private calendarId The calendar id property for the soap envelope.
     * this will default to 'calendar' for most people.
     */
    private static String calendarId = "calendar";

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
      * private timeCalStart If a dayTense isn't passed in, use timeStart
      * to construct a Date.  The string must match the SOAPDATEFORMAT.
      */
     private Date timeCalStart;
     /**
      * private timeCalEnd If a dayTense isn't passed in, use timeEnd to
      * construct a Date.  The string must match the SOAPDATEFORMAT.
      */
     private Date timeCalEnd;

    /**
     * Constructor to use if you need one day in past/future and todays events,
     * or just todays events.
     * @param evtLimit Calender item event limit for the soap envelope.
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
     * Constructor to use if you need one day in past/future and todays events,
     * or just todays events, and if you desire a calenderId that is not the
     * default 'calendar'.
     * @param evtLimit Calendar item event limit for the soap envelope.
     * @param tense The one day past/future tense for calendar event retrieval.
     * @param calId The calendar id to set for the soap envelope.
     */
    public EcsCalendarItemSoap(final int evtLimit, final DayTense tense,
                               final String calId) {
        super();
        eventLimit = evtLimit;
        //Internal typing for Enum will help with this at compile time check.
        this.dayTense = tense;
        calendarId = calId;
        this.setXMLBody();
    }
    /**
     * Constructor to use if you need an unspecified range of calendar items.
     * @param evtLimit Calender item event limit for the soap envelope.
     * @param timeStart String similar in format to SOAPDATEFORMAT.
     * @param timeEnd String similar in format to SOAPDATEFORMAT.
     * won't parse according to SOAPDATEFORMAT.
     * @throws Exception Will throw an IllegalArgumentException if timeStart is
     * after timeEnd in chrono order, and will also throw a ParseException if
     * the string date is not similar to SOAPDATEFORMAT.
     */
    public EcsCalendarItemSoap(final int evtLimit,
                               final String timeStart,
                               final String timeEnd)
            throws Exception {
        super();
        eventLimit = evtLimit;
        SimpleDateFormat sdf = new SimpleDateFormat(SOAPDATEFORMAT);
        this.timeCalStart = sdf.parse(timeStart);
        this.timeCalEnd   = sdf.parse(timeEnd);
        if (this.timeCalStart.after(this.timeCalEnd)) {
            throw new IllegalArgumentException(
                    "timeStart must be before timeEnd chronologically");
        }
        this.setXMLBody();
    }
    /**
     * Constructor to use if you need an unspecified range of calendar items,
     * and if you desire a calenderId that is not the default 'calendar'.
     * @param evtLimit Calender item event limit for the soap envelope.
     * @param timeStart String similar in format to SOAPDATEFORMAT.
     * @param timeEnd String similar in format to SOAPDATEFORMAT.
     * won't parse according to SOAPDATEFORMAT.
     * @param calId The calendar id to set for the soap envelope.
     * @throws Exception Will throw an IllegalArgumentException if timeStart is
     * after timeEnd in chrono order, and will also throw a ParseException if
     * the string date is not similar to SOAPDATEFORMAT.
     */
    public EcsCalendarItemSoap(final int evtLimit,
                               final String timeStart,
                               final String timeEnd,
                               final String calId)
            throws Exception {
        super();
        eventLimit = evtLimit;
        SimpleDateFormat sdf = new SimpleDateFormat(SOAPDATEFORMAT);
        this.timeCalStart = sdf.parse(timeStart);
        this.timeCalEnd   = sdf.parse(timeEnd);
        calendarId = calId;
        if (this.timeCalStart.after(this.timeCalEnd)) {
            throw new IllegalArgumentException(
                    "timeStart must be before timeEnd chronologically");
        }
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
      if (this.dayTense != null) {
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
              dayStart = dateFormat.format(this.getToday(BUSINESSDAYSTART));
              dayEnd   = dateFormat.format(this.getToday(BUSINESSDAYEND));
              break;
          }
      } else {
        dayStart = dateFormat.format(this.timeCalStart);
        dayEnd   = dateFormat.format(this.timeCalEnd);
      }

        String xmlBody =
        "<FindItem xmlns=\"http://schemas.microsoft.com/exchange/services/2006/messages\" xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\" Traversal=\"Shallow\">" + this.getLineEnding()
      +   "<ItemShape>" + this.getLineEnding()
      +     "<t:BaseShape>Default</t:BaseShape>" + this.getLineEnding()
      +   "</ItemShape>" + this.getLineEnding()
      +   "<CalendarView MaxEntriesReturned=\"" + eventLimit + "\" StartDate=\"" + dayStart + "\" EndDate=\"" + dayEnd + "\" />" + this.getLineEnding()
      +   "<ParentFolderIds>" + this.getLineEnding()
      +     this.getFolderQuery() + this.getLineEnding()
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

    /**
     * private method to get the folder query string for the xml body
     * of the soap request.
     * @return Give back the formatted folder query string.
     */
    private String getFolderQuery() {
        String folderQuery = "";
        if(calendarId.equals("calendar")) {
            folderQuery =
                "<t:DistinguishedFolderId Id=\"" + calendarId + "\"/>";
        } else {
            folderQuery =
                "<t:FolderId Id=\"" + calendarId + "\"/>";
        }
        return folderQuery;
    }

}
