package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by bhanuka on 11/18/16.
 */
public class PersistentTransactionDAO extends DAO implements TransactionDAO{

    public static String TABLE_NAME = " transaction ";
    public static String PRIMARY_KEY = " transactionId ";

    public static String TRANSACTION_ID ="transactionId";
    public static String DATE = "date";
    public static String TYPE = "type";
    public static String AMOUNT = "amount";
    public static String ACCOUNT_NUMBER = "accountNumber";

    public PersistentTransactionDAO(DatabaseHandler databaseHandler) {
        super(databaseHandler);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);

        ContentValues values = new ContentValues();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        values.put(this.DATE, date.toString());
        values.put(this.ACCOUNT_NUMBER, accountNo);
        values.put(this.TYPE, expenseType.toString());
        values.put(this.AMOUNT, amount);

        this.databaseHandler.getWritableDatabase().insert(this.TABLE_NAME, null, values);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        String query = "select * from "+ this.TABLE_NAME;

        Cursor cursor = this.databaseHandler.getReadableDatabase().rawQuery(query, null);

        List<Transaction> transactionList = new LinkedList<>();

        if(cursor.moveToFirst()){
            Date date = null;
            String dateString = cursor.getString(cursor.getColumnIndex(this.DATE));
            try {
                date = (new SimpleDateFormat("dd-MM-YYYY")).parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Transaction transaction =  new Transaction(
                    date,
                    cursor.getString(cursor.getColumnIndex(this.ACCOUNT_NUMBER)),
                    ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(this.TYPE))),
                    cursor.getDouble(cursor.getColumnIndex(this.AMOUNT)));

            transactionList.add(transaction);

            while(cursor.moveToNext()){
                transaction =  new Transaction(
                        date,
                        cursor.getString(cursor.getColumnIndex(this.ACCOUNT_NUMBER)),
                        ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(this.TYPE))),
                        cursor.getDouble(cursor.getColumnIndex(this.AMOUNT)));

                transactionList.add(transaction);

            }
        }

        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        String query = "select * from "+ this.TABLE_NAME + " order by "+ this.DATE + " limit " + limit;

        Cursor cursor = this.databaseHandler.getReadableDatabase().rawQuery(query, null);

        List<Transaction> transactionList = new LinkedList<>();

        if(cursor.moveToFirst()){
            Date date = null;
            String dateString = cursor.getString(cursor.getColumnIndex(this.DATE));
            try {
                date = (new SimpleDateFormat("dd-MM-YYYY")).parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Transaction transaction =  new Transaction(
                    date,
                    cursor.getString(cursor.getColumnIndex(this.ACCOUNT_NUMBER)),
                    ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(this.TYPE))),
                    cursor.getDouble(cursor.getColumnIndex(this.AMOUNT)));

            transactionList.add(transaction);

            while(cursor.moveToNext()){
                transaction =  new Transaction(
                        date,
                        cursor.getString(cursor.getColumnIndex(this.ACCOUNT_NUMBER)),
                        ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(this.TYPE))),
                        cursor.getDouble(cursor.getColumnIndex(this.AMOUNT)));

                transactionList.add(transaction);

            }
        }

        return transactionList;

    }
}
