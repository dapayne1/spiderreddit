package com.davepayne.blogcrawler;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import at.theengine.android.simple_rss2_android.RSSItem;

public class ItemListAdapter extends ArrayAdapter<RSSItem> {

    private ArrayList<RSSItem> items;

    private Activity parentActivity;

    public ItemListAdapter(Activity parent, ArrayList<RSSItem> items) {
        super(parent, android.R.layout.simple_list_item_2, items);

        this.parentActivity = parent;
        this.items = items;

        if (this.items == null) {
            this.items = new ArrayList<RSSItem>();
        }
    }

    public void loadFeed(ArrayList<RSSItem> items) {
        if (items != null) {
            this.items = items;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public RSSItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Set up our view and viewholders.
        View rowView = convertView;
        ViewHolder viewHolder = null;

        // If this row hasn't been set up prior,
        if (convertView == null) {
            // Inflate our rowView from XML layout.
            LayoutInflater inflater = parentActivity.getLayoutInflater();
            rowView = inflater.inflate(android.R.layout.simple_list_item_2, null);

            // Fancify background for row view.
            GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]
                    {Color.TRANSPARENT, Color.argb(128, 158, 211, 255)});
            gradient.setShape(GradientDrawable.RECTANGLE);
            gradient.setCornerRadius(25.f);
            rowView.setBackgroundDrawable(gradient);

            // Set up our viewholder.
            viewHolder = new ViewHolder();
            viewHolder.text1 = (TextView) rowView.findViewById(android.R.id.text1);
            viewHolder.text2 = (TextView) rowView.findViewById(android.R.id.text2);

            // Apply our viewholder tag, for reuseability.
            rowView.setTag(viewHolder);
        } else {
            // This row has been set up prior, so simply reference by existing tag.
            viewHolder = (ViewHolder) rowView.getTag();
        }

        // Populate our view UI with values from respective row data.
        RSSItem thisRSSItem = (RSSItem) getItem(position);
        viewHolder.text1.setText(thisRSSItem.getTitle());
        viewHolder.text2.setText(thisRSSItem.getDate());

        return rowView;
    }

    static class ViewHolder {
        public TextView text1;
        public TextView text2;
    }

}
