package com.bignerdranch.android.a07scapebuddy;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by RedSodeeePop on 2016-08-17.
 */
public class ItemSearchActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context){
        return new Intent(context, ItemSearchActivity.class);
    }
    @Override
    protected Fragment createFragment() {
        return ItemSearchActivityFragment.newInstance();
    }
}
