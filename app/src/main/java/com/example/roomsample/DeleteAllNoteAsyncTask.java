package com.example.roomsample;

import android.os.AsyncTask;

public class DeleteAllNoteAsyncTask extends AsyncTask<Void,Void,Void> {
    private INoteDAO iNoteDAO;

    public DeleteAllNoteAsyncTask(INoteDAO iNoteDAO) {
        this.iNoteDAO = iNoteDAO;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        iNoteDAO.deleteAll();
        return null;
    }
}
