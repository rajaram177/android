package com.ftipinfosol.bharath;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.Paths;
import java.util.List;

public class ProductionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<JSONObject> detail;
    private final ProductionAdapter.OnItemClickListener listener;
    Context context;

    public interface OnItemClickListener {
        void onItemClick(JSONObject oid);
    }

    public ProductionAdapter(Context context, List<JSONObject> detail, ProductionAdapter.OnItemClickListener listener) {
        this.detail = detail;
        this.listener = listener;
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date, style_no, packing_qty, ironing_qty;

        MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            style_no = view.findViewById(R.id.style_no);
            packing_qty = view.findViewById(R.id.packing_qty);
            ironing_qty = view.findViewById(R.id.ironing_qty);
        }

        void bind(final JSONObject pay,  final ProductionAdapter.OnItemClickListener listener) {
            Resources res = context.getResources();
            try {
                date.setText(pay.getString("dat"));
                style_no.setText(pay.getString("StyleNo"));
                packing_qty.setText(res.getString(R.string.packing, pay.getString("packing")));
                ironing_qty.setText(res.getString(R.string.ironing, pay.getString("iorning")));
            }catch (JSONException e) {
                e.printStackTrace();
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(pay);
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.production_card, null);
        return new ProductionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ProductionAdapter.MyViewHolder holder = (ProductionAdapter.MyViewHolder) viewHolder;
        holder.bind(detail.get(i),listener);
    }

    @Override
    public int getItemCount() {
        return detail.size();
    }
}