package com.example.roomsample;

import android.os.AsyncTask;

public class DeleteNoteAsyncTask extends AsyncTask<NoteEntity,Void,Void> {
    private INoteDAO iNoteDAO;

    public DeleteNoteAsyncTask(INoteDAO iNoteDAO) {
        this.iNoteDAO = iNoteDAO;
    }

    @Override
    protected Void doInBackground(NoteEntity... noteEntities) {
        iNoteDAO.delete(noteEntities[0]);
        return null;
    }
}
