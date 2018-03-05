package ua.pp.sanderzet.sanderdict.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ua.pp.sanderzet.sanderdict.data.model.DictionaryModel;
import ua.pp.sanderzet.sanderdict.data.model.FavoriteModel;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

/**
 * Created by sander on 13.02.18.
 */

@Dao
public interface DictionaryDAO {

    @Query("select * from dict ")
    LiveData<List<DictionaryModel>> getAllDictionaryItems();

    @Query("select * from dict where word like :suggestedWords")
    LiveData<List<DictionaryModel>> getSuggestedDictionaryItems(String suggestedWords);

    @Query("select * from dict where word like :searchedWord ")
    LiveData<DictionaryModel> getDictionaryItem(String searchedWord);


    @Insert(onConflict = IGNORE)
    void addDictionary(DictionaryModel dictionaryModel);



}
