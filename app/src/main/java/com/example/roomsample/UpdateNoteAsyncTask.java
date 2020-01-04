package com.example.roomsample;

import android.os.AsyncTask;

public class UpdateNoteAsyncTask extends AsyncTask<NoteEntity,Void,Void> {
    private INoteDAO iNoteDAO;

    public UpdateNoteAsyncTask(INoteDAO iNoteDAO) {
        this.iNoteDAO = iNoteDAO;
    }

    @Override
    protected Void doInBackground(NoteEntity... noteEntities) {
        iNoteDAO.update(noteEntities[0]);
        return null;
    }
}
