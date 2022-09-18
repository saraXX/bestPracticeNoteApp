package android.guide.bestpracticenoteapp.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
// TODO 2 create entity class
@Entity(tableName = "favTbl")
public class Favourites {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String fav_url;
    public long fav_date;

    public Favourites() {
    }
}
