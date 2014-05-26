package com.davepayne.blogcrawler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.davepayne.blogcrawler.db.RSSDBData;
import com.davepayne.blogcrawler.db.RSSDBHelper;
import com.davepayne.blogcrawler.db.RSSDBManager;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;
import java.util.List;

import at.theengine.android.simple_rss2_android.RSSItem;
import at.theengine.android.simple_rss2_android.SimpleRss2Parser;
import at.theengine.android.simple_rss2_android.SimpleRss2ParserCallback;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details
 * (if present) is a {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ItemListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ItemListActivity extends ActionBarActivity implements ItemListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private List<RSSItem> rssItems;
    private String currentURL = null;
    private AlertDialog mAlertDialog;
    private SharedPreferences mSharedPreferences;
    private final static String SHARED_PREF_KEY = "shared_preferences";
    //private RSSDBHelper mRSSDBHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Let's initialize our local database.
        RSSDBManager.init(this);

        setContentView(R.layout.activity_item_list);

        // Show the Up button in the action bar.
        getSupportActionBar().setHomeButtonEnabled(true);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ItemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
        }

        // Load our saved shared preferences, if any.
        mSharedPreferences = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE);
        currentURL = mSharedPreferences.getString("currentURL", null);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Let's attempt to load stored data, if any, from our local database.
        boolean foundSavedData = loadSavedLocalData();

        if (!foundSavedData) {
            // No local data saved, so let's display our entry dialog to prompt user for an RSS URL.
            showEntryDialog();
        }
    }

    private ArrayList<RSSItem> convertOutOfDBFormat(List<RSSDBData> dbItems) {
        ArrayList<RSSItem> rssItems = null;
        if ((dbItems != null) && (!dbItems.isEmpty())) {
            rssItems = new ArrayList<RSSItem>(dbItems.size());
            RSSItem newRSSItem;
            for(RSSDBData thisDBItem : dbItems) {
                newRSSItem = thisDBItem.toRSSItem();
                rssItems.add(newRSSItem);
            }
        }
        return rssItems;
    }

    /**
     * This method attempts to load any previously saved RSS data from the app's local database.
     * If it does
     */
    private boolean loadSavedLocalData() {
        boolean didLoadSavedData = false;
        // Query our local database for all of it's RSS data.
        final List<RSSDBData> savedRSSData = RSSDBManager.getInstance().getAllRSSDBDatas(this);

        // If our local database has returned data,
        if (savedRSSData != null) {
            didLoadSavedData = true;
            // Save our newly loaded items.
            rssItems = convertOutOfDBFormat(savedRSSData);

            // Update our lists.
            ((ItemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list)).loadFeed(getRssItemsAsArrayList());
        }

        return didLoadSavedData;
    }


    private void showEntryDialog() {
        EntryDialog newDialog = new EntryDialog(this, currentURL);
        newDialog.setCancelable(false);
        mAlertDialog = newDialog.create();
        mAlertDialog.show();
    }

    /**
     * Callback method from {@link ItemListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID, id);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ItemDetailActivity.class);
            detailIntent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    /**
     * Sets the app URL for RSS.
     * * @param String representing a valid RSS feed URL.
     */
    public void setNewURL(String newURL) {
        // Update our local variable.
        currentURL = newURL;

        // Make sure we save this to our shared preferences as well.
        SharedPreferences.Editor prefEditor = mSharedPreferences.edit();
        prefEditor.clear();
        prefEditor.putString("currentURL", currentURL);
        prefEditor.commit();

        // Dismiss any active open dialogs.
        if (mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }

        // Alert user we are loading new RSS.
        Toast.makeText(this, "Now loading RSS feed for " + currentURL + "...", Toast.LENGTH_SHORT).show();

        // Go ahead and begin fetching the RSS feed for the newly given URL.
        fetchAndParseRSSForCurrentURL();

    }

    /**
     * Assumes that local variable currentURL is valid (save for not having a "http://" in front...
     * this is checked for), asynchronously fetches and parses RSS feed to UI-friendly list
     * of RSSItems.
     */
    private void fetchAndParseRSSForCurrentURL() {
        final Context context = this;

        String urlToFetch = currentURL;
        if (!currentURL.startsWith("http://")) {
            urlToFetch = "http://" + currentURL;
        } else {
            urlToFetch = currentURL;
        }
        SimpleRss2Parser parser = new SimpleRss2Parser(urlToFetch,
                new SimpleRss2ParserCallback() {
                    @Override
                    public void onFeedParsed(List<RSSItem> items) {

                        // Save our newly parsed items.
                        rssItems = items;

                        // Update our database.
                        ArrayList<RSSDBData>itemsDB = new ArrayList<RSSDBData>(items.size());
                        for (RSSItem thisItem : items) {
                            itemsDB.add(new RSSDBData(thisItem));
                        }
                        RSSDBManager.getInstance().setRSSDBData(itemsDB, context);

                        // Update our fragment listview.
                        ((ItemListFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.item_list)).loadFeed(getRssItemsAsArrayList());
                    }
                    @Override
                    public void onError(Exception ex) {
                        Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        parser.parseAsync();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            showEntryDialog();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public List<RSSItem> getRssItems() {
        return rssItems;
    }

    public ArrayList<RSSItem> getRssItemsAsArrayList() {
        if (rssItems != null) {
            return new ArrayList<RSSItem>(rssItems);
        } else {
            return null;
        }
    }

    public String getCurrentURL() {
        return currentURL;
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        if (mRSSDBHelper != null) {
//            OpenHelperManager.releaseHelper();
//            mRSSDBHelper = null;
//        }
//    }
//
//    private RSSDBHelper getHelper() {
//        if (mRSSDBHelper == null) {
//            mRSSDBHelper = OpenHelperManager.getHelper(this, RSSDBHelper.class);
//        }
//        return mRSSDBHelper;
//    }
}
