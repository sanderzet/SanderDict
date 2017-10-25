package ua.pp.sanderzet.sanderdict.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ua.pp.sanderzet.sanderdict.data.model.FavoriteModel;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by sander on 27/08/17.
 */
@Dao
public interface FavoriteDAO {

    @Query("select * from FavoriteModel")
 LiveData<List<FavoriteModel>> getAllFavoriteItems ();

    @Query("select * from FavoriteModel where word like :foundWord LIMIT 1")
LiveData<FavoriteModel> getFavoriteItem(String foundWord);

    @Insert(onConflict = REPLACE)
    void addFavorite (FavoriteModel favoriteModel);

    @Delete
     void deleteFavorite (FavoriteModel favoriteModel);


}
