package ua.pp.sanderzet.sanderdict.view.ui;


import android.app.Activity;
import android.app.SearchManager;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FilenameFilter;

import ua.pp.sanderzet.sanderdict.SanderDictConstants;
import ua.pp.sanderzet.sanderdict.data.model.DictionaryModel;
import ua.pp.sanderzet.sanderdict.viewmodel.MainActivityViewModel;
import ua.pp.sanderzet.sanderdict.R;

import static android.support.design.widget.Snackbar.LENGTH_INDEFINITE;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private String LOG_TAG;

//    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private final String FTAG_LIST_SEARCH = "fls";
    private final String FTAG_WORD_UNFOLDED = "fwu";

    private FragmentListSearch fragmentListSearch;
private FragmentWordUnfolded fragmentWordUnfolded;

    private SearchView searchView;
    String query = "";
    SharedPreferences sp;
    MainActivityViewModel viewModel;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ConstraintLayout constraintLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);
        LOG_TAG = SanderDictConstants.getLogTag();
        constraintLayout = findViewById(R.id.constraintLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();


//        If after rotation, no need to create new fragment
        if (savedInstanceState == null) {
            fragmentWordUnfolded = new FragmentWordUnfolded();
            fragmentListSearch = new FragmentListSearch();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frameLayout, fragmentListSearch, FTAG_LIST_SEARCH).commit();
            // sp = PreferenceManager.getDefaultSharedPreferences(this);
            // sp.edit().putString("dict_now", "").apply();
        }
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
viewModel.getUnfoldedWord().observe(this, new Observer<DictionaryModel>() {
    @Override
    public void onChanged(@Nullable DictionaryModel dictionaryModel) {
        searchView.setQuery(dictionaryModel.getWord(), false);
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragmentWordUnfolded, FTAG_WORD_UNFOLDED).commit();
    }
});

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();


        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);
        searchView.setOnQueryTextListener(onQueryTextListener);
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
                    fragmentManager.beginTransaction().replace(R.id.frameLayout, fragmentListSearch).commit();
                    viewModel.setQuery(query);
                    return true;
                }

            };


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
                Snackbar.make(constraintLayout, "Add dictionary from.net", Snackbar.LENGTH_LONG).show();
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
        return false;
    }

 /*   @NonNull
    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }*/
}

