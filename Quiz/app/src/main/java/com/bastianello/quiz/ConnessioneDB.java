package com.bastianello.quiz;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 CLASSE DELLA GESTIONE DELLA CONNESIONE AL DB
 */
public class ConnessioneDB extends SQLiteOpenHelper{
    private static String DB_PATH = "data/data/com.bastianello.quiz/database"; //< percorso cartella
    private static String DB_NAME = "DBQuiz.db"; //< nome file/DB

    private SQLiteDatabase dbQuiz; //< oggetto DB

    private final Context myContext;


    public SQLiteDatabase getDB(){
        return dbQuiz;
    }


    /**
        Costruttore che crea la connessione con il db impostato negli attributi statici della classe
    */
    public ConnessioneDB(Context context){
        super(context, DB_NAME, null, 1); //context - nome - cursor(factory) - versioneDB
        this.myContext = context;
    }

    /**
     * Costruttore che permette la connessione con un db che ha un determinato "name"
     */
    public ConnessioneDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.myContext = context;
    }

    /**
        Costruttore che crea la connessione con il db con gestione errore personalizzata
    */
    public ConnessioneDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        this.myContext = context;
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private void copyDataBase() throws IOException{
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){
            //database does't exist yet.
        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    public void openDataBase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME; //percorso del db da aprire
        dbQuiz = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY); // apre connessione (percorso, null, "Come viene aperto")
    }

    @Override
    public synchronized void close() {
        if(dbQuiz != null)
            dbQuiz.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        /**/
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /** NON LO USERO' PER QUESTA APP **/
    }


}
