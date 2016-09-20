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
public class EditTaskDialogFragment extends DialogFragment {
    public static final String KEY_ITEM_FIELD = "editItemField";
    private EditText mEditText;
    private View saveButton;
    private OnSaveListener onSaveListener;
    public Task task;

    public interface OnSaveListener {
        void onSave(int taskId, Task updatedTask);
    }

    public EditTaskDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditTaskDialogFragment newInstance(Task task, OnSaveListener onSaveListener) {
        EditTaskDialogFragment frag = new EditTaskDialogFragment();
        frag.setOnSaveListener(onSaveListener);
        frag.task = task;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View dialogView = inflater.inflate(R.layout.fragment_edit_task, container);
        Spinner prioritySpinner = (Spinner) dialogView.findViewById(R.id.priority);
        Spinner timeEstimateSpinner = (Spinner) dialogView.findViewById(R.id.timeEstimate);

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

            }
        });

        timeEstimateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                task.timeEstimate = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        prioritySpinner.setAdapter(priorityAdapter);
        timeEstimateSpinner.setAdapter(timeEstimateAdapter);

        prioritySpinner.setSelection(priorityAdapter.getPosition(task.priority));
        timeEstimateSpinner.setSelection(task.timeEstimate-1);

        mEditText = (EditText) dialogView.findViewById(R.id.editItemField);
        saveButton = dialogView.findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onSaveListener != null) {
                    task.name = mEditText.getText().toString();
                    onSaveListener.onSave(task.id, task);
                }
            }
        });
        if (task != null) {
            mEditText.setText(task.name);
        }
        return dialogView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.editItemField);
        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void setOnSaveListener(OnSaveListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }
}
