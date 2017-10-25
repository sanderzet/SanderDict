package ua.pp.sanderzet.sanderdict.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.Calendar;

import ua.pp.sanderzet.sanderdict.data.model.FavoriteModel;
import ua.pp.sanderzet.sanderdict.data.repository.Repository;

/**
 * Created by sander on 08/10/17.
 */

public class ListSearchViewModel extends AndroidViewModel {

private final Repository repository;
    private final MutableLiveData<Boolean>  wordNotInFavorite = new MutableLiveData<>();

    public ListSearchViewModel(Application application) {
        super(application);
        repository = Repository.getInstance(application);

    }

    public final MutableLiveData getWordNotInFavorite () {
        return wordNotInFavorite;
    }


    public void searchIsDone(FavoriteModel favoriteModel) {
        repository.getWord(favoriteModel);
    }

    public void addWordToFavorite (String word, String definition) {

        repository.addWord(createFavoriteModel(word, definition));

        //        Word has added, so fab in UI must be invisible
        wordNotInFavorite.setValue(false);
    }

    private FavoriteModel createFavoriteModel (String word, String definition) {
        FavoriteModel favoriteModel = new FavoriteModel();
        favoriteModel.setWord(word);
        favoriteModel.setDefinition(definition);
        favoriteModel.setStars(0);
        favoriteModel.setDateOfStoring(Calendar.getInstance().getTime());
        return favoriteModel;
    }
}
