package library;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import app.argos.com.argosapp.MainActivity;
import app.argos.com.argosapp.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final int MESSAGE_NOTIFICATION_ID = 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("", "From: " + remoteMessage.getFrom());
        if(remoteMessage.getNotification() != null) {
            Log.d("", "Notification Message Body: " + remoteMessage.getNotification().getBody());
            createNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.v("NEW_TOKEN",s);

        sendTokenNotification(s);
    }

    private void sendTokenNotification(String token){

        OkHttpClient client = new OkHttpClient();
        String urlString = "https://argosapi.herokuapp.com/notification/send";
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JSONObject value = new JSONObject();
        try {
            value.put("to", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject notification = new JSONObject();
        try {
            notification.put("title", getResources().getString(R.string.title_notification));
            notification.put("body", getResources().getString(R.string.body_notification));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            value.put("notification", notification);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, value.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("NEW_TOKEN", "ECHEC :(");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.v("NEW_TOKEN", "SUCCES :)");
            }
        });
    }

    // Creates notification based on title and body received
    private void createNotification(String title, String body) {
        Context context = getBaseContext();

        final Intent launchNotifiactionIntent = new Intent(context, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchNotifiactionIntent, PendingIntent.FLAG_ONE_SHOT);

        long[] v = {500,1000};
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title)
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