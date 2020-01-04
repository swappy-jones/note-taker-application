package com.example.roomsample;

import android.os.AsyncTask;

public class InsertNoteAsyncTask extends AsyncTask<NoteEntity,Void,Void> {
    private INoteDAO iNoteDAO;

    public InsertNoteAsyncTask(INoteDAO iNoteDAO) {
        this.iNoteDAO = iNoteDAO;
    }

    @Override
    protected Void doInBackground(NoteEntity... noteEntities) {
        iNoteDAO.insert(noteEntities[0]);
        return null;
    }
}
