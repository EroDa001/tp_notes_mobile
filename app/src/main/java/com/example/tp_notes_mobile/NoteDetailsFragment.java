package com.example.tp_notes_mobile;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteDetailsFragment extends Fragment {

    private TextToSpeech textToSpeech;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_details, container, false);
        String note = getArguments().getString("note");
        String noteWithDateTime = note + "\nCreated on: " + getCurrentDateTime();
        TextView textView = view.findViewById(R.id.noteTextView);
        textView.setText(noteWithDateTime);

        // Button to close the fragment
        Button closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction().remove(NoteDetailsFragment.this).commit();
        });

        Button readButton = view.findViewById(R.id.readButton);
        readButton.setOnClickListener(v -> speakText(note));

        return view;
    }

    private void speakText(String text) {
        textToSpeech = new TextToSpeech(requireContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.ENGLISH);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Handle language not supported or missing data
                } else {
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            } else {
                // Handle TextToSpeech initialization failure
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Stop and shutdown the Text-to-Speech engine when the fragment is destroyed
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // Stop and shutdown the Text-to-Speech engine when the app is stopped
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
