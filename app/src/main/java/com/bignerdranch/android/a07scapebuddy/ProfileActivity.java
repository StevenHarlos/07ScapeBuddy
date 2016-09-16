package com.bignerdranch.android.a07scapebuddy;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by RedSodeeePop on 2016-08-15.
 */
public class ProfileActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context){
        return new Intent(context, ProfileActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return ProfileFragment.newInstance();
    }
}
