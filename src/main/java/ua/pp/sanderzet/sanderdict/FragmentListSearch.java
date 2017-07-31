package ua.pp.sanderzet.sanderdict;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.ListFragment;
import android.content.CursorLoader;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */

public class FragmentListSearch extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String QUERY_EXTRA_KEY = "QUERY_EXTRA_KEY";
    private static final String QUERY_URI = "QUERY_URI";
    SimpleCursorAdapter scAdapter;

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
      return  inflater.inflate(R.layout.frag_list_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Створюємо адаптер і настроюємо список
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



        setListAdapter(scAdapter);
// Створюємо курсор-лоадер

        getLoaderManager().initLoader(0,null, this);

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
            Log.d(SanderDictProvider.LOG_TAG, uri.toString());
            getLoaderManager().restartLoader(0, args, this);

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (args != null) {
            if (args.containsKey(QUERY_EXTRA_KEY)) {
                String query = args.getString(QUERY_EXTRA_KEY);
                return new CursorLoader(getActivity(),
                        SanderDictProvider.CONTENT_URI,
                        null,
                        null,
                        new String[]{query},
                        null
                );
            } else if (args.containsKey(QUERY_URI)) {
                Log.d(SanderDictProvider.LOG_TAG, "ggg");
                return new CursorLoader( getActivity(), Uri.parse(args.getString(QUERY_URI)), null, null, null, null);
            }
        }
        Log.d(SanderDictProvider.LOG_TAG, "First on create loader");


        return new CursorLoader(getActivity(), SanderDictProvider.CONTENT_URI, null, null,  null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        scAdapter.swapCursor(data);

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
}

