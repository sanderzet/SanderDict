package ua.pp.sanderzet.sanderdict.data.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import ua.pp.sanderzet.sanderdict.data.model.FavoriteModel;

/**
 * Created by sander on 15/10/17.
 */

public class Repository {
    private static Application mApplication;
private FavoriteDAO favoriteDAO;

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

    public LiveData<FavoriteModel> getWord (FavoriteModel favoriteModel) {
    return  favoriteDAO.getFavoriteItem(favoriteModel.getWord());
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




