package rrhs.xc.ia.data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rrhs.xc.ia.data.database.SQLTypeConversion.SQLTableInformation;
import rrhs.xc.ia.data.i.SQLDataObject;
import rrhs.xc.ia.data.mem.Athlete;
import rrhs.xc.ia.data.mem.Meet;
import rrhs.xc.ia.data.mem.Race;

public class DatabaseManager {

    private static DatabaseManager INSTANCE = null;

    public static DatabaseManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseManager();
        }

        return INSTANCE;
    }



    private Connection conn;
    private String DATABASE_PATH = "jdbc:sqlite:./ia/test.db"; //TODO replace

    private final String MEET_QUERY = 
    """
    SELECT * FROM Meet WHERE name = ?;
    """;

    private final String ATHLETE_QUERY = 
    """
    SELECT * FROM Athlete WHERE name = ?;        
    """;

    private final String ALL_ATHLETES_QUERY = 
    """
    SELECT * FROM Athlete;        
    """;

    private final String ALL_MEETS_QUERY =
    """
    SELECT * FROM Meet;        
    """;

    private final String RACES_FOR_ATHLETE_QUERY =        
    """
    SELECT Race.id, meetid, athleteid, timeSeconds, splitOneSeconds, splitTwoSeconds, place, season, level, date, name 
    FROM Race 
    JOIN Meet ON Race.meetid = Meet.id 
    AND Race.athleteid = ?;
    """;

    private final String RACES_FOR_MEET_QUERY = 
    """
    SELECT Race.id, name, timeSeconds, splitOneSeconds, splitTwoSeconds, place, season, level
    FROM Race
    JOIN Athlete ON Race.athleteid = Athlete.id
    AND Race.meetid = ?;
    """;

    private DatabaseManager() {
        
    }

    public void connect() throws SQLException {
        conn = DriverManager.getConnection(DATABASE_PATH);
    }

    public List<Athlete> getAllAthletes() throws SQLException {
        if (conn == null || conn.isClosed()) {
            return null;
        }

        List<Athlete> list = new ArrayList<Athlete>();

        ResultSet result = conn.createStatement().executeQuery(ALL_ATHLETES_QUERY);

        while(result.next()) {
            list.add(
                new Athlete(
                    getRacesForAthlete(result.getInt("id")),
                    result.getString(SQLTableInformation.Athlete.NAME_STR),
                    result.getInt(SQLTableInformation.Athlete.GRADUATION_YEAR_INT),
                    result.getInt("id"),
                    false
                )
            );
        }

        return list;
    }

    public List<Meet> getAllMeets() throws SQLException {
        if (conn == null || conn.isClosed()) {
            return null;
        }

        List<Meet> list = new ArrayList<Meet>();

        ResultSet result = conn.createStatement().executeQuery(ALL_MEETS_QUERY);

        while(result.next()) {            
            list.add(
                new Meet(
                    getRacesForMeet(result.getInt("id")),
                    result.getString(SQLTableInformation.Meet.MEET_NAME_STR),
                    SQLTypeConversion.getDate(result.getString(SQLTableInformation.Meet.MEET_DATE_DATE)),
                    result.getInt("id"),
                    false
                )
            );
        }

        return list;
    }

    public Athlete getAthlete(String name) throws SQLException {
        if (conn == null || conn.isClosed()) {
            return null;
        }

        PreparedStatement stmt = conn.prepareStatement(ATHLETE_QUERY);
        stmt.setString(1, name);
        ResultSet result = stmt.executeQuery();

        if (!result.next()) {
            return null;
        }

        return new Athlete(
            getRacesForAthlete(result.getInt("id")),
            result.getString(SQLTableInformation.Athlete.NAME_STR),
            result.getInt(SQLTableInformation.Athlete.GRADUATION_YEAR_INT),
            result.getInt("id"),
            false
        );
    }

    public Meet getMeet(String name) throws SQLException {
        if (conn == null || conn.isClosed()) {
            return null;
        }

        PreparedStatement stmt = conn.prepareStatement(MEET_QUERY);
        stmt.setString(1, name);
        ResultSet result = stmt.executeQuery();

        if (!result.next()) {
            return null;
        }

        return new Meet(
            getRacesForMeet(result.getInt("id")),
            result.getString(SQLTableInformation.Meet.MEET_NAME_STR),
            SQLTypeConversion.getDate(result.getString(SQLTableInformation.Meet.MEET_DATE_DATE)),
            result.getInt("id"),
            false
        );
    }

    /**
     * Place each item in the list in to the database. If the item is not in the database
     * (as indicated by <code>SQLSerializable.isNew()</code>), insert it in to the
     * database. If the item is not new but is modified, update it in the database.
     * Otherwise, do nothing. 
     * 
     * @param list
     * @throws SQLException
     */
    public void resolveAll(List<? extends SQLDataObject> list) throws SQLException {
        for (SQLDataObject data : list) {
            if (data.needsDeletion() && data.isNew()) {
                // do nothing. Assume that this object was created in error and should not be entered in to the database
            } else if (data.needsDeletion()) {
                delete(data);
            } else if(data.isNew()) {
                insert(data);
            } else if (data.isModified()) {
                update(data);
            } 
        }
    }

    /**
     * Insert the given object in to the database
     * 
     * @param data
     * @throws SQLException
     */
    private void insert(SQLDataObject data) throws SQLException {
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
    private void update(SQLDataObject data) throws SQLException {
        conn.createStatement().execute(data.writeToSQL().getSQLUpdateString());
    }

    /**
     * Delete the given object from the database.
     * 
     * @param data
     * @throws SQLException
     */
    private void delete(SQLDataObject data) throws SQLException {
        conn.createStatement().execute(data.writeToSQL().getSQLDeleteString());
    }

    /**
     * Get the Races for athlete 
     * @param id
     * @return
     * @throws SQLException
     */
    private List<Race> getRacesForAthlete(int id) throws SQLException {
        List<Race> rtn = new ArrayList<Race>();

        PreparedStatement stmt = conn.prepareStatement(RACES_FOR_ATHLETE_QUERY);
        stmt.setInt(1, id);

        ResultSet result = stmt.executeQuery();

        while (result.next()) {
            rtn.add(new Race(
                null,
                result.getString(SQLTableInformation.Meet.MEET_NAME_STR),
                SQLTypeConversion.getDate(result.getString(SQLTableInformation.Meet.MEET_DATE_DATE)),
                SQLTypeConversion.getLevel(result.getString(SQLTableInformation.Race.LEVEL_ENUM)),
                SQLTypeConversion.getSeason(result.getString(SQLTableInformation.Race.SEASON_ENUM)),
                result.getDouble(SQLTableInformation.Race.TOTAL_TIME_DBL),
                result.getDouble(SQLTableInformation.Race.MILE_SPLIT_ONE_DBL),
                result.getDouble(SQLTableInformation.Race.MILE_SPLIT_TWO_DBL),
                result.getInt(SQLTableInformation.Race.PLACE_INT),
                result.getInt("id"),
                false
            ));
        }

        return rtn;
    }

    private List<Race> getRacesForMeet(int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(RACES_FOR_MEET_QUERY);
        stmt.setInt(1, id);
        ResultSet result = stmt.executeQuery();

        List<Race> rtn = new ArrayList<Race>();
        
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
                result.getInt(SQLTableInformation.Race.PLACE_INT),
                result.getInt("id"),
                false
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
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
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
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                gradYear INTEGER NOT NULL
            );  
        """;

        String createRace = 
        """
            CREATE TABLE IF NOT EXISTS Race (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                meetid INTEGER NOT NULL,
                athleteid INTEGER NOT NULL,
                timeSeconds REAL NOT NULL,
                splitOneSeconds REAL NOT NULL,
                splitTwoSeconds REAL NOT NULL,
                place INTEGER NOT NULL,
                season INTEGER NOT NULL,
                level INTEGER NOT NULL,

                FOREIGN KEY (meetid) REFERENCES Meet(id),
                FOREIGN KEY (athleteid) REFERENCES Athlete(id)
            );
        """;

        conn.createStatement().execute(createMeet);
        conn.createStatement().execute(createAthlete);
        conn.createStatement().execute(createRace);
    }

}
