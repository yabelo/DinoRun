package com.mygdx.game;

import com.mygdx.game.Utils.DataBaseAdapter;

import java.util.Map;


public class Account {
    private static String id;
    private static String name;
    private static int coins;
    private static int meters;
    private static Map<String, Boolean> skins;
    private static String currentSkin;
    public Account(String id, String name, int coins, int meters) {
        Account.id = id;
        Account.name = name;
        Account.coins = coins;
        Account.meters = meters;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        Account.id = id;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        name = name.trim();
        Account.name = name;
    }

    public static int getCoins() {
        return coins;
    }

    public static void increaseCoins(){
        Account.coins += 1;
        DataBaseAdapter.updateUserCoins(Account.coins);
    }
    public static void addCoins(int coins){
        Account.coins += coins;
        DataBaseAdapter.updateUserCoins(Account.coins);
    }
    public static void removeCoins(int coins){
        Account.coins -= coins;
        DataBaseAdapter.updateUserCoins(Account.coins);
    }

    public static void setCoins(int coins) {
        Account.coins = coins;
        DataBaseAdapter.updateUserCoins(coins);
    }

    public static int getMeters() {
        return meters;
    }

    public static void setMeters(int meters) {
        Account.meters = meters;
        DataBaseAdapter.updateUserMeters(meters);
    }

    public static Map<String, Boolean> getSkins() {
        return skins;
    }

    public static void setSkins(Map<String, Boolean> skins) {
        Account.skins = skins;
        DataBaseAdapter.updateUserSkins(skins);
    }

    public static boolean hasSkin(String skin){
        if(Account.skins == null) return false;
        if(!Account.skins.containsKey(skin)) return false;
        return Account.skins.get(skin);
    }

    public static String getCurrentSkin() {
        return currentSkin;
    }

    public static void setCurrentSkin(String currentSkin) {
        Account.currentSkin = currentSkin;
    }
}
