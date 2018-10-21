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

    @Query("select * from fdict")
 LiveData<List<FavoriteModel>> getAllFavoriteItems ();


//  @Query("select * from fdict where word like :foundWord LIMIT 1")
    /*GLOB is case-sensitive, so if "dr" is in favorite,
     * GLOB says that only "dr" is in favorite, when "like" would show that
     * "DR" also in favorite even if truly "DR" dont marked as favorite.
      * */
@Query("select * from fdict where word LIKE :foundWord")
LiveData<FavoriteModel> getFavoriteItem(String foundWord);

@Query("select * from fdict where word GLOB :foundWord")
FavoriteModel getFavoriteModel(String foundWord);

    @Insert(onConflict = REPLACE)
    void addFavorite (FavoriteModel favoriteModel);

    @Delete
     void deleteFavorite (FavoriteModel favoriteModel);


}
