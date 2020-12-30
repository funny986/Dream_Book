package dataBase;

import androidx.room.*;

import java.util.List;


@Dao
public interface WordsDao {

    @Query("SELECT * FROM words WHERE id = :id")
    Words getWordById(int id);

    @Query("SELECT * FROM words ")
    List<Words> getAllWords();

//    @Query("SELECT * FROM words WHERE gender = :gender")
//    List<Words> getAllWordsGender(int gender);

    @Query("SELECT * FROM words WHERE table_name = :tableName")
    List<Words> getWordsListTable(char tableName);


    @Query("SELECT * FROM words WHERE word = :word")
    Words getWordMean(String word);

    @Query("SELECT * FROM words WHERE type = :type")
    List<Words> getWordType(int type);

//    @Query("SELECT mean FROM words WHERE word = :word")
//    String getMean(String word);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Words words);

    @Update
    void update(Words words);

    @Delete
    void delete(Words words);

}
