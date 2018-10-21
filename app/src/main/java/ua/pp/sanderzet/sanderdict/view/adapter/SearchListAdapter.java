package ua.pp.sanderzet.sanderdict.view.adapter;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.function.ToDoubleBiFunction;

import ua.pp.sanderzet.sanderdict.R;
import ua.pp.sanderzet.sanderdict.data.model.DictionaryModel;
import ua.pp.sanderzet.sanderdict.data.util.DictionaryDiffCallback;
import ua.pp.sanderzet.sanderdict.view.ui.FragmentListSearch;
import ua.pp.sanderzet.sanderdict.viewmodel.MainActivityViewModel;

/**
 * Created by sander on 12.02.18.
 */

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

private List<DictionaryModel> dictionaryModels;
private static MainActivityViewModel mainActivityViewModel;

    public SearchListAdapter(List<DictionaryModel> dictionaryModels, MainActivityViewModel mainActivityViewModel) {
this.dictionaryModels = dictionaryModels;
this.mainActivityViewModel = mainActivityViewModel;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
    public TextView wordTextView;
public DictionaryModel dictionaryModel;

        public ViewHolder(View itemView) {
            super(itemView);
wordTextView = itemView.findViewById(R.id.dictionaryItemWord);
wordTextView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mainActivityViewModel.setUnfoldedWord(dictionaryModel);
    }
});
        }
    }


    @Override

    public SearchListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
              return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(SearchListAdapter.ViewHolder holder, int position) {
        holder.dictionaryModel = dictionaryModels.get(position);
holder.wordTextView.setText(dictionaryModels.get(position).getWord());

/*
Yes, we are setting listener each time when items is shown, but if we do it in holder,
        we can`t receive dictionaryModels, only position.
Eventually, listener is light object, so it don't use many resources.
*/

//holder.wordTextView.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        mainActivityViewModel.setUnfoldedWord(holder.dictionaryModel);
//    }
//});

    }

    @Override
    public int getItemCount() {
        return dictionaryModels.size();
    }


    public void updateList(List<DictionaryModel> newList) {
        final DiffUtil.DiffResult diffResult =DiffUtil.calculateDiff(new DictionaryDiffCallback(dictionaryModels, newList));
dictionaryModels = newList;
diffResult.dispatchUpdatesTo(this);


    }
}
