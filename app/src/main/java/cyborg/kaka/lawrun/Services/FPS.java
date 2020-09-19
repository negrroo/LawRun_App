package cyborg.kaka.lawrun.Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.topjohnwu.superuser.ShellUtils;

import java.util.Objects;

import cyborg.kaka.lawrun.R;
import cyborg.kaka.lawrun.Utility.Constants;

public class FPS extends Service {

    public static boolean serviceRunning = false;
    public String fps = "";
    public TextView tv_fps;
    private View layoutView;

    private WindowManager windowManager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("InflateParams")
    @Override
    public void onCreate() {
        // Set State To Running
        serviceRunning = true;

        // Attach View To Left Top Corner
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        LayoutInflater inflater = LayoutInflater.from(this);
        layoutView = inflater.inflate(R.layout.layout_fps, null);
        tv_fps = layoutView.findViewById(R.id.tv_fps);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(-2, -2, 2038, 4980792, -3);
        params.gravity = Gravity.START | Gravity.TOP;

        windowManager.addView(layoutView, params);

        // Keep Alive The Service
        NotificationChannel notificationChannel = new NotificationChannel("LawRun_stats_notification_channel", "LawRun_stats_notification_channel", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setSound(null, null);
        ((NotificationManager) Objects.requireNonNull(getSystemService(NOTIFICATION_SERVICE))).createNotificationChannel(notificationChannel);
        Notification.Builder notificationBuilder = new Notification.Builder(this, "LawRun_stats_notification_channel");
        notificationBuilder.setContentTitle("LawRun FPS Meter").setContentText("Keep FPS meter running...").setSmallIcon(R.mipmap.ic_notification);
        startForeground(69, notificationBuilder.build());
        onConfigurationChanged(getResources().getConfiguration());

        // Update FPS Counts
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                try {
                    FPS.this.fps = ShellUtils.fastCmd("cat " + Constants.MEASURED_FPS_FILE).split(" ")[1];
                    FPS.this.tv_fps.setText(fps.substring(0, fps.indexOf(".")));
                } catch (Exception ignored) {
                }
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Stop Service
        this.windowManager.removeView(this.layoutView);
        serviceRunning = false;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            tv_fps.setPadding((int) getResources().getDimension(R.dimen.primary_padding), (int) getResources().getDimension(R.dimen.primary_padding), 0, 0);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            tv_fps.setPadding((int) getResources().getDimension(R.dimen.primary_padding), 0, 0, 0);
        }
    }
}
