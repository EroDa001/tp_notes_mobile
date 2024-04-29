package com.example.tp_notes_mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddNoteDialogFragment extends DialogFragment {
    private NotesDataSource dataSource;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note_dialog, container, false);
        EditText noteEditText = view.findViewById(R.id.noteEditText);
        Button addButton = view.findViewById(R.id.addButton);

        dataSource = new NotesDataSource(requireContext());
        dataSource.open();

        addButton.setOnClickListener(v -> {
            String note = noteEditText.getText().toString().trim();
            if (!note.isEmpty()) {
                // Add note to the database
                dataSource.addNote(note);
                // Notify MainActivity to update the list
                ((MainActivity) requireActivity()).updateNotesList();
                dismiss();
            } else {
                Toast.makeText(requireContext(), "Please enter a note", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Close the data source when the view is destroyed
        dataSource.close();
    }
}
