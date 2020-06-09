package com.example.sportssos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.SharedElementCallback;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.LocationBias;
import com.google.android.libraries.places.api.model.LocationRestriction;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    int SELECTED, UNSELECTED;
    CardView card_type_coach, card_type_athlete;
    TextView txt_type_coach, txt_type_athlete;
    EditText register_input_loc;
    PlacesClient places_client;
    ArrayList<String> places;
    ListView location_suggestions;
    FusedLocationProviderClient location_provider;
    String[] permissions = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initializations
        card_type_athlete = findViewById(R.id.card_type_athlete);
        card_type_coach = findViewById(R.id.card_type_coach);
        txt_type_athlete = findViewById(R.id.text_type_athlete);
        txt_type_coach = findViewById(R.id.text_type_coach);
        register_input_loc = findViewById(R.id.register_input_loc);
        location_suggestions = findViewById(R.id.location_suggestions);
        SELECTED = getResources().getColor(R.color.Blue);
        UNSELECTED = getResources().getColor(R.color.Black);

        // Initialize places api
        if (!Places.isInitialized()) {
            Places.initialize(this, getResources().getString(R.string.api_key));
        }
        places_client = Places.createClient(this);

        // Set current location as default
        String current_location = getCurrentLocation();
        register_input_loc.setText(current_location);

        //Permissions
        this.requestPermissions(permissions, 0);

        // Set Listeners
        card_type_athlete.setOnClickListener(this);
        card_type_coach.setOnClickListener(this);
        register_input_loc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                                                                .setQuery(text.toString())
                                                                .build();

                places_client.findAutocompletePredictions(request)
                    .addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
                        @Override
                        public void onSuccess(FindAutocompletePredictionsResponse response) {
                            places = new ArrayList<>();

                            for(AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                                places.add(prediction.getFullText(null).toString());
                            }

                            ArrayAdapter<String> places_adapter =  new ArrayAdapter<>(getApplicationContext(), R.layout.basic_list_item, places);
                            location_suggestions.setAdapter(places_adapter);
                            location_suggestions.setVisibility(View.VISIBLE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Location Exception", e.getMessage());
                        }
                    });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        register_input_loc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                location_suggestions.setVisibility(View.GONE);
            }
        });
        location_suggestions.setOnItemClickListener(this);

    }

    private String getCurrentLocation() {

        String location = "Mumbai, Maharashtra, India";

        try {

            LocationManager location_manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

            Location current_location = location_manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(current_location != null) {
                Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = gcd.getFromLocation(current_location.getLatitude(), current_location.getLongitude(), 1);

                location = addresses.get(0).getAddressLine(0);
            }

        }
        catch (SecurityException e) {
            Log.d("Loc Security Exception", e.getMessage());
        }
        catch (Exception e) {
            Log.d("Location Exception", e.getMessage());
        }

        return location;
    }

    @Override
    public void onClick(View v) {

        if (v == findViewById(R.id.card_type_athlete)) {
            txt_type_athlete.setTextColor(SELECTED);
            txt_type_coach.setTextColor(UNSELECTED);
        }
        else if (v == findViewById(R.id.card_type_coach)) {
            txt_type_athlete.setTextColor(UNSELECTED);
            txt_type_coach.setTextColor(SELECTED);
        }
        else {

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent  == findViewById(R.id.location_suggestions)) {
            if (places != null && places.size() >= position) {
                register_input_loc.setText(places.get(position));
                location_suggestions.setVisibility(View.GONE);
            }
        }
    }
}
