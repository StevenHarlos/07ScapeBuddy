package com.bignerdranch.android.a07scapebuddy;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return HomeFragment.newInstance();
    }
}
