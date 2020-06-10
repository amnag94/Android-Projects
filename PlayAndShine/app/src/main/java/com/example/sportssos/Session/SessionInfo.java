package com.example.sportssos.Session;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SessionInfo {
    public static FirebaseAuth firebase_instance;
    public static User user;

    public static void initialize() {
        firebase_instance = FirebaseAuth.getInstance();
        FirebaseUser current_user = firebase_instance.getCurrentUser();

        if(current_user != null) {
            user = new User();
            user.email = current_user.getEmail();
            user.name = current_user.getDisplayName();
        }
        else {
            user = null;
        }
    }
}
