package com.example.tp_notes_mobile;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        // Get the note from the intent
        String note = getIntent().getStringExtra("note");

        // Set the note text in the TextView
        TextView textView = findViewById(R.id.noteTextView);
        if (note != null) {
            String noteWithDateTime = note + "\nCreated on: " + getCurrentDateTime();
            textView.setText(noteWithDateTime);
        }
    }

    // Method to get the current date and time
    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
