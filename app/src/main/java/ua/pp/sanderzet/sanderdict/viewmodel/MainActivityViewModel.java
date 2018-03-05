package ua.pp.sanderzet.sanderdict.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import ua.pp.sanderzet.sanderdict.data.model.DictionaryModel;
import ua.pp.sanderzet.sanderdict.data.repository.DictionaryRepository;
import ua.pp.sanderzet.sanderdict.data.util.DBFromAssetsLiveData;

/**
 * Created by sander on 29/08/17.
 */

public class MainActivityViewModel extends AndroidViewModel {

    private final String DIR_FOR_DB_IN_ASSETS = "databases";

    private final Context context;
    private String dictionaryDBName;
    private DictionaryRepository dictionaryRepository;
    private final MutableLiveData<String> queryLiveData = new MutableLiveData<>();
    private final LiveData<List<DictionaryModel>> listSuggestedWord = Transformations.switchMap(queryLiveData, query -> {
        return dictionaryRepository.getSuggestedWord(query);
            });
private final MutableLiveData<DictionaryModel> unfoldedWord = new MutableLiveData<>();

    final private MutableLiveData<Boolean> isActiveFabFavoriteAdd = new MutableLiveData<>();

    //   Todo  We can use it for progressBar during copying db



    public MainActivityViewModel(@NonNull Application application) {
        super(application);
context = application.getApplicationContext();
//        ToDo dictionaryDBName must have been taken from Preferences
//        dictionaryDBName = "eng_ukr_uni.db";
dictionaryDBName = getDefaultDbInAssets(application);

        dictionaryRepository = DictionaryRepository.getInstance(application, dictionaryDBName );
    }


private String getDefaultDbInAssets(Application application) {
        String dbName = null;
    try {
        String[] files = application.getAssets().list(DIR_FOR_DB_IN_ASSETS);
        dbName = files[0];
    } catch (IOException e) {
        e.printStackTrace();
    }
return dbName;
    }

    public void setIsActiveFabFavoriteAdd(Boolean isActive) {
        isActiveFabFavoriteAdd.setValue(isActive);
    }

    public LiveData<List<DictionaryModel>> getListSuggestedWord() {
        return listSuggestedWord;
    }
    public void setQuery(String query) {
queryLiveData.setValue(query);
    }

public void setUnfoldedWord(DictionaryModel word) {
        unfoldedWord.setValue(word);
}


    public LiveData<DictionaryModel> getUnfoldedWord () {
        return unfoldedWord;
    }

}
