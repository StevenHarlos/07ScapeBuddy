package com.bignerdranch.android.a07scapebuddy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RedSodeeePop on 2016-08-15.
 */
public class Profile {
    private List<Skill> mSkills;
    private String mName;
    private String mType;

    public Profile(String name, String type){
        mName = name;
        mType = type;
        mSkills = new ArrayList<>();

    }

    public void setmSkills(List<Skill> skills){
        mSkills = skills;
        mSkills.get(0).setmName("Overall");
        mSkills.get(1).setmName("Attack");
        mSkills.get(2).setmName("Defence");
        mSkills.get(3).setmName("Strength");
        mSkills.get(4).setmName("Hitpoints");
        mSkills.get(5).setmName("Ranged");
        mSkills.get(6).setmName("Prayer");
        mSkills.get(7).setmName("Magic");
        mSkills.get(8).setmName("Cooking");
        mSkills.get(9).setmName("Woodcutting");
        mSkills.get(10).setmName("Fletching");
        mSkills.get(11).setmName("Fishing");
        mSkills.get(12).setmName("Firemaking");
        mSkills.get(13).setmName("Crafting");
        mSkills.get(14).setmName("Smithing");
        mSkills.get(15).setmName("Mining");
        mSkills.get(16).setmName("Herblore");
        mSkills.get(17).setmName("Agility");
        mSkills.get(18).setmName("Thieving");
        mSkills.get(19).setmName("Slayer");
        mSkills.get(20).setmName("Farming");
        mSkills.get(21).setmName("Runecrafting");
        mSkills.get(22).setmName("Hunter");
        mSkills.get(23).setmName("Construction");


    }
    public List<Skill> getmSkills(){
        return mSkills;
    }
    public String getmName(){
        return mName;
    }
    public String getmType(){
        return mType;
    }
}
