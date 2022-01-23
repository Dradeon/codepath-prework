package com.example.todolist;

import org.apache.commons.io.FileUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> itemList;
    Button addButton;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        addButton = findViewById(R.id.addButton);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();


        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete item from model
                itemList.remove(position);
                // Notify Adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        itemsAdapter = new ItemsAdapter(itemList, onLongClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String todoItem = etItem.getText().toString();
                itemList.add(todoItem);
                itemsAdapter.notifyItemInserted(itemList.size()-1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });




    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    // Function will load items by reading every line of the data file
    private void loadItems(){
        try {
            itemList = new ArrayList<>(FileUtils.readLines(getDataFile(),Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            itemList = new ArrayList<>();
        }
    }

    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(),itemList);
        } catch (Exception e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}