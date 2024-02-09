package rrhs.xc.ia.data.i;

import rrhs.xc.ia.data.database.SQLRow;

public abstract class SQLDataObject {
    
    private final boolean isNew;
    private boolean modified = false; 
    private boolean needsDeletion = false;

    protected SQLDataObject(boolean isNew) {
        this.isNew = isNew;
    }

    public final boolean isModified() {
        return modified;
    }

    public final boolean isNew() {
        return isNew;
    }

    public final boolean needsDeletion() {
        return needsDeletion;
    }

    public final void requestDeletion() {
        needsDeletion = true;
    }

    public final void setModified() {
        modified = true;
    }

    public abstract SQLRow writeToSQL();

}
