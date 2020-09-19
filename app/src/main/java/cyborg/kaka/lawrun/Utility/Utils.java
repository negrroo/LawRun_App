package cyborg.kaka.lawrun.Utility;

import android.content.Context;
import android.widget.Toast;

import com.topjohnwu.superuser.Shell;

public class Utils {

    public static boolean rootCheck(Context ctx) {
        if (Shell.rootAccess())
            return true;
        Toast.makeText(ctx, "No Root !", Toast.LENGTH_SHORT).show();
        return false;
    }
}
