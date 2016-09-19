package com.example.twu.todoapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvItems;
    ArrayList<Task> items;

    private final int REQUEST_CODE = 20;
    private TasksAdapter itemsAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        ImageView addButton = (ImageView) findViewById(R.id.btnCustomAction);
        assert addButton != null;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });
        setContentView(R.layout.activity_main);
        ToDoDatabaseHelper databaseHelper = ToDoDatabaseHelper.getInstance(this);

        lvItems = (ListView) findViewById(R.id.lvItems);
        items = databaseHelper.getAllTasks();
        itemsAdapter = new TasksAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void showEditDialog(Task task) {
        final ToDoDatabaseHelper databaseHelper = ToDoDatabaseHelper.getInstance(this);
        FragmentManager fm = getSupportFragmentManager();
        EditTaskDialogFragment editTaskDialogFragment = EditTaskDialogFragment.newInstance(task,
                new EditTaskDialogFragment.OnSaveListener() {
                    @Override
                    public void onSave(String editedText, Task oldTask) {
                        oldTask.name = editedText;
                        itemsAdapter.notifyDataSetChanged();
                        databaseHelper.updateTask(oldTask.id, editedText);
                        Toast.makeText(getApplicationContext(), "Edited Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
        editTaskDialogFragment.show(fm, "fragment_edit_task");
    }

    private void showAddDialog () {
        final ToDoDatabaseHelper databaseHelper = ToDoDatabaseHelper.getInstance(this);
        FragmentManager fm = getSupportFragmentManager();
        AddTaskDialogFragment addTaskDialogFragment = AddTaskDialogFragment.newInstance(
                new AddTaskDialogFragment.OnSaveListener() {
                    @Override
                    public void onSave(Task task) {
                        databaseHelper.addTask(task);
                        itemsAdapter.add(task);
                    }
                }
        );
        addTaskDialogFragment.show(fm, "fragment_add_task");
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
                        showEditDialog(items.get(position));
                    }
                }
        );
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.twu.todoapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.twu.todoapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
