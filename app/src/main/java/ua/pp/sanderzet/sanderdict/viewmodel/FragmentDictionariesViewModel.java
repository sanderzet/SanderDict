package ua.pp.sanderzet.sanderdict.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.pp.sanderzet.sanderdict.SanderDictConstants;
import ua.pp.sanderzet.sanderdict.data.model.DictionariesModel;
import ua.pp.sanderzet.sanderdict.data.repository.ListOfDictsApi;

public class FragmentDictionariesViewModel extends AndroidViewModel {

    final private MutableLiveData<List<DictionariesModel>> dictionariesModelMutableLiveData = new MutableLiveData<>();

    private static ListOfDictsApi listOfDictsApi;
    private DictionariesModel dictionariesModel;


    public FragmentDictionariesViewModel(@NonNull Application application) {
        super(application);

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(SanderDictConstants.getDictListUrl()).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        listOfDictsApi = retrofit.create(ListOfDictsApi.class);
    }

    public void receiveDictsList() {
        listOfDictsApi.getDictsList().enqueue(new Callback<List<DictionariesModel>>() {
            @Override
            public void onResponse(Call<List<DictionariesModel>> call, Response<List<DictionariesModel>> response) {

                if (response.isSuccessful() && !response.body().isEmpty()) {
                    List<DictionariesModel> dictionariesModels = new ArrayList<>();
                    dictionariesModels.addAll(response.body());
                    dictionariesModelMutableLiveData.postValue(dictionariesModels);
                }
            }

            @Override
            public void onFailure(Call<List<DictionariesModel>> call, Throwable t) {
                Log.d(SanderDictConstants.getLogTag(), "onFailure: " + t);
            }
        });
    }

    public MutableLiveData<List<DictionariesModel>> getDictionariesModelMutableLiveData() {
        return dictionariesModelMutableLiveData;
    }

    public void onClickItem(String fileName, int viewID) {

    }

}
