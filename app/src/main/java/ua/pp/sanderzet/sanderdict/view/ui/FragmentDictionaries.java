package ua.pp.sanderzet.sanderdict.view.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ua.pp.sanderzet.sanderdict.R;
import ua.pp.sanderzet.sanderdict.data.model.DictionariesModel;
import ua.pp.sanderzet.sanderdict.view.adapter.DictionariesListAdapter;
import ua.pp.sanderzet.sanderdict.viewmodel.FragmentDictionariesViewModel;

public class FragmentDictionaries extends Fragment {
    private View rootview;
    private FragmentActivity myActivity;
    private RecyclerView recyclerView;
    private DictionariesListAdapter dictionariesListAdapter;
    private FragmentDictionariesViewModel fragmentDictionariesViewModel;
    private MutableLiveData<List<DictionariesModel>> listMutableLiveData;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.frag_dicts, container, false);
        return rootview;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myActivity = getActivity();
        recyclerView = rootview.findViewById(R.id.rv_dicts);


        fragmentDictionariesViewModel = ViewModelProviders.of(myActivity).get(FragmentDictionariesViewModel.class);
        fragmentDictionariesViewModel.receiveDictsList();

        fragmentDictionariesViewModel. getDictionariesModelMutableLiveData().
                observe(this,new Observer<List<DictionariesModel>>() {
                    @Override
                    public void onChanged (@Nullable List < DictionariesModel > dictionariesModels) {
                        if (recyclerView.getAdapter() == null ) {
                            dictionariesListAdapter = new DictionariesListAdapter(dictionariesModels, myActivity);
                            recyclerView.setAdapter(dictionariesListAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(myActivity));

                                                    }
                                                    else dictionariesListAdapter.updateList(dictionariesModels);
                    }
                });

    }

    public void setOnClickItem(DictionariesModel dictionariesModel, View view) {
        fragmentDictionariesViewModel.onClickItem(dictionariesModel.getFileName(), view.getId());
    }

}
