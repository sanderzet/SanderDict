package ua.pp.sanderzet.sanderdict.data;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import ua.pp.sanderzet.sanderdict.SanderDictConstants;


public class SanderDictProvider extends ContentProvider {


    static final String LOG_TAG = "SanderLog";

    DBSanderDict mDict;
    private String db_path;
    private String db_name;
    Context mCtx;


    // // URI
    // authority
    //UriMatcher
    static final int SEARCH_WORDS = 0;
    static final int GET_WORD = 1;
    static final int SEARCH_SUGGEST = 2;
    static final int GET_RANDOM_WORD = 3;
    // MIME types used for searching words or looking up a single definition
    static  String CONTENT_DIR_TYPE ;
    static  String CONTENT_ITEM_TYPE;
    static  String AUTHORITY;
    //Builds up a UriMatcher for search suggestion
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static
    {
        AUTHORITY = SanderDictConstants.getAUTHORITY();
        sUriMatcher.addURI(AUTHORITY, "dict", SEARCH_WORDS);
        sUriMatcher.addURI(AUTHORITY, "dict/#", GET_WORD);
        sUriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        sUriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
        sUriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT, SEARCH_SUGGEST);
        sUriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", SEARCH_SUGGEST);
        sUriMatcher.addURI(AUTHORITY, "dict_random/#", GET_RANDOM_WORD);
    }


    @Override
    public boolean onCreate() {
        CONTENT_DIR_TYPE = "vnd.android.cursor.dir/vnd."
                + AUTHORITY;
       CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
                + AUTHORITY;
        //Implement this to initialize your content provider on startup.


        //        Checking on database existing
        mCtx = getContext();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mCtx);
        mDict = new DBSanderDict(mCtx);

        String db_def;
boolean b = true;
        // Отримуємо повний шлях до бд
        try {
//            Define default dictionary (it depend on db in assets/dict )
//            If it`s first launch or preferences destroyed - repair it.
            db_def = sp.getString("db_def", null);
            if (db_def == null) {
                String[] defDb = mCtx.getAssets().list("dict");
                sp.edit().putString("db_def", defDb[0]).apply();
                db_def = defDb[0];
//                Also will declare path to db
                File[] dirs = ContextCompat.getExternalFilesDirs(mCtx, null);
                sp.edit().putString("db_path", dirs[0].toString() + File.separator ).apply();
            }

//            Define path and current db
            db_path = sp.getString("db_path", "");
            db_name = sp.getString("db_name", db_def);


//            Checking if dictionary file exist
            File file = new File(db_path + db_name);
            if (!file.exists()) {
//             If dict don`t exist, return to default
                file = new File(db_path + db_def);
                if (file.exists()) {
                    db_name = db_def;
                }
                else {
//                    If even def dict don`t exist, copy from Assets
                   if (!(b = mDict.copyDefDictFromAssets("dict/" + db_def, db_path + db_def)))
                    Log.d(LOG_TAG, "Error while copying db from Assets");
                }
                sp.edit().putString("db_name", db_def).apply();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return b;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        //  \Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (sUriMatcher.match(uri)){
            case SEARCH_WORDS:
                return CONTENT_DIR_TYPE;
            case GET_WORD:
                return CONTENT_ITEM_TYPE ;
            case SEARCH_SUGGEST:
                return SearchManager.SUGGEST_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);

        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

// Use the UriMatcher to see what kind of query we have and format the query accordingly
       switch (sUriMatcher.match(uri)) {

// If search suggest gave his query (only 1 or more symbol must be present
           case SEARCH_SUGGEST:
               if (selectionArgs == null) {
                   throw new IllegalArgumentException(
                           "selectionArg must be provided for the Uri: " + uri);
               }
               else if (selectionArgs[0].length() < 1 ) {
                   selection = BaseColumns._ID + " = 0";
               }
               else {
               selection = SearchManager.SUGGEST_COLUMN_TEXT_1+ " LIKE \"" +
                       selectionArgs[0] + "%\"";
               Log.d(LOG_TAG, "getSuggestion " + uri+ " selection " + selection);
               }

               break;
// We are searching (no less then 1 symbol must be present)
           case SEARCH_WORDS:
               if (selectionArgs == null || selectionArgs[0].length() <1 ) {
                   selection = BaseColumns._ID + " = 0";
               }
               else {
                   selection = SearchManager.SUGGEST_COLUMN_TEXT_1 + " LIKE \"" +
                           selectionArgs[0] + "%\"";
               }
               Log.d(LOG_TAG, "Search_WORDS " + uri+ " selection " + selection);
               break;
           case GET_WORD:
               selection = BaseColumns._ID + " = " + uri.getLastPathSegment();
               Log.d(LOG_TAG, "Get_word " + uri+ " selection " + selection);
               break;
           case GET_RANDOM_WORD:
               mDict.open(db_path+db_name);
               String [] columns =  {BaseColumns._ID};
               Cursor cursor = mDict.query(columns,null);
               cursor.moveToPosition(new Random().nextInt(cursor.getCount()));
               selection = BaseColumns._ID + " = " + cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
               Log.d(LOG_TAG, "Get_random_word " + uri+ " selection " + selection);
               break;
           default:
               throw new IllegalArgumentException("Unknown Uri: " + uri);
       }
        mDict.open(db_path+db_name);

/*        Cursor cursor = mDict.query(selection, selectionArgs[0]);
        */
        return mDict.query(null, selection);

    }



    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
