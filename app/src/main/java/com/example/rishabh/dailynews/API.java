package com.example.rishabh.dailynews;

/**
 * Created by rishabh on 10/7/17.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;


public class API extends AppCompatActivity {

    ListView list;
    ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> NewsList=new ArrayList<>();
    private static final String TAG_Title="title";
    private static final String TAG_Desc = "description";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list=(ListView)findViewById(R.id.list);
        // Kick off an {@link AsyncTask} to perform the network request
        NewsAsyncTask task = new NewsAsyncTask();
        task.execute();
    }




    private class NewsAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            InputStream inputStream = null;
            String result = null;
            try {
                URL url = new URL("https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=162d0b5797e94abb8327a5f6803ee767");
                URLConnection conn = url.openConnection();
                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString();
            } catch (Exception e) {
                // Oops
            }
            finally {
                try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
            }
            return result;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(API.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected void onPostExecute(String result) {
            if (pDialog.isShowing())
                pDialog.dismiss();


            extractFeatureFromJson(result);
        }


        private void extractFeatureFromJson(String earthquakeJSON) {
            try {
                JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
                JSONArray featureArray = baseJsonResponse.getJSONArray("articles");


                // If there are results in the features array
                if (featureArray.length() > 0) {

                    JSONObject firstFeature = featureArray.getJSONObject(0);

                    String title = firstFeature.getString("title");
                    String description = firstFeature.getString("description");

                    HashMap<String,String> persons = new HashMap<String,String>();
                    persons.put(TAG_Title,title);
                    persons.put(TAG_Desc,description);

                    NewsList.add(persons);
                    Log.d("Preonlist detail", String.valueOf(NewsList));

                }

                list.setAdapter(new Adapterr());

            } catch (JSONException e) {

            }
        }


        private class Adapterr extends BaseAdapter {
            LayoutInflater inflater;

            Adapterr() {
                inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public int getCount() {
                return NewsList.size();
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = inflater.inflate(R.layout.apisingle, null);
                TextView t1 = (TextView) v.findViewById(R.id.title);
                TextView t2 = (TextView) v.findViewById(R.id.desc);



                HashMap<String, String> finalPersons = new HashMap<>();
                finalPersons = NewsList.get(position);

                t1.setText(finalPersons.get(TAG_Title));
                t2.setText(finalPersons.get(TAG_Desc));

                final HashMap<String, String> finalPersons1 = finalPersons;
                v.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v)
    {
        Intent intent=new Intent(getApplicationContext(),WorldNews.class);
        intent.putExtra("Mydata", finalPersons1);

    }
});
                return v;
            }
        }


    }

}
