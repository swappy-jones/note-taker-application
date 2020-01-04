package com.example.roomsample;

import android.os.AsyncTask;

public class CheckNoteAsyncTask extends AsyncTask<NoteEntity,Void,NoteEntity> {
    private INoteDAO iNoteDAO;

    public CheckNoteAsyncTask(INoteDAO iNoteDAO) {
        this.iNoteDAO = iNoteDAO;
    }

    @Override
    protected NoteEntity doInBackground(NoteEntity... noteEntities) {
        iNoteDAO.getMatchingNote(noteEntities[0].getDescription(),noteEntities[0].getTitle());
        return null;
    }

    @Override
    protected void onPostExecute(NoteEntity result) {
        myMethod(result);
    }

    private NoteEntity myMethod(NoteEntity myValue) {
        //handle value
        return myValue;
    }
}
