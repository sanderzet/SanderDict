package ua.pp.sanderzet.sanderdict.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import java.util.Calendar;

import ua.pp.sanderzet.sanderdict.data.model.FavoriteModel;
import ua.pp.sanderzet.sanderdict.data.repository.Repository;

/**
 * Created by sander on 08/10/17.
 */

public class ListSearchViewModel extends AndroidViewModel {

private final Repository repository;

    private final MutableLiveData<FavoriteModel> favoriteModelMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<String > newWord = new MutableLiveData<>();
    private final MutableLiveData<Boolean> wordIsFavorite;

    public ListSearchViewModel(Application application) {
        super(application);
        repository = Repository.getInstance(application);
        wordIsFavorite = (MutableLiveData<Boolean>)Transformations.switchMap(newWord, input -> repository.isWordFavorite(input)
        );
    }

    public final MutableLiveData getWordIsFavorite() {
        return wordIsFavorite;
    }


    public void searchIsDone(String word, String definition)
    {
        newWord.setValue(word);
    }

    public void addWordToFavorite (String word, String definition) {

        repository.addWord(createFavoriteModel(word, definition));

        //        Word has added, so fab in UI must be invisible
        wordIsFavorite.setValue(false);
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
