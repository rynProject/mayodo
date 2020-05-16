//package com.mayodo.news;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Build;
//import androidx.core.app.NotificationCompat;
//import androidx.core.content.ContextCompat;
//import android.util.Log;
////import com.google.firebase.messaging.FirebaseMessagingService;
////import com.google.firebase.messaging.RemoteMessage;
//import com.mayodo.news.Home.HomePageActivity;
//import com.mayodo.news.utils.SharedObjects;
//
//
//public class FCMFirebaseMessagingService extends FirebaseMessagingService {
//
//    private String showNotification;
//    private Intent backIntent;
//    private boolean isBackActivityShow = false;
//    SharedObjects sharedObjects;
//
//    /**
//     * Called when message is received.
//     *
//     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
//     */
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        sharedObjects = new SharedObjects(this);
//
//        showNotification = sharedObjects.preferencesEditor.getPreference("Notification");
//        String title = "";
//        String message = "";
///*
//
//        if (TextUtils.isEmpty(userId)) {
//            return;
//        }*/
//
//        if (remoteMessage != null) {
//            Log.e("FCM : " , remoteMessage.toString());
//            Log.e("FCM 1: " , String.valueOf(remoteMessage.getData()));
//            title = remoteMessage.getNotification().getTitle();
//            message = remoteMessage.getNotification().getBody();
//        }
//
//        if (remoteMessage.getNotification() != null) {
//            Log.e("FCM 2:","");
//
//        }
//
//
//        sendNotification(title, message);
//    }
//
//    /**
//     * Called if InstanceID token is updated. This may occur if the security of
//     * the previous token had been compromised. Note that this is called when the InstanceID token
//     * is initially generated so this is where you would retrieve the token.
//     */
//   /* @Override
//    public void onNewToken(String token) {
//        Log.d("Refreshed token: " , token);
//        // prefUtils.setString(Consts.SharedPrefs.FIREBASE_TOKEN, token);
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
//        //sendRegistrationToServer(token);
//    }
//*/
//    /**
//     * Create and show a simple notification containing the received FCM message.
//     */
//    private void sendNotification(String title, String message) {
//
//        Intent intent = null;
//
//        intent = new Intent(this, HomePageActivity.class);
//
//        if (showNotification.equalsIgnoreCase("no")) {
//            return;
//
//        }
//
//        int currentApiVersion = Build.VERSION.SDK_INT;
//
//        PendingIntent pendingIntent;
//        if (isBackActivityShow) {
//            pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{backIntent, intent}, PendingIntent.FLAG_ONE_SHOT);
//        } else {
//            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        }
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        int id = (int) System.currentTimeMillis();
//
//        if (currentApiVersion >= Build.VERSION_CODES.O) {
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            // Sets an ID for the notification, so it can be updated.
//            int notifyID = 1;
//
//            // The id of the channel.
//            String CHANNEL_ID = "default";
//
//            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
//            mChannel.setDescription(message);
//            mChannel.enableLights(true);
//            mChannel.setLightColor(Color.BLUE);
//            mChannel.setSound(defaultSoundUri, null);
//            mChannel.setShowBadge(true);
//            notificationManager.createNotificationChannel(mChannel);
//
//            Notification notification = new Notification.Builder(this, CHANNEL_ID)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setColor(ContextCompat.getColor(this, R.color.app_main_color))
//                    .setContentTitle(title)//Title or App name
//                    .setContentText(message) // Message
//                    .setChannelId(CHANNEL_ID)
//                    .setAutoCancel(true)
//                    .build();
//
//            notificationManager.notify(notifyID, notification);
//        } else if (currentApiVersion >= Build.VERSION_CODES.LOLLIPOP) {
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setColor(ContextCompat.getColor(this, R.color.app_main_color))
//                    .setContentTitle(title)//Title or App name
//                    .setContentText(message) // Message
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(pendingIntent);
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(id, notificationBuilder.build());
//        } else {
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setColor(ContextCompat.getColor(this, R.color.app_main_color))
//                    .setContentTitle(title)//Title or App name
//                    .setContentText(message) // Message
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(pendingIntent);
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(id, notificationBuilder.build());
//        }
//    }
//}
//
//
//
//
///*
//package com.itechnotion.xstore;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Build;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.content.ContextCompat;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//import com.google.gson.Gson;
//import com.itechnotion.xstore.utils.SharedObjects;
//
//public class FCMFirebaseMessagingService extends FirebaseMessagingService {
//
//    private String showNotification;
//    private Intent backIntent;
//    private boolean isBackActivityShow = false;
//    SharedObjects sharedObjects;
//
//    */
///**
// * Called when message is received.
// *
// * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
// *//*
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        sharedObjects = new SharedObjects(this);
//
//        showNotification = sharedObjects.preferencesEditor.getPreference("Notification");
//        String title = "";
//        String message = "";
//*/
///*
//
//        if (TextUtils.isEmpty(userId)) {
//            return;
//        }*//*
//
//
//        if (remoteMessage != null) {
//            Log.e("FCM : " , remoteMessage.toString());
//            if (remoteMessage.getData().size() > 0) {
//                Log.e("FCM 1: " , String.valueOf(remoteMessage.getData()));
//                title = remoteMessage.getNotification().getTitle();
//                message = remoteMessage.getNotification().getBody();
//                }
//            }
//
//            if (remoteMessage.getNotification() != null) {
//                Log.e("FCM 2:","");
//
//            }
//
//
//            sendNotification(title, message);
//        }
//
//    */
///**
// * Called if InstanceID token is updated. This may occur if the security of
// * the previous token had been compromised. Note that this is called when the InstanceID token
// * is initially generated so this is where you would retrieve the token.
// *//*
//
//    @Override
//    public void onNewToken(String token) {
//        Log.d("Refreshed token: " , token);
//       // prefUtils.setString(Consts.SharedPrefs.FIREBASE_TOKEN, token);
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
//        //sendRegistrationToServer(token);
//    }
//
//    */
///**
// * Create and show a simple notification containing the received FCM message.
// *//*
//
//    private void sendNotification(String title, String message) {
//
//        Intent intent = null;
//
//        intent = new Intent(this, MainActivity.class);
//
//        if (showNotification.equalsIgnoreCase("no")) {
//            return;
//
//        }
//
//        int currentApiVersion = Build.VERSION.SDK_INT;
//
//        PendingIntent pendingIntent;
//        if (isBackActivityShow) {
//            pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{backIntent, intent}, PendingIntent.FLAG_ONE_SHOT);
//        } else {
//            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        }
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        int id = (int) System.currentTimeMillis();
//
//        if (currentApiVersion >= Build.VERSION_CODES.O) {
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            // Sets an ID for the notification, so it can be updated.
//            int notifyID = 1;
//
//            // The id of the channel.
//            String CHANNEL_ID = "default";
//
//            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
//            mChannel.setDescription(message);
//            mChannel.enableLights(true);
//            mChannel.setLightColor(Color.BLUE);
//            mChannel.setSound(defaultSoundUri, null);
//            mChannel.setShowBadge(true);
//            notificationManager.createNotificationChannel(mChannel);
//
//            Notification notification = new Notification.Builder(this, CHANNEL_ID)
//                    .setSmallIcon(R.drawable.ic_notification)
//                    .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
//                    .setContentTitle(title)//Title or App name
//                    .setContentText(message) // Message
//                    .setChannelId(CHANNEL_ID)
//                    .setAutoCancel(true)
//                    .build();
//
//            notificationManager.notify(notifyID, notification);
//        } else if (currentApiVersion >= Build.VERSION_CODES.LOLLIPOP) {
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.drawable.ic_notification)
//                    .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
//                    .setContentTitle(title)//Title or App name
//                    .setContentText(message) // Message
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(pendingIntent);
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(id, notificationBuilder.build());
//        } else {
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
//                    .setContentTitle(title)//Title or App name
//                    .setContentText(message) // Message
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(pendingIntent);
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(id, notificationBuilder.build());
//        }
//    }
//}
//*/
