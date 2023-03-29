package uqac.dim.projet_alarme_8inf257;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DBAlarmHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    private final Context myContext;
    private SQLiteDatabase myDataBase;

    private static final String DB_NAME = "alarmDB.db";
    private static final String DB_PATH = "/data/data/uqac.dim.projet_alarme_8inf257/databases/";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME_SAVED_ALARMS = "savedAlarms";
    private static final String ID_SAVED_ALARMS = "id";
    private static final String HOUR_SAVED_ALARMS = "hourSaved";
    private static final String ID_MINIGAME_SAVED_ALARMS = "idMiniGame";
    private static final String ID_RINGTONE_SAVED_ALARMS = "idRingtone";
    private static final String ENABLE_SAVED_ALARMS = "enable";
    private static final String[] WEEK_SAVED_ALARMS = new String[]{
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
            "Sunday"
    };

    private static final String TABLE_NAME_MINIGAMES = "savedAlarms";
    private static final String ID_MINIGAMES = "id";
    private static final String NAME_MINIGAMES = "name";

    private static final String TABLE_NAME_RINGTONE = "savedAlarms";
    private static final String ID_RINGTONE = "id";
    private static final String NAME_RINGTONE = "name";


    /**
     *  creating a constructor for our database handler.
     */
    public DBAlarmHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
    }

    public SQLiteDatabase getMyDataBase() {
        return myDataBase;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.disableWriteAheadLogging();
        }
    }

    /**
     * @return Does my DataBase exist ?
     */
    private boolean checkDataBase(){

        Log.v("DIM", "checkDataBase");

        boolean checkDB = false;

        try{
            String myPath = DB_PATH + DB_NAME;
            Log.v("DIM", "myPath : " + myPath);

            File dbfile = new File(myPath);
            Log.v("DIM", "dbfile : " + dbfile);
            checkDB = dbfile.exists();
            Log.v("DIM", "checkDB : " + checkDB);
        }
        catch(SQLiteException e){
            Log.v("DIM", "PAS SUPPOSE PASSER ICI JAMAIS");
        }
        return checkDB;
    }

    public void createDatabase() throws IOException {

        Log.v("DIM", "*********createDatabase***********");

        boolean dbExist = checkDataBase();

        if(dbExist) {
            Log.v("DIM", "LA BD EXISTE");
            // By calling this method here onUpgrade will be called on a
            // writeable database, but only if the version number has been
            // bumped
            //onUpgrade(myDataBase, DATABASE_VERSION_old, DATABASE_VERSION);
        }
        else{
            Log.v("DIM", "LA BD N'EXISTE PAS");

            try {
                this.getReadableDatabase();
                Log.v("DIM", "close");
                this.close();
                Log.v("DIM", "copydatabase");
                copyDataBase();
                Log.v("DIM", "copie effectue avec succes");
            }
            catch (IOException e){
                throw new Error("Error copying database");
            }
        }
    }

    private void copyDataBase() throws IOException {

        String outFileName = DB_PATH + DB_NAME;

        Log.v("DIM", "outFileName : " + outFileName);
        Log.v("DIM", "inFileName : " + DB_NAME);

        OutputStream myOutput = new FileOutputStream(outFileName);
        Log.v("DIM", "inFileName : YO");
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        Log.v("DIM", "myInput : " + myInput);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            String test = "";
            for (byte b:buffer)
                test += (char)b;
            Log.v("DIM", test);

            myOutput.write(buffer, 0, length);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();
    }

    /**
     *  below method is for creating a database by running a sqlite query
     */
    @Override
    public void onCreate(SQLiteDatabase db) {}

    /**
     * this method is use to add new course to our sqlite database.
     */
    public void addNewAlarm(String hour, int minigameID, int ringtoneID, int enable, int[] week) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(HOUR_SAVED_ALARMS, hour);
        values.put(ID_MINIGAME_SAVED_ALARMS, minigameID);
        values.put(ID_RINGTONE_SAVED_ALARMS, ringtoneID);
        values.put(ENABLE_SAVED_ALARMS, enable);
        values.put(WEEK_SAVED_ALARMS[0], week[0]);
        values.put(WEEK_SAVED_ALARMS[1], week[1]);
        values.put(WEEK_SAVED_ALARMS[2], week[2]);
        values.put(WEEK_SAVED_ALARMS[3], week[3]);
        values.put(WEEK_SAVED_ALARMS[4], week[4]);
        values.put(WEEK_SAVED_ALARMS[5], week[5]);
        values.put(WEEK_SAVED_ALARMS[6], week[6]);

        Log.v("DIM", "\n------INSERT------\nHeure : " + hour + "\nRingtoneID : " + ringtoneID + "\nMiniGameID : " + minigameID);
        db.insert(TABLE_NAME_SAVED_ALARMS, null, values);

        db.close();
    }

    /**
     * Currently not in use
     */
    public void addNewMinigame(String name) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME_MINIGAMES, name);

        db.insert(TABLE_NAME_MINIGAMES, null, values);

        db.close();
    }

    /**
     * Currently not in use
     */
    public void addNewRingtone(String name) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME_RINGTONE, name);

        db.insert(TABLE_NAME_RINGTONE, null, values);

        db.close();
    }

    /**
     * this will send a query to select all the existing alarms
     * @return LinkedList of alarm Objects
     */
    public LinkedList<Alarm> selectAllAlarms(){
        LinkedList<Alarm> res = new LinkedList();
        // SQL query
        Cursor cursor = myDataBase.rawQuery("select * from " + TABLE_NAME_SAVED_ALARMS + " order by " + HOUR_SAVED_ALARMS,null);
        if (cursor.getCount() > 0) {    // while our cursor has something
            cursor.moveToFirst();
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String hourSaved = cursor.getString(1);
                int idMiniGame = Integer.parseInt(cursor.getString(2));
                int idRingtone = Integer.parseInt(cursor.getString(3));
                int enable = Integer.parseInt(cursor.getString(4));
                int[] week = new int[]{Integer.parseInt(cursor.getString(11)), // Sunday
                        Integer.parseInt(cursor.getString(5)),  // Monday
                        Integer.parseInt(cursor.getString(6)),
                        Integer.parseInt(cursor.getString(7)),
                        Integer.parseInt(cursor.getString(8)),
                        Integer.parseInt(cursor.getString(9)),
                        Integer.parseInt(cursor.getString(10))}; // Saturday
                // add this new element to our list
                res.add(new Alarm(id, hourSaved, idMiniGame, idRingtone,enable, week, this));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return res;
    }

    /**
     * Disables an alarm using its ID
     */
    public void disableByID(int id){
        ContentValues cv = new ContentValues();
        cv.put(ENABLE_SAVED_ALARMS,0);
        myDataBase.update(TABLE_NAME_SAVED_ALARMS, cv, ID_SAVED_ALARMS+" = ?", new String[]{String.valueOf(id)});
        Log.v("DIM", getDataDeLaBD());
        Log.v("DIM", "disable alarm with id : "+id);
    }
    /**
     * Ables an alarm using its ID
     */
    public void enableByID(int id){
        ContentValues cv = new ContentValues();
        cv.put(ENABLE_SAVED_ALARMS,1);
        myDataBase.update(TABLE_NAME_SAVED_ALARMS, cv, ID_SAVED_ALARMS+" = ?", new String[]{String.valueOf(id)});
        //myDataBase.execSQL("update " + TABLE_NAME_SAVED_ALARMS + " set " + ENABLE_SAVED_ALARMS + " = 1 where " + ID_SAVED_ALARMS + " = " + id, null);
        Log.v("DIM", getDataDeLaBD());
        Log.v("DIM", "enable alarm with id : "+id);
    }

    /**
     * Deletes an alarm using its ID
     */
    public void deleteByID(int id){
        myDataBase.delete(TABLE_NAME_SAVED_ALARMS, ID_SAVED_ALARMS+" = ?", new String[]{String.valueOf(id)});
    }

    /**
     *  If a newer version exists
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion){
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openDatabase() throws SQLException {

        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     *  Shows a part of the DataBase
     */
    public String getDataDeLaBD(){

        StringBuilder sb = new StringBuilder();

        Log.v("DIM", "isOpen : " + myDataBase.isOpen());
        Log.v("DIM", "isReadOnly : " + myDataBase.isReadOnly());
        Log.v("DIM", "NAME : " + myDataBase.getPath());

        Cursor cursor = myDataBase.rawQuery("select * from savedAlarms",null);  // Cursor showing the result of the query to get the data

        Log.v("DIM", "MyDataBase : " + myDataBase.getPath() + " */* " + myDataBase.toString());

        String nomColonnes[] = cursor.getColumnNames();

        Log.v("DIM", "Nombre Colonnes : " + nomColonnes.length);

        for (String nomColonne : nomColonnes)
            Log.v("DIM", "NOM DE COLONNE : " + nomColonne);

        Log.v("DIM", "cursor.getCount() : " + cursor.getCount());

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String id = cursor.getString(0);
                String hourSaved = cursor.getString(1);
                String idMiniGame = cursor.getString(2);
                String idRingtone = cursor.getString(3);
                String enable = cursor.getString(4);

                sb.append("\n").append(id).append("\n").append(hourSaved).append("\n").append(idMiniGame).append("\n").append(idRingtone).append("\n").append(enable);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return sb.toString();
    }
}