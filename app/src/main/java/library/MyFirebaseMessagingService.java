package library;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import androidx.core.app.NotificationCompat;
import app.argos.com.argosapp.MainActivity;
import app.argos.com.argosapp.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final int MESSAGE_NOTIFICATION_ID = 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("", "From: " + remoteMessage.getFrom());
        if(remoteMessage.getNotification() != null) {
            Log.d("", "Notification Message Body: " + remoteMessage.getNotification().getBody());
            createNotification(remoteMessage.getFrom(), remoteMessage.getNotification().getBody());
        }
    }

    // Creates notification based on title and body received
    private void createNotification(String title, String body) {
        Context context = getBaseContext();

        final Intent launchNotifiactionIntent = new Intent(context, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchNotifiactionIntent, PendingIntent.FLAG_ONE_SHOT);

        long[] v = {500,1000};
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(body)
                .setVibrate(v)
                .setSound(uri)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setTicker(body);   //affiche la notification dans la status bar
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());

    }

}