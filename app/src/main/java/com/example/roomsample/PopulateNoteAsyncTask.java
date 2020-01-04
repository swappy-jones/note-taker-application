package com.example.roomsample;

import android.os.AsyncTask;

public class PopulateNoteAsyncTask extends AsyncTask<Void,Void,Void> {
    private INoteDAO iNoteDAO;

    public PopulateNoteAsyncTask(NoteDatabase noteDatabase) {

        this.iNoteDAO = noteDatabase.iNoteDAO();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }
}
