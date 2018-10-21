package ua.pp.sanderzet.sanderdict.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import java.util.List;

import ua.pp.sanderzet.sanderdict.data.model.FavoriteModel;

/**
 * Created by sander on 15/10/17.
 */

public class FavoriteRepository {
    private static Application mApplication;
private FavoriteDAO favoriteDAO;
private final MutableLiveData<Boolean> wordIsFavorite = new MutableLiveData<>() ;

//    Make FavoriteRepository as Singleton

    private static FavoriteRepository mFavoriteRepository = null;
    private FavoriteRepository(Application application) {
    mApplication = application;
    favoriteDAO = FavoriteDatabase.getDatabase(mApplication).favoriteDAO();
    }

    public static FavoriteRepository getInstance (Application application) {
        if ((mFavoriteRepository == null) || (mApplication != application)) {
            mFavoriteRepository = new FavoriteRepository(application);

        }
        return mFavoriteRepository;
    }



    public FavoriteModel getFavoriteModel (String word) {
        return favoriteDAO.getFavoriteModel(word);
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
            favoriteDAO.addFavorite(favoriteModel);
            return null;
        }
    }.execute(favoriteModel);

}

    public void removeWord(FavoriteModel favoriteModel) {
        new AsyncTask<FavoriteModel, Void, Void>()
        {
            @Override
            protected Void doInBackground(FavoriteModel... favoriteModels) {
                favoriteDAO.deleteFavorite(favoriteModel);
                return null;
            }
        }.execute(favoriteModel);
    }


    public LiveData<List<FavoriteModel>> getAllWords() {
        return favoriteDAO.getAllFavoriteItems();
    }

}






