package rs.tridanwebshop.tridan.fcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import rs.tridanwebshop.tridan.R;
import rs.tridanwebshop.tridan.common.application.MyApplication;

/**
 * Created by 1 on 4/19/2016.
 */
public class NotificationUtils {

    private static String TAG = NotificationUtils.class.getSimpleName();

    private static String CHANNEL_ID = "59785983789";

    private Context mContext;

    public NotificationUtils() {
    }

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications() {
        NotificationManager notificationManager = (NotificationManager) MyApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void showNotificationMessage(String title, String message, long timeMillis, Intent intent) {
        showNotificationMessage(title, message, timeMillis, intent, null);
    }

    public static PendingIntent getContentPendingIntentType(
            Intent notificationIntent,
            Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return getPendingIntent(
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE,
                    notificationIntent, context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getPendingIntent(
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE,
                    notificationIntent, context);
        } else {
            return getPendingIntent(
                    PendingIntent.FLAG_UPDATE_CURRENT,
                    notificationIntent,
                    context);
        }
    }

    private int flag() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            return PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE;

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;

        } else {

            return PendingIntent.FLAG_UPDATE_CURRENT;

        }
    }

    private static PendingIntent getPendingIntent(
            int flag,
            Intent notificationIntent,
            Context context) {
        return PendingIntent.getActivity(context, 0, notificationIntent, flag);
    }

    public void showNotificationMessage(final String title, final String message, final long timeMillis, Intent intent, String imageUrl) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;


        // notification icon
        final int icon = R.mipmap.ic_drill;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        //PendingIntent.FLAG_CANCEL_CURRENT
                        flag()
                );

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext, CHANNEL_ID);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (!TextUtils.isEmpty(imageUrl)) {

            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {

                Bitmap bitmap = getBitmapFromURL(imageUrl);

                if (bitmap != null) {
                    showBigNotification(bitmap, mBuilder, icon, title, message, timeMillis, resultPendingIntent, defaultSoundUri);
                } else {
                    showSmallNotification(mBuilder, icon, title, message, timeMillis, resultPendingIntent, defaultSoundUri);
                }
            }
        } else {
            showSmallNotification(mBuilder, icon, title, message, timeMillis, resultPendingIntent, defaultSoundUri);
            playNotificationSound();
        }
    }

    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, long timeMillis, PendingIntent resultPendingIntent, Uri alarmSound) {

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        if (Config.appendNotificationMessages) {
            // store the notification in shared pref first
            MyApplication.getInstance().getPrefManager().addNotification(message);

            // get the notifications from shared preferences
            String oldNotification = MyApplication.getInstance().getPrefManager().getNotifications();

            List<String> messages = Arrays.asList(oldNotification.split("\\|"));

            for (int i = messages.size() - 1; i >= 0; i--) {
                inboxStyle.addLine(messages.get(i));
            }
        } else {
            inboxStyle.addLine(message);
        }


        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                .setWhen(timeMillis)
                .setSmallIcon(R.mipmap.ic_drill)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID, notification);
    }

    @SuppressWarnings("deprecation")
    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, long timeMillis, PendingIntent resultPendingIntent, Uri alarmSound) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            bigPictureStyle.setSummaryText(Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT).toString());
        } else {
            // This one is deprecated, but called for versions < N
            bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        }
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                .setWhen(timeMillis)
                .setSmallIcon(R.mipmap.ic_drill)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification);
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
            /**TODO get a nice notification Sound */
            //Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
            //        + "://" + MyApplication.getInstance().getApplicationContext().getPackageName() + "/raw/notification");
            //Ringtone r = RingtoneManager.getRingtone(MyApplication.getInstance().getApplicationContext(), alarmSound);
            //r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}