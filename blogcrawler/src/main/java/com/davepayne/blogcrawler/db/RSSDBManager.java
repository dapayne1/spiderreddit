package com.davepayne.blogcrawler.db;

import android.content.Context;
import android.database.SQLException;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Follows singleton design pattern. Initialize first, once, via RSSDBManager.init(context).
 * Access after that via RSSDBManager.getInstance().
 */
public class RSSDBManager {

    private RSSDBHelper helper;

    /** Singleton Design Pattern Begins **/
    static private RSSDBManager instance;

    static public void init(Context ctx) {
        if (null==instance) {
            instance = new RSSDBManager(ctx);
        }
    }

    private RSSDBManager(Context ctx) {
        helper = new RSSDBHelper(ctx);
    }

    static public RSSDBManager getInstance() {
        return instance;
    }
    /** Singleton Design Pattern Ends **/

    /**
     * @return Returns List of all RSSDBData objects in local database, or null if none present or
     * an exception is thrown.
     */
    public List<RSSDBData> getAllRSSDBDatas(Context context) {

        List<RSSDBData> rssdbDatas = null;
        try {
            rssdbDatas = getHelper(context).getRSSDBDataDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rssdbDatas;
    }

    /**
     * Deletes current database, sets data to pass in RSSDBData array.
     * @param newDBDatas ArrayList of RSSDBData objects that will be added to the database.
     * @param context Context of calling activity.
     */
    public void setRSSDBData(ArrayList<RSSDBData> newDBDatas, Context context) {
        try {
            // Delete existing data.
            resetDB(context);

            // Add new data to database.
            for(RSSDBData thisDBData : newDBDatas) {
                getHelper(context).getDao().createIfNotExists(thisDBData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the database by deleting all existing database data and re-initializing the database
     * helper object.
     * @param context Context of calling activity.
     */
    private void resetDB(Context context) {
        deleteAllRSSDBData(context);
        helper = new RSSDBHelper(context);
    }

    /**
     * Deletes all existing database data.
     * @param context Context of calling activity.
     * @return Returns true if deletion was successful, false otherwise.
     */
    private boolean deleteAllRSSDBData(Context context) {
        return context.deleteDatabase(context.getDatabasePath("blogcrawler.db").getPath());
    }

    /**
     * Adds array of new RSSDBData objects to the existing database. Assumes that no RSSDBData
     * in the array is already in the database, as it does not check for that.
     * @param context Context of calling activity.
     * @param newDBDatas ArrayList of RSSDBData objects that will be added to the database.
     */
    public void addRSSDBDatas(Context context, ArrayList<RSSDBData> newDBDatas) {
        try {
            for(RSSDBData thisDBData : newDBDatas) {
                getHelper(context).getDao().create(thisDBData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates values of RSSDBData objects already in the existing database. Assumes that each RSSDBData
     * in the array is already in the database.
     * @param context Context of calling activity.
     * @param newDBDatas ArrayList of RSSDBData objects that will be updated in the database.
     */
    public void updateRSSDBData(Context context, ArrayList<RSSDBData> newDBDatas) {
        try {
            for(RSSDBData thisDBData : newDBDatas) {
                getHelper(context).getDao().update(thisDBData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    private RSSDBHelper getHelper(Context context) {
        return helper;
    }
}
