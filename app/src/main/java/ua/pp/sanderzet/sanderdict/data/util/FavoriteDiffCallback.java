package ua.pp.sanderzet.sanderdict.data.util;

import android.support.v7.util.DiffUtil;

import java.util.List;

import ua.pp.sanderzet.sanderdict.data.model.FavoriteModel;

/**
 * Created by sander on 28.02.18.
 */

public class FavoriteDiffCallback extends DiffUtil.Callback {
    private final List<FavoriteModel> oldFavoriteModel;
    private final List<FavoriteModel> newFavoriteModels;

    @Override
    public int getOldListSize() {
        return oldFavoriteModel.size();
    }

    @Override
    public int getNewListSize() {
        return newFavoriteModels.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return (oldFavoriteModel.get(oldItemPosition).getId()) == (newFavoriteModels.get(newItemPosition).getId()) ;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldFavoriteModel.get(oldItemPosition).getWord().equals(
                newFavoriteModels.get(newItemPosition).getWord()
        );
    }

    public FavoriteDiffCallback(List<FavoriteModel> oldFavoriteModel, List<FavoriteModel> newFavoriteModels) {
        this.oldFavoriteModel = oldFavoriteModel;
        this.newFavoriteModels = newFavoriteModels;
    }
}
