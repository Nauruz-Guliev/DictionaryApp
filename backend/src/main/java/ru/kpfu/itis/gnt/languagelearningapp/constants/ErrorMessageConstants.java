package ru.kpfu.itis.gnt.languagelearningapp.constants;

public class ErrorMessageConstants {
    public static class NOT_FOUND {
        public static final String DICTIONARY = "Unable to retrieve dictionary result";
        public static final String TRANSLATION = "Unable to retrieve translation result";

        public static final String LOCAL = "Unable to load local data";

        public static final String WORD = "Unable to find the dictionary word";
        public static final String USER = "User was not found";

    }

    public static class NOT_AUTHORIZED {
        public static final String ERROR_MESSAGE = "You need to be authorized in order to access this path";
        public static final String USER_NOT_FOUND = "Unknown user";
        public static final String INVALID_PASSWORD = "Invalid password";
        public static final String EXISTING_LOGIN = "Such a login already exists";

    }

    public static class INTERNAL {
        public static final String UNKNOWN = "Internal error occurred";
        public static final String FAVORITE_ADD_FAIL = "Failed to add to favorites";
        public static final String FAVORITES_FAIL = "Failed to retrieve favorite words";
        public static final String PAGING_FAILED = "Paging failed";


        public static final String FAVORITE_REMOVE_FAIL = "Failed to remove from favorites";



    }

    public static class INVALID_DATA {
        public static final String PASSWORDS_NOT_EQUAL = "Passwords are not equal";

    }
}
