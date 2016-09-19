package com.example.twu.todoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by twu on 9/5/16.
 */
public class AddTaskDialogFragment extends DialogFragment {
    public static final String KEY_ITEM_FIELD = "editItemField";
    private EditText mAddTask;
    private View addButton;
    private OnSaveListener onSaveListener;
    public Task task = new Task();

    public interface OnSaveListener {
        void onSave(Task task);
    }

    public AddTaskDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AddTaskDialogFragment newInstance(OnSaveListener onSaveListener) {
        AddTaskDialogFragment frag = new AddTaskDialogFragment();
        frag.setOnSaveListener(onSaveListener);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View dialogView = inflater.inflate(R.layout.fragment_add_task, container);
        Spinner prioritySpinner = (Spinner) dialogView.findViewById(R.id.spinner1);
        Spinner timeEstimateSpinner = (Spinner) dialogView.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.priority, android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> timeEstimateAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.time_estimate, android.R.layout.simple_spinner_item);
        timeEstimateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                task.priority = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                task.priority = "Low";
            }
        });

        timeEstimateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                task.timeEstimate = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                task.timeEstimate = 1;
            }
        });

        prioritySpinner.setAdapter(priorityAdapter);
        timeEstimateSpinner.setAdapter(timeEstimateAdapter);

        mAddTask = (EditText) dialogView.findViewById(R.id.addItemField);
        addButton = dialogView.findViewById(R.id.btnAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onSaveListener != null) {
                    task.name=mAddTask.getText().toString();
                    onSaveListener.onSave(task);
                }
            }
        });
        return dialogView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mAddTask = (EditText) view.findViewById(R.id.addItemField);
        // Show soft keyboard automatically and request focus to field
        mAddTask.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }
}
