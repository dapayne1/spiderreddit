package com.davepayne.blogcrawler.db;

import android.content.Context;
import android.database.SQLException;
import java.util.List;

public class RSSDBManager {

    /** Singleton Design Pattern **/
    static private RSSDBManager instance;
    static public void init(Context ctx) {
        if (null==instance) {
            instance = new RSSDBManager(ctx);
        }
    }

    static public RSSDBManager getInstance() {
        return instance;
    }

    private RSSDBHelper helper;
    private RSSDBManager(Context ctx) {
        helper = new RSSDBHelper(ctx);
    }

    private RSSDBHelper getHelper() {
        return helper;
    }

    /**
     * @return Returns all saved RSSDBData in local database, or null.
     */
    public List<RSSDBData> getAllRSSDBDatas() {
        List<RSSDBData> rssdbDatas = null;
        try {
            rssdbDatas = getHelper().getRSSDBDataDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rssdbDatas;
    }

    public void addRSSDBData(RSSDBData l) {
        try {
            getHelper().getDao().create(l);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRSSDBData(RSSDBData wishList) {
        try {
            getHelper().getDao().update(wishList);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}
