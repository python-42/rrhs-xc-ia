package rrhs.xc.ia.data.mem;

import java.io.File;

import rrhs.xc.ia.data.database.SQLRow;
import rrhs.xc.ia.data.database.SQLTypeConversion;
import rrhs.xc.ia.data.database.SQLTypeConversion.SQLTableInformation;
import rrhs.xc.ia.data.i.PDFExportable;
import rrhs.xc.ia.data.i.SQLSerializable;

public class Athlete implements PDFExportable, SQLSerializable{

    private String name;
    private boolean isBoysTeam;
    private int gradYear;
    private Race[] races;

    private boolean canSqlWrite = true;

    public Athlete(Race[] races) {
        this.races = races;
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
