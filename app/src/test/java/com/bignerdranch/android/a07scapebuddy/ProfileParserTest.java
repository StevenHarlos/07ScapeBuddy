package com.bignerdranch.android.a07scapebuddy;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by RedSodeeePop on 2016-08-15.
 */
public class ProfileParserTest {

    Profile profile;

    @Before
    public void setUp(){
        profile = new Profile("dark_slaya25", "standard");

    }

    @Test
    public void testSkillSize(){
        assertEquals(24,profile.getmSkills().size());
    }
    @Test
    public void testSkillEXP(){
        assertEquals(profile.getmSkills().get(0).getmXP(), 29840138 );
        assertEquals(profile.getmSkills().get(5).getmXP(), 886247);
        assertEquals(profile.getmSkills().get(23).getmXP(),101372);
    }


}
