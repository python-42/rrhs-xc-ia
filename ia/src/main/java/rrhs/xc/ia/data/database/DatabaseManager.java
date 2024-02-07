package rrhs.xc.ia.data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rrhs.xc.ia.data.database.SQLTypeConversion.SQLTableInformation;
import rrhs.xc.ia.data.i.SQLSerializable;
import rrhs.xc.ia.data.mem.Athlete;
import rrhs.xc.ia.data.mem.Meet;
import rrhs.xc.ia.data.mem.Race;

public class DatabaseManager {
    private Connection conn;
    private final String DATABASE_PATH;

    public DatabaseManager(String path) {
        DATABASE_PATH = "jdbc:sqlite:" + path;
    }

    public void connect() throws SQLException {
        conn = DriverManager.getConnection(DATABASE_PATH);
    }

    public Athlete getAthlete(String name) throws SQLException {
        if (conn == null || conn.isClosed()) {
            return null;
        }

        String query = "SELECT * FROM Athlete WHERE name = '" + name + "';";
        ResultSet result = conn.createStatement().executeQuery(query);

        if (!result.next()) {
            return null;
        }

        return new Athlete(
            getRacesForAthlete(result.getInt("id")),
            result.getString(SQLTableInformation.Athlete.NAME_STR),
            result.getInt(SQLTableInformation.Athlete.GRADUATION_YEAR_INT)
        );
    }

    public Meet getMeet(String name) throws SQLException {
        if (conn == null || conn.isClosed()) {
            return null;
        }

        String query = "SELECT * FROM Meet WHERE name = '" + name + "';";
        ResultSet result = conn.createStatement().executeQuery(query);

        if (!result.next()) {
            return null;
        }

        return new Meet(
            getRacesForMeet(result.getInt("id")),
            result.getString(SQLTableInformation.Meet.MEET_NAME_STR),
            result.getDate(SQLTableInformation.Meet.MEET_DATE_DATE).toLocalDate(),
            result.getInt(SQLTableInformation.Meet.TOTAL_VAR_BOYS_INT),
            result.getInt(SQLTableInformation.Meet.TOTAL_VAR_GIRLS_INT),
            result.getInt(SQLTableInformation.Meet.TOTAL_JV_BOYS_INT),
            result.getInt(SQLTableInformation.Meet.TOTAL_JV_GIRLS_INT)
        );
    }

    /**
     * Insert the given object in to the database
     * 
     * @param data
     * @throws SQLException
     */
    public void insert(SQLSerializable data) throws SQLException {
        conn.createStatement().execute(data.writeToSQL().getSQLInsertString());
    }

    /**
     * Update the given object in the database. After this method is called, the
     * database representation of this object will exactly match the in memory
     * representation of this object.
     * 
     * @param data
     * @throws SQLException
     */
    public void update(SQLSerializable data) throws SQLException {
        conn.createStatement().execute(data.writeToSQL().getSQLUpdateString());
    }

    /**
     * Delete the given object from the database.
     * 
     * @param data
     * @throws SQLException
     */
    public void delete(SQLSerializable data) throws SQLException {
        conn.createStatement().executeQuery(data.writeToSQL().getSQLDeleteString());
    }

    /**
     * Get the Races for athlete 
     * @param id
     * @return
     * @throws SQLException
     */
    private List<Race> getRacesForAthlete(int id) throws SQLException {
        String query = "SELECT * FROM Race WHERE athleteID = '" + id + "' JOIN Meet ON Race.meetid = Meet.name;";
        List<Race> rtn = new ArrayList<Race>();

        ResultSet result = conn.createStatement().executeQuery(query);

        while (result.next()) {
            rtn.add(new Race(
                null,
                result.getString(SQLTableInformation.Meet.MEET_NAME_STR),
                result.getDate(SQLTableInformation.Meet.MEET_DATE_DATE).toLocalDate(),
                SQLTypeConversion.getLevel(result.getString(SQLTableInformation.Race.LEVEL_ENUM)),
                SQLTypeConversion.getSeason(result.getString(SQLTableInformation.Race.SEASON_ENUM)),
                result.getDouble(SQLTableInformation.Race.TOTAL_TIME_DBL),
                result.getDouble(SQLTableInformation.Race.MILE_SPLIT_ONE_DBL),
                result.getDouble(SQLTableInformation.Race.MILE_SPLIT_TWO_DBL),
                result.getInt(SQLTableInformation.Race.PLACE_INT)
            ));
        }

        return rtn;
    }

    private List<Race> getRacesForMeet(int id) throws SQLException {
        String query = "SELECT * FROM Race WHERE meetID = '" + id + "';"; //TODO SQL join
        List<Race> rtn = new ArrayList<Race>();

        ResultSet result = conn.createStatement().executeQuery(query);

        while (result.next()) {
            rtn.add(new Race(
                result.getString(SQLTableInformation.Athlete.NAME_STR),
                null,
                null,
                SQLTypeConversion.getLevel(result.getString(SQLTableInformation.Race.LEVEL_ENUM)),
                SQLTypeConversion.getSeason(result.getString(SQLTableInformation.Race.SEASON_ENUM)),
                result.getDouble(SQLTableInformation.Race.TOTAL_TIME_DBL),
                result.getDouble(SQLTableInformation.Race.MILE_SPLIT_ONE_DBL),
                result.getDouble(SQLTableInformation.Race.MILE_SPLIT_TWO_DBL),
                result.getInt(SQLTableInformation.Race.PLACE_INT)
            ));
        }

        return rtn;
    }

    public void createDatabase() throws SQLException {
        if (conn == null || conn.isClosed()) {
            return;
        }
        
        String createMeet = 
        """
            CREATE TABLE IF NOT EXISTS Meet (
                name TEXT PRIMARY KEY,
                date DATE NOT NULL,
                totalVarBoys INTEGER NOT NULL,
                totalVarGirls INTEGER NOT NULL,
                totalJVBoys INTEGER NOT NULL,
                totalJVGirls INTEGER NOT NULL
            );
        """;

        String createAthlete = 
        """
            CREATE TABLE IF NOT EXISTS Athlete (
                name TEXT PRIMARY KEY,
                gradYear INTEGER NOT NULL
            );  
        """;

        String createRace = 
        """
            CREATE TABLE IF NOT EXISTS Race (
                id INTEGER PRIMARY KEY,
                meetid TEXT NOT NULL,
                athleteid TEXT NOT NULL,
                timeSeconds REAL NOT NULL,
                splitOneSeconds REAL NOT NULL,
                splitTwoSeconds REAL NOT NULL,
                place INTEGER NOT NULL,
                season INTEGER NOT NULL,
                level INTEGER NOT NULL,

                FOREIGN KEY (meetid) REFERENCES Meet(name),
                FOREIGN KEY (athleteid) REFERENCES Athlete(name)
            );
        """;

        conn.createStatement().execute(createMeet);
        conn.createStatement().execute(createAthlete);
        conn.createStatement().execute(createRace);
    }

}
