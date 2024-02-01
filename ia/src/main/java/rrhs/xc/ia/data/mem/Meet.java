package rrhs.xc.ia.data.mem;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import com.itextpdf.text.DocumentException;

import rrhs.xc.ia.data.database.SQLRow;
import rrhs.xc.ia.data.database.SQLTypeConversion;
import rrhs.xc.ia.data.database.SQLTypeConversion.SQLTableInformation;
import rrhs.xc.ia.data.i.PDFExportable;
import rrhs.xc.ia.data.i.SQLSerializable;

public class Meet implements SQLSerializable, PDFExportable  {

    private String name;
    private LocalDate date;
    private int[] athleteCounts = {-1, -1, -1, -1}; //[varsityBoys, varsityGirls, JVBoys, JVGirls]
    private HashMap<Level, List<Race>> raceMap = new HashMap<Level, List<Race>>();

    private boolean canSqlWrite = true;

    public Meet(List<Race> list) {
        if (list == null) {
            return;
        }
        for (Race r : list) {
            if (raceMap.get(r.getLevel()) == null) {
                raceMap.put(r.getLevel(), new ArrayList<Race>());
            }
            raceMap.get(r.getLevel()).add(r);
        }
    }

    public double getAverageTimeSeconds(Level l) {
        return getAverage(raceMap.get(l), (Race r) -> {return r.getTimeSeconds();});
    }

    public double getAverageSplitSeconds(int mile, Level l) {
        if (mile == 1) {
            return getAverage(raceMap.get(l), (Race r) -> {return r.getMileOneSplitSeconds();});
        }

        if (mile == 2) {
            return getAverage(raceMap.get(l), (Race r) -> {return r.getMileTwoSplitSeconds();});
        }

        if (mile == 3) {
            return getAverage(raceMap.get(l), (Race r) -> {return r.getMileThreeSplitSeconds();});
        }

        return -1;        
    }

    public double getTimeSpreadSeconds(Level l) {
        return getSpread(raceMap.get(l), (Race r) -> {return r.getTimeSeconds();}).doubleValue();
    }

    public int getPlaceSpread(Level l ) {
        return getSpread(raceMap.get(l), (Race r) -> {return r.getPlace();}).intValue();
    }

    private double getAverage(List<Race> races, Function<Race, Double> getter) {
        if (races == null || races.size() == 0) {
            return -1;
        }

        double rtn = 0;
        for (Race r : races) {
            rtn += getter.apply(r);
        }

        return rtn / races.size();
    }

    private Number getSpread(List<Race> races, Function<Race, Number> getter) {
        if (races == null || races.size() == 0) {
            return -1;
        }

        Number max = getter.apply(races.get(0));
        for (Race r : races) {
            if (getter.apply(r).doubleValue() > max.doubleValue()) {
                max = getter.apply(r);
            }
        }

        Number min = getter.apply(races.get(0));
        for (Race r : races) {
            if (getter.apply(r).doubleValue() < min.doubleValue()) {
                min = getter.apply(r);
            }
        }

        return max.doubleValue() - min.doubleValue();
    }

    //Getters
    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    /**
     * Gets the total athlete counts as an array.
     * @return int array structured as follows: [varsityBoys, varsityGirls, JVBoys, JVGirls];
     */
    public int[] getTotalAthleteCounts() {
        return athleteCounts;
    }


    @Override
    public void loadFromSQL(SQLRow result) {
        if (canSqlWrite) {
            name =             SQLTypeConversion.getString(result.get(SQLTableInformation.Meet.MEET_NAME_STR));
            date =             SQLTypeConversion.getDate(result.get(SQLTableInformation.Meet.MEET_DATE_DATE));
            athleteCounts[0] = SQLTypeConversion.getInt(result.get(SQLTableInformation.Meet.TOTAL_VAR_BOYS_INT));
            athleteCounts[1] = SQLTypeConversion.getInt(result.get(SQLTableInformation.Meet.TOTAL_VAR_GIRLS_INT));
            athleteCounts[2] = SQLTypeConversion.getInt(result.get(SQLTableInformation.Meet.TOTAL_JV_BOYS_INT));
            athleteCounts[3] = SQLTypeConversion.getInt(result.get(SQLTableInformation.Meet.TOTAL_JV_GIRLS_INT));

            canSqlWrite = false;
        }
    }

    @Override
    public SQLRow writeTOSQL() {
        SQLRow rtn = new SQLRow("Meet", 0); //TODO id
        
        rtn.putPair(SQLTableInformation.Meet.MEET_NAME_STR      , name);
        rtn.putPair(SQLTableInformation.Meet.MEET_DATE_DATE     , date);
        rtn.putPair(SQLTableInformation.Meet.TOTAL_VAR_BOYS_INT , athleteCounts[0]);
        rtn.putPair(SQLTableInformation.Meet.TOTAL_VAR_GIRLS_INT, athleteCounts[1]);
        rtn.putPair(SQLTableInformation.Meet.TOTAL_JV_BOYS_INT  , athleteCounts[2]);
        rtn.putPair(SQLTableInformation.Meet.TOTAL_JV_GIRLS_INT , athleteCounts[3]);

        return rtn;
    }

    @Override
    public void writeToPDF(File file) throws IOException, DocumentException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'writeToPDF'");
    }
    
}
