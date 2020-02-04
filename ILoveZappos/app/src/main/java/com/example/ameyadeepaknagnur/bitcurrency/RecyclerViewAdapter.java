package com.example.ameyadeepaknagnur.bitcurrency;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter<E> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<E> list_order;
    String type;

    public RecyclerViewAdapter(Context context, ArrayList<E> list_order, String type) {
        this.context = context;
        this.list_order = list_order;
        this.type = type;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView col1_value, col2_value;

        public ViewHolder(View itemView) {

            super(itemView);

            col1_value = itemView.findViewById(R.id.col1_value);
            col2_value = itemView.findViewById(R.id.col2_value);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate view
        View recycle_view = LayoutInflater.from(context).inflate(R.layout.recycler_table_view, parent, false);

        recycle_view.setBackgroundColor(context.getResources().getColor(R.color.black));

        // Create a view holder from inflated view
        ViewHolder viewHolder = new ViewHolder(recycle_view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        String value, amount;

        // Print header
        if (position == 0) {
            value = type + "        ";
            amount = "Amount";
        }

        else {
            String list_value = String.valueOf(list_order.get(position - 1));

            // This string is of the form : "Value : 9897.4 Amount : 0.232323"

            value = list_value.substring(list_value.indexOf(':') + 2, list_value.indexOf('A') - 1);
            amount = list_value.substring(list_value.lastIndexOf(" ") + 1, list_value.length());

            while (value.length() < 7) {
                value += " ";
            }
        }

        int color;

        if (type.equals("Ask")) {
            color = context.getResources().getColor(R.color.MediumPurple);
        }
        else {
            color = context.getResources().getColor(R.color.Gold);
        }

        ((ViewHolder) viewHolder).col1_value.setText(value);

        ((ViewHolder) viewHolder).col1_value.setTextColor(color);

        ((ViewHolder) viewHolder).col2_value.setText(amount);

        ((ViewHolder) viewHolder).col2_value.setTextColor(context.getResources().getColor(R.color.White));

        //((ViewHolder) viewHolder).col1_value.setBackgroundColor(context.getResources().getColor(R.color.black));

    }

    @Override
    public int getItemCount() {
        return list_order.size();
    }

}
