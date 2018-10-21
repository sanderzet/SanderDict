package ua.pp.sanderzet.sanderdict.view.ui;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
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
import ua.pp.sanderzet.sanderdict.data.model.FavoriteModel;
import ua.pp.sanderzet.sanderdict.viewmodel.FavoriteListViewModel;
import ua.pp.sanderzet.sanderdict.viewmodel.MainActivityViewModel;

public class FragmentFavoriteWordUnfolded extends Fragment {

    private View rootView;
    private MainActivityViewModel mainActivityViewModel;
    private FavoriteListViewModel favoriteListViewModel;
    private TextView word;
    private TextView definition;
    private ImageButton ib_DoFavorite;
    private Boolean isFavorite;
    private FavoriteModel favoriteModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.favorite_word_unfolded, container, false);
        return rootView;
            }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        word = rootView.findViewById(R.id.fwordUnfolded);
        definition = rootView.findViewById(R.id.fdefinitionUnfolded);
        ib_DoFavorite = rootView.findViewById(R.id.ib_FDoFavorite);


//       Building of dialog window
AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
builder.setMessage("Remove the word from favorite ?");

builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
    @Override
    public void onClick(DialogInterface dialog, int which) {
        try {
            favoriteListViewModel.removeWordFromFavorite(favoriteModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
//    Must back to favorite list
        mainActivityViewModel.setBackStack(true);
    }
});
builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
});
AlertDialog alertDialog = builder.create();

///*End of building dialog window*//*




        favoriteListViewModel = ViewModelProviders.of(getActivity()).get(FavoriteListViewModel.class);
        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

        mainActivityViewModel.getUnfoldedFavoriteWord().observe(getActivity(), new Observer<FavoriteModel>() {
            @Override
            public void onChanged(@Nullable FavoriteModel fm) {
                favoriteModel = fm;
                if (favoriteModel.getWord() != null)
                {
                    word.setText(favoriteModel.getWord());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        definition.setText(Html.fromHtml(favoriteModel.getDefinition(), Html.FROM_HTML_MODE_COMPACT));
                    }
                    else {
                        definition.setText(Html.fromHtml(favoriteModel.getDefinition()));}
                }

            }
        });


        ib_DoFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
alertDialog.show();

            }
        });


    }
}
