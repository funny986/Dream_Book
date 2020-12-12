package dataBase;

import android.app.Application;
import androidx.room.Room;

public class App extends Application {
    private static App instance;

    private MeaningDatabase database;

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, MeaningDatabase.class, "database")
//                .fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build();
    }
public static App getInstance(){return instance;}
public MeaningDatabase getDatabase(){return database;}


}
