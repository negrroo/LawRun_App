package cyborg.kaka.lawrun;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import static cyborg.kaka.lawrun.Utility.Utils.updateCheck;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_splash);

        // FullScreen
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Check for update
        updateCheck(SplashScreen.this);

        // Start Profile Screen
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreen.this, MainScreen.class));
            finish();
        }, 350);
    }
}
