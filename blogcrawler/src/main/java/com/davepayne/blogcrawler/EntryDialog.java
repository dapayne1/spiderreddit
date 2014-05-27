package com.davepayne.blogcrawler;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.davepayne.blogcrawler.activity.ItemListActivity;

/**
 * Convenience method to supply a standardized AlertDialog.Builder for wheneve the user wishes
 * to update the RSS URL. Provides an edittable field for user to do so.
 */
public class EntryDialog extends AlertDialog.Builder {

    private EditText editTextURL;
    private Button buttonGo;
    private Activity mActivity;

    public EntryDialog(Activity activity, String currentURL) {
        super(activity);

        mActivity = activity;

        this.setTitle("SpiderReddit RSS Reader");
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.entry_dialog, null);
        this.setView(dialoglayout);

        editTextURL = (EditText) dialoglayout.findViewById(R.id.entry_dialog_edittext_url);

        // Populate our EditText with existing URL, if any.
        if (currentURL != null && !currentURL.isEmpty()) {
            editTextURL.setText(currentURL);
        }

        buttonGo = (Button) dialoglayout.findViewById(R.id.entry_dialog_button_go);
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextURL != null && !editTextURL.getText().toString().isEmpty()) {
                    String potentialNewURL = editTextURL.getText().toString();
                    if (validURL(potentialNewURL)) {
                        setNewURL(potentialNewURL);
                    } else {
                        // Request focus back to edittext.
                        editTextURL.requestFocus();
                        editTextURL.setSelection(0);

                        // Alert user to enter a valid URL.
                        Toast.makeText(mActivity, "Please enter a valid URL.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validURL(String urlStringToValidate) {
        // As of Android API level 8 there is a WEB_URL pattern. Quoting the source, it "match[es] most part of RFC 3987".
        return Patterns.WEB_URL.matcher(urlStringToValidate).matches();
    }

    private void setNewURL(String newURL) {
        ((ItemListActivity)mActivity).setNewURL(newURL);
    }
}
