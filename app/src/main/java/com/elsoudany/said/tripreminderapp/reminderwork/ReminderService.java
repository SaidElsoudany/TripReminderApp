package com.elsoudany.said.tripreminderapp.reminderwork;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.elsoudany.said.tripreminderapp.R;
import com.elsoudany.said.tripreminderapp.room.AppDatabase;
import com.elsoudany.said.tripreminderapp.room.Trip;
import com.elsoudany.said.tripreminderapp.room.TripDAO;

public class ReminderService extends Service {
    DialogHandler dialogHandler;
    TextView name;
    TextView startPoint;
    TextView endPoint;
    Trip trip;
    AppDatabase db;
    TripDAO tripDAO;
    public ReminderService() {
        dialogHandler = new DialogHandler();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        long uid = intent.getLongExtra("tripUid",60);

        new Thread(){
            @Override
            public void run() {

                db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"DataBase-name").build();
                tripDAO = db.tripDAO();
                trip = tripDAO.getTrip(uid);
                dialogHandler.sendEmptyMessage(1);


            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }
    private void displayNotification(String tripName) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "simplifiedcoding")
                .setContentTitle(tripName)
                .setSmallIcon(R.drawable.ic_notification)
                .addAction(R.drawable.ic_notification,"START",null)
                .addAction(R.drawable.ic_notification,"CANCEL",null)
                .setColor(ContextCompat.getColor(ReminderService.this, R.color.colorPrimaryDark))
                .setSmallIcon(R.drawable.ic_notification);
        notificationManager.notify(1, notification.build());
    }

    private class DialogHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

            final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(
                    500, 200, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE,
                    PixelFormat.TRANSPARENT);

            parameters.gravity = Gravity.CENTER | Gravity.CENTER;
            parameters.x = 0;
            parameters.y = 0;
            SharedPreferences sharedPreferences = getSharedPreferences("checkingComingFromService",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("comingFromService",true).commit();
            Dialog dialog = new Dialog(ReminderService.this);
            dialog.getWindow().setAttributes(parameters);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.trip_item_dialog);
            dialog.setTitle("Trip Reminder");
            name = dialog.findViewById(R.id.trip_name);
            startPoint = dialog.findViewById(R.id.startPointField);
            endPoint = dialog.findViewById(R.id.endPointField);
            name.setText(trip.tripName);
            startPoint.setText(trip.startPoint);
            endPoint.setText(trip.endPoint);
            dialog.show();

            Button startBtn = dialog.findViewById(R.id.startBtn);
            Button cancelBtn = dialog.findViewById(R.id.cancelBtn);
            Button snoozeBtn = dialog.findViewById(R.id.snooze);
            snoozeBtn.setOnClickListener(view -> {
                displayNotification( "snoozed");
                dialog.dismiss();
            });
            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    trip.status = "started";
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + trip.endPoint));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    dialog.dismiss();
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            tripDAO.insert(trip);
                        }

                    }.start();


                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    trip.status = "cancelled";
                    dialog.dismiss();
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            tripDAO.insert(trip);
                        }
                    }.start();

                }
            });

        }
    }
}