package com.mygdx.game.Screens.Leaderboard;

public class LeaderboardPlayer {
    private String userId;
    private String username;
    private int coins;
    private int meters;

    public LeaderboardPlayer(String userId, String username, int coins, int meters) {
        this.userId = userId;
        this.username = username;
        this.coins = coins;
        this.meters = meters;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCoins() {
        return coins;
    }

    public int getMeters() {
        return meters;
    }
}