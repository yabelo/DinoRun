package com.mygdx.game.Utils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.FirebaseDatabase;
import com.mygdx.game.Account;
import com.mygdx.game.Main;

import java.util.HashMap;
import java.util.Map;

public class DataBaseAdapter {

    public static final String databaseUrl = "https://game-c25ee-default-rtdb.europe-west1.firebasedatabase.app/";
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance(databaseUrl);
    public static Main game = null;

    public static void updateUserCoins(int coins) {
        Map<String, Object> map = new HashMap<>();
        map.put("Coins", coins);

        String username = game.getPrefs().getString("UserName");
        Account.setName(username);

        database.getReference("users/" + Account.getName()).updateChildren(map)
                .addOnFailureListener(e -> System.out.println("Error by updating coins, " + e.getMessage()));
    }

    public static void updateUserMeters(int meters) {
        Map<String, Object> map = new HashMap<>();
        map.put("Meters", meters);

        String username = game.getPrefs().getString("UserName");
        Account.setName(username);

        database.getReference("users/" + Account.getName()).updateChildren(map)
                .addOnFailureListener(e -> System.out.println("Error by updating meters, " + e.getMessage()));
    }

    public static void updateUserSkins(Map<String, Boolean> data) {
        Map<String, Object> map = new HashMap<>();
        map.put("Skins", data);

        String username = game.getPrefs().getString("UserName");
        Account.setName(username);

        database.getReference("users/" + Account.getName()).updateChildren(map)
                .addOnFailureListener(e -> System.out.println("Error by updating skins, " + e.getMessage()));
    }
}
