package com.bignerdranch.android.a07scapebuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.support.v4.app.Fragment;

/**
 * Created by RedSodeeePop on 2016-08-18.
 */
public class ItemPageActivity extends SingleFragmentActivity{

    public static Intent newIntent(Context context, Item item) {
        Intent i = new Intent(context, ItemPageActivity.class);
        i.putExtra("item", item);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        return ItemPageActivityFragment.newInstance(getIntent().getParcelableExtra("item"));
    }
}
