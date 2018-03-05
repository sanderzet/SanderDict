package ua.pp.sanderzet.sanderdict.view.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.pp.sanderzet.sanderdict.R;

/**
 * Created by sander on 31.10.17.
 */

public class FragmentFavoriteList extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favorite_list, container, false);
    }
}
