package cyborg.kaka.lawrun.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.res.Configuration
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.topjohnwu.superuser.ShellUtils
import com.topjohnwu.superuser.io.SuFile
import cyborg.kaka.lawrun.R
import kotlin.math.roundToInt


class FPS : Service() {
    private var fps = ""
    private var fpsFilePath: String = ""
    private lateinit var tvFps: TextView
    private lateinit var layoutView: View
    private lateinit var windowManager: WindowManager

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @SuppressLint("InflateParams")
    override fun onCreate() {
        // Attach View To Left Top Corner
        val inflater: LayoutInflater = LayoutInflater.from(this)
        layoutView = inflater.inflate(R.layout.layout_fps, null)

        tvFps = layoutView.findViewById(R.id.tv_fps)
        findFpsFilePath()

        val params: WindowManager.LayoutParams =
            WindowManager.LayoutParams(-2, -2, 2038, 4980792, -3)
        params.gravity = Gravity.START or Gravity.TOP

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.addView(layoutView, params)

        // Keep Alive The Service
        val notificationChannel = NotificationChannel(
            "LawRun_stats_notification_channel",
            "LawRun_stats_notification_channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.setSound(null, null)
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
            notificationChannel
        )
        val notificationBuilder = Notification.Builder(this, "LawRun_stats_notification_channel")
        notificationBuilder.setContentTitle("LawRun FPS Meter")
            .setContentText("Keep FPS meter running...").setSmallIcon(R.mipmap.ic_notification)
        startForeground(69, notificationBuilder.build())
        onConfigurationChanged(resources.configuration)

        // Update FPS Counts
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    fps = ShellUtils.fastCmd("cat $fpsFilePath").split(" ").toTypedArray()[1]
                    this@FPS.tvFps.text = fps.toDouble().roundToInt().toString()
                } catch (ignored: Exception) {}
                handler.postDelayed(this, 1000)
            }
        }, 1000)

        FPSTile.Service.isRunning = true
    }

    // Find The FPS File From /sys
    private fun findFpsFilePath() {
        when {
            SuFile.open("/sys/class/drm/sde-crtc-0/measured_fps").exists() -> {
                fpsFilePath = "/sys/class/drm/sde-crtc-0/measured_fps"
            }
            SuFile.open("/sys/class/graphics/fb0/measured_fps").exists() -> {
                fpsFilePath = "/sys/class/graphics/fb0/measured_fps"
            }
            else -> {
                Thread {
                    fpsFilePath = ShellUtils.fastCmd("find /sys -name measured_fps 2>/dev/null")
                        .trim { it <= ' ' }.split("\n").minOrNull().toString()
                    if (fpsFilePath == "") {
                        fpsFilePath = ShellUtils.fastCmd("find /sys -name fps 2>/dev/null")
                            .trim { it <= ' ' }.split("\n").minOrNull().toString()
                    }
                }.start()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        // Stop Service
        FPSTile.Service.isRunning = true
        windowManager.removeView(layoutView)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            tvFps.setPadding(
                resources.getDimension(R.dimen.primary_padding).toInt(),
                resources.getDimension(R.dimen.primary_padding).toInt(),
                0,
                0
            )
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            tvFps.setPadding(resources.getDimension(R.dimen.primary_padding).toInt(), 0, 0, 0)
        }
    }
}