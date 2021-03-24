package com.elsoudany.said.tripreminderapp.ReminderWork;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.elsoudany.said.tripreminderapp.R;

public class ReminderWorker extends Worker {
    private static final String TAG = "MYTAG";
    long uid;
    Context context;
    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        Data data =  workerParams.getInputData();
        uid = data.getLong("tripUid",0);
        this.context = getApplicationContext();
        Log.i(TAG, "MyWorker: ");
    }

    @NonNull
    @Override
    public Result doWork() {
        displayNotification("My Worker", "Hey I finished my work" + uid);
        Intent intent = new Intent(context,ReminderService.class);
        intent.putExtra("tripUid",uid);
        context.startService(intent);

        return Result.success(); //true - success / false - failure
    }
    private void displayNotification(String title, String task) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "simplifiedcoding")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.ic_launcher);
        notificationManager.notify(1, notification.build());
    }

}

