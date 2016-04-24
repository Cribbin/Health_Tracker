package com.pjcribbin.healthtracker;

public class Food {
    private int id;
    private String name;
    private int calories;
    private double carbohydrates;
    private double fat;
    private double protein;
    private double sodium;
    private double potassium;

    Food(int id, String name) {
        this.id = id;
        this.name = name;
    }

    Food(int id, String name, int calories, double carbohydrates, double fat, double protein, double sodium, double potassium) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.carbohydrates = carbohydrates;
        this.fat = fat;
        this.protein = protein;
        this.sodium = sodium;
        this.potassium = potassium;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public double getFat() {
        return fat;
    }

    public double getProtein() {
        return protein;
    }

    public double getSodium() {
        return sodium;
    }

    public double getPotassium() {
        return potassium;
    }
}
