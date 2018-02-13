package ua.pp.sanderzet.sanderdict.view.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ua.pp.sanderzet.sanderdict.data.DBSanderDict;
import ua.pp.sanderzet.sanderdict.view.adapter.SearchListAdapter;
import ua.pp.sanderzet.sanderdict.viewmodel.ListSearchViewModel;
import ua.pp.sanderzet.sanderdict.R;
import ua.pp.sanderzet.sanderdict.SanderDictConstants;

/**
 * A placeholder fragment containing a simple view.
 */

public class FragmentListSearch extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final String QUERY_EXTRA_KEY = "QUERY_EXTRA_KEY";
    private static final String QUERY_URI = "QUERY_URI";
private static  String LOG_TAG;
private static Uri CONTENT_URI;
private String word;
private String definition;
private View rootView;
private ListView listView;
private RecyclerView recyclerView;
private RecyclerView.Adapter rvAdapter;
    SimpleCursorAdapter scAdapter;
    private ListSearchViewModel viewModel ;
    private FloatingActionButton addWordToFavoriteFloatingActionButton;
private ImageButton ib_Favorite;
public static FragmentListSearch newInstance  (String query){
    FragmentListSearch fragmentListSearch = new FragmentListSearch();
Bundle args = new Bundle();
     args.putString("query", query);
     fragmentListSearch.setArguments(args);
    return fragmentListSearch;
}



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       rootView = inflater.inflate(R.layout.frag_list_search, container, false);
       addWordToFavoriteFloatingActionButton = rootView.findViewById(R.id.fab);
       listView = rootView.findViewById(R.id.listView);
ib_Favorite = rootView.findViewById(R.id.ib_Favorite);
       return rootView;

   }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LOG_TAG = SanderDictConstants.getLogTag();
        CONTENT_URI = SanderDictConstants.getContentUri();
        viewModel = ViewModelProviders.of((FragmentActivity)getActivity()).get(ListSearchViewModel.class);
        viewModel.getWordIsFavorite().observe((FragmentActivity)getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean o) {
                if (o) {
//                    addWordToFavoriteFloatingActionButton.setImageResource(R.drawable.ic_remove_24dp);
                    ib_Favorite.setImageResource(R.drawable.ic_star_24dp);
                }
                else {
//                    addWordToFavoriteFloatingActionButton.setImageResource(R.drawable.ic_add_24dp);
                    ib_Favorite.setImageResource(R.drawable.ic_star_border_24dp);
                }
            }
        });
addWordToFavoriteFloatingActionButton.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View view) {

//ToDo        Temporary it is so, then it`ll move from CursorLoader and will be used LiveDate
 viewModel.addWordToFavorite(word,definition);
    }
});


// RecyclerView adapter

        recyclerView = rootView.findViewById(R.id.rv_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAdapter = new SearchListAdapter();
        recyclerView.setAdapter(rvAdapter);



     /*   // Створюємо адаптер і настроюємо список
        String[] from = new String[] {SearchManager.SUGGEST_COLUMN_TEXT_1, DBSanderDict.KEY_DETAILS};
        int[] to = new int[] {R.id.tv_word, R.id.tv_definition};
        scAdapter = new SimpleCursorAdapter(getActivity(), R.layout.items, null, from, to, 0){
            @Override
            public void setViewText(TextView v, String text) {
                if (v.getId() == R.id.tv_definition) {

                    // З версіі N змінилася функція Html.fromHtml
                    // перевіряємо версію Андроїд для потрібної функції
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        v.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));}
                    else {v.setText(Html.fromHtml(text));}
                }
                else
                super.setViewText(v, text);
            }
        };


listView.setAdapter(scAdapter);*/
// Створюємо курсор-лоадер

                getLoaderManager().initLoader(0,null,  this);

    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle args = new Bundle();
Intent intent = getActivity().getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            //handle a search query
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);
            args.putString(QUERY_EXTRA_KEY, searchQuery);
            getLoaderManager().restartLoader(0, args, this);

        }
        else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            //handles a click on a search suggestion; showing word
           Uri uri = intent.getData();
            args.putString(QUERY_URI, uri.toString());
            Log.d(LOG_TAG, uri.toString());
            getLoaderManager().restartLoader(0, args, this);

        }
    }

    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {
        if (args != null) {
            if (args.containsKey(QUERY_EXTRA_KEY)) {
                String query = args.getString(QUERY_EXTRA_KEY);
                return new CursorLoader(getActivity(),
                        CONTENT_URI,
                        null,
                        null,
                        new String[]{query},
                        null
                );
            } else if (args.containsKey(QUERY_URI)) {
                Log.d(LOG_TAG, "ggg");
                                return new CursorLoader( getActivity(), Uri.parse(args.getString(QUERY_URI)), null, null, null, null);
            }
        }
        Log.d(LOG_TAG, "First on create loader");


        return new CursorLoader(getActivity(), CONTENT_URI, null, null,  null, null);
    }



    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        scAdapter.swapCursor(data);
        if(data.moveToFirst()) {
            word = data.getString(data.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
            definition = data.getString(data.getColumnIndex(DBSanderDict.KEY_DETAILS));
            if (!word.isEmpty() && !definition.isEmpty())
                try {
                    viewModel.searchIsDone(word, definition);            //            Snackbar.make(rootView, word + " /n" + definition, Snackbar.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
scAdapter.swapCursor(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
getLoaderManager().destroyLoader(0);
    }


   /* @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }*/
}

