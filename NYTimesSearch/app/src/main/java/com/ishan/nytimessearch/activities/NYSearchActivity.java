package com.ishan.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
import android.widget.DatePicker;
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
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

import static com.ishan.nytimessearch.utils.Constants.API_KEY;
import static com.ishan.nytimessearch.utils.Constants.APP_NAME;
import static com.ishan.nytimessearch.utils.Constants.RESP_TYPE;
import static com.ishan.nytimessearch.utils.Constants.SEARCH_URL;

public class NYSearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
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
        DatePickerFragment newFragment = new DatePickerFragment();
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
                Log.d(APP_NAME,"query: "+query);
                if(query!=null) {
                    AsyncHttpClient client = new AsyncHttpClient();
                    String queryURL = SEARCH_URL + RESP_TYPE;
                    RequestParams params = new RequestParams();
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

                                    articleJSONResults = response.getJSONObject("response").getJSONArray("docs");

                                    articleArrayAdapter.addAll(Article.fromJSONArray(articleJSONResults));

                                    //articleArrayAdapter.notifyDataSetChanged();

                                    Log.d(APP_NAME, "articles: " + articles.toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                Log.d(APP_NAME, "Failed in fetching articles " + throwable.getMessage());
                                Toast.makeText(getApplicationContext(),"Something went wrong while fetching articles ",Toast.LENGTH_SHORT);

                            }
                        });
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Please check your internet connection!",Toast.LENGTH_SHORT);
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

    public void onDatePickerClicked(View view) {
        if(view.getId() == R.id.etDate){
            Log.d(APP_NAME,"Launch Date Picker Dialogue");
            //showDatePickerDialog();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Log.d(APP_NAME,"Date set");

    }
}
