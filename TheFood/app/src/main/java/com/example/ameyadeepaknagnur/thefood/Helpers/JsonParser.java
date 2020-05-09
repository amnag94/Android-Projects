package com.example.ameyadeepaknagnur.thefood.Helpers;

import com.example.ameyadeepaknagnur.thefood.BaseClasses.Ingredient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class JsonParser {

    public static String json_string;

    public static ArrayList<Ingredient> getIngredients() {

        ArrayList<Ingredient> ingredients = new ArrayList<>();

        if(json_string == null) {
            return null;
        }
        else {
            try {
                JSONObject ingreds_obj = new JSONObject(json_string);

                Iterator<String> ingreds = ingreds_obj.keys();

                while (ingreds.hasNext()) {
                    String ingred_name = ingreds.next();

                    JSONObject ingred_info = (JSONObject) ingreds_obj.get(ingred_name);

                    int id = ingred_info.getInt(Ingredient.ID);
                    String measure = ingred_info.getString(Ingredient.MEASURE);
                    double quantity = ingred_info.getDouble(Ingredient.QUANTITY);
                    Ingredient ingredient = new Ingredient(id, ingred_name, measure, quantity);

                    ingredients.add(ingredient);
                }

                return ingredients;
            }
            catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

    }

}
