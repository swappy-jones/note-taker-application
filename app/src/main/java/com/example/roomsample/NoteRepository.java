package com.example.roomsample;

import android.app.Application;

import androidx.lifecycle.LiveData;
import java.util.List;

public class NoteRepository {
    private INoteDAO iNoteDAO;
    private LiveData<List<NoteEntity>> allNotes;
    private NoteEntity noteEntity;

    public NoteRepository(Application application){
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        iNoteDAO = noteDatabase.iNoteDAO();
        allNotes = iNoteDAO.getAll();
    }

    void getNoteForAlarm(int id){
        noteEntity = iNoteDAO.getNoteForAlarm(id).getValue().get(0);
    }

    public void insert(NoteEntity noteEntity){
        new InsertNoteAsyncTask(iNoteDAO).execute(noteEntity);
    }

    public void update(NoteEntity noteEntity){
        new UpdateNoteAsyncTask(iNoteDAO).execute(noteEntity);
    }

    public void delete(NoteEntity noteEntity){
        new DeleteNoteAsyncTask(iNoteDAO).execute(noteEntity);
    }

    public void deleteAll(){
        new DeleteAllNoteAsyncTask(iNoteDAO).execute();
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }
}
