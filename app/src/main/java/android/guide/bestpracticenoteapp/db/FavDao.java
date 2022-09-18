package android.guide.bestpracticenoteapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
// TODO 3 : CREATE DAO CLASS
@Dao
public interface FavDao {

    @Query("SELECT * FROM favTbl")
    List<Favourites> getAll();

    @Insert
    void insert(Favourites favourites);

    @Delete
    void delete(Favourites favourites);
}
