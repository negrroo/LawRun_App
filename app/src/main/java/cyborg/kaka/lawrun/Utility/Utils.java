package cyborg.kaka.lawrun.Utility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.topjohnwu.superuser.Shell;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import cyborg.kaka.lawrun.BuildConfig;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cyborg.kaka.lawrun.Utility.Constants.UPDATE_URL;

public class Utils {

    public static boolean rootCheck(Context ctx) {
        if (Shell.rootAccess())
            return true;
        Toast.makeText(ctx, "No Root !", Toast.LENGTH_SHORT).show();
        return false;
    }

    public static void updateCheck(Context ctx) {
        OkHttpClient updateClient = new OkHttpClient();
        Request updateRequest = new Request.Builder()
                .url(UPDATE_URL)
                .build();
        updateClient.newCall(updateRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String updateResponse = Objects.requireNonNull(response.body()).string();

                int onlineVersion = Integer.parseInt(updateResponse.split("Version = ")[1].split("\n")[0]);
                String updateUrl = updateResponse.split("UpdateUrl = ")[1].split("\n")[0];

                if (onlineVersion != BuildConfig.VERSION_CODE) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(updateUrl));
                    ctx.startActivity(browserIntent);
                }
            }
        });
    }
}
