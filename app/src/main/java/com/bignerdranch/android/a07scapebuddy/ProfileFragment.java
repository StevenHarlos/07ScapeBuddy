package com.bignerdranch.android.a07scapebuddy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RedSodeeePop on 2016-08-15.
 */
public class ProfileFragment extends Fragment {

    private RecyclerView mSkillRecyclerView;
    private SkillAdapter mSkillAdapter;
    private TextView mProfileName;
    private String mMethod = "standard";
    private Profile mProfile;
    private List<Skill> mProfileSkills = new ArrayList<>();


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        mSkillRecyclerView = (RecyclerView) view.findViewById(R.id.skill_recycler_view);
        mSkillRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mProfileName = (TextView) view.findViewById(R.id.profile_name);

        setupAdapter();
        updateItems();
        return view;


    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_fragment_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search_profile);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                QueryPreferences.setStoredQueryProfile(getActivity(), query);
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
                String query = QueryPreferences.getStoredQueryProfile(getActivity());
                searchView.setQuery(query, false);
            }
        });
    }

    public void updateItems() {
        String query = QueryPreferences.getStoredQueryProfile(getActivity());

        if (query != null) {
            mProfile = new Profile(query, mMethod);
            new FetchProfileTask().execute();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_standard:
                if (item.isChecked()) {
                    item.setChecked(false);
                    return true;
                } else {
                    item.setChecked(true);
                    mMethod = "standard";
                    return true;
                }
            case R.id.profile_ironman:
                if (item.isChecked()) {
                    item.setChecked(false);
                    return true;
                } else {
                    item.setChecked(true);
                    mMethod = "ironman";
                    return true;
                }

            case R.id.profile_ultimate:
                if (item.isChecked()) {
                    item.setChecked(false);
                    return true;
                } else {
                    item.setChecked(true);
                    mMethod = "ultimate";
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class FetchProfileTask extends AsyncTask<Void, Void, List<Skill>> {


        public FetchProfileTask() {

        }

        @Override
        protected List<Skill> doInBackground(Void... voids) {
            return new OSRSServiceParser().fetchProfile(mProfile.getmName(), mProfile.getmType());
        }

        protected void onPostExecute(List<Skill> skills) {
            if (skills.isEmpty()) {
                onListReturnedEmpty();
            } else {

                mProfileSkills = skills;
                mProfile.setmSkills(mProfileSkills);
                mProfileName.setText(mProfile.getmName());
                setupAdapter();
            }

        }
    }

    public void onListReturnedEmpty() {
        mProfileName.setText("Could not find profile");
    }

    private void setupAdapter() {
        if (isAdded()) {
            mSkillRecyclerView.setAdapter(new SkillAdapter(mProfileSkills));
        }
    }

    private class SkillHolder extends RecyclerView.ViewHolder {
        private TextView mSkillName;
        private TextView mSkillLevel;
        private TextView mSkillRank;
        private TextView mSkillEXP;
        private ImageView mSkillIcon;

        private Skill mSkill;

        public SkillHolder(View itemView) {
            super(itemView);

            mSkillName = (TextView) itemView.findViewById(R.id.skill_name);
            mSkillLevel = (TextView) itemView.findViewById(R.id.skill_level);
            mSkillRank = (TextView) itemView.findViewById(R.id.skill_rank);
            mSkillEXP = (TextView) itemView.findViewById(R.id.skill_exp);
            mSkillIcon = (ImageView) itemView.findViewById(R.id.icon_view);
        }

        public void bindSkill(Skill skill) {
            mSkill = skill;

            mSkillName.setText(mSkill.getmName());
            mSkillLevel.setText(String.valueOf(mSkill.getmLevel()));
            mSkillRank.setText(String.valueOf(mSkill.getmRank()));
            mSkillEXP.setText(String.valueOf(mSkill.getmXP()));

            //Drawable placeholder = getResources().getDrawable(R.mipmap.attack_icon);

            Context context = mSkillIcon.getContext();

            int id = context.getResources().getIdentifier(mSkill.getmName().toLowerCase() + "_icon", "mipmap", context.getPackageName());

            mSkillIcon.setImageResource(id);
        }


    }

    private class SkillAdapter extends RecyclerView.Adapter<SkillHolder> {
        private List<Skill> mSkills;

        public SkillAdapter(List<Skill> skills) {
            mSkills = skills;
        }

        public SkillHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.skill_view, parent, false);
            return new SkillHolder(view);
        }

        public void onBindViewHolder(SkillHolder holder, int position) {
            Skill skill = mSkills.get(position);
            holder.bindSkill(skill);
        }

        public int getItemCount() {
            return mSkills.size();
        }

        public void setSkills(List<Skill> skills) {
            mSkills = skills;
        }
    }
}
