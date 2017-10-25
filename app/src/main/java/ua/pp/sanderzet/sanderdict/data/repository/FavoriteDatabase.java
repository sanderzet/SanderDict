package ua.pp.sanderzet.sanderdict.data.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ua.pp.sanderzet.sanderdict.data.model.FavoriteModel;

/**
 * Created by sander on 27/08/17.
 */

@Database(entities = {FavoriteModel.class}, version = 1 )
//@TypeConverters({DateConverter.class})
public abstract class FavoriteDatabase extends RoomDatabase {
    private static FavoriteDatabase INSTANCE ;
    public static FavoriteDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),FavoriteDatabase.class,
                    "favorite.db").build();
        }
      return INSTANCE;
    }
    public abstract FavoriteDAO favoriteDAO();
}
