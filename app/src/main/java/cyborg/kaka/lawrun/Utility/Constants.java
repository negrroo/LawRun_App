package cyborg.kaka.lawrun.Utility;

public class Constants {

    // Profiles
    public static int PROFILE_BALANCE = 0;
    public static int PROFILE_EXTRA_BALANCE = 1;
    public static int PROFILE_BATTERY = 2;
    public static int PROFILE_PERFORMANCE = 3;
    public static int PROFILE_GAMING = 4;

    // PUBGM
    public static String PUBG_GFX_FILE = "/data/media/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Config/Android/UserCustom.ini";

    // Stats
    public static String MEASURED_FPS_FILE = "/sys/class/drm/sde-crtc-0/measured_fps";

    // Kernel
    public static String CHARGING_MAX_FILE = "/sys/class/power_supply/battery/constant_charge_current_max";
    public static String BATTERY_THERMAL_COOL_FILE = "/sys/class/power_supply/bms/temp_cool";
    public static String BATTERY_THERMAL_WARM_FILE = "/sys/class/power_supply/bms/temp_warm";
}
