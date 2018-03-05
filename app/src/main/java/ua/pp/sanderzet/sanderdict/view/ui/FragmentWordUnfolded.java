package ua.pp.sanderzet.sanderdict.view.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

import ua.pp.sanderzet.sanderdict.R;
import ua.pp.sanderzet.sanderdict.data.model.DictionaryModel;
import ua.pp.sanderzet.sanderdict.viewmodel.MainActivityViewModel;

/**
 * Created by sander on 03.03.18.
 */

public class FragmentWordUnfolded extends Fragment {
    private View rootView;
    private MainActivityViewModel mainActivityViewModel;
    private TextView word;
    private TextView definition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.word_unfolded, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        word = rootView.findViewById(R.id.wordUnfolded);
        definition = rootView.findViewById(R.id.definitionUnfolded);

        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        mainActivityViewModel.getUnfoldedWord().observe(getActivity(), new Observer<DictionaryModel>() {
            @Override
            public void onChanged(@Nullable DictionaryModel dictionaryModel) {
        word.setText(dictionaryModel.getWord());
                // З версіі N змінилася функція Html.fromHtml
                // перевіряємо версію Андроїд для потрібної функції
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    definition.setText(Html.fromHtml(dictionaryModel.getDefinition(),Html.FROM_HTML_MODE_COMPACT));
                }
                else {definition.setText(Html.fromHtml(dictionaryModel.getDefinition()));}
            }
        });

    }
}
