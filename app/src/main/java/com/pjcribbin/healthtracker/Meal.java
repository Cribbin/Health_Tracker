package com.pjcribbin.healthtracker;

import java.util.ArrayList;

public class Meal {
    private final static String TAG = "PJ_Health_Tracker";

    private ArrayList<ArrayList> mealContents;

    Meal() {
        mealContents.set(0, new ArrayList<Food>());
        mealContents.set(1, new ArrayList<Integer>());
    }

    public void addFood(Food food) {
        mealContents.get(0).add(food);
        mealContents.get(1).add(1);
    }

    public void addFood(Food food, int num) {
        Integer qty = new Integer(num);

        mealContents.get(0).add(food);
        mealContents.get(1).add(qty);
    }

    public ArrayList getMeal() {
        return mealContents;
    }
}
