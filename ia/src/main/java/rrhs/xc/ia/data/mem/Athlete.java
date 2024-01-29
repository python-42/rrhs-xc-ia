package rrhs.xc.ia.data.mem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import rrhs.xc.ia.data.Season;
import rrhs.xc.ia.data.database.SQLRow;
import rrhs.xc.ia.data.database.SQLTypeConversion;
import rrhs.xc.ia.data.database.SQLTypeConversion.SQLTableInformation;
import rrhs.xc.ia.data.i.PDFExportable;
import rrhs.xc.ia.data.i.SQLSerializable;

public class Athlete implements PDFExportable, SQLSerializable {

    private String name;
    private boolean isBoysTeam;
    private int gradYear;
    private HashMap<Season, List<Race>> raceMap = new HashMap<Season, List<Race>>();

    private boolean canSqlWrite = true;

    public Athlete(List<Race> list) {
        if (list == null) {
            return;
        }
        for (Race r : list) {
            if (raceMap.get(r.getSeason()) == null) {
                raceMap.put(r.getSeason(), new ArrayList<Race>());
            }
            raceMap.get(r.getSeason()).add(r);
        }
    }

    /**
     * Get the race with the best time. If the list contains races with identical
     * times, one of them will be returned. There is no guarantee of which one.
     * 
     * If the list is null or empty, return null.
     * 
     * @param list a list of race objects.
     * @return the fastest race or null
     */
    private Race getFastest(List<Race> list) {
        if(list == null || list.size() == 0) {
            return null;
        }

        int index = 0;
        for (int i = 1; i < list.size(); i ++) {
            if(list.get(i).getTimeSeconds() < list.get(index).getTimeSeconds()) {
                index = i;
            }
        }
        
        return list.get(index);
    }

    /**
     * Get the race with the worst time. If the list contains race with identical
     * times, one of them will be returned. There is no guarnatee of which one.
     * 
     * If the list is null or empty, return null.
     * 
     * @param list a list of race objects.
     * @return the slowest race or null
     */
    private Race getSlowest(List<Race> list) {
        if(list == null || list.size() == 0) {
            return null;
        }
        int index = 0;
        for (int i = 1; i < list.size(); i ++) {
            if(list.get(i).getTimeSeconds() > list.get(index).getTimeSeconds()) {
                index = i;
            }
        }
        
        return list.get(index);
    }

    /**
     * Condense all of the races from the HashMap in to a single list.
     * 
     * @return All the races from the map in a single list
     */
    private List<Race> condense() {
        List<Race> races = new ArrayList<Race>();
        for (List<Race> r : raceMap.values()) {
            if (r == null) {
                continue;
            }
            races.addAll(r);
        }
        return races;
    }

    public String getName() {
        return name;
    }

    public boolean isBoysTeam() {
        return isBoysTeam;
    }

    public int getGradYear() {
        return gradYear;
    }

    public Race getWorstCareerRace() {
        return getSlowest(condense());
    }

    public Race getWorstSeasonRace(Season s) {
        return getSlowest(raceMap.get(s));

    }

    public Race getBestCareerRace() {
        return getFastest(condense());
    }

    public Race getBestSeasonRace(Season s) {
        return getFastest(raceMap.get(s));
    }

    public double getCareerTimeDropSeconds() {
        Race best = getBestCareerRace();
        Race worst = getWorstCareerRace();

        if(best != null && worst != null) {
            return worst.getTimeSeconds() - best.getTimeSeconds();
        }
        return -1;
    }

    public double getSeasonTimeDropSeconds(Season s) {
        Race best = getBestSeasonRace(s);
        Race worst = getWorstSeasonRace(s);

        if(best != null && worst != null) {
            return worst.getTimeSeconds() - best.getTimeSeconds();
        }
        return -1;
    }

    public double getCareerAverageTime() {
        double totalTime = 0;
        int count = 0;
        for (Race r : condense()) {
            totalTime += r.getTimeSeconds();
            count++;
        }

        if(count == 0) {
            return -1;
        }

        return totalTime / count;
    }

    public double getSeasonAverageTime(Season s) {
        if(raceMap.get(s) == null || raceMap.get(s).size() == 0) {
            return -1;
        }

        double totalTime = 0;
        for (Race r : raceMap.get(s)) {
            totalTime += r.getTimeSeconds();
        }

        return totalTime / raceMap.size();
    }

    public Season[] getSeasons() {
        Season[] s = {};
        s = raceMap.keySet().toArray(s);
        Arrays.sort(s);
        return s;
    }

    public double getAverageCareerSplitSeconds(int mile) {
        if (mile > 3 || mile < 1) {
            return -1;
        }

        double totalTime = 0;
        int count = 0;
        for (Race r : condense()) {
            count++;
            if(mile == 1) {
                totalTime += r.getMileOneSplitSeconds();
            }else if(mile == 2) {
                totalTime += r.getMileTwoSplitSeconds();
            }else {
                totalTime += r.getMileThreeSplitSeconds();
            }
        }

        if(count == 0) {
            return -1;
        }

        return totalTime / count;
    }

    public double getAverageSeasonSplitSeconds(Season s, int mile) {
        if (mile > 4 || mile < 1 || raceMap.get(s) == null || raceMap.get(s).size() == 0) {
            return -1;
        }

        double totalTime = 0;
        for (Race r : raceMap.get(s)) {
            if(mile == 1) {
                totalTime += r.getMileOneSplitSeconds();
            }else if(mile == 2) {
                totalTime += r.getMileTwoSplitSeconds();
            }else {
                totalTime += r.getMileThreeSplitSeconds();
            }
        }

        return totalTime / raceMap.get(s).size();
    }

    @Override
    public void loadFromSQL(SQLRow result) {
        if (canSqlWrite) {
            name = SQLTypeConversion.getString(result.get(SQLTableInformation.Athlete.NAME_STR));
            isBoysTeam = SQLTypeConversion.getBoolean(result.get(SQLTableInformation.Athlete.BOYS_TEAM_BOOL));
            gradYear = SQLTypeConversion.getInt(result.get(SQLTableInformation.Athlete.GRADUATION_YEAR_INT));

            canSqlWrite = false;
        }
    }

    @Override
    public SQLRow writeTOSQL() {
        SQLRow rtn = new SQLRow("Athlete", 0);

        rtn.putPair(SQLTableInformation.Athlete.NAME_STR, name);
        rtn.putPair(SQLTableInformation.Athlete.BOYS_TEAM_BOOL, isBoysTeam);
        rtn.putPair(SQLTableInformation.Athlete.GRADUATION_YEAR_INT, gradYear);

        return rtn;
    }

    @Override
    public void writeToPDF(File file) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'writeToPDF'");
    }

}
