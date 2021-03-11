package com.dreambook.dataBase;

import androidx.room.*;

import java.util.List;


@Dao
public interface WordsDao {

    @Query("SELECT word FROM words WHERE type = :type AND gender = :gender")
    List<String> getWordsByType(int type, int gender);

    @Query("SELECT word FROM words WHERE mean = :mean AND (type = 0 OR type = 1)" )
    String getFullWord(String mean);

    @Query("SELECT mean FROM words WHERE gender = :gender AND word = :word")
    String getMean(String word, int gender);

    @Query("SELECT * FROM words WHERE gender = :gender AND (type = 0 OR type = 1) ORDER BY word" )
    List<Words> listForFragment(int gender);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Words words);

}
