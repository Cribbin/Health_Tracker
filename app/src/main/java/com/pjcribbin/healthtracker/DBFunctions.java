package com.pjcribbin.healthtracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class DBFunctions {
    private SQLiteDatabase db;
    private Cursor c;

    DBFunctions(SQLiteDatabase db) {
        this.db = db;
    }

    public void removeAllDataFromDatabase() {
        db.execSQL("DELETE FROM Food");
        db.execSQL("DELETE FROM Meal");
        db.execSQL("DELETE FROM Food_Meal");
        db.execSQL("DELETE FROM Meal_Entry");
        db.execSQL("DELETE FROM Num_Steps");
    }

    public void initializeTodayIntoNum_Steps() {
        String query = "INSERT INTO Num_Steps (steps) VALUES (?)";
        SQLiteStatement statement = db.compileStatement(query);
        statement.bindString(1, "0");
        statement.execute();
    }

    public String getCurrentStepCountFromDatabase() {
        c = db.rawQuery("SELECT steps FROM Num_Steps WHERE day = date('now', 'localtime')", null);
        if (c.getCount() < 1) {
            db.execSQL("INSERT INTO Num_Steps (steps) VALUES (0)");
            c = db.rawQuery("SELECT steps FROM Num_Steps WHERE day = date('now', 'localtime')", null);
        }
        c.moveToFirst();

        return c.getString(c.getColumnIndex("steps"));
    }

    public int getCaloriesConsumedToday() {
        c = db.rawQuery("SELECT sum(calories) AS cal " +
                "FROM Meal_Entry INNER JOIN Meal ON Meal_Entry.meal_id = Meal._id " +
                "INNER JOIN Food_Meal ON Meal._id = Food_Meal.meal_id " +
                "INNER JOIN Food ON Food_Meal.food_id = Food._id " +
                "WHERE date(timestamp) = date('now', 'localtime')" , null);

        c.moveToFirst();

        return c.getInt(c.getColumnIndex("cal"));
    }

    public void populateNum_StepsTableWithSampleData() {
        db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                "VALUES ('2016-04-29', 5045)");
        db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                "VALUES ('2016-04-28', 3000)");
        db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                "VALUES ('2016-04-27', 5000)");
        db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                "VALUES ('2016-04-26', 3478)");
        db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                "VALUES ('2016-04-25', 4812)");
        db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                "VALUES ('2016-04-24', 7122)");
        db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                "VALUES ('2016-04-23', 4598)");
        db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                "VALUES ('2016-04-22', 6444)");
        db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                "VALUES ('2016-04-21', 5012)");
        db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                "VALUES ('2016-04-20', 3897)");
        db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                "VALUES ('2016-04-19', 3924)");
        db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                "VALUES ('2016-04-18', 4001)");
        db.execSQL("INSERT INTO Num_Steps (day, steps) " +
                "VALUES ('2016-04-17', 5159)");
    }

    public void populateFoodTableWithSampleData() {
        db.execSQL("INSERT INTO Food (food_name, calories, carbohydrates, fat, protein, sodium, sugar) " +
                "VALUES ('Apple', 95, 25, 0.3, 0.5, 1.8, 19)");
        db.execSQL("INSERT INTO Food (food_name, calories, carbohydrates, fat, protein, sodium, sugar) " +
                "VALUES ('2 Weetabix Biscuits', 134, 25.7, 0.8, 4.3, 0.1, 1.7)");
        db.execSQL("INSERT INTO Food (food_name, calories, carbohydrates, fat, protein, sodium, sugar) " +
                "VALUES ('Full Fat Milk', 124, 9.32, 6.7, 6.64, 83, 10.85)");
        db.execSQL("INSERT INTO Food (food_name, calories, carbohydrates, fat, protein, sodium, sugar) " +
                "VALUES ('Glass of Orange Juice', 112, 26, 0, 2, 2, 21)");
    }

    public void populateMealsTableWithSampleData() {
        db.execSQL("INSERT INTO Meal (meal_name, meal_type) " +
                "VALUES ('Weetabix and Orange Juice', 'Breakfast')");
        db.execSQL("INSERT INTO Food_Meal (food_id, meal_id) " +
                "VALUES (2,1)");
        db.execSQL("INSERT INTO Food_Meal (food_id, meal_id) " +
                "VALUES (3,1)");
        db.execSQL("INSERT INTO Food_Meal (food_id, meal_id) " +
                "VALUES (4,1)");
    }

    public void populateMeal_EntryTableWithSampleData() {
        db.execSQL("INSERT INTO Meal_Entry (meal_id, timestamp) " +
                "VALUES (1, '2016-04-30 08:44:00')");
        db.execSQL("INSERT INTO Meal_Entry (meal_id, timestamp) " +
                "VALUES (1, '2016-04-29 08:53:12')");
        db.execSQL("INSERT INTO Meal_Entry (meal_id, timestamp) " +
                "VALUES (1, '2016-04-28 08:48:52')");
    }

    public String getStepsTakenToday() {
        c = db.rawQuery("SELECT steps FROM Num_Steps WHERE day = date('now', 'localtime')", null);
        c.moveToFirst();
        String steps = c.getString(c.getColumnIndex("steps"));
        c.close();

        return steps;
    }
}
