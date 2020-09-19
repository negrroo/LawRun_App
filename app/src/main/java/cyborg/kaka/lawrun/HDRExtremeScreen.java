package cyborg.kaka.lawrun;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.topjohnwu.superuser.Shell;
import com.topjohnwu.superuser.ShellUtils;
import com.topjohnwu.superuser.io.SuFile;

import static cyborg.kaka.lawrun.Utility.Constants.PUBG_GFX_FILE;

public class HDRExtremeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check Root Support
        if (!Shell.rootAccess()) {
            Toast.makeText(this, "Root access not found !", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Check PUBGM Installed
        if (ShellUtils.fastCmd("pm list packages | grep com.tencent.ig").equals("")) {
            Toast.makeText(this, "PUBGM not found !", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Check PUBGM Data
        if (!SuFile.open(PUBG_GFX_FILE).exists()) {
            Toast.makeText(this, "Install PUBGM & open atleast one time", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Stop PUBGM
        ShellUtils.fastCmd("am kill com.tencent.ig");
        ShellUtils.fastCmd("am force-stop com.tencent.ig");

        // Patch File
        String temp_file_path = "/data/media/0/UserCustom.ini";

        ShellUtils.fastCmd("cat " + PUBG_GFX_FILE + " >> " + temp_file_path);

        ShellUtils.fastCmd("echo '[UserCustom DeviceProfile]' >> " + temp_file_path);
        ShellUtils.fastCmd("echo '+CVars=0B57292C3B3E3D1C0F101A1C3F292A35160E444F49' >> " + temp_file_path);
        ShellUtils.fastCmd("echo '+CVars=0B57292C3B3E3D1C0F101A1C3F292A34101D444F49' >> " + temp_file_path);
        ShellUtils.fastCmd("echo '+CVars=0B57292C3B3E3D1C0F101A1C3F292A31101E11444F49' >> " + temp_file_path);
        ShellUtils.fastCmd("echo '+CVars=0B57292C3B3E3D1C0F101A1C3F292A313D2B444F49' >> " + temp_file_path);

        ShellUtils.fastCmd("rm -f " + PUBG_GFX_FILE);
        ShellUtils.fastCmd("cp " + temp_file_path + " " + PUBG_GFX_FILE);
        ShellUtils.fastCmd("rm -f " + temp_file_path);

        // Start PUBGM
        ShellUtils.fastCmd("am start -a android.intent.action.MAIN -n com.tencent.ig/com.epicgames.ue4.SplashActivity");

        // Done.
        Toast.makeText(this, "Law HDR", Toast.LENGTH_SHORT).show();
        finish();
    }
}
