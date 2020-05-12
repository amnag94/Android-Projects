package com.example.ameyadeepaknagnur.thefood.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.ameyadeepaknagnur.thefood.BaseClasses.Ingredient;
import com.example.ameyadeepaknagnur.thefood.R;

import java.util.ArrayList;

public class IngredientsAdapter implements ListAdapter {

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0) {
            return false;
        }
        return true;
    }

    private class ViewHandler {
        EditText name, measure, quantity;

        public ViewHandler(View view) {
            name = view.findViewById(R.id.new_item_name);
            measure = view.findViewById(R.id.new_item_measure);
            quantity = view.findViewById(R.id.new_item_quantity);
        }
    }

    public ArrayList<Ingredient> ingredients;
    Context context;
    int layout = R.layout.list_items;

    public IngredientsAdapter(Context context, int layout, ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public Object getItem(int position) {
        return ingredients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        try {
            view = LayoutInflater.from(context).inflate(layout, null);

            final ViewHandler view_handler = new ViewHandler(view);

            view_handler.name.setText(ingredients.get(position).name);
            view_handler.measure.setText(ingredients.get(position).measure);
            view_handler.quantity.setText(String.valueOf(ingredients.get(position).quantity));

            view_handler.name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        ingredients.get(position).name = view_handler.name.getText().toString();
                    }
                    catch (Exception e) {
                        Toast.makeText(context, "Invalid Format", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            view_handler.measure.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        ingredients.get(position).measure = view_handler.measure.getText().toString();
                    }
                    catch (Exception e) {
                        Toast.makeText(context, "Invalid Format", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            view_handler.quantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        ingredients.get(position).quantity = Double.parseDouble(view_handler.quantity.getText().toString());
                    }
                    catch (Exception e) {
                        Toast.makeText(context, "Invalid Format", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
