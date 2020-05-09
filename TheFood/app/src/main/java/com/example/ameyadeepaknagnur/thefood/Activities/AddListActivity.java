package com.example.ameyadeepaknagnur.thefood.Activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ameyadeepaknagnur.thefood.BaseClasses.Ingredient;
import com.example.ameyadeepaknagnur.thefood.Helpers.ApiInfo;
import com.example.ameyadeepaknagnur.thefood.Helpers.Constants;
import com.example.ameyadeepaknagnur.thefood.Helpers.JsonParser;
import com.example.ameyadeepaknagnur.thefood.R;
import com.example.ameyadeepaknagnur.thefood.Session.SessionInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class AddListActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton add_list_item;
    ListView items_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        // Initializations
        add_list_item = findViewById(R.id.add_list_item);
        items_list = findViewById(R.id.list_of_lists);

        //initializeList();
        getAllIngredients();

        // Listeners
        add_list_item.setOnClickListener(this);
    }

    private void initializeList() {
        ArrayList<String> foods =  new ArrayList<>();
        for(int group_index = 0; group_index < 5; group_index++) {
            foods.add("Food 1");
            foods.add("Food 2");
            foods.add("Food 3");
        }
        items_list.setAdapter(new ArrayAdapter<>(this, R.layout.list_items, foods));
    }

    private void addToList() {
        float width = items_list.getLayoutParams().width;
        float height = items_list.getLayoutParams().height + getResources().getDimension(R.dimen.option_icon_size);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)width, (int)height);
        params.addRule(RelativeLayout.BELOW, R.id.layout_name_list);

        items_list.setLayoutParams(params);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_list_item :
                addToList();
                break;
            default:
                Log.d(Constants.DEFAULT_CLICK_TAG, Constants.DEFAULT_CLICK_MSG);
        }
    }

    private void getAllIngredients() {

        IngredientsTask ingredients_task = new IngredientsTask();
        ingredients_task.execute();

    }

    private class IngredientsTask extends AsyncTask<Void, Void, ArrayList<Ingredient>> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected ArrayList<Ingredient> doInBackground(Void... voids) {

            BufferedReader reader = null;

            try {

                URL connection_url = new URL(ApiInfo.url + "info/");
                HttpURLConnection http_connection = (HttpURLConnection) connection_url.openConnection();

                http_connection.setRequestMethod("GET");
                //http_connection.setRequestProperty();

                int response_code = http_connection.getResponseCode();

                if(response_code == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(http_connection.getInputStream()));

                    JsonParser.json_string = reader.readLine();
                    return JsonParser.getIngredients();
                }
                else {
                    Log.d("IngredientsTask", ApiInfo.NO_RESPONSE);
                    return null;
                }

            }
            catch (Exception e) {

                Log.d("IngredientsTask",  e.getMessage());
                e.printStackTrace();
                return null;

            }
            finally {
                if(reader != null) {
                    try {
                        reader.close();
                    }
                    catch (IOException e) {
                        Log.d("IngredientsTask", e.getMessage());
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Ingredient> ingredients) {
            if (ingredients != null) {
                ArrayList<String> ingred_names = new ArrayList<>();

                for(Ingredient ingredient : ingredients) {
                    ingred_names.add(ingredient.name);
                }

                items_list.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.list_items, ingred_names));
            }
        }

    }

}
