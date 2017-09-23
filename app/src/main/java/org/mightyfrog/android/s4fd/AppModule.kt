package org.mightyfrog.android.s4fd

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import org.mightyfrog.android.s4fd.util.Const
import org.mightyfrog.android.s4fd.util.KHService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @author Shigehiro Soejima
 */
@Module
class AppModule(private val application: Application) {

    @Singleton
    @Provides
    fun getApplicationContext(): Context = application.applicationContext

    @Singleton
    @Provides
    fun getKHService(): KHService = Retrofit.Builder()
            .baseUrl(Const.KH_END_POINT)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KHService::class.java)

    @Singleton
    @Provides
    fun getSharedPreferences(): SharedPreferences
            = PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
}