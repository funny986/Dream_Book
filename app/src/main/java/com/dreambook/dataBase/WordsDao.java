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

    @Query("SELECT * FROM words WHERE gender = :gender AND (type = 0 OR type = 1)")
    List<Words> listForFragment(int gender);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Words words);

    @Update
    void update(Words words);

    @Delete
    void delete(Words words);

}
