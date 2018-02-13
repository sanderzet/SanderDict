package ua.pp.sanderzet.sanderdict.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import ua.pp.sanderzet.sanderdict.data.model.FavoriteModel;

/**
 * Created by sander on 15/10/17.
 */

public class Repository {
    private static Application mApplication;
private FavoriteDAO favoriteDAO;
private final MutableLiveData<Boolean> wordIsFavorite = new MutableLiveData<>() ;

//    Make Repository as Singleton

    private static Repository mRepository = null;
    private Repository(Application application) {
    mApplication = application;
    favoriteDAO = FavoriteDatabase.getDatabase(mApplication).favoriteDAO();
    }

    public static Repository getInstance (Application application) {
        if ((mRepository == null) || (mApplication != application)) {
            mRepository = new Repository(application);

        }
        return mRepository;
    }

    public LiveData<FavoriteModel> getWord (String word) {
    return  favoriteDAO.getFavoriteItem(word);
    }

public LiveData<Boolean> isWordFavorite (String word) {
    return Transformations.map(getWord(word), favoriteModel -> {
        if (favoriteModel == null) return false;
        else return true;
    });

}


public void addWord (FavoriteModel favoriteModel) {
    new  AsyncTask<FavoriteModel, Void, Void>()
    {
        @Override
        protected Void doInBackground(FavoriteModel... favoriteModels) {
            favoriteDAO.addFavorite(favoriteModels[0]);
            return null;
        }
    }.execute(favoriteModel);

}

    }




