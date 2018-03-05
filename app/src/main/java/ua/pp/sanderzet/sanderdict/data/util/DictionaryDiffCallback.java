package ua.pp.sanderzet.sanderdict.data.util;

import android.support.v7.util.DiffUtil;

import java.util.List;

import ua.pp.sanderzet.sanderdict.data.model.DictionaryModel;

/**
 * Created by sander on 28.02.18.
 */

public class DictionaryDiffCallback extends DiffUtil.Callback {
    private final List<DictionaryModel>  oldDictionaryList;
    private final List<DictionaryModel> newDictionaryList;

    public DictionaryDiffCallback(List<DictionaryModel> oldDictionaryList, List<DictionaryModel> newDictionaryList) {
this.oldDictionaryList = oldDictionaryList;
this.newDictionaryList = newDictionaryList;
    }

    @Override
    public int getOldListSize() {
        return oldDictionaryList.size();
    }

    @Override
    public int getNewListSize() {
        return newDictionaryList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

        return oldDictionaryList.get(oldItemPosition).getWord().equals(newDictionaryList.get(newItemPosition).getWord());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldDictionaryList.get(oldItemPosition).getDefinition().equals
                (newDictionaryList.get(newItemPosition).getDefinition())
                ;
    }
}
