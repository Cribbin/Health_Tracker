package com.pjcribbin.healthtracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class AddFood extends AppCompatActivity {
    private final static String TAG = "PJ_Health_Tracker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        setUpFoodList();
    }

    private void setUpFoodList() {
        final ListView foodList = (ListView) findViewById(R.id.food_list);

        try {
            Database dbHelper = new Database(this);

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM Food ORDER BY food_name ASC", null);

            foodList.setAdapter(
                    new SimpleCursorAdapter(this, R.layout.food_row, c, new String[]{"_id", "food_name", "calories", "carbohydrates", "fat", "protein", "sodium", "sugar"}, new int[]{R.id.food_id, R.id.food_name, R.id.num_calories, R.id.num_carbohyrates, R.id.num_fats, R.id.num_protein, R.id.num_sodium, R.id.num_sugar}, 0)
            );

            db.close();
        } catch (Exception e) {
            Log.e(TAG, "Error setting up list from database");
            e.printStackTrace();
        }

        try {
            foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        TextView textView = (TextView) view.findViewById(R.id.food_id);
                        int foodId = Integer.parseInt((String) textView.getText());

                        textView = (TextView) view.findViewById(R.id.food_name);
                        String foodName = (String) textView.getText();

                        Food foodClicked = new Food(foodId, foodName);

                    /* Add calories */
                        textView = (TextView) view.findViewById(R.id.num_calories);
                        String calories = (String) textView.getText();
                        if (!calories.equals("")) {
                            foodClicked.setCalories(Integer.parseInt(calories));
                        }

                    /* Add carbohydrates */
                        textView = (TextView) view.findViewById(R.id.num_carbohyrates);
                        String carbohydrates = (String) textView.getText();
                        if (!carbohydrates.equals("")) {
                            foodClicked.setCarbohydrates(Double.parseDouble(carbohydrates));
                        }

                    /* Add fat */
                        textView = (TextView) view.findViewById(R.id.num_fats);
                        String fat = (String) textView.getText();
                        if (!fat.equals("")) {
                            foodClicked.setFat(Double.parseDouble(fat));
                        }

                    /* Add protein */
                        textView = (TextView) view.findViewById(R.id.num_protein);
                        String protein = (String) textView.getText();
                        if (!protein.equals("")) {
                            foodClicked.setProtein(Double.parseDouble(protein));
                        }

                    /* Add sodium */
                        textView = (TextView) view.findViewById(R.id.num_sodium);
                        String sodium = (String) textView.getText();
                        if (!sodium.equals("")) {
                            foodClicked.setSodium(Double.parseDouble(sodium));
                        }

                    /* Add sugar */
                        textView = (TextView) view.findViewById(R.id.num_sugar);
                        String sugar = (String) textView.getText();
                        if (!sugar.equals("")) {
                            foodClicked.setSugar(Double.parseDouble(sugar));
                        }

                        Log.i(TAG, "ID: " + foodId + "\nName: " + foodName + "\nCalories: " + foodClicked.getCalories());
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error selecting item", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error on create Food item");
                        e.printStackTrace();
                    }
                }
            });


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error on item click", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error on click");
            e.printStackTrace();
        }
    }

    public void addNewFood(View view) {
        Intent i = new Intent(getApplicationContext(), AddNewFood.class);
        startActivity(i);
    }
}
