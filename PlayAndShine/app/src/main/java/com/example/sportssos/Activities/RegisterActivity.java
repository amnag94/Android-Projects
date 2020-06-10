package com.example.sportssos.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.sportssos.R;
import com.example.sportssos.Session.SessionInfo;
import com.example.sportssos.Session.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    int SELECTED, UNSELECTED;
    CardView card_type_coach, card_type_athlete, card_register;
    TextView txt_type_coach, txt_type_athlete, warning_txt_pass, warning_txt_reg;
    EditText register_input_loc, register_input_phone;
    EditText register_input_email, register_input_pass, register_input_conf;
    PlacesClient places_client;
    ArrayList<String> places;
    ListView location_suggestions;
    String[] permissions = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initializations
        card_type_athlete = findViewById(R.id.card_type_athlete);
        card_type_coach = findViewById(R.id.card_type_coach);
        card_register = findViewById(R.id.card_register);
        txt_type_athlete = findViewById(R.id.text_type_athlete);
        txt_type_coach = findViewById(R.id.text_type_coach);
        warning_txt_pass = findViewById(R.id.warning_txt_pass);
        warning_txt_reg = findViewById(R.id.warning_txt_reg);
        register_input_loc = findViewById(R.id.register_input_loc);
        register_input_email = findViewById(R.id.register_input_email);
        register_input_pass = findViewById(R.id.register_input_pass);
        register_input_conf = findViewById(R.id.register_input_conf);
        register_input_phone = findViewById(R.id.register_input_phone);
        location_suggestions = findViewById(R.id.location_suggestions);
        SELECTED = getResources().getColor(R.color.Blue, getTheme());
        UNSELECTED = getResources().getColor(R.color.Black, getTheme());

        // Default type
        txt_type_coach.setSelected(true);
        txt_type_coach.setTextColor(SELECTED);
        txt_type_athlete.setSelected(false);

        // If user logged in then redirect to dashboard
        if(SessionInfo.firebase_instance != null && SessionInfo.user != null) {
            Intent home = new Intent(this, DashboardActivity.class);
            startActivity(home);
        }

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
        card_register.setOnClickListener(this);
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

        String location = "";

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
            txt_type_athlete.setSelected(true);
            txt_type_athlete.setTextColor(SELECTED);
            txt_type_coach.setSelected(false);
            txt_type_coach.setTextColor(UNSELECTED);
        }
        else if (v == findViewById(R.id.card_type_coach)) {
            txt_type_athlete.setSelected(false);
            txt_type_athlete.setTextColor(UNSELECTED);
            txt_type_coach.setSelected(true);
            txt_type_coach.setTextColor(SELECTED);
        }
        else if (v == findViewById(R.id.card_register)) {
            User register_user = new User();
            register_user.email = register_input_email.getText().toString();
            register_user.password = register_input_pass.getText().toString();
            String conf_password = register_input_conf.getText().toString();

            register_user.type = txt_type_athlete.isSelected() ? "Athlete" : "Coach";
            register_user.phone = register_input_phone.getText().toString();
            register_user.location = register_input_loc.getText().toString();

            if(!register_user.password.equals(conf_password)) {
                warning_txt_pass.setVisibility(View.VISIBLE);
            }
            else {
                warning_txt_pass.setVisibility(View.GONE);
                validateCredentials(register_user);
            }
        }
        else {

        }

    }

    private void registerUser(final User user) {
        if (SessionInfo.firebase_instance != null) {
            SessionInfo.firebase_instance.createUserWithEmailAndPassword(user.email, user.password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SessionInfo.user = user;
                                signInUser(user);
                            }
                            else {
                                if (warning_txt_reg != null) {
                                    warning_txt_reg.setText(task.getException().getLocalizedMessage());
                                    warning_txt_reg.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (warning_txt_reg != null) {
                                warning_txt_reg.setText(e.getLocalizedMessage());
                                warning_txt_reg.setVisibility(View.VISIBLE);
                            }
                            //Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void signInUser(final User user) {
        if (SessionInfo.firebase_instance != null) {
            SessionInfo.firebase_instance.signInWithEmailAndPassword(user.email, user.password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SessionInfo.user = user;
                                Intent home = new Intent(getApplicationContext(), DashboardActivity.class);
                                startActivity(home);
                            }
                            else {
                                if (warning_txt_reg != null) {
                                    warning_txt_reg.setText(task.getException().getLocalizedMessage());
                                    warning_txt_reg.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (warning_txt_reg != null) {
                                warning_txt_reg.setText(e.getLocalizedMessage());
                                warning_txt_reg.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }

    private boolean validateCredentials(User user) {
        if (!user.email.matches(".+@.+\\..+")) {
            warning_txt_reg.setText("Invalid Email");
            warning_txt_reg.setVisibility(View.VISIBLE);
            return false;
        }
        if (!(user.password.length() >= 6)) {
            warning_txt_reg.setText("Password should be 6 or more characters");
            warning_txt_reg.setVisibility(View.VISIBLE);
            return false;
        }
        if (user.phone.length() < 10) {
            warning_txt_reg.setText("Invalid Phone Number");
            warning_txt_reg.setVisibility(View.VISIBLE);
            return false;
        }
        if (user.location.length() == 0) {
            warning_txt_reg.setText("No Location Entered");
            warning_txt_reg.setVisibility(View.VISIBLE);
            return false;
        }
        verifyPhoneNum(user);
        return true;
    }

    private void verifyPhoneNum(final User register_user) {

        final int verification_id, resend_token;

        PhoneAuthProvider.OnVerificationStateChangedCallbacks firebase_phone = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.

                registerUser(register_user);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                warning_txt_reg.setText(e.getLocalizedMessage());//"Phone number could not be verified, Try Again!");
                warning_txt_reg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                //Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                //verification_id = verificationId;
                //resend_token = token;

                // ...
            }

        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                register_user.phone,
                60,
                TimeUnit.SECONDS,
                this,
                firebase_phone);

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
