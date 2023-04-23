package com.example.fitnessapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class ExercisesActivity extends AppCompatActivity {

    private ArrayList<ItemCard> itemList = new ArrayList<>();
    ;

    private RecyclerView recyclerView;
    private RviewAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        init(savedInstanceState);

        //Nave Bar actions
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        // Handle the home action
                        Intent intentHome = new Intent(ExercisesActivity.this, NewActivity.class);
                        startActivity(intentHome);

                        break;
                    case R.id.action_gym:
                        // Handle the search action
                        Intent intentGym = new Intent(ExercisesActivity.this, GymActivity.class);
                        startActivity(intentGym);

                        break;
                    case R.id.action_profile:
                        // Handle the settings action
                        Intent intent = new Intent(ExercisesActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });


        //Specify what action a specific gesture performs, in this case swiping right or left deletes the entry
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                ItemCard deletedItem = itemList.get(viewHolder.getAdapterPosition());
                int position = viewHolder.getAdapterPosition();
                Snackbar.make(recyclerView, deletedItem.getItemName()+" deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // adding on click listener to our action of snack bar.
                                // below line is to add our item to array list with a position.
                                itemList.add(position, deletedItem);

                                // below line is to notify item is
                                // added to our adapter class.
                                rviewAdapter.notifyItemInserted(position);
                            }
                        }).show();

                itemList.remove(position);

                rviewAdapter.notifyItemRemoved(position);

            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    public void showDialog(View view) {
        final EditText[] txt = new EditText[2]; // user input bar
        AlertDialog.Builder alertName = new AlertDialog.Builder(this);
        final EditText editTextName1 = new EditText(ExercisesActivity.this);
        editTextName1.setHint(" Enter Name:");
        final EditText editTextName2 = new EditText(ExercisesActivity.this);
        editTextName2.setHint(" Enter Link:");

        alertName.setTitle(" Add New Link");
        // titles can be used regardless of a custom layout or not
        alertName.setView(editTextName1);
        LinearLayout layoutName = new LinearLayout(this);
        layoutName.setOrientation(LinearLayout.VERTICAL);
        layoutName.addView(editTextName1); // displays the user input bar
        layoutName.addView(editTextName2); // displays the user input bar
        alertName.setView(layoutName);

        alertName.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                txt[0] = editTextName1; // variable to collect user input
                txt[1] = editTextName2; // variable to collect user input
                // convert edit text to string
                String getInput1 = txt[0].getText().toString();
                String getInput2 = txt[1].getText().toString();

                // ensure that user input bar is not empty
                if (getInput1 ==null || getInput1.trim().equals("")){
                    Toast.makeText(getBaseContext(), "Please add link name", Toast.LENGTH_LONG).show();
                }
                else if (getInput2 ==null || getInput2.trim().equals("")){
                    Toast.makeText(getBaseContext(), "Please add link", Toast.LENGTH_LONG).show();
                }
                // add input into an data collection arraylist
                else {
                    addItem(0,getInput1, getInput2);

                    Snackbar.make(recyclerView, getInput1 +" link added", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // adding on click listener to our action of snack bar.
                                    // below line is to add our item to array list with a position.
                                    itemList.remove(0);

                                    // below line is to notify item is
                                    // added to our adapter class.
                                    rviewAdapter.notifyItemRemoved(0);
                                }
                            }).show();
                }



            }
        });

        alertName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel(); // closes dialog
            }
        });
        alertName.show(); // display the dialog
    }

    public void GetUrlFromIntent(View view) {

        TextView text = findViewById(R.id.item_desc);
        String link = text.getText().toString();
        Intent i = new Intent(Intent.ACTION_VIEW);

        if (URLUtil.isValidUrl(link)) {

            i.setData(Uri.parse(link));
            startActivity(i);
            link = "";
        }
        else {
            Toast.makeText(ExercisesActivity.this, "Link is not a valid URL", Toast.LENGTH_SHORT).show();
        }

    }



    // Handling Orientation Changes on Android
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {


        int size = itemList == null ? 0 : itemList.size();
        outState.putInt(NUMBER_OF_ITEMS, size);

        // Need to generate unique key for each item
        // This is only a possible way to do, please find your own way to generate the key
        for (int i = 0; i < size; i++) {
            // put itemName information into instance
            outState.putString(KEY_OF_INSTANCE + i + "0", itemList.get(i).getItemName());
            // put itemDesc information into instance
            outState.putString(KEY_OF_INSTANCE + i + "1", itemList.get(i).getItemDesc());
        }
        super.onSaveInstanceState(outState);

    }

    private void addItem(int position, String itemName, String itemDesc) {
        itemList.add(position, new ItemCard(itemName, itemDesc));
        //Toast.makeText(NewActivityLinks.this, "Add an item", Toast.LENGTH_SHORT).show();

        rviewAdapter.notifyItemInserted(position);
    }

    public void editItem(View view) {
//        ItemCard updatedItem = itemList.get(viewHolder.getAdapterPosition());
//        int position = viewHolder.getAdapterPosition();
//
//        System.out.println("--" + updatedItem.getItemName() + "--");
        RviewHolder rviewHolder = null;
        final EditText[] txt = new EditText[2]; // user input bar
        AlertDialog.Builder alertName = new AlertDialog.Builder(this);
        final EditText editTextName1 = new EditText(ExercisesActivity.this);
        editTextName1.setHint(" Enter Exercise Name:");
        final EditText editTextName2 = new EditText(ExercisesActivity.this);
        editTextName2.setHint(" Enter Type:");

        alertName.setTitle(" Edit Link Details");
        // titles can be used regardless of a custom layout or not
        alertName.setView(editTextName1);
        LinearLayout layoutName = new LinearLayout(this);
        layoutName.setOrientation(LinearLayout.VERTICAL);
        layoutName.addView(editTextName1); // displays the user input bar
        layoutName.addView(editTextName2); // displays the user input bar
        alertName.setView(layoutName);

        alertName.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                txt[0] = editTextName1; // variable to collect user input
                txt[1] = editTextName2; // variable to collect user input
                // convert edit text to string
                String getInput1 = txt[0].getText().toString();
                String getInput2 = txt[1].getText().toString();

                // ensure that user input bar is not empty
                if (getInput1 ==null || getInput1.trim().equals("")){
                    Toast.makeText(getBaseContext(), "Please add link name", Toast.LENGTH_LONG).show();
                }
                else if (getInput2 ==null || getInput2.trim().equals("")){
                    Toast.makeText(getBaseContext(), "Please add link description", Toast.LENGTH_LONG).show();
                }
                // add input into an data collection arraylist
                else {
                    int position = rviewHolder.getAdapterPosition();

                    ItemCard item = new ItemCard(getInput1,getInput2);
                    //itemList.set(1,item);
                    Snackbar.make(recyclerView, getInput1 +" link edited", Snackbar.LENGTH_LONG)
                            .show();
                    rviewAdapter.notifyItemChanged(position);
                }



            }
        });

        alertName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel(); // closes dialog
            }
        });
        alertName.show(); // display the dialog



    }

    private void init(Bundle savedInstanceState) {

        initialItemData(savedInstanceState);
        createRecyclerView();
    }

    private void createRecyclerView() {


        rLayoutManger = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        rviewAdapter = new RviewAdapter(itemList, this);
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //attributions bond to the item has been changed
                //itemList.get(position).onItemClick(position);

                rviewAdapter.notifyItemChanged(position);
            }

        };
        rviewAdapter.setOnItemClickListener(itemClickListener);

        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);


    }

    private void initialItemData(Bundle savedInstanceState) {

        // Not the first time to open this Activity
        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
            if (itemList == null || itemList.size() == 0) {

                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);

                // Retrieve keys we stored in the instance
                for (int i = 0; i < size; i++) {
                    String itemName = savedInstanceState.getString(KEY_OF_INSTANCE + i + "0");
                    String itemDesc = savedInstanceState.getString(KEY_OF_INSTANCE + i + "1");

                    ItemCard itemCard = new ItemCard( itemName, itemDesc);

                    itemList.add(itemCard);
                }
            }
        }
        // The first time to opne this Activity
        else {
//            ItemCard item1 = new ItemCard( "Gmail", "www.gmail.com");
//            ItemCard item2 = new ItemCard("Google", "www.google.com");
//            itemList.add(item1);
//            itemList.add(item2);
        }
    }


}
