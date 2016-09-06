package com.example.twu.todoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

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
        void onSave(String editedText, Task task);
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
        mEditText = (EditText) dialogView.findViewById(R.id.editItemField);
        saveButton = dialogView.findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onSaveListener != null) {
                    onSaveListener.onSave(mEditText.getText().toString(), task);
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
