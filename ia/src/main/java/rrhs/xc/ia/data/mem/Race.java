package rrhs.xc.ia.data.mem;

import java.time.LocalDate;

import rrhs.xc.ia.data.database.SQLRow;
import rrhs.xc.ia.data.database.SQLTypeConversion.SQLTableInformation;
import rrhs.xc.ia.data.i.SQLDataObject;

public class Race extends SQLDataObject implements Comparable<Race>{

    private String athleteName;
    private String meetName;
    private LocalDate meetDate;

    private Level level;
    private Season season;
    private double timeSeconds;
    private double splitOneSeconds;
    private double splitTwoSeconds;
    private int place;

    private int id;

    public Race(String athleteName, String meetName, LocalDate meetDate, Level level, Season season, double timeSeconds, double splitOneSeconds, double splitTwoSeconds, int place, int id, boolean isNew) {
        super(isNew);

        this.athleteName = athleteName;
        this.meetName = meetName;
        this.meetDate = meetDate;
        this.level = level;
        this.season = season;
        this.timeSeconds = timeSeconds;
        this.splitOneSeconds = splitOneSeconds;
        this.splitTwoSeconds = splitTwoSeconds;
        this.place = place;
        this.id = id;
    }

    public double getAverageSplitSeconds() {
        return (timeSeconds) / 3.1;
    }

    public double getMileThreeSplitSeconds() {
        return (timeSeconds - (splitOneSeconds + splitTwoSeconds)) / 1.1;
    }

    /**
     * Find the difference between the longest split and the shortest split. 
     * @return the split difference in seconds
     */
    public double getSplitDifferenceSeconds() {
        double max = Math.max(splitOneSeconds, Math.max(splitTwoSeconds, getMileThreeSplitSeconds()));
        double min = Math.min(splitOneSeconds, Math.min(splitTwoSeconds, getMileThreeSplitSeconds()));

        return max - min;
    }

    //=======Basic getters=======

    public String getAthleteName() {
        return athleteName;
    }

    public String getMeetName() {
        return meetName;
    }

    public LocalDate getMeetDate() {
        return meetDate;
    }

    public Level getLevel() {
        return level;
    }

    public Season getSeason() {
        return season;
    }

    public double getTimeSeconds() {
        return timeSeconds;
    }

    public double getMileOneSplitSeconds() {
        return splitOneSeconds;
    }

    public double getMileTwoSplitSeconds() {
        return splitTwoSeconds;
    }

    public int getPlace() {
        return place;
    }

    //========Setters============

    public void setAthleteName(String athleteName) {
        this.setModified();
        this.athleteName = athleteName;
    }

    public void setMeetName(String meetName) {
        this.setModified();
        this.meetName = meetName;
    }

    public void setMeetDate(LocalDate meetDate) {
        this.setModified();
        this.meetDate = meetDate;
    }

    public void setLevel(Level level) {
        this.setModified();
        this.level = level;
    }

    public void setSeason(Season season) {
        this.setModified();
        this.season = season;
    }

    public void setTimeSeconds(double timeSeconds) {
        this.setModified();
        this.timeSeconds = timeSeconds;
    }

    public void setSplitOneSeconds(double splitOneSeconds) {
        this.setModified();
        this.splitOneSeconds = splitOneSeconds;
    }

    public void setSplitTwoSeconds(double splitTwoSeconds) {
        this.setModified();
        this.splitTwoSeconds = splitTwoSeconds;
    }

    public void setPlace(int place) {
        this.setModified();
        this.place = place;
    }


    @Override
    public SQLRow writeToSQL() {
        SQLRow row = new SQLRow("Race", id);
        row.putPair(SQLTableInformation.Athlete.NAME_STR, athleteName);
        row.putPair(SQLTableInformation.Meet.MEET_NAME_STR, meetName);
        row.putPair(SQLTableInformation.Meet.MEET_DATE_DATE, meetDate);
        row.putPair(SQLTableInformation.Race.LEVEL_ENUM, level);
        row.putPair(SQLTableInformation.Race.SEASON_ENUM, season);
        row.putPair(SQLTableInformation.Race.TOTAL_TIME_DBL, timeSeconds);
        row.putPair(SQLTableInformation.Race.MILE_SPLIT_ONE_DBL, splitOneSeconds);
        row.putPair(SQLTableInformation.Race.MILE_SPLIT_TWO_DBL, splitTwoSeconds);
        row.putPair(SQLTableInformation.Race.PLACE_INT, place);
        return row;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Race other = (Race) obj;
        if (athleteName == null) {
            if (other.athleteName != null)
                return false;
        } else if (!athleteName.equals(other.athleteName))
            return false;
        if (meetName == null) {
            if (other.meetName != null)
                return false;
        } else if (!meetName.equals(other.meetName))
            return false;
        if (meetDate == null) {
            if (other.meetDate != null)
                return false;
        } else if (!meetDate.equals(other.meetDate))
            return false;
        if (season != other.season)
            return false;
        if (level != other.level)
            return false;
        if (Double.doubleToLongBits(timeSeconds) != Double.doubleToLongBits(other.timeSeconds))
            return false;
        if (Double.doubleToLongBits(splitOneSeconds) != Double.doubleToLongBits(other.splitOneSeconds))
            return false;
        if (Double.doubleToLongBits(splitTwoSeconds) != Double.doubleToLongBits(other.splitTwoSeconds))
            return false;
        if (place != other.place)
            return false;
        return true;
    }

    @Override
    public int compareTo(Race other) {
        if (this.equals(other)) {
            return 0;
        }

        if (this.getMeetDate().equals(other.getMeetDate())) {
            return (int) ((this.getTimeSeconds() - other.getTimeSeconds()) + 0.5);
        }else {
            return this.getMeetDate().compareTo(other.getMeetDate());
        }

    }
}