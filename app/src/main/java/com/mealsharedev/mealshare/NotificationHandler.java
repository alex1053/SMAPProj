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
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("A meal has been cancelled!")
                .setContentText("A user has cancelled a meal you were assigned to")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(getPendingIntentMain());
        notificationManagerCompat.notify(1,mBuilder.build());
    }

    public void NotifyMealReserved(Meal meal)
    {
        initChannel();

        notificationManagerCompat = NotificationManagerCompat.from(context);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Someone has joined your meal!")
                .setContentText("A user has joined " + meal.mealName + ". Portions left: " + meal.portions)
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
                .setContentTitle("User has comment on a meal!")
                .setContentText("A user has comment on the same meal as you")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(getPendingIntentMealDetails(meal));
        notificationManagerCompat.notify(1,mBuilder.build());
    }

    public void NotifyCommentOnYourMeal(Meal meal)
    {
        initChannel();

        notificationManagerCompat = NotificationManagerCompat.from(context);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("A user has comment on your meal!")
                .setContentText("A user has written a comment on your meal: " + meal.mealName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(getPendingIntentMealDetails(meal));
        notificationManagerCompat.notify(1,mBuilder.build());
    }

    public void initChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, "Weather App", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Weather App Channel");
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
