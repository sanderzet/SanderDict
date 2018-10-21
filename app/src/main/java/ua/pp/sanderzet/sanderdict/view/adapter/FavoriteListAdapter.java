package ua.pp.sanderzet.sanderdict.view.adapter;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import ua.pp.sanderzet.sanderdict.R;
import ua.pp.sanderzet.sanderdict.data.model.FavoriteModel;
import ua.pp.sanderzet.sanderdict.data.util.DictionaryDiffCallback;
import ua.pp.sanderzet.sanderdict.data.util.FavoriteDiffCallback;
import ua.pp.sanderzet.sanderdict.viewmodel.MainActivityViewModel;

/**
 * Created by sander on 31.10.17.
 */

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.FavoriteViewHolder> {

private List<FavoriteModel> favoriteModels;
private MainActivityViewModel mainActivityViewModel;

    public FavoriteListAdapter(List<FavoriteModel> favoriteModels, MainActivityViewModel mainActivityViewModel) {
        this.favoriteModels = favoriteModels;
        this.mainActivityViewModel = mainActivityViewModel;
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private TextView favoriteItemWord;
        public FavoriteViewHolder(View itemView) {
            super(itemView);
            favoriteItemWord = itemView.findViewById(R.id.favoriteItemWord);

        }
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.favorite_item, parent, false);
        return new FavoriteViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(FavoriteListAdapter.FavoriteViewHolder holder, int position) {

holder.favoriteItemWord.setText(favoriteModels.get(position).getWord());

        holder.favoriteItemWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
mainActivityViewModel.setUnfoldedFavoriteWord(favoriteModels.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return favoriteModels.size();
    }

    public void updateList(List<FavoriteModel> newList) {
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new FavoriteDiffCallback(favoriteModels, newList));
        favoriteModels = newList;
        diffResult.dispatchUpdatesTo(this);

    }
}

