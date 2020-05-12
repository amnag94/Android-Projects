package com.example.ameyadeepaknagnur.thefood.Activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ameyadeepaknagnur.thefood.Adapters.IngredientsAdapter;
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

    ImageButton add_list_item, refresh_list, save_list;
    ListView items_list;
    EditText name_edittxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        // Initializations
        add_list_item = findViewById(R.id.add_list_item);
        refresh_list = findViewById(R.id.refresh_list);
        save_list = findViewById(R.id.save_list);
        items_list = findViewById(R.id.list_of_lists);
        name_edittxt = findViewById(R.id.list_name);

        //initializeList();
        getAllIngredients();

        // Listeners
        add_list_item.setOnClickListener(this);
        refresh_list.setOnClickListener(this);
        save_list.setOnClickListener(this);

        dismissKeyboard(name_edittxt);
    }

    private void dismissKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_list_item :
                addToList();
                break;
            case R.id.refresh_list :
                refreshList();
                break;
            case R.id.save_list :
                save_list.requestFocus();
                saveList();
                break;
            default:
                Log.d(Constants.DEFAULT_CLICK_TAG, Constants.DEFAULT_CLICK_MSG);
        }
    }

    private void modifyListSize() {
        // Show another item in list
        float width = items_list.getLayoutParams().width;
        float height = items_list.getLayoutParams().height + getResources().getDimension(R.dimen.basic_height_item);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)width, (int)height);
        params.addRule(RelativeLayout.BELOW, R.id.header_layout);

        items_list.setLayoutParams(params);
    }

    private void addToList() {
        // Adding default new item to the list
        IngredientsAdapter list_adapter = (IngredientsAdapter) items_list.getAdapter();

        Ingredient new_item = new Ingredient(list_adapter.ingredients.size() + 1, Ingredient.DEF_NAME, Ingredient.DEF_MEASURE, Ingredient.DEF_QUANTITY);
        list_adapter.ingredients.add(new_item);

        items_list.setAdapter(list_adapter);

        modifyListSize();
    }

    private void saveList() {
        IngredientsAdapter list_adapter = (IngredientsAdapter) items_list.getAdapter();

        if(list_adapter != null) {
            emptyIngredients();
            updateAllIngredients(list_adapter.ingredients);
        }
    }

    private void refreshList() {
        resetList();
        getAllIngredients();
        Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
    }

    private void resetList() {
        float width = items_list.getLayoutParams().width;
        float height = 0;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)width, (int)height);
        params.addRule(RelativeLayout.BELOW, R.id.header_layout);

        items_list.setLayoutParams(params);
    }

    private void getAllIngredients() {

        IngredientsTask ingredients_task = new IngredientsTask();
        ingredients_task.execute();

    }

    private void emptyIngredients() {

        IngredientsEmptyTask ingredients_empty = new IngredientsEmptyTask();
        ingredients_empty.execute();

    }

    private void updateAllIngredients(ArrayList<Ingredient> ingredients) {

        IngredientsUpdateTask ingredients_task = new IngredientsUpdateTask(ingredients);
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

                URL connection_url = new URL(ApiInfo.url + ApiInfo.url_ext_ingred);
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
                //ArrayList<String> ingred_names = new ArrayList<>();

                for(Ingredient ingredient : ingredients) {
                    modifyListSize();
                }

                try {
                    items_list.setAdapter(new IngredientsAdapter(getApplicationContext(), R.layout.list_items, ingredients));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private class IngredientsUpdateTask extends AsyncTask<Void, Void, String> {

        ArrayList<Ingredient> ingredients;

        IngredientsUpdateTask(ArrayList<Ingredient> ingredients) {
            this.ingredients = ingredients;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... voids) {

            BufferedReader reader = null;
            String success = ApiInfo.RESULT_SUCCESS;

            try {

                for(Ingredient ingredient : ingredients) {
                    URL connection_url = new URL(ApiInfo.url + ApiInfo.url_ext_ingred + "add/" + ingredient.name + "/" + ingredient.measure + "/" + ingredient.quantity);
                    HttpURLConnection http_connection = (HttpURLConnection) connection_url.openConnection();

                    http_connection.setRequestMethod("PUT");
                    //http_connection.setRequestProperty();

                    int response_code = http_connection.getResponseCode();

                    if (response_code == HttpURLConnection.HTTP_OK) {
                        reader = new BufferedReader(new InputStreamReader(http_connection.getInputStream()));

                        String result = reader.readLine();
                        if (!(result.contains(ApiInfo.RESULT_UPDATED) || result.contains(ApiInfo.RESULT_ADDED))) {
                            success = ApiInfo.RESULT_FAILED;
                        }
                    } else {
                        Log.d("IngredientsTask", ApiInfo.NO_RESPONSE);
                        return ApiInfo.RESULT_FAILED;
                    }
                }

                return success;
            }
            catch (Exception e) {

                Log.d("IngredientsTask",  e.getMessage());
                e.printStackTrace();
                return ApiInfo.RESULT_FAILED;

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
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Saving List : " + result, Toast.LENGTH_SHORT).show();
        }

    }

    private class IngredientsEmptyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... voids) {

            BufferedReader reader = null;

            try {

                URL connection_url = new URL(ApiInfo.url + ApiInfo.url_ext_ingred + "empty");
                HttpURLConnection http_connection = (HttpURLConnection) connection_url.openConnection();

                http_connection.setRequestMethod("PUT");
                //http_connection.setRequestProperty();

                int response_code = http_connection.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {

                    reader = new BufferedReader(new InputStreamReader(http_connection.getInputStream()));

                    String result = reader.readLine();
                    if (result.contains(ApiInfo.RESULT_EMPTIED)) {
                        return ApiInfo.RESULT_SUCCESS;
                    }
                    else {
                        return ApiInfo.RESULT_FAILED;
                    }

                }
                else {
                    Log.d("IngredientsTask", ApiInfo.NO_RESPONSE);
                    return ApiInfo.RESULT_FAILED;
                }

            }
            catch (Exception e) {

                Log.d("IngredientsTask",  e.getMessage());
                e.printStackTrace();
                return ApiInfo.RESULT_FAILED;

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
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(), "Empty List ... " + result, Toast.LENGTH_SHORT).show();
        }

    }

}
