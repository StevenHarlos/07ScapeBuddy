package com.bignerdranch.android.a07scapebuddy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RedSodeeePop on 2016-08-17.
 */
public class ItemSearchActivityFragment extends Fragment {

    private RecyclerView mItemRecyclerView;
    private List<Item> mItems = new ArrayList<>();
    private ThumbnailDownloader mThumbnailDownloader;


    public static ItemSearchActivityFragment newInstance() {
        return new ItemSearchActivityFragment();
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(new ThumbnailDownloader.ThumbnailDownloadListener<ItemHolder>() {
            @Override
            public void onThumbnailDownloaded(ItemHolder target, Bitmap thumbnail) {
                Drawable drawable = new BitmapDrawable(getResources(), thumbnail);


                target.bindDrawable(drawable);
            }
        });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.item_search_list, container, false);

        mItemRecyclerView = (RecyclerView) v.findViewById(R.id.item_recycler_view);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter();
        updateItems();

        return v;
    }

    public void onDestroyView(){
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
    }

    public void onDestroy(){
        super.onDestroy();
        mThumbnailDownloader.quit();
    }

    private void setupAdapter(){
        if(isAdded()) {
            mItemRecyclerView.setAdapter(new ItemAdapter(mItems));
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.item_list_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search_item);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                QueryPreferences.setStoredQuery(getActivity(), query);
                updateItems();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = QueryPreferences.getStoredQuery(getActivity());
                searchView.setQuery(query, false);
            }
        });

    }
    private void updateItems(){
        String query = QueryPreferences.getStoredQuery(getActivity());

        if(query != null){
            new FetchItemsTask(query).execute();
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mItemIconView;

        private TextView mItemNameView;
        private TextView mItemDescriptionView;
        private TextView mItemPriceView;
        private TextView mItemTrendView;

        private Item mItem;

        public ItemHolder(View itemView) {
            super(itemView);

            mItemNameView = (TextView) itemView.findViewById(R.id.name_textview);
            mItemDescriptionView = (TextView) itemView.findViewById(R.id.description_textview);
            mItemPriceView = (TextView) itemView.findViewById(R.id.price_textview);
            mItemTrendView = (TextView) itemView.findViewById(R.id.trend_textview);

            mItemIconView = (ImageView) itemView.findViewById(R.id.item_icon_view);
            itemView.setOnClickListener(this);
        }

        public void bindDrawable(Drawable drawable){

            mItemIconView.setImageDrawable(drawable);
            mItemIconView.invalidate();
        }
        public void bindItem(Item item){
            mItem = item;

            mItemNameView.setText(mItem.getmName());
            mItemDescriptionView.setText(mItem.getmDescription());
            mItemPriceView.setText(mItem.getmPrice());

            if(mItem.getmTrendType().equals("neutral")){
                mItemTrendView.setText("No trend");
            }else if(mItem.getmTrendType().equals("negative")){
                mItemTrendView.setText("Today: " + mItem.getmTrendPrice());
                mItemTrendView.setTextColor(Color.RED);
            }else if(mItem.getmTrendType().equals("positive")){
                mItemTrendView.setText("Today: " + mItem.getmTrendPrice());
                mItemTrendView.setTextColor(Color.GREEN);
            }


        }

        public void onClick(View view){
            Intent i = ItemPageActivity.newIntent(getActivity(),mItem);
            startActivity(i);
        }
    }

    private class ItemAdapter extends  RecyclerView.Adapter<ItemHolder> {

        private List<Item> mItemsList;

        public ItemAdapter(List<Item> items){
            mItemsList = items;
        }

        public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.item_list_view, viewGroup, false);
            return new ItemHolder(view);
        }

        public void onBindViewHolder(ItemHolder itemHolder, int position){
            Item item = mItemsList.get(position);
            itemHolder.bindItem(item);
            mThumbnailDownloader.queueThumbnail(itemHolder, item.getmIconUrl());
        }

        public int getItemCount(){
            return mItemsList.size();
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<Item>> {
        private String mQuery;

        public FetchItemsTask(String query){
            mQuery = query;
        }

        protected List<Item> doInBackground(Void... params){

            if(mQuery == null){
                return new ArrayList<>();
            }else{
                return new OSRSServiceParser().fetchItems(mQuery);
            }
        }

        protected void onPostExecute(List<Item> items){
            mItems = items;
            setupAdapter();
        }

    }






}
