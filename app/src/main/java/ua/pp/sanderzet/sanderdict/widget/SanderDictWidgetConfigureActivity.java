package ua.pp.sanderzet.sanderdict.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import ua.pp.sanderzet.sanderdict.R;

/**
 * The configuration screen for the {@link SanderDictWidget SanderDictWidget} AppWidget.
 */
public class SanderDictWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "ua.pp.sanderzet.sanderdict.Widget.SanderDictWidget";
    private static final String PREF_AUTOUPDATE = "autoupdate_";
    private static final String PREF_UPDATE_PERIOD = "update_period_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    CheckBox cbAutoUpdate;
    Button btnOk;
private    static Boolean isAutoUpdate;
    private static Integer updatePeriod;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = SanderDictWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            isAutoUpdate = cbAutoUpdate.isChecked();
            savePref(context, mAppWidgetId );

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            SanderDictWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };


    // Write the prefix to the SharedPreferences object for this widget
    static void savePref(Context context, int appWidgetId ) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putBoolean(PREF_AUTOUPDATE + appWidgetId, isAutoUpdate);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static void loadPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        isAutoUpdate = prefs.getBoolean(PREF_AUTOUPDATE + appWidgetId, true);

    }

    static void deletePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_AUTOUPDATE + appWidgetId);
        prefs.apply();
    }

    public static Boolean getIsAutoUpdate() {
        return isAutoUpdate;
    }

    public static Integer getUpdatePeriod() {
        return updatePeriod;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.sander_dict_widget_configure);


    cbAutoUpdate = (CheckBox) findViewById(R.id.cbAutoUpdate);
        btnOk = findViewById(R.id.ok_button);
                btnOk.setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
loadPref(SanderDictWidgetConfigureActivity.this,mAppWidgetId);
        cbAutoUpdate.setChecked(isAutoUpdate);


    }
}

