package com.github.ecorpbro.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.github.ecorpbro.R;

public class SettingsDialogAdd extends DialogFragment {
    public static final String EXTRA_NAME = "com.github.ecorpbro.name";
    public static final String EXTRA_URL = "com.github.ecorpbro.url";

    private EditText mTxtAdd;
    private EditText mTxtUrl;

    public static SettingsDialogAdd newInstance() {
        SettingsDialogAdd fragment = new SettingsDialogAdd();
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_addurl,null);
        mTxtAdd = (EditText)view.findViewById(R.id.txtNameAdd);
        mTxtUrl = (EditText)view.findViewById(R.id.txtUrlAdd);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.btn_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_NAME, mTxtAdd.getText());
                        intent.putExtra(EXTRA_URL, mTxtUrl.getText());
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }
}
