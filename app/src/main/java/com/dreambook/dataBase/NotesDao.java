package com.dreambook.dataBase;

import androidx.room.*;

import java.util.List;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM Notes ORDER BY date")
    List<Notes> getNotesListByDate();

    @Query("SELECT * FROM Notes ORDER BY name")
    List<Notes> getNotesListByName();

    @Query("SELECT * FROM Notes ORDER by id")
    List<Notes> getIdList();

    @Query("SELECT * FROM Notes WHERE id = :id")
    Notes getNoteById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Notes notes);

    @Update
    void update(Notes notes);

    @Delete
    void delete(Notes notes);

}
