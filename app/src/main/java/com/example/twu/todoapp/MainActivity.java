package com.example.twu.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvItems;
    ArrayList<Task> items;

    private final int REQUEST_CODE = 20;
    private TasksAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ToDoDatabaseHelper databaseHelper = ToDoDatabaseHelper.getInstance(this);

        lvItems = (ListView) findViewById(R.id.lvItems);
        items = databaseHelper.getAllTasks();
        itemsAdapter = new TasksAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        final ToDoDatabaseHelper databaseHelper = ToDoDatabaseHelper.getInstance(this);

        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        databaseHelper.deleteTask(items.get(position).id);
                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        launchEditItemView(items.get(position).name, position);
                    }
                }
        );
    }

    private void launchEditItemView(String item, int position) {
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        i.putExtra("toDoItem", item);
        i.putExtra("position", position);
        startActivityForResult(i, REQUEST_CODE);
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        ToDoDatabaseHelper databaseHelper = ToDoDatabaseHelper.getInstance(this);

        Task newTask = new Task();
        newTask.name = itemText;
        databaseHelper.addTask(newTask);
        itemsAdapter.add(newTask);
        etNewItem.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final ToDoDatabaseHelper databaseHelper = ToDoDatabaseHelper.getInstance(this);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String item = data.getExtras().getString("item");
                int position = data.getExtras().getInt("position");

                databaseHelper.updateTask(items.get(position).id, item);
                Task currentTask = items.get(position);
                currentTask.name = item;
                items.set(position, currentTask);
                itemsAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Edited Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
