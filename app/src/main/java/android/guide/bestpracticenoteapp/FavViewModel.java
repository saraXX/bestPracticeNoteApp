package android.guide.bestpracticenoteapp;

import android.app.Application;
import android.guide.bestpracticenoteapp.db.DbSettings;
import android.guide.bestpracticenoteapp.db.FavDao;
import android.guide.bestpracticenoteapp.db.Favourites;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
// TODO 5 : CREATE VIEW MODEL CLASS

// we extends AndroidViewModel instead of ViewModel
// cuz we need to use application Context to initialize database
//
public class FavViewModel extends AndroidViewModel {
    private DbSettings mDb;
    private FavDao mFavDao;
    String TAG= "FavViewModel";
    //    this is the main object here
    private MutableLiveData<List<Favourites>> mFavList;

    //    this constructor is required or error
    public FavViewModel(@NonNull Application application) {
        super(application);

//        declare both database and dao objects
        mDb = Room.databaseBuilder(application.getApplicationContext(),
                DbSettings.class,"fav_db").allowMainThreadQueries().build();
        mFavDao = mDb.favDao();
    }

    public MutableLiveData<List<Favourites>> getFavList(){
        if(mFavList == null){
            mFavList = new MutableLiveData<>();
            loadFavs();
        }
        return mFavList;
    }

    private void loadFavs(){
        mFavList.setValue(mFavDao.getAll());
    }

    public void addFav(String url, long date){
        Favourites f = new Favourites();
        f.fav_date = date;
        f.fav_url = url;
        mFavDao.insert(f);
//        after insert to database reread the database and update UI
        loadFavs();
    }

    public void removeFav(Favourites fav){
        mFavDao.delete(fav);
//        after finish deleting reread the database and update UI
        loadFavs();
    }
}
