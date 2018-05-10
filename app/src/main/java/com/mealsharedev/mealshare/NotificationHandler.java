package com.mealsharedev.mealshare;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import com.mealsharedev.mealshare.Models.Meal;

public class NotificationHandler {

    Context context;
    NotificationChannel notificationChannel = null;
    NotificationManager notificationManager;
    NotificationManagerCompat notificationManagerCompat;
    private static final String CHANNEL_ID = "MealShare_Channel";

    public NotificationHandler(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void NotifyMealRemoved()
    {
        initChannel();

        notificationManagerCompat = NotificationManagerCompat.from(context);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(context.getString(R.string.mealWasCanceled))
                .setContentText(context.getString(R.string.mealWasCanceled2))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(getPendingIntentMain());
        notificationManagerCompat.notify(1,mBuilder.build());
    }

    public void NotifyMealReserved(Meal meal)
    {
        initChannel();

        notificationManagerCompat = NotificationManagerCompat.from(context);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(context.getString(R.string.mealWasJoined))
                .setContentText(context.getString(R.string.mealWasJoined2) + meal.mealName + context.getString(R.string.mealWasJoined3) + meal.portions)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(getPendingIntentMealDetails(meal));
        notificationManagerCompat.notify(1,mBuilder.build());
    }

    public void NotifyCommentOnSameMeal(Meal meal)
    {
        initChannel();

        notificationManagerCompat = NotificationManagerCompat.from(context);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(context.getString(R.string.mealWasCommented))
                .setContentText(context.getString(R.string.mealWasCommented2))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(getPendingIntentMealDetails(meal));
        notificationManagerCompat.notify(1,mBuilder.build());
    }

    public void NotifyCommentOnYourMeal(Meal meal)
    {
        initChannel();

        notificationManagerCompat = NotificationManagerCompat.from(context);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(context.getString(R.string.myMealWasCommented))
                .setContentText(context.getString(R.string.myMealWasCommented2) + meal.mealName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(getPendingIntentMealDetails(meal));
        notificationManagerCompat.notify(1,mBuilder.build());
    }

    public void initChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, "mealShare", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("MealShare Channel");
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private PendingIntent getPendingIntentMealDetails(Meal meal) {
        Intent resultIntent = new Intent(context, MealDetailsActivity.class);
        resultIntent.putExtra("meal", meal);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MealDetailsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        return resultPendingIntent;
    }

    private PendingIntent getPendingIntentMain() {
        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        return resultPendingIntent;
    }



}
