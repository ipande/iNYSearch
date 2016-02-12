package com.ishan.nytimessearch.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.ishan.nytimessearch.fragments.DatePickerFragment;
import com.ishan.nytimessearch.model.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

import static com.ishan.nytimessearch.utils.Constants.API_KEY;
import static com.ishan.nytimessearch.utils.Constants.APP_NAME;
import static com.ishan.nytimessearch.utils.Constants.RESP_TYPE;
import static com.ishan.nytimessearch.utils.Constants.SEARCH_URL;

public class NYSearchActivity extends AppCompatActivity implements FilterSearchFragment.FiltersEditedDialogListener{
    GridView gvSearchResults;

    ArrayList<Article> articles;
    private String searchQuery;
    ArticleArrayAdapter articleArrayAdapter;
    private RequestParams params = new RequestParams();
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nysearch);
        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);
        gvSearchResults = (GridView) findViewById(R.id.gvsearchresults);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

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

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchSearchResults(searchQuery);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    private void fetchSearchResults(String query) {
        searchQuery = query;
        Log.d(APP_NAME,"query: "+searchQuery);
        if(query!=null) {
            AsyncHttpClient client = new AsyncHttpClient();
            String queryURL = SEARCH_URL + RESP_TYPE;
            //params = new RequestParams();
            params.put("api-key", API_KEY);
            params.put("page", 0);
            params.put("q", query);
            if (isOnline() && isNetworkAvailable()) {

                client.get(queryURL, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d(APP_NAME, "response: " + response);
                        JSONArray articleJSONResults = null;
                        try {

                            articleArrayAdapter.clear();

                            articleJSONResults = response.getJSONObject("response").getJSONArray("docs");

                            articleArrayAdapter.addAll(Article.fromJSONArray(articleJSONResults));
                            swipeContainer.setRefreshing(false);

                            Log.d(APP_NAME, "articles: " + articles.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d(APP_NAME, "Failed in fetching articles " + throwable.getMessage());
                        swipeContainer.setRefreshing(false);
                        Toast.makeText(getApplicationContext(),"Something went wrong while fetching articles ",Toast.LENGTH_SHORT);
                    }
                });
            }
            else {
                Log.d(APP_NAME, "No internet connection ");
                Toast.makeText(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(NYSearchActivity.this, "Please enter a valid search string", Toast.LENGTH_SHORT).show();
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                showFilterDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance("Pick Date");
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterSearchFragment fragment = FilterSearchFragment.newInstance("Filter Search Options");
        fragment.show(fm,"Search Options");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_nysearch, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        MenuItem settingsItem = menu.findItem(R.id.action_settings);




        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                swipeContainer.setRefreshing(true);
                fetchSearchResults(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void onDatePickerClicked(View view) {
        if(view.getId() == R.id.etDate){
            Log.d(APP_NAME,"Launch Date Picker Dialogue");
            showDatePickerDialog();
        }
    }

    @Override
    public void onFinishFilters(String sortedOrder, boolean artChecked, boolean fnsCheckBoxChecked, boolean sportsCheckBoxChecked, String begDate) {
        Log.d(APP_NAME,"sorted order:"+sortedOrder);
        if(sortedOrder!=null){
            params.put("sort",sortedOrder);
        }
        if(begDate!=null){
            params.put("begin_date",begDate);

        }
        HashMap<String,String> hashMap = new HashMap<>();
        if(artChecked){
            hashMap.put("news_desk","Arts");
        }
        if(fnsCheckBoxChecked){
            hashMap.put("news_desk",hashMap.get("news_desk") + "&" + "Fashion");
        }
        if(sportsCheckBoxChecked){
            hashMap.put("news_desk",hashMap.get("news_desk") + "&" + "Sports");
        }
        if(hashMap.size()!=0)
            params.put("news_desk",hashMap.get("news_desk"));
    }
}
