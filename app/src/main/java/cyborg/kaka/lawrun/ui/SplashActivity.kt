package cyborg.kaka.lawrun.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cyborg.kaka.lawrun.R
import cyborg.kaka.lawrun.utils.Utils

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // FullScreen
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = ( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN)

        // Check for update
        Utils.updateCheck(this@SplashActivity)

        // Start Profile Screen
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 350)

    }
}