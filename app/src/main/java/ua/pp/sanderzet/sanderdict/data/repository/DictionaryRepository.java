package ua.pp.sanderzet.sanderdict.data.repository;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import ua.pp.sanderzet.sanderdict.data.SanderDictProvider;
import ua.pp.sanderzet.sanderdict.data.model.DictionaryModel;
import ua.pp.sanderzet.sanderdict.data.util.DBFromAssetsLiveData;

/**
 * Created by sander on 18.02.18.
 */

public class DictionaryRepository {
    private final String LOG_TAG = "SanderZet"+getClass().getName();
    private static Application mApplication;
    private static DictionaryRepository mDictionaryRepository = null;
    private DictionaryDAO dictionaryDAO;
    private String databaseName;

    //    Make DictionaryRepository as Singleton

    public DictionaryRepository(Application application, String databaseName) {
        this.mApplication = application;
        this.databaseName = databaseName;
        dictionaryDAO = DictionaryDatabase.getDatabase(application, databaseName).dictionaryDAO();


    }

    public static DictionaryRepository getInstance (Application application, String databaseName) {
    if (mDictionaryRepository == null || (application != mApplication ))
        mDictionaryRepository = new DictionaryRepository(application, databaseName);
    return mDictionaryRepository;
    }

    public  LiveData<List<DictionaryModel>> getSuggestedWords(String query) {
        String pattern = (query.isEmpty())? query : query+"%";
        return dictionaryDAO.getSuggestedDictionaryItems(pattern);
    }

    public LiveData<DictionaryModel> getSearchedWord (String word) {
        return dictionaryDAO.getDictionaryItem(word);
    }


}

