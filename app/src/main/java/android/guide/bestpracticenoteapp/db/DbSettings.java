package android.guide.bestpracticenoteapp.db;

import android.provider.BaseColumns;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RoomDatabase;

// TODO 4 CREATE DATABASE CLASS
@Database(
            entities =  {Favourites.class},
            version = 1
//            ,autoMigrations = {
//                    @AutoMigration(from = 1, to = 2)}
         )
public abstract class DbSettings extends RoomDatabase {

    public abstract FavDao favDao();
}
