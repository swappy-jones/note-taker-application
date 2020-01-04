package com.example.roomsample;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface INoteDAO {
    @Insert
    void insert(NoteEntity noteEntity);

    @Update
    void update(NoteEntity noteEntity);

    @Delete
    void delete(NoteEntity noteEntity);

    @Query("DELETE FROM note_table")
    void deleteAll();

    @Query("SELECT * FROM note_table")
    LiveData<List<NoteEntity>> getAll();

    @Query("SELECT * FROM note_table WHERE description = :description AND title = :title")
    NoteEntity getMatchingNote(String description, String title);

    @Query("SELECT * FROM NOTE_TABLE WHERE id = :id")
    LiveData<List<NoteEntity>> getNoteForAlarm(int id);
}
