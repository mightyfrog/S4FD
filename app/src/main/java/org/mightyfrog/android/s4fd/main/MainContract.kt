package org.mightyfrog.android.s4fd.main

import org.mightyfrog.android.s4fd.BaseView
import org.mightyfrog.android.s4fd.data.KHCharacter
import java.io.File
import java.io.InputStream

/**
 * @author Shigehiro Soejima
 */
interface MainContract {

    interface View : BaseView<MainPresenter> {
        fun showCharacters(list: List<KHCharacter>?)

        fun showDetails(id: Int, position: Int)

        fun showActivityCircle()

        fun hideActivityCircle()

        fun showProgressDialog(resId: Int, arg: String? = null)

        fun hideProgressDialog()

        fun showFallbackDialog()

        fun showDatabaseCopiedDialog()
    }

    interface Presenter {
        fun loadCharacters()

        fun copyDatabase(input: InputStream, dbFile: File)

        fun destroy()
    }
}