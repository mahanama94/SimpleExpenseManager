package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by bhanuka on 11/21/16.
 */
public class DatabaseTransactionDAO implements TransactionDAO {

    private SQLiteDatabase database;

    public DatabaseTransactionDAO(SQLiteDatabase database){
        this.database = database;
    }

    /***
     * Log the transaction requested by the user.
     *
     * @param date        - date of the transaction
     * @param accountNo   - account number involved
     * @param expenseType - type of the expense
     * @param amount      - amount involved
     */
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        String query = "INSERT INTO transaction_Data (accountNumber,type,amount,date) VALUES (?,?,?,?)";

        SQLiteStatement statement = database.compileStatement(query);

        statement.bindString(1,accountNo);
        statement.bindLong(2,(expenseType == ExpenseType.EXPENSE) ? 0 : 1);
        statement.bindDouble(3,amount);
        statement.bindLong(4,date.getTime());

        statement.executeInsert();

    }

    /***
     * Return all the transactions logged.
     *
     * @return - a list of all the transactions
     */
    @Override
    public List<Transaction> getAllTransactionLogs() {

        Cursor cursor = this.database.rawQuery("SELECT * FROM transaction_Data ", null);

        List<Transaction> transactionList = new ArrayList<>();

        if(cursor.moveToFirst()){

            Transaction transaction = new Transaction(
                    new Date(cursor.getLong(cursor.getColumnIndex("date"))),
                    cursor.getString(cursor.getColumnIndex("accountNumber")),
                    (cursor.getInt(cursor.getColumnIndex("type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                    cursor.getDouble(cursor.getColumnIndex("amount"))
            );

            transactionList.add(transaction);

            while(cursor.moveToNext()){
                transaction = new Transaction(
                        new Date(cursor.getLong(cursor.getColumnIndex("date"))),
                        cursor.getString(cursor.getColumnIndex("accountNumber")),
                        (cursor.getInt(cursor.getColumnIndex("type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        cursor.getDouble(cursor.getColumnIndex("amount"))
                );

                transactionList.add(transaction);
            }
        }

        return transactionList;
    }

    /***
     * Return a limited amount of transactions logged.
     *
     * @param limit - number of transactions to be returned
     * @return - a list of requested number of transactions
     */
    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        Cursor cursor = database.rawQuery("SELECT * FROM transaction_Data LIMIT " + limit,null);
        List<Transaction> transactionList = new ArrayList<Transaction>();

        if(cursor.moveToFirst()){

            Transaction transaction = new Transaction(
                    new Date(cursor.getLong(cursor.getColumnIndex("date"))),
                    cursor.getString(cursor.getColumnIndex("accountNumber")),
                    (cursor.getInt(cursor.getColumnIndex("type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                    cursor.getDouble(cursor.getColumnIndex("amount"))
            );

            transactionList.add(transaction);

            while(cursor.moveToNext()){
                transaction = new Transaction(
                        new Date(cursor.getLong(cursor.getColumnIndex("date"))),
                        cursor.getString(cursor.getColumnIndex("accountNumber")),
                        (cursor.getInt(cursor.getColumnIndex("type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        cursor.getDouble(cursor.getColumnIndex("amount"))
                );

                transactionList.add(transaction);
            }
        }

        return transactionList;
    }
}
