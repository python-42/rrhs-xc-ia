package rrhs.xc.ia.data.database;

import java.time.LocalDate;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import rrhs.xc.ia.data.mem.Level;
import rrhs.xc.ia.data.mem.Season;

public class SQLRow {
    private final SortedMap<String, String> row = new TreeMap<String, String>();
    private final String tableName;
    private final int id;

    public SQLRow(String tableName, int id) {
        this.tableName = tableName;
        this.id = id;
    }

    /**
     * Put a new key value pair in to the internal map. Will overwrite an existing key with the same name.
     * @param key Key in the map, represents the name of the SQL column in the row this class represents.
     * @param value Corresponding value in the map, represents the value in the SQL row.
     */
    public void putPair(String key, String value) {
        row.put(key, SQLTypeConversion.convert(value));
    }

    public void putPair(String key, int value) {
        row.put(key, SQLTypeConversion.convert(value));
    }

    public void putPair(String key, double value) {
        row.put(key, SQLTypeConversion.convert(value));
    }

    public void putPair(String key, boolean value) {
        row.put(key, SQLTypeConversion.convert(value));
    }

    public void putPair(String key, LocalDate d) {
        row.put(key, SQLTypeConversion.convert(d));
    }

    public void putPair(String key, Season value) {
        row.put(key, SQLTypeConversion.convert(value));
    }

    public void putPair(String key, Level value) {
        row.put(key, SQLTypeConversion.convert(value));
    }

    public String getSQLInsertString() {
        StringBuilder rtn = new StringBuilder();

        rtn.append("INSERT INTO ");
        rtn.append(tableName);
        rtn.append(" (");

        for (String s : row.keySet()) {
            rtn.append(s);
            rtn.append(", ");
        }
        rtn.delete(rtn.length() - 2, rtn.length());
        rtn.append(") VALUES (");

        for (String s : row.values()) {
            rtn.append(s);
            rtn.append(", ");
        }
        rtn.delete(rtn.length() - 2, rtn.length());
        rtn.append(");");
        return rtn.toString();
    }

    public String getSQLUpdateString() {
        StringBuilder rtn = new StringBuilder();

        rtn.append("UPDATE ");
        rtn.append(tableName);
        rtn.append(" SET ");

        for (Entry<String, String> s : row.entrySet()) {
            rtn.append(s.getKey());
            rtn.append(" = ");
            rtn.append(s.getValue());
            rtn.append(", ");
        }
        rtn.delete(rtn.length() -2, rtn.length());
        rtn.append(" WHERE id = ");
        rtn.append(id);
        rtn.append(';');
        
        return rtn.toString();
    }

    public String getSQLDeleteString() {
        StringBuilder rtn = new StringBuilder();

        rtn.append("DELETE FROM ");
        rtn.append(tableName);
        rtn.append(" WHERE id = ");
        rtn.append(id);
        rtn.append(";");

        return rtn.toString();
    }

}
