package ua.pp.sanderzet.sanderdict;

import android.net.Uri;

/**
 * Created by sander on 08/10/17.
 */

public final  class SanderDictConstants {
    private static final String LOG_TAG = "SanderLog";
        private static final String AUTHORITY = "ua.pp.sanderzet.sanderdict.data.SanderDictProvider";
    private static final String SCHEME = "content://";
    private static final Uri CONTENT_URI = Uri.parse(SCHEME+AUTHORITY+ "/databases");

    public static final String KEY_FILES_TO_DB = "filesToDb";
    public static final String KEY_PATH_TO_DB = "pathToDb";
    public static final String EXT_FILE_TO_DB = ".dsl.dz";
    private static final String DICT_LIST_URL = "http://sanderzet.pp.ua/sanderdict/dict/";


    public static String getDictListUrl() {
        return DICT_LIST_URL;
    }
    public static String getLogTag() {
        return LOG_TAG;
    }

    public static String getAUTHORITY() {
        return AUTHORITY;
    }

    public static String getSCHEME() {
        return SCHEME;
    }

    public static Uri getContentUri() {
        return CONTENT_URI;
    }
}
