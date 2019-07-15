package com.ftipinfosol.bharath;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class DetailsActivity extends AppCompatActivity {

    private String URL, TODAY_URL;
    private RequestParams params = new RequestParams();
    private AsyncHttpClient client = new AsyncHttpClient();

    RecyclerView recyclerView;
    private DetailsAdapter adapter;
    private List<JSONObject> detail_list = new ArrayList<>();
    private JSONObject detail_value, day, order;
    private int count = 0;
    private boolean completed = false;
    private boolean isLoading = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    TextView date, style_no, packing_qty, packing_balance, ironing_qty, ironing_balance;
    CardView today_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Intent intent = getIntent();
        String oid =  intent.getStringExtra("oid");
        try {
            order = new JSONObject(oid);
            URL = getString(R.string.base_url) + getString(R.string.orders_url) +"/"+ order.getString("OID");
            TODAY_URL = getString(R.string.base_url) + getString(R.string.today_production_url) +"/"+ order.getString("OID");
            toolbar.setTitle("Order Details : "+order.getString("ONo"));
        } catch (JSONException e) {
            e.printStackTrace();
            finish();
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        date = findViewById(R.id.date);
        style_no = findViewById(R.id.style_no);
        packing_qty = findViewById(R.id.packing_qty);
        packing_balance = findViewById(R.id.packing_balance);
        ironing_qty = findViewById(R.id.ironing_qty);
        ironing_balance = findViewById(R.id.ironing_balance);
        today_card = findViewById(R.id.today_card);
        today();

        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        adapter = new DetailsAdapter(detail_list);
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

    private void today() {

        client.addHeader("Accept", "application/json");
        client.get(TODAY_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response != null) {
                    day = response;
                    today_card.setVisibility(View.VISIBLE);
                    Resources res = getResources();
                    try {
                        date.setText(response.getString("dat"));
                        style_no.setText(response.getString("StyleNo"));
                        packing_qty.setText(res.getString(R.string.packing, response.getString("packing")));
                        packing_balance.setText(res.getString(R.string.balance, response.getString("remaing_packing_qty")));
                        ironing_qty.setText(res.getString(R.string.ironing, response.getString("iorning")));
                        ironing_balance.setText(res.getString(R.string.balance, response.getString("remaing_iorn_qty")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                e.printStackTrace();
            }
        });
    }

    public void getTodayProduction(View view) {
        startActivity(new Intent(getApplicationContext(), ColorwiseActivity.class).putExtra("day", String.valueOf(day)));
    }

    public void getProduction(View view) {
        startActivity(new Intent(getApplicationContext(), ProductionActivity.class).putExtra("order", String.valueOf(order)));
    }
}
