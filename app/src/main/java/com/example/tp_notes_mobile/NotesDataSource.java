package com.example.tp_notes_mobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class NotesDataSource {

    private SQLiteDatabase database;
    private NotesDbHelper dbHelper;

    public NotesDataSource(Context context) {
        dbHelper = new NotesDbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addNote(String note) {
        ContentValues values = new ContentValues();
        values.put(NotesContract.NoteEntry.COLUMN_NOTE, note);
        database.insert(NotesContract.NoteEntry.TABLE_NAME, null, values);
    }

    public List<String> getAllNotes() {
        List<String> notes = new ArrayList<>();
        Cursor cursor = database.query(
                NotesContract.NoteEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(NotesContract.NoteEntry.COLUMN_NOTE);
            if (columnIndex != -1) {
                do {
                    String note = cursor.getString(columnIndex);
                    notes.add(note);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return notes;
    }


    public void deleteNote(String note) {
        database.delete(
                NotesContract.NoteEntry.TABLE_NAME,
                NotesContract.NoteEntry.COLUMN_NOTE + " = ?",
                new String[]{note}
        );
    }

    public void deleteNoteByContent(String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(NotesContract.NoteEntry.TABLE_NAME,
                NotesContract.NoteEntry.COLUMN_NOTE + " = ?",
                new String[]{content});
    }


}
