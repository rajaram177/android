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

import java.util.List;

public class ColorwiseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<JSONObject> detail;
    Context context;

    public ColorwiseAdapter(Context context, List<JSONObject> detail) {
        this.detail = detail;
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date, color, packing_qty, ironing_qty;

        MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            color = view.findViewById(R.id.color);
            packing_qty = view.findViewById(R.id.packing_qty);
            ironing_qty = view.findViewById(R.id.ironing_qty);
        }

        void bind(final JSONObject pay) {
            Resources res = context.getResources();
            try {
                date.setText(pay.getString("dat"));
                color.setText(pay.getString("Color"));
                packing_qty.setText(res.getString(R.string.packing, pay.getString("packing")));
                ironing_qty.setText(res.getString(R.string.ironing, pay.getString("iorning")));
            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.colorwise_card, null);
        return new ColorwiseAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ColorwiseAdapter.MyViewHolder holder = (ColorwiseAdapter.MyViewHolder) viewHolder;
        holder.bind(detail.get(i));
    }

    @Override
    public int getItemCount() {
        return detail.size();
    }
}
