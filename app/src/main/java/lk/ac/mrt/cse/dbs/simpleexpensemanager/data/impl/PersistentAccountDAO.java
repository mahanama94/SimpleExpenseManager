package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by bhanuka on 11/18/16.
 */
public class PersistentAccountDAO extends DAO implements AccountDAO {


    /**
     * Colum names for the in the accounts table
     */
    public static String TABLE_NAME = "account";
    public static String PRIMARY_KEY = "accountNumber";

    public static String ACCOUNT_NUMBER = "accountNumber";
    public static String BANK_NAME = "bankName";
    public static String ACCOUNT_HOLDER_NAME = "holderName";
    public static String BALANCE = "balance";

    public PersistentAccountDAO(DatabaseHandler databaseHandler){
        super(databaseHandler);
    }

    @Override
    public List<String> getAccountNumbersList() {

        String query = "Select " + PersistentAccountDAO.PRIMARY_KEY + " from " + PersistentAccountDAO.TABLE_NAME;
        Cursor cursor  = this.databaseHandler.getReadableDatabase().rawQuery(query, null);

        List<String> accountList = new ArrayList<>();

        if(cursor.moveToFirst()){
            accountList.add(cursor.getString(cursor.getColumnIndex(PersistentAccountDAO.ACCOUNT_NUMBER)));

            Log.d("DATABASE",cursor.getString(cursor.getColumnIndex(PersistentAccountDAO.ACCOUNT_NUMBER)));
            while (cursor.moveToNext()){
                accountList.add(cursor.getString(cursor.getColumnIndex(PersistentAccountDAO.ACCOUNT_NUMBER)));
                Log.d("DATABASE",cursor.getString(cursor.getColumnIndex(PersistentAccountDAO.ACCOUNT_NUMBER)));
            }
        }

        cursor.close();

        return accountList;
    }

    @Override
    public List<Account> getAccountsList() {

        String query = "select * from " + PersistentAccountDAO.TABLE_NAME;

        Cursor cursor = this.databaseHandler.getReadableDatabase().rawQuery(query, null);

        List<Account> accountList = new LinkedList<>();

        if(cursor.moveToFirst()){
            accountList.add(new Account(
                    cursor.getString(cursor.getColumnIndex(PersistentAccountDAO.ACCOUNT_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(PersistentAccountDAO.BANK_NAME)),
                    cursor.getString(cursor.getColumnIndex(PersistentAccountDAO.ACCOUNT_HOLDER_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(PersistentAccountDAO.BALANCE))
            ));

            while(cursor.moveToNext()){

                accountList.add(new Account(
                        cursor.getString(cursor.getColumnIndex(PersistentAccountDAO.ACCOUNT_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(PersistentAccountDAO.BANK_NAME)),
                        cursor.getString(cursor.getColumnIndex(PersistentAccountDAO.ACCOUNT_HOLDER_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(PersistentAccountDAO.BALANCE))
                ));
            }
        }

        cursor.close();

        return accountList;
    }

    /**
     *
     * @param accountNo as String
     * @return Account
     * @throws InvalidAccountException
     */
    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String query = " select * from "+PersistentAccountDAO.TABLE_NAME + " where "+ PersistentAccountDAO.PRIMARY_KEY + " = '"+accountNo+"' ";

        Cursor cursor= this.databaseHandler.getReadableDatabase().rawQuery(query, null);

        Account account = null;

        if(cursor.moveToFirst()){
            account = new Account(
                    cursor.getString(cursor.getColumnIndex(PersistentAccountDAO.ACCOUNT_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(PersistentAccountDAO.BANK_NAME)),
                    cursor.getString(cursor.getColumnIndex(PersistentAccountDAO.ACCOUNT_HOLDER_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(PersistentAccountDAO.BALANCE))
            );
        }

        cursor.close();

        return account;
    }

    /**
     *
     * @param account - the account to be added.
     */
    @Override
    public void addAccount(Account account) {

        ContentValues values = new ContentValues();

        values.put(PersistentAccountDAO.ACCOUNT_NUMBER, account.getAccountNo());
        values.put(PersistentAccountDAO.BANK_NAME, account.getBankName());
        values.put(PersistentAccountDAO.ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        values.put(PersistentAccountDAO.BALANCE, account.getBalance());

        //(this.databaseHandler.getWritableDatabase()).insert(PersistentAccountDAO.TABLE_NAME, null, values);

        this.databaseHandler.getWritableDatabase().rawQuery("INSERT INTO "+ PersistentAccountDAO.TABLE_NAME +" ( "
                + PersistentAccountDAO.ACCOUNT_NUMBER+ " , "
                + PersistentAccountDAO.BANK_NAME + " , "
                + PersistentAccountDAO.ACCOUNT_HOLDER_NAME + " , "
                + PersistentAccountDAO.BALANCE + ") VALUES ("
                +"'" + account.getAccountNo()+"',"
                +"'"+ account.getBankName() +"' ,"
                +"'"+account.getAccountHolderName()+"' , "
                + Double.toString(account.getBalance())+ " )", null, null);
    }

    /**
     *
     * @param accountNo - of the account to be removed.
     * @throws InvalidAccountException
     */
    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        Account account = this.getAccount(accountNo);

        if( account != null){
            this.databaseHandler.getWritableDatabase().delete(this.TABLE_NAME, " where " + this.ACCOUNT_NUMBER + " = ? ", new String[]{accountNo});
        }

    }

    /**
     *
     * @param accountNo   - account number of the respective account
     * @param expenseType - the type of the transaction
     * @param amount      - amount involved
     * @throws InvalidAccountException
     */
    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Account account = null;

        account = this.getAccount(accountNo);

        if(account != null) {

            ContentValues values = new ContentValues();

            values.put(PersistentAccountDAO.ACCOUNT_NUMBER, account.getAccountNo());
            values.put(PersistentAccountDAO.BANK_NAME, account.getBankName());
            values.put(PersistentAccountDAO.ACCOUNT_HOLDER_NAME, account.getAccountHolderName());

            if (expenseType == ExpenseType.EXPENSE) {
                values.put(PersistentAccountDAO.BALANCE, account.getBalance() - amount);
            } else if (expenseType == ExpenseType.INCOME) {
                values.put(PersistentAccountDAO.BALANCE, account.getBalance() + amount);
            }

            this.databaseHandler.getWritableDatabase().update(PersistentAccountDAO.TABLE_NAME, values, PersistentAccountDAO.ACCOUNT_NUMBER + " = '" + accountNo + "' ", null);
        }

    }
}
