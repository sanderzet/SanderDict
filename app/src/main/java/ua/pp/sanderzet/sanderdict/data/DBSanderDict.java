package ua.pp.sanderzet.sanderdict.data;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Created by alz on 18.03.16.
 * for github
 */
public class DBSanderDict {
    private static final String db_table = "databases";
    private static final int DB_VERSION = 1;
    //Column Names for table
    private static final String KEY_ID = BaseColumns._ID;
    private static final String KEY_DATE = "word";
    public static final String KEY_DETAILS = "definition";

    private static final String DB_CREATE =
            "create table " + db_table + "(" +
                    KEY_ID + " integer primary key, " +
                    KEY_DATE + " text, " +
                    KEY_DETAILS + " text );";

    private static final HashMap<String, String> SEARCH_PROJECTION_MAP;
    static {
        SEARCH_PROJECTION_MAP = new HashMap<>();
        SEARCH_PROJECTION_MAP.put(BaseColumns._ID, DBSanderDict.KEY_ID + " AS " + "_id");
        SEARCH_PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_TEXT_1, KEY_DATE + " AS " +
                SearchManager.SUGGEST_COLUMN_TEXT_1);
        SEARCH_PROJECTION_MAP.put(KEY_DETAILS,KEY_DETAILS + " AS " + KEY_DETAILS);
        SEARCH_PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, KEY_ID + " AS " +
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        SEARCH_PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_QUERY, KEY_DATE + " AS " +
        SearchManager.SUGGEST_COLUMN_QUERY);

    }



    private Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;


    public DBSanderDict(Context ctx) {
        mCtx = ctx;

    }


//    Copy default db from assets/dict
 boolean copyDefDictFromAssets(String inputFile, String outputFile) {

    try {

        int length;
        byte [] buff = new byte[1024];
        InputStream fileInputStream = mCtx.getAssets().open(inputFile);
        OutputStream fos = new FileOutputStream(outputFile);

//                FragmentListSearch.mProgressDialog.setIndeterminate(false);
//                FragmentListSearch.mProgressDialog.setMax(44270);

        while ((length = fileInputStream.read(buff)) > 0) {
            fos.write(buff,0,length);
        }

        fileInputStream.close();
        fos.close();

    }
    catch (FileNotFoundException e)
    {
        e.printStackTrace();
        Log.d(SanderDictProvider.LOG_TAG, "File not found: " + e.getLocalizedMessage());
    }
    catch (SecurityException e)
    {
        e.printStackTrace();
        Log.d(SanderDictProvider.LOG_TAG, "Security exception: " + e.getLocalizedMessage());
    }
    catch (IOException e) {
        e.printStackTrace();
        Log.d(SanderDictProvider.LOG_TAG, e.getMessage());
    }
// For further implementing
    return true;
}

    public SQLiteDatabase open(String dbPathName) {
        // Перевіряємо, чи немає вже екземпляра дбхелпера - курсорЛоадер може запускати кілька потоків.
        // Якщо вже є - на тобі, отримуй існуючий, ні - то створимо.
       if(mDBHelper == null) mDBHelper = new DBHelper(mCtx, dbPathName, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
return mDB;
    }

    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }


