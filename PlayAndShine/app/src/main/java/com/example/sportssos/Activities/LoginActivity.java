package com.example.sportssos.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sportssos.R;
import com.example.sportssos.Session.SessionInfo;
import com.example.sportssos.Session.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button login_btn, register_btn;
    EditText login_input_email, login_input_pass;
    TextView warning_txt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initializations
        login_btn = findViewById(R.id.login_btn);
        register_btn = findViewById(R.id.register_btn);
        login_input_email = findViewById(R.id.login_input_email);
        login_input_pass = findViewById(R.id.login_input_pass);
        warning_txt_login = findViewById(R.id.warning_txt_login);

        // Session
        SessionInfo.initialize();

        // If user logged in then redirect to dashboard
        if(SessionInfo.firebase_instance != null && SessionInfo.user != null) {
            Intent home = new Intent(this, DashboardActivity.class);
            startActivity(home);
        }

        // Set Listeners
        login_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent next_activity;

        if(v == findViewById(R.id.login_btn)) {
            final User user = new User();
            user.email = login_input_email.getText().toString();
            user.password = login_input_pass.getText().toString();

            signInUser(user);

            next_activity = null;
        }
        else if(v == findViewById(R.id.register_btn)) {
            next_activity = new Intent(this, RegisterActivity.class);
        }
        else {
            next_activity = null;
        }

        if(next_activity != null) {
            startActivity(next_activity);
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
                                if (warning_txt_login != null) {
                                    warning_txt_login.setText(task.getException().getLocalizedMessage());
                                    warning_txt_login.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (warning_txt_login != null) {
                                warning_txt_login.setText(e.getLocalizedMessage());
                                warning_txt_login.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {

    }
}
