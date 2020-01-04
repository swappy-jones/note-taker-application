package com.example.roomsample;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UpdateNoteViewModel extends AndroidViewModel {
    private NoteRepository repository;

    public UpdateNoteViewModel(@NonNull Application application) {
        super(application);
        repository =  new NoteRepository(application);
    }

    public void insert(NoteEntity noteEntity){
        repository.insert(noteEntity);
    }

    public void delete (NoteEntity noteEntity){
        repository.delete(noteEntity);
    }

    public void update(NoteEntity noteEntity){
        repository.update(noteEntity);
    }
}
