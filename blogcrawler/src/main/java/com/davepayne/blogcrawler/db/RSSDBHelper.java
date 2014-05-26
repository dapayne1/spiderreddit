package com.davepayne.blogcrawler.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.davepayne.blogcrawler.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class RSSDBHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "blogcrawler.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the RSSDBData table
    private Dao<RSSDBData, Integer> simpleDao = null;
    private RuntimeExceptionDao<RSSDBData, Integer> simpleRuntimeDao = null;

    public RSSDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(RSSDBHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, RSSDBData.class);
        } catch (SQLException e) {
            Log.e(RSSDBHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }

        // here we try inserting data in the on-create as a test
//        RuntimeExceptionDao<RSSDBData, Integer> dao = getRSSDBDataDao();
//        long millis = System.currentTimeMillis();
//        // create some entries in the onCreate
//        RSSDBData simple = new RSSDBData(millis);
//        dao.create(simple);
//        simple = new RSSDBData(millis + 1);
//        dao.create(simple);
//        Log.i(RSSDBHelper.class.getName(), "created new entries in onCreate: " + millis);
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(RSSDBHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, RSSDBData.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(RSSDBHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our RSSDBData class. It will create it or just give the cached
     * value.
     */
    public Dao<RSSDBData, Integer> getDao() throws SQLException {
        if (simpleDao == null) {
            simpleDao = getDao(RSSDBData.class);
        }
        return simpleDao;
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our RSSDBData class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<RSSDBData, Integer> getRSSDBDataDao() {
        if (simpleRuntimeDao == null) {
            simpleRuntimeDao = getRuntimeExceptionDao(RSSDBData.class);
        }
        return simpleRuntimeDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        simpleDao = null;
        simpleRuntimeDao = null;
    }
}
