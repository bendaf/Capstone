package hu.bendaf.spip.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import hu.bendaf.spip.AddGroupActivity;
import hu.bendaf.spip.R;
import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class AddGroupWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent addGroupActivity = new Intent(context, AddGroupActivity.class);
        PendingIntent p = PendingIntent.getActivity(context, 0, addGroupActivity, 0);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_add_group);
        views.setOnClickPendingIntent(R.id.appwidget_text, p);

        views.setRemoteAdapter(R.id.lv_groups, new Intent(context, GroupsWidgetAdapter.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_groups);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        Timber.d("UpdateAppWidget");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for(int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

