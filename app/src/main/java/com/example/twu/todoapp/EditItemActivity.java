package com.example.twu.todoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String toDoItem = getIntent().getStringExtra("toDoItem");
        EditText etText = (EditText) findViewById(R.id.editItemField);
        etText.setText(toDoItem, TextView.BufferType.EDITABLE);
    }
}
