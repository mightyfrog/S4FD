package org.mightyfrog.android.s4fd

import android.content.Context
import android.content.SharedPreferences
import dagger.Component
import org.mightyfrog.android.s4fd.util.KHService
import javax.inject.Singleton

/**
 * @author Shigehiro Soejima
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun exposeApplicationContext(): Context

    fun exposeKHService(): KHService

    fun exposeSharedPreferences(): SharedPreferences
}