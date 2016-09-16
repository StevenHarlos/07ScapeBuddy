package com.bignerdranch.android.a07scapebuddy;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by RedSodeeePop on 2016-08-15.
 */
public class OSRSServiceParser {
    private static final String TAG = "ServiceParser";
    private static final String BASE_URL_PROFILE = "http://services.runescape.com/m=hiscore_oldschool";
    private static final String STANDARD_METHOD = "";
    private static final String IRONMAN_METHOD = "_ironman";
    private static final String ULTIMATE_METHOD = "_ultimate";
    private static final String ENDPOINT = "/index_lite.ws?player=";

    private static final String BASE_URL_GRAND_EXCHANGE = "http://services.runescape.com/m=itemdb_oldschool/api/catalogue/items.json?category=1&alpha=";
    private static final String ENDPOINT_GRAND_EXCHANGE = "&page=1";

    private static final String BASE_URL_ITEM_GRAPH = "http://services.runescape.com/m=itemdb_oldschool/api/graph/";
    private static final String ENDPOINT_ITEM_GRAPH = ".json";

    private static final String BASE_URL_SINGLE_ITEM = "http://services.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json?item=";


    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<Skill> fetchProfile(String displayName, String method)  {
        List<Skill> skills = new ArrayList<>();
        String url = BASE_URL_PROFILE;

        if (method.equals("standard")) {
            url = url + STANDARD_METHOD;
        } else if (method.equals("ironman")) {
            url = url + IRONMAN_METHOD;
        } else if (method.equals("ultimate")) {
            url = url + ULTIMATE_METHOD;
        }

        url = url + ENDPOINT + displayName;


            try{
                String jsonString = getUrlString(url);
                parseSkills(skills, jsonString);
            }catch (IOException ioe){
                return new ArrayList<>();
            }



        return skills;
    }

    private void parseSkills(List<Skill> skills, String csvString) throws IOException {
        String[] skillVals = csvString.split("\\s");
        for (int count = 0; count < 24; count++) {
            String[] skill = skillVals[count].split(",");
            skills.add(new Skill(Integer.parseInt(skill[0]), Integer.parseInt(skill[1]), Integer.parseInt(skill[2])));
        }
    }

    public List<Item> fetchItems(String search) {
        String url = BASE_URL_GRAND_EXCHANGE + search + ENDPOINT_GRAND_EXCHANGE;
        List<Item> items = new ArrayList<>();

        System.out.println(url);
        try {
            String jsonString = getUrlString(url);

            JSONObject jsonBody = new JSONObject(jsonString);
            System.out.println(jsonBody.getClass());
            parseItems(items, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            System.out.println("Caught jsonException");
        } catch (NullPointerException e) {
            System.out.println("caught fucking njullpointer");
        }

        return items;

    }

    private void parseItems(List<Item> itemList, JSONObject jsonBody) throws IOException, JSONException {
        JSONArray itemJsonArray = jsonBody.getJSONArray("items");

        for (int i = 0; i < itemJsonArray.length(); i++) {
            Item item = new Item();
            JSONObject itemJsonObject = itemJsonArray.getJSONObject(i);

            item.setmName(itemJsonObject.getString("name"));
            item.setmID(itemJsonObject.getInt("id"));
            item.setmDescription(itemJsonObject.getString("description"));
            item.setmIconUrl(itemJsonObject.getString("icon"));
            item.setmPrice(itemJsonObject.getJSONObject("current").getString("price"));
            item.setmTrendType(itemJsonObject.getJSONObject("today").getString("trend"));
            item.setmTrendPrice(itemJsonObject.getJSONObject("today").getString("price"));

            itemList.add(item);

        }
    }

    public void fetchItemData(Item item) {

        String urlItemData = BASE_URL_SINGLE_ITEM + Integer.toString(item.getmID());
        String urlGraphData = BASE_URL_ITEM_GRAPH + Integer.toString(item.getmID()) + ENDPOINT_ITEM_GRAPH;

        System.out.println(urlItemData);
        System.out.println(urlGraphData);

        try {
            String jsonStringItemData = getUrlString(urlItemData);

            String jsonStringGraphData = getUrlString(urlGraphData);

            JSONObject jsonBodyItemData = new JSONObject(jsonStringItemData);
            JSONObject jsonBodyGraphData = new JSONObject(jsonStringGraphData);

            parseItemData(jsonBodyItemData, item);
            parseGraphData(jsonBodyGraphData, item);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            System.out.println("Caught jsonException");
        } catch (NullPointerException e) {
            System.out.println("caught fucking njullpointer");
        }
    }

    private void parseItemData(JSONObject jsonItemData, Item item) throws IOException, JSONException {

        JSONObject jsonObject = jsonItemData.getJSONObject("item");

        item.setmTrend30DayType(jsonObject.getJSONObject("day30").getString("trend"));
        item.setmTrend30DayPrice(jsonObject.getJSONObject("day30").getString("change"));
        item.setmTrend90DayType(jsonObject.getJSONObject("day90").getString("trend"));
        item.setmTrend90DayPrice(jsonObject.getJSONObject("day90").getString("change"));
        item.setmTrend180DayType(jsonObject.getJSONObject("day180").getString("trend"));
        item.setmTrend180DayPrice(jsonObject.getJSONObject("day180").getString("change"));

    }

    private void parseGraphData(JSONObject jsonGraphData, Item item) throws IOException, JSONException {

        List<Date> dates = new ArrayList<>();
        List<Integer> prices = new ArrayList<>();

        JSONObject jsonAverageGraphData = jsonGraphData.getJSONObject("average");

        JSONArray jsonDatesArray = jsonAverageGraphData.names();
        JSONArray jsonPricesArray = jsonAverageGraphData.toJSONArray(jsonDatesArray);

        for (int i = 0; i < jsonDatesArray.length(); i++) {
            Date date = new Date(jsonDatesArray.getLong(i));
            dates.add(date);

        }
        for (int i = 0; i < jsonPricesArray.length(); i++) {
            prices.add(jsonPricesArray.getInt(i));
        }

        item.setmGraphDates(dates);
        item.setmGraphPrices(prices);


    }

}



