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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.github.ecorpbro.R;
import com.github.ecorpbro.database.SettingsBaseManager;

public class SettingsDialogDel extends DialogFragment {
    private TextView mTxtName;
    private TextView mTxtUrl;

    public static SettingsDialogDel newInstance() {
        SettingsDialogDel fragment = new SettingsDialogDel();
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_delurl,null);
        mTxtName = (TextView) view.findViewById(R.id.txtNameDel);
        mTxtUrl = (TextView) view.findViewById(R.id.txtUrlDel);
        SettingsBaseManager baseManage = SettingsBaseManager.get(getActivity());
        mTxtName.setText(baseManage.getDefName());
        mTxtUrl.setText(baseManage.getDefUrl());

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.btn_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
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