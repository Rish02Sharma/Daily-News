package com.example.rishabh.dailynews.dummy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rishabh.dailynews.R;
import com.example.rishabh.dailynews.WebSports;
import com.squareup.picasso.Picasso;

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


public class SportsNews extends Fragment
{

    ListView list;
    ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> NewsList=new ArrayList<>();
    private static final String TAG_Title="title";
    private static final String TAG_Desc = "description";
    private static final String TAG_ImageURL = "urlToImage";
    public static final String TAG_URLwebview = "url";



    View v;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        v = inflater.inflate(R.layout.fragment_sports, container, false);
        list=(ListView)v.findViewById(R.id.listsports);
        SportsNews.NewsAsyncTask task = new SportsNews.NewsAsyncTask();
        task.execute();
        return v;

    }



    private class NewsAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            InputStream inputStream = null;
            String result = null;
            try {
                URL url = new URL("https://newsapi.org/v1/articles?source=bbc-sport&sortBy=top&apiKey=162d0b5797e94abb8327a5f6803ee767");
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
            pDialog = new ProgressDialog(getContext());
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


                for (int i=0; i<featureArray.length() ; i++) {

                    JSONObject firstFeature = featureArray.getJSONObject(i);

                    String title = firstFeature.getString("title");
                    String description = firstFeature.getString("description");
                    String url = firstFeature.getString("urlToImage");
                    String URLview = firstFeature.getString("url");

                    HashMap<String,String> persons = new HashMap<String,String>();
                    persons.put(TAG_Title,title);
                    persons.put(TAG_Desc,description);
                    persons.put(TAG_ImageURL,url);
                    persons.put(TAG_URLwebview,URLview);

                    NewsList.add(persons);
                    Log.d("Preonlist detail", String.valueOf(NewsList));

                }

                SportsNews.NewsAsyncTask.Adapterr adpt=new SportsNews.NewsAsyncTask.Adapterr(getActivity(),NewsList);
                list.setAdapter(adpt);

            } catch (JSONException e) {

            }
        }


        private class Adapterr extends BaseAdapter
        {
            LayoutInflater inflater;
            Context activity;
            ArrayList<HashMap<String, String>> news=new ArrayList<>();
            public Adapterr(FragmentActivity activity, ArrayList<HashMap<String, String>> newsList) {
                this.activity=activity;
                this.news=newsList;
            }


            @Override
            public int getCount() {
                return news.size();
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
            public View getView(int position, View v, ViewGroup parent) {
                if(inflater==null)
                    inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if(v==null)

                    v = inflater.inflate(R.layout.apisingle, null);
                TextView t1 = (TextView) v.findViewById(R.id.title);
                TextView t2 = (TextView) v.findViewById(R.id.desc);

                ImageView imageView = (ImageView) v.findViewById(R.id.image);
                if (imageView == null) {
                    imageView = new ImageView(getContext());
                }



                HashMap<String, String> finalPersons = new HashMap<>();
                finalPersons = NewsList.get(position);

                t1.setText(finalPersons.get(TAG_Title));
                t2.setText(finalPersons.get(TAG_Desc));

                String url = finalPersons.get(TAG_ImageURL);

                Picasso.with(getContext()).load(url).into(imageView);

                String weburl = finalPersons.get(TAG_URLwebview);

                final HashMap<String, String> finalPersons1 = finalPersons;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),WebSports.class);
                        intent.putExtra("Mywebdata", finalPersons1);
                        startActivity(intent);
                    }
                });


                return v;
            }
        }


    }



}