package ru.kpfu.itis.gnt.languagelearningapp.constants;

public class ApiEndPoints {
    public static class Authentication {
        public static final String REGISTER = "/register";
        public static final String LOGIN = "/login";
        public static final String PROFILE = "/profile";

    }

    public static final String TRANSLATE = "/translate";

    public static final String DICTIONARY = "/dictionary";
    public static final String WORD = "/word";


    public static final String FAVORITES = "/dictionary/favorites";


    public static class Favorites {
        public static final String ADD = "/favorites/add";
        public static final String REMOVE = "/favorites/remove";
    }



}
