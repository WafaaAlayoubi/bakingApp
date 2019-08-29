package bakingapplication.com.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;
import android.widget.TextView;

import bakingapplication.com.R;
import bakingapplication.com.models.Recipe;
import bakingapplication.com.ui.activities.DetailsActivity;
import bakingapplication.com.ui.activities.MainActivity;
import bakingapplication.com.ui.fragments.AllRecipesFragment;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    public static String EXTRA_WORD = "com.commonsware.android.appwidget.lorem.WORD";
    public static String name = "Recipe";

    TextView textView;

    @Override
    public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        ComponentName component;
        for (int i=0; i<appWidgetIds.length; i++) {

            RemoteViews remoteViews = updateWidgetListView(ctxt,
                    appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);

            component=new ComponentName(ctxt,AppWidget.class);
            appWidgetManager.updateAppWidget(component, remoteViews);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview);

        }

        super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        remoteViews.setTextViewText(R.id.recipe_widget_name_text, name);
        remoteViews.setOnClickPendingIntent(R.id.recipe_widget_name_text, pendingIntent);


        Intent svcIntent = new Intent(context, AppWidgetService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.widget_listview, svcIntent);

        return remoteViews;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}