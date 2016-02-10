package com.ishan.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ishan.nytimessearch.R;
import com.ishan.nytimessearch.adapters.ArticleArrayAdapter;
import com.ishan.nytimessearch.model.Article;
import com.ishan.nytimessearch.utils.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class NYSearchActivity extends AppCompatActivity {
    GridView gvSearchResults;

    ArrayList<Article> articles;

    ArticleArrayAdapter articleArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nysearch);
        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);
        gvSearchResults = (GridView) findViewById(R.id.gvsearchresults);
        articles = new ArrayList<>();

        articleArrayAdapter = new ArticleArrayAdapter(this,articles);
        gvSearchResults.setAdapter(articleArrayAdapter);

        gvSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // create an intent to display the article
                Intent displayArticleIntent = new Intent(getApplicationContext(),ArticleActivity.class);

                // get the article to display
                Article article = articles.get(position);

                // pass in that article into the intent
                displayArticleIntent.putExtra("article",article);

                // launch the activity
                startActivity(displayArticleIntent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_nysearch, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(Constants.APP_NAME,"query: "+query);
                if(query!=null){
                    AsyncHttpClient client = new AsyncHttpClient();
                    String queryURL = Constants.SEARCH_URL + Constants.RESP_TYPE;
                    RequestParams params = new RequestParams();
                    params.put("api-key","ffcb95b28db4e3f1dd90bef0879667a3:17:74338942");
                    params.put("page",0);
                    params.put("q",query);

                    client.get(queryURL,params,new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(Constants.APP_NAME,"response: "+response);
                            JSONArray articleJSONResults = null;
                            try {

                                articleJSONResults = response.getJSONObject("response").getJSONArray("docs");

                                articleArrayAdapter.addAll(Article.fromJSONArray(articleJSONResults));

                                //articleArrayAdapter.notifyDataSetChanged();

                                Log.d(Constants.APP_NAME, "articles: " + articles.toString());

                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else
                    Toast.makeText(NYSearchActivity.this, "Please enter a valid search string", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
