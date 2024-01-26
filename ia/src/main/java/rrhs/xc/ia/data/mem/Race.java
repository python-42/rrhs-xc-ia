package rrhs.xc.ia.data.mem;

import rrhs.xc.ia.data.database.SQLRow;
import rrhs.xc.ia.data.database.SQLTypeConversion;
import rrhs.xc.ia.data.database.SQLTypeConversion.SQLTableInformation;
import rrhs.xc.ia.data.i.SQLSerializable;

public class Race implements SQLSerializable{

    private String athleteName;
    private String meetName;
    private boolean varsity;
    private double timeSeconds;
    private double splitOneSeconds;
    private double splitTwoSeconds;
    private int place;

    private boolean cansqlWrite = true;

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

    public boolean isVarsity() {
        return varsity;
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

    @Override
    public void loadFromSQL(SQLRow result) {
        if(cansqlWrite) {
            athleteName = SQLTypeConversion.getString(result.get(SQLTableInformation.Race.ATHLETE_NAME_STR));
            meetName = SQLTypeConversion.getString(result.get(SQLTableInformation.Race.MEET_NAME_STR));
            varsity = SQLTypeConversion.getBoolean(result.get(SQLTableInformation.Race.VARSITY_BOOL));
            timeSeconds = SQLTypeConversion.getDouble(result.get(SQLTableInformation.Race.TOTAL_TIME_DBL));
            splitOneSeconds = SQLTypeConversion.getDouble(result.get(SQLTableInformation.Race.MILE_SPLIT_ONE_DBL));
            splitTwoSeconds = SQLTypeConversion.getDouble(result.get(SQLTableInformation.Race.MILE_SPLIT_TWO_DBL));
            place = SQLTypeConversion.getInt(result.get(SQLTableInformation.Race.PLACE_INT));

            cansqlWrite = false;
        }
        
    }

    @Override
    public SQLRow writeTOSQL() {
        SQLRow row = new SQLRow("Race", 0); //TODO ID
        row.putPair(SQLTableInformation.Race.ATHLETE_NAME_STR, athleteName);
        row.putPair(SQLTableInformation.Race.MEET_NAME_STR, meetName);
        row.putPair(SQLTableInformation.Race.VARSITY_BOOL, varsity);
        row.putPair(SQLTableInformation.Race.TOTAL_TIME_DBL, timeSeconds);
        row.putPair(SQLTableInformation.Race.MILE_SPLIT_ONE_DBL, splitOneSeconds);
        row.putPair(SQLTableInformation.Race.MILE_SPLIT_TWO_DBL, splitTwoSeconds);
        row.putPair(SQLTableInformation.Race.PLACE_INT, place);
        return row;
    }
    
}