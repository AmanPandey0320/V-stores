package com.dark_phoenix09.app2pcon2k20.Managers

import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import com.dark_phoenix09.app2pcon2k20.BuildConfig

public class StrictModeManager {
    var enabled : Boolean = false
    fun StrictModeManager(){}


    fun turnOnStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath().build())
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath().build())
        }
    }

    fun permitDiskReads(func: () -> Any) : Any {
        if (BuildConfig.DEBUG) {
            val oldThreadPolicy = StrictMode.getThreadPolicy()
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder(oldThreadPolicy)
                    .permitDiskReads().build())
            val anyValue = func()
            StrictMode.setThreadPolicy(oldThreadPolicy)

            return anyValue
        } else {
            return func()
        }
    }
}