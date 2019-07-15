package com.ftipinfosol.bharath;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ColorwiseActivity extends AppCompatActivity {

    private String URL;
    private RequestParams params = new RequestParams();
    private AsyncHttpClient client = new AsyncHttpClient();

    RecyclerView recyclerView;
    private ColorwiseAdapter adapter;
    private List<JSONObject> detail_list = new ArrayList<>();
    private JSONObject detail_value;
    private int count = 0;
    private boolean completed = false;
    private boolean isLoading = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorwise);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Intent intent = getIntent();
        String oid =  intent.getStringExtra("day");
        try {
            JSONObject order = new JSONObject(oid);
            URL = getString(R.string.base_url) + getString(R.string.production_details_url);
            params.put("ID", order.getString("ID"));
            params.put("dat", order.getString("dat"));
//            +"/"+ order.getString("ID")+"?";
            toolbar.setTitle("Production Details : "+order.getString("StyleNo"));
        } catch (JSONException e) {
            e.printStackTrace();
            finish();
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        adapter = new ColorwiseAdapter(this, detail_list);
        recyclerView = findViewById(R.id.recycler_list);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        prepareData();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                detail_list.clear();
                adapter.notifyDataSetChanged();
                count = 0;
                isLoading = false;
                completed = false;
                prepareData();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareData() {
        if(completed||isLoading){return;}
        else
        { isLoading=true;}
        count=detail_list.size();
        params.put("skip", detail_list.size());
        client.addHeader("Accept", "application/json");
        client.get(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            detail_value = response.getJSONObject(i);
                            detail_list.add(detail_value);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if(response.length()==0)
                    {
                        completed = true;
                    }
                    isLoading=false;
                    mSwipeRefreshLayout.setRefreshing(false);
                    adapter.notifyItemRangeInserted(count, detail_list.size());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                e.printStackTrace();
            }
        });
    }


}
