package bakingapplication.com.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
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
        for (int i=0; i<appWidgetIds.length; i++) {
            Intent svcIntent=new Intent(ctxt, AppWidgetService.class);

            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
          //  svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));



            RemoteViews widget=new RemoteViews(ctxt.getPackageName(),
                    R.layout.app_widget);


            LoremViewsFactory.items = DetailsActivity.ingredientsList2.toArray(new String[0]);
            PendingIntent pendingIntent = PendingIntent.getActivity(ctxt, 0, new Intent(ctxt, MainActivity.class), 0);


            widget.setTextViewText(R.id.recipe_widget_name_text, name);
            widget.setOnClickPendingIntent(R.id.recipe_widget_name_text, pendingIntent);

            widget.setRemoteAdapter(appWidgetIds[i], R.id.recipe_widget_listview,
                    svcIntent);

            Intent clickIntent=new Intent(ctxt, DetailsActivity.class);
            PendingIntent clickPI=PendingIntent
                    .getActivity(ctxt, 0,
                            clickIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            // Initialize the list view
            Intent intent = new Intent(ctxt, AppWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            // Bind the remote adapter
            widget.setRemoteAdapter(R.id.recipe_widget_listview, intent);

            widget.setPendingIntentTemplate(R.id.recipe_widget_listview, clickPI);

            appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
        }

        super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}

