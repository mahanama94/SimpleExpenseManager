package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;
import android.util.Log;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

/**
 * Created by bhanuka on 11/20/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {


    private static DatabaseHandler instance = null;

    private DatabaseHandler(Context context){
        super(context, "140381E", null, 1);
    }

    public static  DatabaseHandler getInstance(Context context){
        if( DatabaseHandler.instance == null){
            DatabaseHandler.instance = new DatabaseHandler(context);
        }
        return DatabaseHandler.instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        this.createAccountTable(sqLiteDatabase);

        this.createTransactionTable(sqLiteDatabase);

        //this.createTransactionTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PersistentTransactionDAO.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PersistentAccountDAO.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    private void createAccountTable(SQLiteDatabase db){
        String query = "CREATE TABLE if not exists  " + PersistentAccountDAO.TABLE_NAME + "("
                + PersistentAccountDAO.ACCOUNT_NUMBER + " VARCHAR(6),"
                + PersistentAccountDAO.BANK_NAME + " VARCHAR(50),"
                + PersistentAccountDAO.ACCOUNT_HOLDER_NAME + " VARCHAR(100),"
                + PersistentAccountDAO.BALANCE + " REAL "+ "); ";

        db.execSQL(query);
    }

    private void createTransactionTable(SQLiteDatabase db){
        String query = "CREATE TABLE if not exists  " + PersistentTransactionDAO.TABLE_NAME + " ("
                + PersistentTransactionDAO.TRANSACTION_ID + " INTEGER PRIMARY KEY, "
                + PersistentTransactionDAO.ACCOUNT_NUMBER + " VARCHAR(6),"
                + PersistentTransactionDAO.DATE  + " VARCHAR(80),"
                + PersistentTransactionDAO.TYPE + " VARCHAR(10), "
                + PersistentTransactionDAO.AMOUNT + " REAL, "
                + "FOREIGN KEY(" + PersistentTransactionDAO.ACCOUNT_NUMBER + ") REFERENCES "
                + PersistentAccountDAO.TABLE_NAME +"("+ PersistentAccountDAO.ACCOUNT_NUMBER +")"+"); ";

        db.execSQL(query);

    }
}
