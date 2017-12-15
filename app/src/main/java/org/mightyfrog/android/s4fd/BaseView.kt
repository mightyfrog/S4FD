package org.mightyfrog.android.s4fd

/**
 * @author Shigehiro Soejima
 */
interface BaseView<in T> {
    fun setPresenter(presenter: T)

    fun showErrorMessage(msg: String)

    fun showErrorMessage(resId: Int)
}