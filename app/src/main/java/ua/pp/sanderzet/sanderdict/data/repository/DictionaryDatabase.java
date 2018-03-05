package ua.pp.sanderzet.sanderdict.data.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.huma.room_for_asset.RoomAsset;

import ua.pp.sanderzet.sanderdict.data.model.DictionaryModel;

/**
 * Created by sander on 13.02.18.
 */
@Database(entities = {DictionaryModel.class}, version = 2)
public abstract class DictionaryDatabase extends RoomDatabase {
    public abstract DictionaryDAO dictionaryDAO();

    private static DictionaryDatabase INSTANCE;

    public static DictionaryDatabase getDatabase(Context context, String databaseName) {
        if (INSTANCE == null) {

// return Room.databaseBuilder(context.getApplicationContext(), DictionaryDatabase.class,databaseName).build();
// Instead of Room builder we use RoomAssets builder -
// if no db file in my app private data dir then it will be copied from assets.
//    return RoomAsset
           INSTANCE = RoomAsset.databaseBuilder(context.getApplicationContext(),DictionaryDatabase.class,databaseName).build();
        }
        return INSTANCE;
    }

}
