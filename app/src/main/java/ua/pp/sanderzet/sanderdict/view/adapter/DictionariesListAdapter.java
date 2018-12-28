package ua.pp.sanderzet.sanderdict.view.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import ua.pp.sanderzet.sanderdict.R;
import ua.pp.sanderzet.sanderdict.data.model.DictionariesModel;
import ua.pp.sanderzet.sanderdict.data.model.DictionaryModel;
import ua.pp.sanderzet.sanderdict.data.util.DectionariesDiffCallback;
import ua.pp.sanderzet.sanderdict.view.dialog.DictionaryDownloadDialogFragment;

public class DictionariesListAdapter extends RecyclerView.Adapter<DictionariesListAdapter.ViewHolder>  {

List<DictionariesModel> dictionariesModels;
Resources res ;
Context context;
FragmentActivity activity;

    public DictionariesListAdapter(List<DictionariesModel> dictionariesModels, FragmentActivity activity) {
        this.dictionariesModels = dictionariesModels;
        this.activity = activity;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        res = viewGroup.getResources();
       context = viewGroup.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.frag_dicts_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String inLang = dictionariesModels.get(position).getInLang();
        String outLang = dictionariesModels.get(position).getOutLang();
        String dictName = "";

        if (res != null) {
            switch (inLang) {
                case "EN": dictName = res.getString(R.string.EN);
                break;
                case "UA": dictName = res.getString(R.string.UA);
                break;
                case "PL": dictName = res.getString(R.string.PL);
                break;
                case "RU": dictName = res.getString(R.string.RU);
                            }
                            dictName = dictName + " -> ";
            switch (outLang) {
                case "EN": dictName = dictName + res.getString(R.string.EN);
                    break;
                case "UA": dictName = dictName + res.getString(R.string.UA);
                    break;
                case "PL": dictName = dictName + res.getString(R.string.PL);
                    break;
                case "RU": dictName = dictName + res.getString(R.string.RU);
            }


        }

        holder.tv_dict.setText(dictName);


    }

    @Override
    public int getItemCount() {
        return dictionariesModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
ImageView iv_checked;
TextView tv_dict;
ImageView iv_downloaded;

// We create in ViewHolder listener which then will be realized in fragment calling this adapter
//        through implimantation metod onItemClick in interface OnItemClickListener


        public ViewHolder(@NonNull View itemView)  {
            super(itemView);
            iv_checked = itemView.findViewById(R.id.iv_checked);
            tv_dict = itemView.findViewById(R.id.tv_dict);
            iv_downloaded = itemView.findViewById(R.id.iv_downloaded);
            iv_checked.setOnClickListener(this);
            tv_dict.setOnClickListener(this);
            iv_downloaded.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Bundle args = new Bundle();
            args.putInt("download", R.string.df_dictionary_download);
            DictionaryDownloadDialogFragment dictionaryDownloadDF = new DictionaryDownloadDialogFragment();
            dictionaryDownloadDF.setArguments(args);

            dictionaryDownloadDF.show(activity.getSupportFragmentManager(), "dictionatyDownloadDF");
        }
    }

    public void updateList(List<DictionariesModel> newList) {
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DectionariesDiffCallback(dictionariesModels, newList));
        dictionariesModels = newList;
        diffResult.dispatchUpdatesTo(this);
    }



}
