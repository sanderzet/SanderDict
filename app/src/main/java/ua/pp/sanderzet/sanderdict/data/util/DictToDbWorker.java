package ua.pp.sanderzet.sanderdict.data.util;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;

import androidx.work.Result;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import ua.pp.sanderzet.sanderdict.SanderDictConstants;
import ua.pp.sanderzet.sanderdict.data.DBSanderDict;


/**
 * Created by sander on 20.02.17.


 */

public class DictToDbWorker extends Worker {

    public DictToDbWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context appContext = getApplicationContext();
        DBSanderDict dbSanderDict = new DBSanderDict(appContext);
        String[] filesName = getInputData().getStringArray(SanderDictConstants.KEY_FILES_TO_DB);
String path = getInputData().getString(SanderDictConstants.KEY_PATH_TO_DB);
        File[] files = new File[filesName.length];
String dbName;


        try {

            for (int i = 0; i < filesName.length; i++) {
                files[i] = new File(path+File.separator+filesName[i]);
                Log.d(SanderDictConstants.getLogTag(), files[i].getName());
                dbName = files[i].getName().replace(SanderDictConstants.EXT_FILE_TO_DB,".db");
//                dbSanderDict.open(dbName); // create db file
                dbSanderDict.transToDB(files[i],dbName);
                dbSanderDict.close();

            }

            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }
}


