package com.izzy2lost.x1box

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.OrientationEventListener

/**
 * Prevents reversed (upside-down) orientations.
 *
 * UI mode:  portrait when upright/right-rotated, left landscape otherwise.
 * Game mode: always left landscape, no exceptions.
 */
class OrientationLocker(private val activity: Activity, private val landscapeOnly: Boolean = false) {

    private val listener = object : OrientationEventListener(activity) {
        override fun onOrientationChanged(orientation: Int) {
            if (orientation == ORIENTATION_UNKNOWN) return

            val target = if (landscapeOnly) {
                // In-game: always left landscape regardless of how the device is held.
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                // UI: portrait when upright or rotating right, landscape otherwise.
                //   315-360 / 0-134  → portrait (upright + right rotation)
                //   135-314           → landscape (upside-down + left rotation)
                if (orientation < 135 || orientation >= 315)
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                else
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            if (activity.requestedOrientation != target) {
                activity.requestedOrientation = target
            }
        }
    }

    fun enable() {
        if (listener.canDetectOrientation()) listener.enable()
    }

    fun disable() {
        listener.disable()
    }
}
