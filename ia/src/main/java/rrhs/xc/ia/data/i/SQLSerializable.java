package rrhs.xc.ia.data.i;

import rrhs.xc.ia.data.database.SQLRow;

public interface SQLSerializable {
    
    public void loadFromSQL(SQLRow result);

    public SQLRow writeTOSQL();

}
