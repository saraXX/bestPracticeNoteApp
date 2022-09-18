// this project is study of live data view model , room components
// the app will allow you to create a list of note of faviourate websites
// so you can write a website url and click save and the list will updated automaticly
//ones you click to the note it will open that website you've wrote.
//
// there are some changes from the refrence like :
//          1- i used room insted of regular database implemntation,
//          2- i used androidX for UI insted of v7.design library
//          3- i shorten query methods in the view model class
//see ref : https://www.digitalocean.com/community/tutorials/android-livedata


package android.guide.bestpracticenoteapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.guide.bestpracticenoteapp.db.Favourites;
import android.os.Bundle;

import android.content.DialogInterface;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FavAdapter mFavAdapter;
    private FavViewModel mFavViewModel;
    private List<Favourites> mFav;
    FloatingActionButton fab;
    String TAG = "Main Activity";
// TODO 7 CREATE onCreate METHODE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFavViewModel = new ViewModelProvider(this).get(FavViewModel.class);

        final Observer<List<Favourites>> favsObserver = new Observer<List<Favourites>>() {
            @Override
            public void onChanged(@Nullable final List<Favourites> updatedList) {
                if (mFav == null) {
//                    this section refresh recycle view each time the activity start
                    mFav = updatedList;
                    mFavAdapter = new FavAdapter();
                    recyclerView.setAdapter(mFavAdapter);
                } else {
//                    this section refresh recycle view each time the list updated
//                    this is a way to apply Differance algorithm.
                    DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {

                        @Override
                        public int getOldListSize() {
                            return mFav.size();
                        }

                        @Override
                        public int getNewListSize() {
                            return updatedList.size();
                        }

                        @Override
                        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                            return mFav.get(oldItemPosition).id ==
                                    updatedList.get(newItemPosition).id;
                        }

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            Favourites oldFav = mFav.get(oldItemPosition);
                            Favourites newFav = updatedList.get(newItemPosition);
                            return oldFav.equals(newFav);
                        }
                    });
                    result.dispatchUpdatesTo(mFavAdapter);
                    Log.d(TAG, "onChanged: mFav before updated : "+mFav.size());
                    Log.d(TAG, "onChanged: updatedList : "+updatedList.size());
                    mFav = updatedList;
                }
            }
        };

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inUrl = new EditText(MainActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("New favourite")
                        .setMessage("Add a url link below")
                        .setView(inUrl)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url = String.valueOf(inUrl.getText());
                                long date = (new Date()).getTime();

                                mFavViewModel.addFav(url, date);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });
//        this line will executing every time activity started, when: starting app, rotate Screen
        mFavViewModel.getFavList().observe(this, favsObserver);
        Log.d(TAG, "onCreate: mFavViewModel.getFavList() "+mFavViewModel.getFavList().getValue().size());


    }


// TODO 6 CREATE ADAPTER CLASSES TO HANDLE RECYCLE VIEW

    public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavViewHolder> {

        @Override
        public FavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_row, parent, false);
            return new FavViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FavViewHolder holder, int position) {
            Favourites favourites = mFav.get(position);
            holder.mTxtUrl.setText(favourites.fav_url);
            holder.mTxtDate.setText((new Date(favourites.fav_date).toString()));
        }

        @Override
        public int getItemCount() {
            return mFav.size();
        }

        class FavViewHolder extends RecyclerView.ViewHolder {

            TextView mTxtUrl;
            TextView mTxtDate;

            FavViewHolder(View itemView) {
                super(itemView);
                mTxtUrl = itemView.findViewById(R.id.tvUrl);
                mTxtDate = itemView.findViewById(R.id.tvDate);
                ImageButton btnDelete = itemView.findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = getAdapterPosition();
                        Favourites favourites = mFav.get(pos);
                        mFavViewModel.removeFav(favourites);
                    }
                });
            }
        }
    }
}