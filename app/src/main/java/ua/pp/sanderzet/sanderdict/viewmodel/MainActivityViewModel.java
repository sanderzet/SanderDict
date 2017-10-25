package ua.pp.sanderzet.sanderdict.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * Created by sander on 29/08/17.
 */

public class MainActivityViewModel extends ViewModel {
  final private   MutableLiveData<Boolean> isActiveFabFavoriteAdd = new MutableLiveData<>();



    public final   LiveData<Boolean> getIsActiveFabFavoriteAdd() {
        return isActiveFabFavoriteAdd;
    }

    public void setIsActiveFabFavoriteAdd(Boolean isActive) {
        isActiveFabFavoriteAdd.setValue(isActive);
    }

}
