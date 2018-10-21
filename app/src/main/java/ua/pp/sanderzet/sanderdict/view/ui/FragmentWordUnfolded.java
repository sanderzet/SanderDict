package ua.pp.sanderzet.sanderdict.view.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import ua.pp.sanderzet.sanderdict.R;
import ua.pp.sanderzet.sanderdict.data.model.DictionaryModel;
import ua.pp.sanderzet.sanderdict.viewmodel.FavoriteListViewModel;
import ua.pp.sanderzet.sanderdict.viewmodel.MainActivityViewModel;

/**
 * Created by sander on 03.03.18.
 */

public class FragmentWordUnfolded extends Fragment {
    private View rootView;
    private MainActivityViewModel mainActivityViewModel;
    private FavoriteListViewModel favoriteListViewModel;
    private TextView word;
    private TextView definition;
    private ImageButton ib_DoFavorite;
    private Boolean isFavorite;
    private DictionaryModel  dictionaryModel;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


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
ib_DoFavorite = rootView.findViewById(R.id.ib_DoFavorite);
        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        favoriteListViewModel = ViewModelProviders.of(getActivity()).get(FavoriteListViewModel.class);

        mainActivityViewModel.getUnfoldedWord().observe(getActivity(), new Observer<DictionaryModel>() {
            @Override
            public void onChanged(@Nullable DictionaryModel dm ) {
                dictionaryModel = dm;
                String word = dictionaryModel.getWord();
                String definition = dictionaryModel.getDefinition();
                if (word == null) word ="";
                FragmentWordUnfolded.this.word.setText(word);
                // З версіі N змінилася функція Html.fromHtml
                // перевіряємо версію Андроїд для потрібної функції
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    FragmentWordUnfolded.this.definition.setText(Html.fromHtml(definition, Html.FROM_HTML_MODE_COMPACT));
                }
                else {
                    FragmentWordUnfolded.this.definition.setText(Html.fromHtml(definition));}
                /*Inform favoriteListViewModel that search is done and we have new word*
                 */
                favoriteListViewModel.searchIsDone(word, definition);
            }

        });

        favoriteListViewModel.getWordIsFavorite().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean o) {
                isFavorite = o;
                if (isFavorite) ib_DoFavorite.setImageResource(R.drawable.ic_star_24dp);
                else ib_DoFavorite.setImageResource(R.drawable.ic_star_border_24dp);
            }
        });

        ib_DoFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dictionaryModel != null && dictionaryModel.getWord() != null)
                {
                    if (isFavorite) {
                        favoriteListViewModel.removeWordFromFavorite(dictionaryModel.getWord());
                    } else {
                        favoriteListViewModel.addWordToFavorite(dictionaryModel.getWord(), dictionaryModel.getDefinition());
                    }
                }
            }
        });

    }


}
