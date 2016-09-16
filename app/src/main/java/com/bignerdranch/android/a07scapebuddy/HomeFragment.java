package com.bignerdranch.android.a07scapebuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.roughike.bottombar.BottomBar;

/**
 * Created by RedSodeeePop on 2016-08-11.
 */
public class HomeFragment extends Fragment {
    private Button mProfileButton;
    private Button mGrandExchangeButton;


    public static HomeFragment newInstance(){
        return new HomeFragment();
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.home_activity, container, false);

        mProfileButton = (Button) v.findViewById(R.id.profile_button);
        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = ProfileActivity.newIntent(getActivity());
                startActivity(i);
            }
        });



        mGrandExchangeButton = (Button) v.findViewById(R.id.grand_exchange_button);
        mGrandExchangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = ItemSearchActivity.newIntent(getActivity());
                startActivity(i);
            }
        });

        return v;
    }


}
