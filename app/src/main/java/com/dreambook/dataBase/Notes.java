package com.dreambook.dataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Notes {

        public Notes(int id, String name, String note, String date, String label){
            this.id = id;
            this.name = name;
            this.note = note;
            this.date = date;
            this.label = label;
        }

        @PrimaryKey
        @ColumnInfo(name = "id")
        int id;

    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "label")
    String label;

        @ColumnInfo(name = "note")
        String note;

    @ColumnInfo(name = "date")
    String date;



    public int getId() {            return id;
        }

    public String getNameNote(){return name;}

    public String getLabelNote(){return label;}

        public String getNote(){return note;}
    public String getDate(){return date;}




}
