package ua.pp.sanderzet.sanderdict.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.pp.sanderzet.sanderdict.SanderDictConstants;
import ua.pp.sanderzet.sanderdict.data.model.DictionaryModel;
import ua.pp.sanderzet.sanderdict.data.model.FavoriteModel;
import ua.pp.sanderzet.sanderdict.data.model.DictionariesModel;
import ua.pp.sanderzet.sanderdict.data.repository.DictionaryRepository;
import ua.pp.sanderzet.sanderdict.data.repository.ListOfDictsApi;
import ua.pp.sanderzet.sanderdict.data.util.DictToDbWorker;

/**
 * Created by sander on 29/08/17.
 */

public class MainActivityViewModel extends AndroidViewModel {

    private final String DIR_FOR_DB_IN_ASSETS = "databases";
    private final String TAG_WORKER_TO_DB = "workerToDB";

    private WorkManager mWorkManager;
    private LiveData<List<WorkInfo>> workerToDbInfo;

    private static Retrofit retrofit;
    private SearchView searchView;
    private final Context context;
    private String dictionaryDBName;
    private DictionaryRepository dictionaryRepository;
    //    From SearchView in app
    private final MutableLiveData<String> queryLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> messageForUser = new MutableLiveData<>();
    //Query from another app through Share
    private final MutableLiveData<String> queryFromAnotherApp = new MutableLiveData<>();
    private final LiveData<List<DictionaryModel>> listSuggestedWords = Transformations.switchMap(queryLiveData, query -> {
        return dictionaryRepository.getSuggestedWords(query);
    });

    private final MutableLiveData<DictionaryModel> unfoldedWord = new MutableLiveData<>();


    final private MutableLiveData<Boolean> isActiveFabFavoriteAdd = new MutableLiveData<>();

    final private MutableLiveData<FavoriteModel> unfoldedFavoriteWord = new MutableLiveData<>();

    /*If we need to go back, for example, after removing word from favorite list*/
    final private MutableLiveData<Boolean> backStack = new MutableLiveData<>();


    private final LiveData<DictionaryModel> wordFromAnotherApp = Transformations.switchMap(
            queryFromAnotherApp, new Function<String, LiveData<DictionaryModel>>() {

                @Override
                public LiveData<DictionaryModel> apply(String input) {
                    String myInput = input.trim();
                    Integer myInputLength = myInput.length();
                    LiveData<List<DictionaryModel>> suggestedWords = dictionaryRepository.getSuggestedWords(input);
                    LiveData<DictionaryModel> dictionaryModelLiveData = Transformations.map(suggestedWords,
                            new Function<List<DictionaryModel>, DictionaryModel>() {
                                @Override
                                public DictionaryModel apply(List<DictionaryModel> dictionaryModels) {
                                    if (dictionaryModels != null && !dictionaryModels.isEmpty()) {
                                        return dictionaryModels.get(0);
                                    } else {

                                        if (myInputLength > 0) {
                                            setQueryFromAnotherApp(myInput.substring(0, myInputLength - 1));
                                        }
                                        return null;
                                    }
                                }

                                ;
                            });
                    return dictionaryModelLiveData;
                }
            });


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
//        ToDo dictionaryDBName must have been taken from Preferences
//        dictionaryDBName = "eng_ukr_uni.db";
        dictionaryDBName = getDefaultDbInAssets(application);

        dictionaryRepository = DictionaryRepository.getInstance(application, dictionaryDBName);
        backStack.setValue(false);
        mWorkManager = WorkManager.getInstance();
        workerToDbInfo = mWorkManager.getWorkInfosByTagLiveData(TAG_WORKER_TO_DB);


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

    public LiveData<List<DictionaryModel>> getListSuggestedWords() {
        return listSuggestedWords;
    }

    public void setQuery(String query) {
        queryLiveData.setValue(query);
    }

    //    From another app through intent.send

    public void setQueryFromAnotherApp(String word) {
        queryFromAnotherApp.setValue(word);
    }


    public void saveSearchView(SearchView searchView) {
        this.searchView = searchView;
    }

    public SearchView retainSearchView() {
        return searchView;
    }

    public void setUnfoldedWord(DictionaryModel word) {
        unfoldedWord.setValue(word);
    }

    public void setUnfoldedFavoriteWord(FavoriteModel favoriteWord) {
        unfoldedFavoriteWord.setValue(favoriteWord);
    }

    public LiveData<DictionaryModel> getUnfoldedWord() {
        return unfoldedWord;
    }

    public LiveData<DictionaryModel> getWordFromAnotherApp() {
        return wordFromAnotherApp;
    }

    public LiveData<String> getMessageForUser() {
        return messageForUser;
    }

    public LiveData<FavoriteModel> getUnfoldedFavoriteWord() {
        return unfoldedFavoriteWord;
    }

    public MutableLiveData<Boolean> getBackStack() {
        return backStack;
    }

    public LiveData<List<WorkInfo>> getWorkerToDbInfo() {
        return workerToDbInfo;
    }




    public void setBackStack(Boolean aBoolean) {
        backStack.setValue(aBoolean);
    }


    public void addDictFromDir() {

        try {
// Convert to db from files in external storage /Sanderzet directory.
//            Files must have extension SanderDictConstants.EXT_FILE_TO_DB

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {


                File dir = context.getExternalFilesDir(null);
//            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "SanderDict" + File.separator);
                FilenameFilter filenameFilter = new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        if (name.endsWith(SanderDictConstants.EXT_FILE_TO_DB)) return true;
                        else return false;
                    }
                };
                String[] filesName = dir.list(filenameFilter);
                String filePath = dir.toString();
//    If exist dir /SanderDict on external storage & in this dir exist one or more file .dsl.dz
                if (filesName != null && filesName.length != 0) {


//                Create data bind for Worker
                    Data.Builder dBuilder = new Data.Builder();
                    Data filesToDb = dBuilder.putStringArray(SanderDictConstants.KEY_FILES_TO_DB, filesName).
                            putString(SanderDictConstants.KEY_PATH_TO_DB, filePath).build();
                    OneTimeWorkRequest dictToDbRequest = new OneTimeWorkRequest.Builder(DictToDbWorker.class).
                            setInputData(filesToDb).
                            addTag(TAG_WORKER_TO_DB).build();
                    mWorkManager.enqueue(dictToDbRequest);

                } else ;
//                Snackbar.make(constraintLayout, "No appropriate directory or files have been found", Snackbar.LENGTH_LONG).show();

            } else ;
//            Snackbar.make(constraintLayout, "No external storage have been found", LENGTH_INDEFINITE).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
