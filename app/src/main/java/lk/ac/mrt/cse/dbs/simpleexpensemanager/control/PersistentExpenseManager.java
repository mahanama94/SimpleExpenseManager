package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DatabaseTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 * Created by bhanuka on 11/21/16.
 */
public class PersistentExpenseManager extends ExpenseManager {

    private Context context;

    public PersistentExpenseManager(Context context){
        this.context = context;

        try {
            setup();
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }
    }

    /***
     * This method should be implemented by the concrete implementation of this class. It will dictate how the DAO
     * objects will be initialized.
     */
    @Override
    public void setup() throws ExpenseManagerException {

        SQLiteDatabase myDatabase = context.openOrCreateDatabase("140381", context.MODE_PRIVATE, null);

        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS account(" +
                "accountNumber VARCHAR PRIMARY KEY," +
                "bank VARCHAR," +
                "accountHolderName VARCHAR," +
                "balance REAL" +
                " );");

        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS transaction_Data(" +
                "transactionId INTEGER PRIMARY KEY," +
                "accountNumber VARCHAR," +
                "type INT," +
                "amount REAL," +
                "date DATE," +
                "FOREIGN KEY (accountNumber) REFERENCES account(accountNumber)" +
                ");");


        AccountDAO accountDAO = new DatabaseAccountDAO(myDatabase);
        setAccountsDAO(accountDAO);

        setTransactionsDAO(new DatabaseTransactionDAO(myDatabase));

//        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
//        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
//        getAccountsDAO().addAccount(dummyAcct1);
//        getAccountsDAO().addAccount(dummyAcct2);

    }
}
