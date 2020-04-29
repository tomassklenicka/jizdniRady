package com.example.smartrady_final;

import android.content.SearchRecentSuggestionsProvider;

public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String auth = "com.example.jizdnirady.MySuggestionProvider";
    public final static int mode = DATABASE_MODE_QUERIES;
    public MySuggestionProvider() {
        setupSuggestions(auth, mode);
    }
}