package com.github.ecorpbro.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.ecorpbro.R;
import com.github.ecorpbro.database.SettingsBaseManager;
import com.github.ecorpbro.database.SettingsCursorWrapper;
import com.github.ecorpbro.database.SettingsDbSchema.SourceUrlTable;

public class SettingsFragment extends Fragment {

    public static final String TAG = "SettingsFragment";

    private static final String DIALOG_ADD = "DialogAdd";
    private static final String DIALOG_DEL = "DialogDelete";
    private static final String DIALOG_UPD = "DialogUpdate";

    private static final int REQUEST_ADD = 0;
    private static final int REQUEST_DEL = 1;
    private static final int REQUEST_UPD = 2;

    private SimpleCursorAdapter mSimpleCursorAdapter;
    private SettingsBaseManager mBaseManager;

    int mSpinnerPosition;
    boolean isResume;

    private Spinner mSpinner;
    private Button mBtnAddUrl;
    private Button mBtnDelUrl;
    private Button mBtnUpdUrl;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isResume = false;
        mBaseManager = SettingsBaseManager.get(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products_setting, container, false);

        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        mBtnAddUrl = (Button) view.findViewById(R.id.btnAddUrl);
        mBtnDelUrl = (Button) view.findViewById(R.id.btnDelUrl);
        mBtnUpdUrl = (Button)view.findViewById(R.id.btnUpdUrl);

        mBtnAddUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                SettingsDialogAdd dialogAdd = SettingsDialogAdd.newInstance();
                dialogAdd.setTargetFragment(SettingsFragment.this,REQUEST_ADD);
                dialogAdd.show(manager,DIALOG_ADD);
            }
        });

        mBtnDelUrl.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (mBaseManager.getDefUrl().equals(SettingsBaseManager.NOSORUCE)) {
                    Toast toast = Toast.makeText(getActivity(),"Добавьте источник данных",Toast.LENGTH_LONG);
                    toast.setGravity(1,1,1);
                    toast.show();
                } else {
                    FragmentManager manager = getFragmentManager();
                    SettingsDialogDel dialogDel = SettingsDialogDel.newInstance();
                    dialogDel.setTargetFragment(SettingsFragment.this,REQUEST_DEL);
                    dialogDel.show(manager,DIALOG_DEL);
                }

            }
        });

        mBtnUpdUrl.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (mBaseManager.getDefUrl().equals(SettingsBaseManager.NOSORUCE)) {
                    Toast toast = Toast.makeText(getActivity(),"Добавьте источник данных",Toast.LENGTH_LONG);
                    toast.setGravity(1,1,1);
                    toast.show();
                } else {
                    FragmentManager manager = getFragmentManager();
                    SettingsDialogUpd dialogUpd = SettingsDialogUpd.newInstance();
                    dialogUpd.setTargetFragment(SettingsFragment.this,REQUEST_UPD);
                    dialogUpd.show(manager,DIALOG_UPD);
                }
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //isResume - проверка на обновление выбора спинера после загрузки true или после выбора из списка false
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isResume) {
                    mSpinnerPosition = position;
                    SettingsBaseManager.get(getActivity()).addDefUrl(getUrl());
                    mSpinner.setSelection(mSpinnerPosition);
                } else {
                    mSpinner.setSelection(mSpinnerPosition);
                }
                isResume = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        updateUI();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else if (requestCode == REQUEST_ADD) {
            String name = data.getExtras().get(SettingsDialogAdd.EXTRA_NAME).toString();
            String url = data.getExtras().get(SettingsDialogAdd.EXTRA_URL).toString();
            boolean isCheckUrl = SettingsBaseManager.get(getActivity()).check(name, url);
            if (isCheckUrl) {
                SettingsBaseManager.get(getActivity()).addSourceUrl(name, url);
                SettingsBaseManager.get(getActivity()).addDefUrl(url);
            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.warning_nounique, Toast.LENGTH_LONG);
                toast.setGravity(1,1,1);
                toast.show();
            }
            updateUI();
        } else if (requestCode == REQUEST_DEL) {
            SettingsBaseManager.get(getActivity()).deleteSourceUrl();
            mSpinner.setSelection(0);
            mBaseManager.addDefUrl(mBaseManager.setFirst());
            updateUI();
        } else if (requestCode == REQUEST_UPD) {
            String name = data.getExtras().get(SettingsDialogAdd.EXTRA_NAME).toString();
            String url = data.getExtras().get(SettingsDialogAdd.EXTRA_URL).toString();
            SettingsBaseManager.get(getActivity()).updateSource(name,url);
            updateUI();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        setStartPositionSpinner();
    }

    public void updateUI() {
        updateSpinner();
    }

    public void updateSpinner() {
        SettingsCursorWrapper cursorSrcUrl = SettingsBaseManager.get(getActivity()).getCursorWrapper(SourceUrlTable.DB_TABLE_NAME,new String[]{"_id","name"},null,null);
        mSimpleCursorAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item, cursorSrcUrl, new String[] {"name"}, new int[] {android.R.id.text1},0);
        mSimpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mSimpleCursorAdapter);
        SettingsBaseManager baseManager = SettingsBaseManager.get(getActivity());
        String defUrl = baseManager.getDefUrl();
        mSpinner.setSelection(baseManager.getPositionUrl(defUrl));
    }

    //Устанавливаем стартовую позицию по url для установки в спиннере
    public void setStartPositionSpinner() {
        SettingsBaseManager baseManager = SettingsBaseManager.get(getActivity());
        String defUrl = baseManager.getDefUrl();
        if (defUrl == SettingsBaseManager.NOSORUCE) {
            mSpinnerPosition = 0;
        } else {
            mSpinnerPosition = SettingsBaseManager.get(getActivity()).getPositionUrl(defUrl);
        }
    }

    //Берем url из спинера по текущей выбраной позиции
    private String getUrl(){
        SettingsCursorWrapper cursor = (SettingsCursorWrapper) mSpinner.getItemAtPosition(mSpinnerPosition);
        cursor.moveToPosition(mSpinnerPosition);
        String url = cursor.getString(cursor.getColumnIndex(SourceUrlTable.Cols.URL));
        return url;
    }
}
