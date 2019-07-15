package com.ftipinfosol.bharath;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private String URL;
    private RequestParams params = new RequestParams();
    private AsyncHttpClient client = new AsyncHttpClient();

    RecyclerView recyclerView;
    private OrdersAdapter adapter;
    private List<JSONObject> order_list = new ArrayList<>();
    private JSONObject order_value;
    private int count = 0;
    private boolean completed = false;
    private boolean isLoading = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Orders");
        setSupportActionBar(toolbar);

        URL = getString(R.string.base_url) + getString(R.string.orders_url);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        adapter = new OrdersAdapter(order_list, new OrdersAdapter.OnItemClickListener() {
            @Override public void onItemClick(JSONObject oid) {
                startActivity(new Intent(getApplicationContext(), DetailsActivity.class).putExtra("oid", String.valueOf(oid)));
            }
        });
        recyclerView = findViewById(R.id.recycler_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        prepareData();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                order_list.clear();
                adapter.notifyDataSetChanged();
                count = 0;
                isLoading = false;
                completed = false;
                prepareData();
            }
        });
    }

    private void prepareData() {
        if(completed||isLoading){return;}
        else
        { isLoading=true;}
        count=order_list.size();
        params.put("skip", order_list.size());
        client.addHeader("Accept", "application/json");

        client.get(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            order_value = response.getJSONObject(i);
                            order_list.add(order_value);
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
                    adapter.notifyItemRangeInserted(count, order_list.size());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                e.printStackTrace();
            }
        });
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:

                return true;
             default:

                return super.onOptionsItemSelected(item);

        }
    }


}
