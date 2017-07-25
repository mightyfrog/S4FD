package org.mightyfrog.android.s4fd.details.tabcontents

import android.content.Context
import android.content.res.Configuration
import android.support.v4.app.Fragment
import android.view.Surface.ROTATION_0
import android.view.WindowManager

/**
 * @author Shigehiro Soejima
 */
open class BaseFragment : Fragment() {
    var surfaceRotation = ROTATION_0

    override fun onResume() {
        super.onResume()

        updateSurfaceRotation()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        updateSurfaceRotation()
    }

    private fun updateSurfaceRotation() {
        val display = (activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        surfaceRotation = display.rotation
    }
}