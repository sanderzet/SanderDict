package ua.pp.sanderzet.sanderdict.view.ui;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.Callable;

import ua.pp.sanderzet.sanderdict.SanderDictConstants;
import ua.pp.sanderzet.sanderdict.data.model.DictionaryModel;
import ua.pp.sanderzet.sanderdict.data.model.FavoriteModel;
import ua.pp.sanderzet.sanderdict.viewmodel.FragmentDictionariesViewModel;
import ua.pp.sanderzet.sanderdict.viewmodel.MainActivityViewModel;
import ua.pp.sanderzet.sanderdict.R;

import static android.support.design.widget.Snackbar.LENGTH_INDEFINITE;
import static android.support.design.widget.Snackbar.LENGTH_LONG;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private String LOG_TAG;

//    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private final String FTAG_LIST_SEARCH = "fls";
    private final String FTAG_WORD_UNFOLDED = "fwu";
private final String FTAG_FAVORITE_LIST = "ffl";
private final String FTAG_FAVORITE_UNFOLDED = "ffu";
private final String FTAG_DICTIONARIES = "fd";

private final String KEY_SEARCH = "key_search";


    private FragmentListSearch fragmentListSearch;
private FragmentWordUnfolded fragmentWordUnfolded;
private FragmentFavoriteList fragmentFavoriteList;
private FragmentFavoriteWordUnfolded fragmentFavoriteWordUnfolded;
private FragmentDictionaries fragmentDictionaries;

    private SearchView searchView;
    private String myQuery ;
    SharedPreferences sp;
    MainActivityViewModel viewModel;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ConstraintLayout constraintLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
private Toolbar toolbar;
private ActionBar actionBar;
private LiveData<Boolean> searchViewHasFocus;

  @Override
    public void onBackPressed() {

      int count = getFragmentManager().getBackStackEntryCount();

      if (count == 0) {
          super.onBackPressed();
          //additional code
      } else {
          getFragmentManager().popBackStack();
      }
      //moveTaskToBack(true);


  }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);
        LOG_TAG = SanderDictConstants.getLogTag();
        constraintLayout = findViewById(R.id.constraintLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
 actionBar = getSupportActionBar();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        fragmentWordUnfolded = new FragmentWordUnfolded();
        fragmentListSearch = new FragmentListSearch();
        fragmentFavoriteList = new FragmentFavoriteList();
        fragmentFavoriteWordUnfolded = new FragmentFavoriteWordUnfolded();
        fragmentDictionaries = new FragmentDictionaries();

        if (savedInstanceState == null) {

           /* fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragmentListSearch, FTAG_LIST_SEARCH).
                    addToBackStack(null).commit();
*/
            // sp = PreferenceManager.getDefaultSharedPreferences(this);
            // sp.edit().putString("dict_now", "").apply();
        myQuery = "";
        }
        else myQuery = savedInstanceState.getString(KEY_SEARCH);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        viewModel.getWordFromAnotherApp().observe(this, new Observer<DictionaryModel>() {
    @Override
    public void onChanged(@Nullable DictionaryModel dictionaryModel) {
        if (dictionaryModel != null) viewModel.setUnfoldedWord(dictionaryModel);
    }
});

        viewModel.getUnfoldedWord().observe(this, new Observer<DictionaryModel>() {
            @Override
            public void onChanged(@Nullable DictionaryModel dictionaryModel) {
                viewModel.setQuery(dictionaryModel.getWord());
   if (searchView != null)
       searchView.setQuery(dictionaryModel.getWord(),true );



                //                Remove focus from searchView (if we are sent to it from other app,
//                then we can tap Back only one for returning to sending app, but if
//                we have focus on searchView, we can tap Back twice.
constraintLayout.requestFocus();
                    fragmentManager.beginTransaction().replace(R.id.frameLayout, fragmentWordUnfolded, FTAG_WORD_UNFOLDED).
                commit();

// Snackbar.make(constraintLayout, "New FragmentSearch", Snackbar.LENGTH_SHORT).show();
            }
        });


        /*If we need to go back, for example, after removing word from favorite list*/

        viewModel.getBackStack().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean == true) {
                    fragmentManager.popBackStack();
                    viewModel.setBackStack(false);
                }
            }
        });

