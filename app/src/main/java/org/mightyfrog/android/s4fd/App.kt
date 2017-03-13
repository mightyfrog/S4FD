package org.mightyfrog.android.s4fd

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager


/**
 * @author Shigehiro Soejima
 */
class App : Application() {
    private lateinit var mAppComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        mAppComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        FlowManager.init(FlowConfig.Builder(this).build())
    }

    override fun onTerminate() {
        FlowManager.destroy()

        super.onTerminate()
    }

    fun getAppComponent(): AppComponent = mAppComponent
}