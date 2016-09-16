package com.bignerdranch.android.a07scapebuddy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by RedSodeeePop on 2016-08-15.
 */
public class ItemParserTest {
    List<Item> items;

    @Before
    public void setUp() {
        items = new ArrayList<>();
        new OSRSServiceParser().fetchItems("abyssal");
    }

    @Test
    public void testItemsSize() {
       try {
           String jsonString = new OSRSServiceParser().getUrlString("http://services.runescape.com/m=itemdb_oldschool/api/catalogue/items.json?category=1&alpha=abyssal&page=1");
          new JSONObject(jsonString);
       }catch (IOException e){
           assertEquals(1,0);
       }catch (JSONException ex){
           assertEquals(1,0);
       }

    }
}
