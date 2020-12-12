package dataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Words.class, Notes.class}, version = 1)
public abstract class MeaningDatabase extends RoomDatabase {
    public abstract WordsDao wordsDao();
    public abstract NotesDao notesDao();
}