//Unfold favorite word
        viewModel.getUnfoldedFavoriteWord().observe(this, new Observer<FavoriteModel>() {
            @Override
            public void onChanged(@Nullable FavoriteModel favoriteModel) {
                    fragmentManager.beginTransaction().replace(R.id.frameLayout, fragmentFavoriteWordUnfolded, FTAG_FAVORITE_UNFOLDED).
                   commit();
                            }
        });

viewModel.getMessageForUser().observe(this, new Observer<String>() {
    @Override
    public void onChanged(@Nullable String s) {
        Snackbar.make(constraintLayout, s, Snackbar.LENGTH_SHORT).show();
    }
});


        //Searching from other app
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
            handleIntent(intent);
        }

    }

//End onCreate




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
searchView.setSaveEnabled(true);
searchView.setQueryHint(getString(R.string.hint));
searchView.setBackgroundColor(Color.WHITE);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);
        searchView.setOnQueryTextListener(onQueryTextListener);
        searchView.setOnQueryTextFocusChangeListener(onFocusChangeListener);
//         To avoid having fullscreen keyboard editing on landscape in order to access to the suggestions
        int options = searchView.getImeOptions();
        searchView.setImeOptions(options| EditorInfo.IME_FLAG_NO_EXTRACT_UI);
searchView.setQuery(myQuery,false);


        return super.onCreateOptionsMenu(menu);
    }


    private SearchView.OnCloseListener onCloseListener =
            new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
            viewModel.setQuery("");
                    return true;
                }
            };

    private SearchView.OnQueryTextListener onQueryTextListener =
            new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {


                    if (!fragmentListSearch.isAdded()) {
                        fragmentManager.beginTransaction().
                                replace(R.id.frameLayout, fragmentListSearch, FTAG_LIST_SEARCH).
                                addToBackStack(null).commit();
                    }

                    viewModel.setQuery(query);
                    return true;
                }

            };


    private SearchView.OnFocusChangeListener onFocusChangeListener =
            new SearchView.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        if (!fragmentListSearch.isAdded() ) {

                        }
                        Snackbar.make(constraintLayout, "FragmentListSearch Is added = " + fragmentListSearch.isAdded(), LENGTH_LONG).show();
                    }
                }
            };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
outState.putString(KEY_SEARCH, searchView.getQuery().toString());    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

//        searchView = viewModel.retainSearchView();
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//We in menu so no FABFavoriteAdd
        viewModel.setIsActiveFabFavoriteAdd(false);
        switch (id) {
            case R.id.my_add_dict_from_dir:
                addDictFromDir();
                break;
            case R.id.my_add_dict_from_net:
                Snackbar.make(constraintLayout, R.string.Add_dictionary_from_net, Snackbar.LENGTH_LONG).show();
//            Toast.makeText(this, R.string.add_dictionary_from_net, Toast.LENGTH_LONG).show();
                break;
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void addDictFromDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "SanderDict");
            FilenameFilter filenameFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (name.endsWith(".dsl.dz")) return true;
                    else return false;
                }
            };

            String[] files = file.list(filenameFilter);
//    If exist dir /SanderDict on external storage & in this dir exist one or more file .dsl.dz
            if (files != null && files.length != 0) {
                for (int i = 0; i < files.length; i++) {
                    String s = files[i];
                }
            } else
                Snackbar.make(constraintLayout, "No appropriate directory or files have been found", Snackbar.LENGTH_LONG).show();

        } else
            Snackbar.make(constraintLayout, "No external storage have been found", LENGTH_INDEFINITE).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
int id = item.getItemId();
        switch (id) {
            case R.id.drawer_item_favorite:
     toolbar.setTitle("Favorite");

           showFavorite();
                break;
            case R.id.drawer_item_dictionaries:
           toolbar.setTitle("Dictionaries");
showDictionaries();
break;
        }


mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showFavorite () {
           fragmentManager.beginTransaction().replace(R.id.frameLayout, fragmentFavoriteList, FTAG_FAVORITE_LIST).
                    addToBackStack(null).commit();

    }

    public void showDictionaries() {
            fragmentManager.beginTransaction().replace(R.id.frameLayout, fragmentDictionaries, FTAG_DICTIONARIES).
                    commit();


    }

    private void handleIntent(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
viewModel.setQueryFromAnotherApp(sharedText);
//        if (sharedText != null) {searchView.setQuery(sharedText.trim(), true);}
    }

}

