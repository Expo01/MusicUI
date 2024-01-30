package sample.model;


import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Datasource {

    public static final String DB_NAME = "music.db";

    public static final String CONNECTION_STRING = "jdbc:sqlite:/Users/chrisdailey/Documents/CODING/Projects/Music/" + DB_NAME;

    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";
    public static final int INDEX_ALBUM_ID = 1;
    public static final int INDEX_ALBUM_NAME = 2;
    public static final int INDEX_ALBUM_ARTIST = 3;

    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_ID = "_id";
    public static final String COLUMN_SONG_TRACK = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";
    public static final int INDEX_SONG_ID = 1;
    public static final int INDEX_SONG_TRACK = 2;
    public static final int INDEX_SONG_TITLE = 3;
    public static final int INDEX_SONG_ALBUM = 4;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2; // ascending
    public static final int ORDER_BY_DESC = 3; // descending

    public static final String QUERY_ALBUMS_BY_ARTIST_START =
            "SELECT " + TABLE_ALBUMS + '.' + COLUMN_ALBUM_NAME + " FROM "
            + TABLE_ALBUMS + " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST +
            " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID + " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + " =\"";

    public static final String QUERY_ALBUMS_BY_ARTIST_SORT =
            " ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";
    // Advantage of not hard coding any names because it would be horrendous to change

    public static final String QUERY_ARTIST_FOR_SONG_START =
            "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " +
                    TABLE_SONGS + "." + COLUMN_SONG_TRACK + " FROM " + TABLE_SONGS +
                    " INNER JOIN " + TABLE_ALBUMS + " ON " +
                    TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
                    " INNER JOIN " + TABLE_ARTISTS + " ON " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
                    " WHERE " + TABLE_SONGS + "." + COLUMN_SONG_TITLE + " = \"";

    public static final String QUERY_ARTIST_FOR_SONG_SORT =
            " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME
            + " COLLATE NOCASE ";

    public static final String TABLE_ARTIST_SONG_VIEW = "artist_list";

    public static final String CREATE_ARTIST_FOR_SONG_VIEW = "CREATE VIEW IF NOT EXISTS " +
            TABLE_ARTIST_SONG_VIEW + " AS SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " AS " + COLUMN_SONG_ALBUM + ", " +
            TABLE_SONGS + "." + COLUMN_SONG_TRACK + ", " + TABLE_SONGS + "." + COLUMN_SONG_TITLE +
            " FROM " + TABLE_SONGS +
            " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS +
            "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
            " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST +
            " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID +
            " ORDER BY " +
            TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " +
            TABLE_SONGS + "." + COLUMN_SONG_TRACK;

    public static final String QUERY_VIEW_SONG_INFO =  "SELECT " + COLUMN_ARTIST_NAME + ", " +
            COLUMN_SONG_ALBUM + ", " + COLUMN_SONG_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW +
            " WHERE " + COLUMN_SONG_TITLE + " = \"";

    public static final String QUERY_VIEW_SONG_INFO_PREP = "SELECT " + COLUMN_ARTIST_NAME + ", " +
            COLUMN_SONG_ALBUM + ", " + COLUMN_SONG_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW +
            " WHERE " + COLUMN_SONG_TITLE + " = ?";

    public static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS +
            '(' + COLUMN_ARTIST_NAME + ") VALUES(?)";
    public static final String INSERT_ALBUMS = "INSERT INTO " + TABLE_ALBUMS +
            '(' + COLUMN_ALBUM_NAME + ", " + COLUMN_ALBUM_ARTIST + ") VALUES(?, ?)";

    public static final String INSERT_SONGS = "INSERT INTO " + TABLE_SONGS +
            '(' + COLUMN_SONG_TRACK + ", " + COLUMN_SONG_TITLE + ", " + COLUMN_SONG_ALBUM +
            ") VALUES(?, ?, ?)";

    public static final String QUERY_ARTIST = "SELECT " + COLUMN_ARTIST_ID + " FROM " +
            TABLE_ARTISTS + " WHERE " + COLUMN_ARTIST_NAME + " = ?";

    public static final String QUERY_ALBUM = "SELECT " + COLUMN_ALBUM_ID + " FROM " +
            TABLE_ALBUMS + " WHERE " + COLUMN_ALBUM_NAME + " = ?";

    public static final String QUERY_ALBUMS_BY_ARTIST_ID = "SELECT * FROM " + TABLE_ALBUMS +
            " WHERE " + COLUMN_ALBUM_ARTIST + " = ? ORDER BY " + COLUMN_ALBUM_NAME + " COLLATE NOCASE";

    public static final String UPDATE_ARTIST_NAME = "UPDATE " + TABLE_ARTISTS + " SET " +
            COLUMN_ARTIST_NAME + " = ? WHERE " + COLUMN_ARTIST_ID + " = ?";


    private Connection conn;

    private PreparedStatement querySongInfoView;

    private PreparedStatement insertIntoArtists;
    private PreparedStatement insertIntoAlbums;
    private PreparedStatement insertIntoSongs;
    private PreparedStatement queryArtist;
    private PreparedStatement queryAlbum;
    private PreparedStatement queryAlbumsByArtistId;
    private PreparedStatement updateArtistName;

    private static Datasource instance = new Datasource(); // lazy instantiation. instance not created until the class is
    // first loaded.

    public static Datasource getInstance(){
        return instance;
//        if (instance == null) {
//            instance = new Datasource(); if no instance exists, creates it. this is called lazy instantiation since
           // the instance is not created until its used. downside is that it is not thread-safe such as if a thread
            // interrupted after null check and then a second run executes successfully and first thread then finishes
            // resulting in 2 instances
//        }
        // instead, just instantiate the field and return it here

        // the intention here was to create a singleton where the single instance and its methods will be called with
        // syntax such as   Datasource.getInstance().methodName
    }

    private Datasource(){
        //private so only this class can create an instance of this.

    }

    // --------------------- METHODS ----------------------------

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            querySongInfoView = conn.prepareStatement(QUERY_VIEW_SONG_INFO_PREP);
            insertIntoArtists = conn.prepareStatement(INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS);
            insertIntoAlbums = conn.prepareStatement(INSERT_ALBUMS, Statement.RETURN_GENERATED_KEYS);
            insertIntoSongs = conn.prepareStatement(INSERT_SONGS);
            queryArtist = conn.prepareStatement(QUERY_ARTIST);
            queryAlbum = conn.prepareStatement(QUERY_ALBUM);
            queryAlbumsByArtistId = conn.prepareStatement(QUERY_ALBUMS_BY_ARTIST_ID);
            updateArtistName = conn.prepareStatement(UPDATE_ARTIST_NAME);

            return true;
        } catch(SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    } // open the connection

    public void close() {
        try {
            if(querySongInfoView != null){
                querySongInfoView.close();
            }

            if(insertIntoArtists != null) {
                insertIntoArtists.close();
            }

            if(insertIntoAlbums != null) {
                insertIntoAlbums.close();
            }

            if(insertIntoSongs !=  null) {
                insertIntoSongs.close();
            }

            if(queryArtist != null) {
                queryArtist.close();
            }

            if(queryAlbum != null) {
                queryAlbum.close();
            }

            if(queryAlbumsByArtistId != null){
                queryAlbumsByArtistId.close();
            }

            if(updateArtistName != null){
                updateArtistName.close();
            }

            if(conn != null) {
                conn.close();
            }
        } catch(SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    } // close the connection

    public List<Artist> queryArtists(int sortOrder) {
        StringBuilder sb = new StringBuilder("SELECT * FROM " + TABLE_ARTISTS);
        if(sortOrder != ORDER_BY_NONE){
            sb.append(" ORDER BY " + COLUMN_ARTIST_NAME + " COLLATE NOCASE ");
            if(sortOrder == ORDER_BY_DESC){
                sb.append("DESC");
            } else {
                sb.append("ASC");
                // SQL statement = SELECT * FROM artists ORDER BY name COLLATE NOCASE ASC

            }
        }

        System.out.println("SQL statement = " + sb.toString());

        try(Statement statement = conn.createStatement();
//            ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_ARTISTS)) {
            ResultSet results = statement.executeQuery(sb.toString())) {
            // this is a try with resources block? Objects are declared in the try parameters instead of
            // having to create objects assigned to null, then reassign them in the try block
            // removes the need for a finally clause which would otherwise be used to try and catch for both
            // closing results and statement
            // statement and result set automatically closed using the 'try with resources' syntax

            // DEF: a try with resources statement is a try statement that declares 1 or more resources that
            // will close after program is finished with it. Resources implement the java.lang.Autoclosable
            // interface. Resources that are in the try-with-resource are closed whether the try statement
            // completes normally or via exception error

            List<Artist> artists = new ArrayList<>();
            while(results.next()) {
                try{
                    Thread.sleep(10); // 10 milisecond sleep just to see that the progress bar actually works in this small DB
                } catch(InterruptedException e){
                    System.out.println("Interrupted: " + e.getMessage());
                }
                Artist artist = new Artist(); //calls on empty constructor
//                artist.setId(results.getInt(COLUMN_ARTIST_ID)); these are calls via column title
//                artist.setName(results.getString(COLUMN_ARTIST_NAME));
                artist.setId(results.getInt(INDEX_ARTIST_ID)); // these are calls via column index instead of the column title
                artist.setName(results.getString(INDEX_ARTIST_NAME)); // these also take an int instead of just string. overloaded?

                artists.add(artist);
            }

            return artists;

        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }

        // why no finally block for closing results and statement?

    } // returns all column content for all artists in artists table


    public List<Album> queryAlbumsForArtistId(int id) {
        try {
            queryAlbumsByArtistId.setInt(1, id);
            ResultSet results = queryAlbumsByArtistId.executeQuery();

            List<Album> albums = new ArrayList<>();
            while(results.next()) {
                Album album = new Album();
                album.setId(results.getInt(1));
                album.setName(results.getString(2));
                album.setArtistId(id);
                albums.add(album);
            }

            return albums;
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }


    public List<String> queryAlbumsForArtist(String artistName, int sortOrder){

        //SQL statement = SELECT albums.name FROM albums INNER JOIN artists ON albums.artist = artists._id WHERE
        // artists.name = "Iron Maiden" ORDER BY albums.name COLLATE NOCASE ASC
        // this is full SQL statement being built
            StringBuilder sb = new StringBuilder(QUERY_ALBUMS_BY_ARTIST_START);
            sb.append(artistName);
            sb.append("\"");

        if(sortOrder != ORDER_BY_NONE){
            sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
            if(sortOrder == ORDER_BY_DESC){
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        System.out.println("SQL statement = " + sb.toString());

        try(Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(sb.toString())){ // ultimately here the stringbuilder just
            // becomes a string.

            List<String> albums = new ArrayList<>();
            while(results.next()){
                albums.add(results.getString(1)); // index corresponds to column in resultSet not index of
                // column in the table. indices start at 1 not zero here.
            }

            return albums;

        } catch(SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }

    } // uses artist ID from albums table
    // and links to ID in artist table. orders all albums alphabetically from the albums table that where artist name
    // from artist table is "Iron Maiden" as an example


    public void querySongsMetadata() {
        String sql = "SELECT * FROM " + TABLE_SONGS;

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sql)) {

            ResultSetMetaData meta = results.getMetaData(); //ResultSetMetaData is an object that can be used to get
            // information about the types and properties of the columns in a ResultSet object
            int numColumns = meta.getColumnCount();
            for(int i=1; i<= numColumns; i++) { // indices in table columns start at 1
                System.out.format("Column %d in the songs table is names %s\n", i, meta.getColumnName(i));
                // \n here is just a normal escape for a new line
                // 'souf' format allows passing of %d for 'i' and %s for 'meta.getColumnName(i)' for int and string
                // where 'i' is incremented so prints column index and name for all existing columns in songs table
            }
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    } // prints column titles from the songs table


    public int getCount(String table) {
        String sql = "SELECT COUNT(*) AS count FROM " + table; // 'AS' used to assign name to resulting column...
        try(Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(sql)) {

            int count = results.getInt("count");
//            int count = results.getInt(1); only one column from result set, returns and assigns that int val

            System.out.format("Count = %d\n", count);
            return count;
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return -1;
        }
    } // will return the number of rows in specified table

    public boolean createViewForSongArtists() {

        try(Statement statement = conn.createStatement()) {

            statement.execute(CREATE_ARTIST_FOR_SONG_VIEW);
            return true;

        } catch(SQLException e) {
            System.out.println("Create View failed: " + e.getMessage());
            return false;
        }
    } // creates a view which is added to the database and will be viewable
    // in DB browser with a different icon indicating it is a view not a table. This particular view will have columns:
    // artist name, album name, album track #, song name
    // view will be queriable with SQL statement just like a table such as....
    // SELECT name, album, track FROM artist_list WHERE title = "Go Your Own Way"



    // transaction: here is the concept. Suppose an action requires multiple SQL statements to complete the function such
    // as having a bank transfer where $ is subtracted from one account and added to another through 2 separate SQL functions
    // if the subtraction command succeeds but the addition to second account fails, bank 1 loses money and bank 2 doesn't
    // get it and the $ is now effectively missing. The goal here is that we want all SQL statements involved in a single
    // larger function to execute in an all or nothing manner. We want to withhold all changes unless all SQL commands run properly
    // A transaction is effectively a sequence of SQL statements treated as a single unit. if anything in the sequence fails,
    // changes can be rolled back or just not saved (committed).
    // Only have to use transactions when editing the database, not for querying.
    // SQLite uses transactions by default and auto-commits by default such with the update, delete and insert statements
    // uses, SQLite was automatically creating a transaction, running the statement, then committing
    // JDBC Connection class also auto-commits by default. When we turned off auto-commit, SQLite stopped autocommiting but
    // but transactions still created

    // Should adhere to Database ACID concepts

    // MANUAL CREATION OF TRANSACTION
    // - commands: BEGIN TRANSACTION, END TRANSACTION (interchangeable with COMMIT), ROLLBACK
    // we don't code transcation SQL statement and use statement objects to execute them, instead we use methods of the
    // Connection class to execute transction realted commands
    // turn off auto-commits via Connection.setAutoCommit(false) --> perofrm SQL operations that form the transaction -->
    // call Connection.commit()


    // to simplify, we won't be updating the view and we won't be checking if content already exists in the tables/view

    private int insertArtist(String name) throws SQLException {

        queryArtist.setString(1, name);
        ResultSet results = queryArtist.executeQuery(); // query to see if artist exists
        if(results.next()) {
            return results.getInt(1); // if artists already exists, return it
        } else {
            // Insert the artist
            insertIntoArtists.setString(1, name); // pass name of artist to be inserted
            int affectedRows = insertIntoArtists.executeUpdate(); // should udate one row and return # of rows updated

            if(affectedRows != 1) { // if one row isn't updated, then its a problem
                throw new SQLException("Couldn't insert artist!");
            }

            ResultSet generatedKeys = insertIntoArtists.getGeneratedKeys();
            if(generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for artist");
            }
        }
    }

    private int insertAlbum(String name, int artistId) throws SQLException {

        queryAlbum.setString(1, name);
        ResultSet results = queryAlbum.executeQuery();
        if(results.next()) {
            return results.getInt(1);
        } else {
            // Insert the album
            insertIntoAlbums.setString(1, name);
            insertIntoAlbums.setInt(2, artistId);
            int affectedRows = insertIntoAlbums.executeUpdate(); // 2 fields updated, but still only 1 row

            if(affectedRows != 1) {
                throw new SQLException("Couldn't insert album!");
            }

            ResultSet generatedKeys = insertIntoAlbums.getGeneratedKeys();
            if(generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for album");
            }
        }
    }

    public boolean updateArtistName(int id, String newName) {
        try {
            updateArtistName.setString(1, newName);
            updateArtistName.setInt(2, id);
            int affectedRecords = updateArtistName.executeUpdate();

            return affectedRecords == 1;

        } catch(SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    public void insertSong(String title, String artist, String album, int track) {
// no query exists in this method to check if song already exists
        try {
            conn.setAutoCommit(false); // signals start of manual transaction

            int artistId = insertArtist(artist);
            int albumId = insertAlbum(album, artistId);
            insertIntoSongs.setInt(1, track);
            insertIntoSongs.setString(2, title);
            insertIntoSongs.setInt(3, albumId);
            int affectedRows = insertIntoSongs.executeUpdate();
            if(affectedRows == 1) {
                conn.commit(); // commit ends a transaction
            } else {
                throw new SQLException("The song insert failed");
            }

        } catch(Exception e) { // Exception not SQLexception since we can get stuff like array out of bounds exception
            // if trying to input a song at an OOB index, it will update the albums and artist table but not the song
            // table which goes against what we were trying to do with creating a transaction, but since its not a SQL
            // exception, the rollback doesn't occur since no error thrown and then jumps to finally block and commits
            // the artist and album table changes when autoCommit reset
            System.out.println("Insert song exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch(SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }

        }
    }
}


