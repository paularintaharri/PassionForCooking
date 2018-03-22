package com.bignerdranch.android.passionforcooking.database;

/**
 * Created by Paula on 22.3.2018.
 */

public class RecipeDbSchema {
    public static final class RecipeTable{
        public static final String NAME = "recipes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String LIKED = "liked";
            public static final String RATE = "rate";

        }

    }
}
