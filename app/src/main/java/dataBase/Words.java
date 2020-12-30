package dataBase;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "words" )
public class Words {

    public Words(int id, int type, //int gender,
                 char tableName, String word, String mean){
        this.id = id;
        this.type = type;
//        this.gender = gender;
        this.tableName = tableName;
        this.word = word;
        this.mean = mean;
    }

    @PrimaryKey
    @ColumnInfo(name = "id")
    int id;

    @ColumnInfo(name = "type")
    int type;

//    @ColumnInfo(name = "gender")
//    int gender;

    @ColumnInfo(name = "table_name")
    char tableName;

    @ColumnInfo(name = "word")
    String word;

    @ColumnInfo(name = "mean")
    String mean;


    public int getId() {
        return id;
    }

//    public int getTableID(){return tableID;}

    public char getTableName(){return tableName;}

    public String getWord(){return word;}

    public String getMean() {
        return mean;
    }


}
