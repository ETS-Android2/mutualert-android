package com.mcuevapps.mutualert.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.mcuevapps.mutualert.R;
import com.mcuevapps.mutualert.common.Constantes;
import com.mcuevapps.mutualert.common.SharedPreferencesManager;
import com.mcuevapps.mutualert.model.NotificationEmergency;
import com.mcuevapps.mutualert.retrofit.AuthMutuAlertClient;
import com.mcuevapps.mutualert.retrofit.AuthMutuAlertService;
import com.mcuevapps.mutualert.retrofit.request.RequestUserSessionFcm;
import com.mcuevapps.mutualert.retrofit.response.ResponseSuccess;
import com.mcuevapps.mutualert.ui.home.HomeActivity;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 *
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 *
 * <intent-filter>
 *   <action android:name="com.google.firebase.MESSAGING_EVENT" />
 * </intent-filter>
 */
public class FirebaseCMService extends FirebaseMessagingService {

    private static final String TAG = FirebaseCMService.class.getSimpleName();

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            String event = remoteMessage.getData().get(Constantes.NOTIFY_EVENT);
            if(event.equals(Constantes.NOTIFY_EVENT_NOTIFY)){
                handleNow(remoteMessage.getData());
            } else if(event.equals(Constantes.NOTIFY_EVENT_PROCESS)){
                //scheduleJob();
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
    // [END receive_message]


    // [START on_new_token]
    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        sendRegistrationToServer(token);
    }

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow(Map<String, String> payload) {
        if(SharedPreferencesManager.getSomeBooleanValue(Constantes.PREF_ALERT_APP, false)){
            return;
        }

        String type = payload.get(Constantes.NOTIFY_TYPE);
        if(type.equals(Constantes.NOTIFY_TYPE_EMERGENCY_INIT)){
            String origin = payload.get(Constantes.NOTIFY_ORIGIN);

            Gson gson = new Gson();
            NotificationEmergency emergency = gson.fromJson(payload.get(Constantes.NOTIFY_DATA), NotificationEmergency.class);

            String channelId;
            String title;
            String message;
            if(origin.equals(Constantes.NOTIFY_EMERGENCY_FROM_MYCONTACT)){
                channelId = Constantes.CHANNEL_ID_EMERGENCY_MYCONTACT;
                title = getString(R.string.notify_title_emergency_mycontact);
                message = getString(R.string.notify_message_emergency_mycontact);
            } else if(origin.equals(Constantes.NOTIFY_EMERGENCY_FROM_SELFCONTACT)){
                channelId = Constantes.CHANNEL_ID_EMERGENCY_SELFCONTACT;
                title = getString(R.string.notify_title_emergency_selfcontact);
                message = getString(R.string.notify_message_emergency_selfcontact);
            } else {
                channelId = Constantes.CHANNEL_ID_EMERGENCY_DEFAULT;
                title = getString(R.string.notify_title_emergency_default);
                message = getString(R.string.notify_message_emergency_default);
            }
            message = message.replace(Constantes.KEY_NAME, emergency.getNombres());
            sendNotification(channelId, title, message, Integer.parseInt(Constantes.NOTIFY_ID_EMERGENCY +""+emergency.getId()));
        } else if (type.equals(Constantes.NOTIFY_TYPE_EMERGENCY_END)){
            int id = Integer.parseInt(payload.get(Constantes.NOTIFY_DATA));
            cancelNotification(Integer.parseInt(Constantes.NOTIFY_ID_EMERGENCY +""+id));
        }
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM registration token with any
     * server-side account maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        SharedPreferencesManager.setSomeBooleanValue(Constantes.PREF_FCM_TOKEN, false);

        AuthMutuAlertClient authMutuAlertClient = AuthMutuAlertClient.getInstance();;
        AuthMutuAlertService authMutuAlertService = authMutuAlertClient.getAuthMutuAlertService();

        RequestUserSessionFcm requestUserSessionFcm = new RequestUserSessionFcm(token);
        Call<ResponseSuccess> call = authMutuAlertService.fcmSession(requestUserSessionFcm);
        call.enqueue(new Callback<ResponseSuccess>() {
            @Override
            public void onResponse(Call<ResponseSuccess> call, Response<ResponseSuccess> response) {
                if(response.isSuccessful() && response.body().getSuccess()){
                    SharedPreferencesManager.setSomeBooleanValue(Constantes.PREF_FCM_TOKEN, true);
                }
            }

            @Override
            public void onFailure(Call<ResponseSuccess> call, Throwable t) { }
        });
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param channelId FCM message body received
     * @param title FCM message body received
     * @param message FCM message body received
     *
     */
    private void sendNotification(String channelId, String title, String message, int notificationId) {
        String channelName;
        int priority;
        int importance;
        if(channelId.equals(Constantes.CHANNEL_ID_EMERGENCY_MYCONTACT)){
            channelName = getString(R.string.channel_name_emergency_mycontact);
            priority = Notification.PRIORITY_MAX;
            importance = NotificationManager.IMPORTANCE_MAX;
        } else if(channelId.equals(Constantes.CHANNEL_ID_EMERGENCY_SELFCONTACT)){
            channelName = getString(R.string.channel_name_emergency_selfcontact);
            priority = Notification.PRIORITY_HIGH;
            importance = NotificationManager.IMPORTANCE_HIGH;
        } else {
            channelName = getString(R.string.channel_name_emergency_default);
            priority = Notification.PRIORITY_DEFAULT;
            importance = NotificationManager.IMPORTANCE_DEFAULT;
        }

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setContentIntent(pendingIntent)
                        .setPriority(priority)
                        .setSmallIcon(R.drawable.icon_mutualert_notification)
                        .setSound(defaultSoundUri);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private void cancelNotification(int notificationId){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }
}