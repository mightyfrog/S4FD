package org.mightyfrog.android.s4fd.compare

import dagger.Module
import dagger.Provides
import org.mightyfrog.android.s4fd.util.ActivityScope

/**
 * @author Shigehiro Soejima
 */
@Module
class CompareModule(val mView: CompareContract.View) {

    @ActivityScope
    @Provides
    fun provideCompareContractView(): CompareContract.View = mView
}