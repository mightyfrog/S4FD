package org.mightyfrog.android.s4fd.data

import com.raizlabs.android.dbflow.annotation.Database

/**
 * @author Shigehiro Soejima
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION, generatedClassSeparator = "_")
object AppDatabase {
    const val NAME = "s4fd"
    const val VERSION = 1
}