public Cursor query(String[] projection, String selection){
/*
    return mDB.query(db_table, null, selection, selectionArgs, null,null, null );

*/
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    qb.setTables(db_table);
    qb.setProjectionMap(SEARCH_PROJECTION_MAP);
    Log.d(SanderDictProvider.LOG_TAG, qb.buildQuery(null, selection, null,  null, null, null));
    Cursor cursor = qb.query(mDB, projection, selection, null, null,null, null);
    return cursor;
}

    void transToDB( String archivedDictPath, String archivedDictName) {
        // int progress = 0;
        mDB.execSQL(DB_CREATE);
        GZIPInputStream gz;
        Reader decoder;
        BufferedReader br;
        String line, detail;
        StringBuilder lines = new StringBuilder();
        ContentValues cv = new ContentValues();
/*    regexp  для обробки dsl
//   p1 - [p] на <i>  и [/p] на </i>  - частини мови і інші скорочення на italics
//   p2 - [Х] - все інше, що в квадратних скобках, нафіг.
//   p3 -  \[ и  \] - міняємо на [<font color="#00ff00">  и </font>] відповідно - транскрипція
*/
        Pattern p1 = Pattern.compile("(\\[p\\])(.+?)(\\[\\/p])");
        Pattern p2 = Pattern.compile("\\[.+?\\]");
        Pattern p3 = Pattern.compile("(\\\\)(.+?)(\\\\\\])");

        Matcher m;
        boolean nextWord = false ;
        try {
            AssetManager am = mCtx.getAssets();
            gz = new GZIPInputStream(am.open("databases/univer_en_uk.dsl.dz"));
            decoder = new InputStreamReader(gz, "UTF-16LE");
            br = new BufferedReader(decoder);

//            FragmentListSearch.mProgressDialog.setIndeterminate(false);
//            FragmentListSearch.mProgressDialog.setMax(44270);

/* Оброблюємо dsl
пустий рядок  з таб - перед словом.
Слово на один рядок. Далі дефінішн до наступного пустого рядка з табом.
*/

            while ((line = br.readLine()) != null) {

//Перед новим словом
                if (line.equals("\t")) {
                    if (cv.containsKey(KEY_DATE)) {
                        m = p1.matcher(lines);
                        detail = m.replaceAll("<i>$2</i>");
                        m = p2.matcher(detail);
                        detail = m.replaceAll("");
                        m = p3.matcher(detail);
                        detail = m.replaceAll("[<font color=#008000>$2</font>]");
                        cv.put(KEY_DETAILS, detail);
                       mDB.insert(db_table, null, cv);

                    }
                    nextWord = true;
                    lines.setLength(0);
                }
//Дефінішн слова
                else if (line.startsWith("\t")) {
                    if (!nextWord) {
                        lines.append(line).append("<br>");
                    }
                }
// Слово
                else {
                    if (nextWord) {
                        cv.put(KEY_DATE, line);
                        nextWord = false;
                    }

                }
            }

            gz.close();
            decoder.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
//            IF db not exist end it`s failed to copy from Assets - create new empty db
            db.execSQL(DB_CREATE);

            /*
            * If db don`t exist, extract it from assets`s archive.
            * */
 /*           GZIPInputStream gz;
            byte [] buff = new byte [1024];
            int length ;
            pd.setIndeterminate(true);
            pd.setTitle("Dictionary setting ");
            pd.setMessage(runningDict);
//pd.show();

            try {
                AssetManager am = mCtx.getAssets();
                String [] filesAssets = am.list("");
                for (int i = 0; i < filesAssets.length; i++) {
                    Log.d(SanderDictProvider.LOG_TAG, filesAssets[i]);
                                    }


//                gz = new GZIPInputStream(am.open(runningDict + ".gz"));
InputStream fileInputStream = am.open((runningDict));
               OutputStream fos = new FileOutputStream(db_name);

//                FragmentListSearch.mProgressDialog.setIndeterminate(false);
//                FragmentListSearch.mProgressDialog.setMax(44270);

                while ((length = fileInputStream.read(buff)) > 0) {
                    fos.write(buff,0,length);
                }

                fileInputStream.close();
                fos.close();

            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
                Log.d(SanderDictProvider.LOG_TAG, "File not found: " + e.getLocalizedMessage());
            }
            catch (SecurityException e)
            {
                e.printStackTrace();
                Log.d(SanderDictProvider.LOG_TAG, "Security exception: " + e.getLocalizedMessage());
            }
            catch (IOException e) {
                e.printStackTrace();
                Log.d(SanderDictProvider.LOG_TAG, e.getMessage());
            }
finally {
pd.dismiss();            }
*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
