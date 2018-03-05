package ua.pp.sanderzet.sanderdict.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import java.util.Calendar;
import java.util.List;

import ua.pp.sanderzet.sanderdict.data.model.DictionaryModel;
import ua.pp.sanderzet.sanderdict.data.model.FavoriteModel;
import ua.pp.sanderzet.sanderdict.data.repository.DictionaryRepository;
import ua.pp.sanderzet.sanderdict.data.repository.FavoriteRepository;

/**
 * Created by sander on 08/10/17.
 */

public class ListSearchViewModel extends AndroidViewModel {

private final FavoriteRepository favoriteRepository;
private final DictionaryRepository dictionaryRepository;
private final String dbName = "eng_ukr_uni.db";

    private final MutableLiveData<FavoriteModel> favoriteModelMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<String > newWord = new MutableLiveData<>();
    private final MutableLiveData<Boolean> wordIsFavorite;
    private final MutableLiveData<String> searchedWord = new MutableLiveData<>();
    private final LiveData<List<DictionaryModel>> suggestedWords;

    public ListSearchViewModel(Application application) {
        super(application);
        favoriteRepository = FavoriteRepository.getInstance(application);
        dictionaryRepository = DictionaryRepository.getInstance(application, dbName);
        suggestedWords = Transformations.switchMap(searchedWord, dictionaryRepository::getSuggestedWord);
        wordIsFavorite = (MutableLiveData)Transformations.switchMap(newWord, input -> favoriteRepository.isWordFavorite(input)
        );


    }

    public final LiveData getWordIsFavorite() {
        return wordIsFavorite;
    }

    //  When we type symbol in SerchView
    public void setSearchedWord(String pattern) {
        searchedWord.setValue(pattern);
    }

    public void searchIsDone(String word, String definition)
    {
        newWord.setValue(word);
    }

    public void addWordToFavorite (String word, String definition) {

        favoriteRepository.addWord(createFavoriteModel(word, definition));

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
