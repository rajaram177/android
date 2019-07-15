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

public class DetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<JSONObject> detail;

    public DetailsAdapter(List<JSONObject> detail) {
        this.detail = detail;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView a_name, order_qty, received_qty, balance_qty, unit;

        MyViewHolder(View view) {
            super(view);
            a_name = view.findViewById(R.id.a_name);
            order_qty = view.findViewById(R.id.order_qty);
            received_qty = view.findViewById(R.id.received_qty);
            balance_qty = view.findViewById(R.id.balance_qty);
            unit = view.findViewById(R.id.unit);
        }

        void bind(final JSONObject pay) {
            try {
                a_name.setText(pay.getString("AName"));
                order_qty.setText(pay.getString("Qty"));
                received_qty.setText(pay.getString("RQty"));
                balance_qty.setText(pay.getString("Bal_Qty"));
                unit.setText(pay.getString("Unit"));
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_detail_card, null);
        return new DetailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        DetailsAdapter.MyViewHolder holder = (DetailsAdapter.MyViewHolder) viewHolder;
        holder.bind(detail.get(i));
    }

    @Override
    public int getItemCount() {
        return detail.size();
    }
}
