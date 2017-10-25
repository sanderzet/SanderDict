package ua.pp.sanderzet.sanderdict.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import ua.pp.sanderzet.sanderdict.R;
import ua.pp.sanderzet.sanderdict.view.ui.MainActivity;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link SanderDictWidgetConfigureActivity SanderDictWidgetConfigureActivity}
 */
public class SanderDictWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SanderDictWidgetConfigureActivity.loadPref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.sander_dict_widget);
        views.setTextViewText(R.id.appwidget_text, SanderDictWidgetConfigureActivity.getIsAutoUpdate().toString());

//Start MainActivity
        Intent mainActivityIntent = new Intent(context, MainActivity.class);
        mainActivityIntent.setAction(Intent.ACTION_MAIN);
        mainActivityIntent.putExtra(appWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, mainActivityIntent, 0);
        views.setOnClickPendingIntent(R.id.imageButtonMain,pendingIntent);

//Start configuring widget
        Intent confIntent = new Intent(context, SanderDictWidgetConfigureActivity.class);
confIntent.setAction(appWidgetManager.ACTION_APPWIDGET_CONFIGURE);
confIntent.putExtra(appWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
pendingIntent = PendingIntent.getActivity(context,appWidgetId,confIntent,0);
views.setOnClickPendingIntent(R.id.imageButtonConf, pendingIntent);

//        Update widget

        Intent updateIntent = new Intent(context, SanderDictWidget.class);
        updateIntent.setAction(appWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(appWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, updateIntent,0);
        views.setOnClickPendingIntent(R.id.imageButtonUpdate,pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            SanderDictWidgetConfigureActivity.deletePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        // Enter relevant functionality for when the last widget is disabled
    }
}

