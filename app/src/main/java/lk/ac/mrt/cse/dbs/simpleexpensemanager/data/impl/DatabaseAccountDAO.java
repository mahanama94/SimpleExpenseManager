package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by bhanuka on 11/21/16.
 */
public class DatabaseAccountDAO implements AccountDAO {

    private SQLiteDatabase database;


    public DatabaseAccountDAO(SQLiteDatabase database){
        this.database = database;
    }
    /***
     * Get a list of account numbers.
     *
     * @return - list of account numbers as String
     */
    @Override
    public List<String> getAccountNumbersList() {

        Cursor cursor = this.database.rawQuery("SELECT accountNumber FROM account", null);

        List<String> accountNumbers = new ArrayList<>();

        if(cursor.moveToFirst()){

            accountNumbers.add(cursor.getString(cursor.getColumnIndex("accountNumber")));

            Log.d("DATABASE", cursor.getString(cursor.getColumnIndex("accountNumber")));
            while(cursor.moveToNext()){
                accountNumbers.add(cursor.getString(cursor.getColumnIndex("accountNumber")));
                Log.d("DATABASE", cursor.getString(cursor.getColumnIndex("accountNumber")));
            }
        }

        return accountNumbers;
    }

    /***
     * Get a list of accounts.
     *
     * @return - list of Account objects.
     */
    @Override
    public List<Account> getAccountsList() {

        Cursor cursor = this.database.rawQuery("SELECT * FROM account", null);

        List<Account> accountList = new ArrayList<>();

        if(cursor.moveToFirst()){

            Account account =  new Account(
                    cursor.getString(cursor.getColumnIndex("accountNumber")),
                    cursor.getString(cursor.getColumnIndex("bank")),
                    cursor.getString(cursor.getColumnIndex("accountHolderName")),
                    cursor.getDouble(cursor.getColumnIndex("balance")));

            accountList.add(account);

            while(cursor.moveToNext()){

                account =  new Account(
                        cursor.getString(cursor.getColumnIndex("accountNumber")),
                        cursor.getString(cursor.getColumnIndex("bank")),
                        cursor.getString(cursor.getColumnIndex("accountHolderName")),
                        cursor.getDouble(cursor.getColumnIndex("balance")));

                accountList.add(account);

            }
        }

        return accountList;

    }

    /***
     * Get the account given the account number.
     *
     * @param accountNo as String
     * @return - the corresponding Account
     * @throws InvalidAccountException - if the account number is invalid
     */
    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        Cursor cursor = this.database.rawQuery("SELECT FROM account WHERE accountNumber = "+accountNo, null);

        Account account = null;

        if(cursor.moveToFirst()){

            account = new Account(
                    cursor.getString(cursor.getColumnIndex("accountNumber")),
                    cursor.getString(cursor.getColumnIndex("bank")),
                    cursor.getString(cursor.getColumnIndex("accountHolderName")),
                    cursor.getDouble(cursor.getColumnIndex("balance")));

        }


        return account;

    }

    /***
     * Add an account to the accounts collection.
     *
     * @param account - the account to be added.
     */
    @Override
    public void addAccount(Account account) {

        String query = "INSERT INTO account (accountNumber,bank,accountHolderName,balance) VALUES (?,?,?,?)";

        SQLiteStatement statement = database.compileStatement(query);

        statement.bindString(1, account.getAccountNo());

        statement.bindString(2, account.getBankName());

        statement.bindString(3, account.getAccountHolderName());

        statement.bindDouble(4, account.getBalance());

        statement.executeInsert();

    }

    /***
     * Remove an account from the accounts collection.
     *
     * @param accountNo - of the account to be removed.
     * @throws InvalidAccountException - if the account number is invalid
     */
    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        String sql = "DELETE FROM account WHERE accountNumber = ?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1,accountNo);

        statement.executeUpdateDelete();

    }

    /***
     * Update the balance of the given account. The type of the expense is specified in order to determine which
     * action to be performed.
     *
     * The implementation has the flexibility to figure out how the updating operation is committed based on the type
     * of the transaction.
     *
     * @param accountNo   - account number of the respective account
     * @param expenseType - the type of the transaction
     * @param amount      - amount involved
     * @throws InvalidAccountException - if the account number is invalid
     */
    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        String sql = "UPDATE account SET balance = balance + ? WHERE accountNumber = ?";

        SQLiteStatement statement = database.compileStatement(sql);

        if(expenseType == ExpenseType.EXPENSE){
            statement.bindDouble(1,-amount);
        }else{
            statement.bindDouble(1,amount);
        }

        statement.bindString(2,accountNo);

        statement.executeUpdateDelete();
    }
}
