package com.ftipinfosol.bharath;
import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<JSONObject> order;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(JSONObject oid);
    }

    public OrdersAdapter(List<JSONObject> order, OnItemClickListener listener) {
        this.order = order;
        this.listener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date, party_name, order_no, order_qty, received_qty, balance_qty, status;

        MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            party_name = view.findViewById(R.id.party_name);
            order_no = view.findViewById(R.id.order_no);
            order_qty = view.findViewById(R.id.order_qty);
            received_qty = view.findViewById(R.id.received_qty);
            balance_qty = view.findViewById(R.id.balance_qty);
            status = view.findViewById(R.id.status);

        }

        void bind(final JSONObject ord, final OnItemClickListener listener) {
            try {
                date.setText(ord.getString("dat"));
                party_name.setText(ord.getString("CName"));
                order_no.setText(ord.getString("ONo"));
                order_qty.setText(ord.getString("Qty"));
                received_qty.setText(ord.getString("RQty"));
                balance_qty.setText(ord.getString("Bal_Qty"));
                status.setText(ord.getString("Status"));
            }catch (JSONException e) {
                e.printStackTrace();
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                        listener.onItemClick(ord);
                }
            });

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_card, null);
        return new OrdersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        OrdersAdapter.MyViewHolder holder = (OrdersAdapter.MyViewHolder) viewHolder;
        holder.bind(order.get(i),listener);
    }

    @Override
    public int getItemCount() {
        return order.size();
    }
}
