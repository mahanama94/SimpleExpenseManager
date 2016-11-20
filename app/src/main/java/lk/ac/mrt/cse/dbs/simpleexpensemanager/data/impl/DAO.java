package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DatabaseHandler;

/**
 * Created by bhanuka on 11/20/16.
 */
public abstract class DAO {

    protected static String TABLE_NAME = "";

    protected static String PRIMARY_KEY = "";

    protected DatabaseHandler databaseHandler = null;

    public DAO(DatabaseHandler databaseHandler){
        this.databaseHandler = databaseHandler;
    }

}
