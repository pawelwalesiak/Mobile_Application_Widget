package com.example.smb_widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class Widget extends AppWidgetProvider {

    private static int[] imgTab = {R.drawable.foto1, R.drawable.foto2};
    private static int imgId = 0;
    private MediaPlayer mediaPlayer;

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        //WWW
        String link = "https://www.youtube.com/";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.goWebButton, pendingIntent);

        //obrazek
        views.setImageViewResource(R.id.imageView, imgTab[imgId]);

        Intent intent2 = new Intent(context, Widget.class);

        intent2.setAction("com.example.smb_widget.action.prevImg");
        PendingIntent pil = PendingIntent.getBroadcast(context, 0, intent2, 0);
        views.setOnClickPendingIntent(R.id.prevImageButton, pil);

        intent2.setAction("com.example.smb_widget.action.nextImg");
        PendingIntent pir = PendingIntent.getBroadcast(context, 0, intent2, 0);
        views.setOnClickPendingIntent(R.id.nextImageButton, pir);
        //muzyka
        int bitmapId = 0;
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);


        Intent start = new Intent(context, MediaService.class);
        Intent pause = new Intent(context, MediaService.class);
        Intent stop = new Intent(context, MediaService.class);
        Intent skip = new Intent(context, MediaService.class);

        start.setAction("com.example.smb_widget.widget.action.start");
        pause.setAction("com.example.smb_widget.widget.action.pause");
        stop.setAction("com.example.smb_widget.widget.action.stop");
        skip.setAction("com.example.smb_widget.widget.action.skip");


        PendingIntent pendingStartIntent = PendingIntent.getService(context, 0, start, 0);
        PendingIntent pendingPauseIntent = PendingIntent.getService(context, 0, pause, 0);
        PendingIntent pendingStopIntent = PendingIntent.getService(context, 0, stop, 0);
        PendingIntent pendingSkipIntent = PendingIntent.getService(context, 0, skip, 0);


        remoteViews.setOnClickPendingIntent(R.id.playButton, pendingStartIntent);
        remoteViews.setOnClickPendingIntent(R.id.pauseButton, pendingPauseIntent);
        remoteViews.setOnClickPendingIntent(R.id.pauseButton, pendingStopIntent);
        remoteViews.setOnClickPendingIntent(R.id.nextMusicButton, pendingSkipIntent);



        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
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

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals("com.example.smb_widget.action.prevImg")){
            imgId--;
            if(imgId<0){
                imgId = imgTab.length-1;
            }

            Toast.makeText(context, "Prevv", Toast.LENGTH_SHORT).show();
        }
        else if(intent.getAction().equals("com.example.smb_widget.action.nextImg")){
            imgId++;
            if(imgId>=imgTab.length){
                imgId = 0;
            }

            Toast.makeText(context, "Next", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Null", Toast.LENGTH_SHORT).show();
        }
        onUpdate(context);
        ////
    }

    private void onUpdate(Context context) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(), getClass().getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }
}

