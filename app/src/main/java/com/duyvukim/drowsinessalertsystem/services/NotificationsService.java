package com.duyvukim.drowsinessalertsystem.services;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.duyvukim.drowsinessalertsystem.R;
import com.duyvukim.drowsinessalertsystem.utils.AppCts;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationsService extends FirebaseMessagingService {

    /**
     * Called when a message is received.
     *
     * <p>This should complete within 20 seconds. Taking longer may interfere with your ability to
     * complete your work and may affect pending messages.
     *
     * <p>This is also called when a notification message is received while the app is in the
     * foreground. The notification parameters can be retrieved with {@link
     * RemoteMessage#getNotification()}.
     *
     * @param message Remote message that has been received.
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        if (message.getNotification() != null) {
            String title = message.getNotification().getTitle();
            String body = message.getNotification().getBody();
            sendNotification(title, body);
        }
    }

    private void sendNotification(String title, String message) {

        // Local notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, AppCts.Notifications.NOTIFICATION_CHANNEL)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

        // Check if permission is granted for post notification
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // Send local notification
        NotificationManagerCompat.from(this).notify(AppCts.Notifications.NOTIFICATION_ID, builder.build());
    }
}
