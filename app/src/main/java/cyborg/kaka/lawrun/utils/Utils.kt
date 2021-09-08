package cyborg.kaka.lawrun.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.ShellUtils
import cyborg.kaka.lawrun.BuildConfig
import cyborg.kaka.lawrun.utils.Constants.UPDATE_URL
import java.net.URL

object Utils {
    fun rootCheck(ctx: Context?): Boolean {
        if (Shell.rootAccess()) return true
        Toast.makeText(ctx, "No Root !", Toast.LENGTH_SHORT).show()
        return false
    }

    fun updateCheck(ctx: Context) {
        Thread {
            try {
               val updateResponse = URL(UPDATE_URL).readText()
                val onlineVersion =
                    updateResponse.split("Version = ")[1].split("\n")[0].trim().toInt()
                val updateUrl = updateResponse.split("UpdateUrl = ")[1].split("\n")[0].trim()
                if (onlineVersion != BuildConfig.VERSION_CODE) {
                    val browserIntent = Intent(Intent.ACTION_VIEW)
                    browserIntent.data = Uri.parse(updateUrl)
                    ctx.startActivity(browserIntent)
                }
            } catch (e: Exception) {}
        }.start()
    }

    fun getProp(prop: String): String {
        return ShellUtils.fastCmd("getprop $prop")
    }

    fun setProp(prop: String, value: String) {
        ShellUtils.fastCmd("setprop $prop $value")
    }
}
