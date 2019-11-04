package com.example.amit.uniconnexample.Others

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by amit on 7/1/17.
 */

class Foreground : Application.ActivityLifecycleCallbacks {

    var isForeground = false
        private set
    private var paused = true
    private val handler = Handler()
    private val listeners = CopyOnWriteArrayList<Listener>()
    private var check: Runnable? = null

    val isBackground: Boolean
        get() = !isForeground

    interface Listener {

        fun onBecameForeground()

        fun onBecameBackground()

    }

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    override fun onActivityResumed(activity: Activity) {
        paused = false
        val wasBackground = !isForeground
        isForeground = true

        if (check != null)
            handler.removeCallbacks(check)

        if (wasBackground) {
            Log.i(TAG, "went foreground")
            for (l in listeners) {
                try {
                    l.onBecameForeground()
                } catch (exc: Exception) {
                    Log.e(TAG, "Listener threw exception!", exc)
                }

            }
        } else {
            Log.i(TAG, "still foreground")
        }
    }

    override fun onActivityPaused(activity: Activity) {
        paused = true

        if (check != null)
            handler.removeCallbacks(check)

        handler.postDelayed(check = Runnable {
            if (isForeground && paused) {
                isForeground = false
                Log.i(TAG, "went background")
                for (l in listeners) {
                    try {
                        l.onBecameBackground()
                    } catch (exc: Exception) {
                        Log.e(TAG, "Listener threw exception!", exc)
                    }

                }
            } else {
                Log.i(TAG, "still foreground")
            }
        }, CHECK_DELAY)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    companion object {

        val CHECK_DELAY: Long = 500
        val TAG = Foreground::class.java.name

        private var instance: Foreground? = null

        /**
         * Its not strictly necessary to use this method - _usually_ invoking
         * get with a Context gives us a path to retrieve the Application and
         * initialise, but sometimes (e.g. in test harness) the ApplicationContext
         * is != the Application, and the docs make no guarantees.
         *
         * @param application
         * @return an initialised Foreground instance
         */
        fun init(application: Application): Foreground {
            if (instance == null) {
                instance = Foreground()
                application.registerActivityLifecycleCallbacks(instance)
            }
            return instance
        }

        operator fun get(application: Application): Foreground? {
            if (instance == null) {
                init(application)
            }
            return instance
        }

        operator fun get(ctx: Context): Foreground {
            if (instance == null) {
                val appCtx = ctx.applicationContext
                if (appCtx is Application) {
                    init(appCtx)
                }
                throw IllegalStateException(
                        "Foreground is not initialised and " + "cannot obtain the Application object")
            }
            return instance
        }

        fun get(): Foreground {
            checkNotNull(instance) { "Foreground is not initialised - invoke " + "at least once with parameterised init/get" }
            return instance
        }
    }
}