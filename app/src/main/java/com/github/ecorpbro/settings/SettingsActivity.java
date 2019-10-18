package com.github.ecorpbro.settings;

import androidx.fragment.app.Fragment;

import com.github.ecorpbro.SingleFragmentActivity;

public class SettingsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SettingsFragment.newInstance();
    }
}
