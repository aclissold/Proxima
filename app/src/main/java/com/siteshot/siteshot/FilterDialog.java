package com.siteshot.siteshot;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by aclissold on 11/6/14.
 */
public class FilterDialog extends DialogFragment {
    private final String TAG = getClass().getName();

    private int mSelectedItem;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSelectedItem = -1;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_dialog_filter)
                .setSingleChoiceItems(R.array.filter_fields, 2,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSelectedItem = which;
                            }
                        })
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, "should re-query map here");
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
