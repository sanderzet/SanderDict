package ua.pp.sanderzet.sanderdict.data.util;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by sander on 01.03.18.
 */

public class DBFromAssetsLiveData extends LiveData<String> {

    private final String LOG_TAG = "SanderZet"+getClass().getName();
    private final String ASSETS_SUB_DIRECTORY = "databases";
    private final Application application;
    private String databaseName = getValue();
    private File dbFile;
    private Integer progress;

    public DBFromAssetsLiveData(Application application, String databaseName) {
        this.application = application;
        this.databaseName = databaseName;
        copyAttachedDB_FromAssets();
    }

    private void copyAttachedDB_FromAssets() {
        new  AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    if (databaseName != null) {
                        dbFile = application.getDatabasePath(databaseName);
                        if (dbFile.exists()) {
                            return databaseName;
                        }
                    }
//If databaseName is null or file don`t exist, copy from asset
                    String [] files = application.getAssets().list(ASSETS_SUB_DIRECTORY);
                    databaseName = files[0];


                    int length;
                    byte [] buff = new byte[8092];

                    InputStream fileInputStream = application.getAssets().open(ASSETS_SUB_DIRECTORY+File.separator+databaseName);
                    OutputStream fos = new FileOutputStream(dbFile);

                    while ((length = fileInputStream.read(buff)) > 0) {
                        fos.write(buff,0,length);
                    }

                    fileInputStream.close();
                    fos.close();

                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "File not found: " + e.getLocalizedMessage());
                }
                catch (SecurityException e)
                {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "Security exception: " + e.getLocalizedMessage());
                }
                catch (IOException e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, e.getMessage());
                }

                return databaseName;
            }

            @Override
            protected void onPostExecute(String s) {
                setValue(databaseName);
            }
        };
    }



}