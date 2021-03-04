package com.example.andoid.probe;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /**
     * Adapter for the list of earthquakes
     */
    private CartiAdapter mAdapter;
    private TextView mEmptyStateTextView;

    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * Sample JSON response for a USGS query
     */
    private static final String SAMPLE_JSON_RESPONSE = "https://content.guardianapis.com/search?order-by=newest&show-fields=all&page-size=30&api-key= ENTER API KEY HERE";

    private ListView cartiListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cartiListView = (ListView) findViewById(R.id.list);
        cartiListView.setTextFilterEnabled(true);
        mAdapter = new CartiAdapter(this, new ArrayList<Carti>());
        cartiListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        cartiListView.setEmptyView(mEmptyStateTextView);

        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(SAMPLE_JSON_RESPONSE);

        cartiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Carti currentCarti = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri cartiUri = Uri.parse(currentCarti.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, cartiUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

    }

    /**
     * Make search menu
     **/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Initialize menu inflater
        MenuInflater menuInflater = getMenuInflater();
        // Inflate menu
        menuInflater.inflate(R.menu.menu_search, menu);
        // Initialize menu item
        MenuItem menuItem = menu.findItem(R.id.search_view);
        // Initialize Search view
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Carti> listaNouadeCarti = new ArrayList<>();
                for (Carti item : mAdapter.getItems()) {
                    item.getTitle().trim().toLowerCase().contains(query);
                    listaNouadeCarti.add(item);
                }

                if (!listaNouadeCarti.isEmpty()) {
                    mAdapter = new CartiAdapter(MainActivity.this, listaNouadeCarti);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    cartiListView.clearTextFilter();
                } else {
                    cartiListView.setFilterText(newText.trim());
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Carti>> {

        @Override
        protected List<Carti> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Carti> result = Utils.fetchEarthquakeData(urls[0]);
            return result;

        }

        @Override
        protected void onPostExecute(List<Carti> data) {
            mAdapter.clear();
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.GONE);
            } else {
                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.GONE);
                mEmptyStateTextView.setText(R.string.no_books);
            }
        }
    }
}