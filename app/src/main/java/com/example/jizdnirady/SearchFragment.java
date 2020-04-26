package com.example.jizdnirady;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.media.Image;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        if (intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getActivity(), MySuggestionProvider.auth, MySuggestionProvider.mode);
            suggestions.saveRecentQuery(query, null);
            searchStation(query);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        final SearchView searchView1 = getActivity().findViewById(R.id.search1);
        searchView1.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        final SearchView searchViewVia = getActivity().findViewById(R.id.search_via);
        searchViewVia.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        final SearchView searchView2 = getActivity().findViewById(R.id.search2);
        searchView2.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        ImageButton swap = getActivity().findViewById(R.id.swap);
        swap.bringToFront();
        swap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CharSequence temp = searchView1.getQuery();
                searchView1.setQuery(searchView2.getQuery(), false);
                searchView2.setQuery(temp, false);
            }
        });

        final ImageButton add_via = getActivity().findViewById(R.id.add_via);
        add_via.bringToFront();
        add_via.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                searchViewVia.setVisibility(View.VISIBLE);
                add_via.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void searchStation(String query) {
        //hledani stanice
    }
}
