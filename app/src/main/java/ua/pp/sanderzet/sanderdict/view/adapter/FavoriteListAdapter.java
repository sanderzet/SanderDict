package ua.pp.sanderzet.sanderdict.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import ua.pp.sanderzet.sanderdict.R;
import ua.pp.sanderzet.sanderdict.data.model.FavoriteModel;

/**
 * Created by sander on 31.10.17.
 */

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.FavoriteViewHolder> {

private List<FavoriteModel> favoriteModels;

    public FavoriteListAdapter(List<FavoriteModel> favoriteModels) {
        this.favoriteModels = favoriteModels;
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
FavoriteModel favoriteModel = favoriteModels.get(position);
holder.favoriteItemWord.setText(favoriteModel.getWord());
    }

    @Override
    public int getItemCount() {
        return favoriteModels.size();
    }
}

