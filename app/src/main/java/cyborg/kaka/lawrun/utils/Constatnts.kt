package cyborg.kaka.lawrun.utils

object Constants {
    // Profiles
    const val PROFILE_BALANCE = 0
    const val PROFILE_EXTRA_BALANCE = 1
    const val PROFILE_BATTERY = 2
    const val PROFILE_PERFORMANCE = 3
    const val PROFILE_GAMING = 4

    const val PROP_LAWRUN_SUPPORT = "lawrun.support"
    const val PROP_KERNEL_NAME = "lawrun.kernel.name"
    const val PROP_APPLIED_PROFILE = "persist.lawrun.profile"

    // Kernel
    const val DT2W_FILE = "/proc/touchpanel/gesture_enable"

    const val PROP_ZRAM_TWEAKS = "persist.lawrun.zram_tweaks"
    const val PROP_TOUCH_TWEAKS = "persist.lawrun.touch_tweaks"

    const val PROP_POWER_TWEAKS = "persist.lawrun.power_tweaks"
    const val PROP_SYSTEM_TWEAKS = "persist.lawrun.system_tweaks"

    // Battery
    const val CHARGING_MAX_FILE = "/sys/class/power_supply/battery/constant_charge_current_max"
    const val BATTERY_THERMAL_COOL_FILE = "/sys/class/power_supply/bms/temp_cool"
    const val BATTERY_THERMAL_WARM_FILE = "/sys/class/power_supply/bms/temp_warm"

    const val PROP_BATTERY_TWEAKS = "persist.lawrun.battery_tweaks"

    // Gaming
    const val PUBGM_HDR_FILE = "/storage/emulated/0/Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Config/Android/EnjoyCJZC.ini"
    const val PUBGM_KR_HDR_FILE = "/storage/emulated/0/Android/data/com.pubg.krmobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Config/Android/EnjoyCJZC.ini"
    const val PUBGM_IN_HDR_FILE = "/storage/emulated/0/Android/data/com.pubg.imobile/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/Config/Android/EnjoyCJZC.ini"
    const val PROP_HDR_FILE_PATH = "lawrun.hdr_extreme_path"
    const val PROP_GAME_TWEAKS = "persist.lawrun.game_tweaks"

    // Others
    const val UPDATE_URL = "https://raw.githubusercontent.com/negrroo/LawRun_App/master/README.md"
}
