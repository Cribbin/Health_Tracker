package com.pjcribbin.healthtracker;

public class Food {
    private final static String TAG = "PJ_Health_Tracker";

    private int id;
    private String name;
    private int calories = -1;
    private double carbohydrates = -1;
    private double fat = -1;
    private double protein = -1;
    private double sodium = -1;
    private double sugar = -1;

    Food(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getCalories() {
        return calories;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getFat() {
        return fat;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getProtein() {
        return protein;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public double getSugar() {
        return sugar;
    }
}
