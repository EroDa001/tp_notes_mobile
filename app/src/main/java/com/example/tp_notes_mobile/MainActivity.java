package com.example.tp_notes_mobile;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected ArrayList<String> notes = new ArrayList<>();
    protected ArrayAdapter<String> adapter;
    private boolean isDualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(adapter);

        // Check for landscape mode
        isDualPane = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;


        // Click to view note details
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String note = notes.get(position);
            if (isDualPane) {
                NoteDetailsFragment fragment = new NoteDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("note", note);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detailsContainer, fragment)
                        .commit();
            } else {
                Intent intent = new Intent(MainActivity.this, NoteDetailsActivity.class);
                intent.putExtra("note", note);
                startActivity(intent);
            }
        });


        // Open the database and fetch previous notes
        NotesDataSource dataSource = new NotesDataSource(this);
        dataSource.open();
        notes.addAll(dataSource.getAllNotes());
        dataSource.close();

        // Add note button
        FloatingActionButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(view -> openAddNoteDialog());

        // Long click to delete item
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Delete from the database
                        String note = notes.get(position);
                        dataSource.open();
                        dataSource.deleteNoteByContent(note);
                        dataSource.close();

                        // Remove from the list view
                        notes.remove(position);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });

        // Update the list view
        adapter.notifyDataSetChanged();
    }

    private void openAddNoteDialog() {
        AddNoteDialogFragment dialogFragment = new AddNoteDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "AddNoteDialogFragment");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("notes", notes);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Back button pressed", Toast.LENGTH_SHORT).show();
    }

    // Method to update the notes list after adding a new note
    public void updateNotesList() {
        notes.clear();
        NotesDataSource dataSource = new NotesDataSource(this);
        dataSource.open();
        notes.addAll(dataSource.getAllNotes());
        dataSource.close();
        adapter.notifyDataSetChanged();
    }
}
