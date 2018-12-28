package ua.pp.sanderzet.sanderdict.data.util;

import android.support.v7.util.DiffUtil;

import java.util.List;

import ua.pp.sanderzet.sanderdict.data.model.DictionariesModel;

/**
 * Created by sander on 28.02.18.
 */

public class DectionariesDiffCallback extends DiffUtil.Callback {
    private final List<DictionariesModel> oldDictionariesModel;
    private final List<DictionariesModel> newDictionariesModels;

    @Override
    public int getOldListSize() {
        return oldDictionariesModel.size();
    }

    @Override
    public int getNewListSize() {
        return newDictionariesModels.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return (oldDictionariesModel.get(oldItemPosition).getId()) == (newDictionariesModels.get(newItemPosition).getId()) ;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldDictionariesModel.get(oldItemPosition).getId().equals(
                newDictionariesModels.get(newItemPosition).getId()
        );
    }

    public DectionariesDiffCallback(List<DictionariesModel> oldDictionariesModel, List<DictionariesModel> newDictionariesModels) {
        this.oldDictionariesModel = oldDictionariesModel;
        this.newDictionariesModels = newDictionariesModels;
    }
}
