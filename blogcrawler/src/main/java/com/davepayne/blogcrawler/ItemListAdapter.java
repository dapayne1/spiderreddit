package com.davepayne.blogcrawler;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
            items = new ArrayList<RSSItem>();
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

        View rowView = convertView;
        ViewHolder viewHolder = null;

        if (convertView == null) {
            LayoutInflater inflater = parentActivity.getLayoutInflater();
            rowView = inflater.inflate(android.R.layout.simple_list_item_2, null);

            viewHolder = new ViewHolder();
            viewHolder.text1 = (TextView) rowView.findViewById(android.R.id.text1);
            viewHolder.text2 = (TextView) rowView.findViewById(android.R.id.text2);

            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

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
