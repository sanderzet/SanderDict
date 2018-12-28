package ua.pp.sanderzet.sanderdict.data.repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import ua.pp.sanderzet.sanderdict.data.model.DictionariesModel;

public interface ListOfDictsApi {

    @GET("SanderDictList.json")
    Call<List<DictionariesModel>> getDictsList();

}
