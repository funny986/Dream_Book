package com.dreambook.dataBase;

import androidx.room.*;

import java.util.List;


@Dao
public interface WordsDao {

    @Query("SELECT * FROM words WHERE id = :id")
    Words getWordById(int id);

    @Query("SELECT * FROM words ")
    List<Words> getAllWords();

    @Query("SELECT * FROM words WHERE gender = :gender")
    List<Words> getAllWordsGender(int gender);

    @Query("SELECT * FROM words WHERE table_name = :tableName")
    List<Words> getWordsListTable(char tableName);

    @Query("SELECT * FROM words WHERE word = :word")
    Words getWordMean(String word);

    @Query("SELECT word FROM words WHERE type = :type AND gender = :gender")
    List<String> getWordsByType(int type, int gender);

    @Query("SELECT mean FROM words WHERE word = :word")
    String getMean(String word);

    @Query("SELECT * FROM words WHERE gender = 0 AND (type = 1 OR type = 2)")
    List<Words> listForFragment();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Words words);

    @Update
    void update(Words words);

    @Delete
    void delete(Words words);

}
