package com.example.roomsample;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = NoteEntity.class, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract INoteDAO iNoteDAO();

    public static synchronized NoteDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context,NoteDatabase.class,"note_database").fallbackToDestructiveMigration().addCallback(roomCallBack).build();
        }
        return instance;
    }
    private static RoomDatabase.Callback roomCallBack= new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateNoteAsyncTask(instance).execute();
        }
    };
}
