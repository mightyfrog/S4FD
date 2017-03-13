package org.mightyfrog.android.s4fd.details.tabcontents.attributes

import com.raizlabs.android.dbflow.sql.language.Select
import org.mightyfrog.android.s4fd.data.MovementDatum
import org.mightyfrog.android.s4fd.data.MovementDatum_Table

/**
 * @author Shigehiro Soejima
 */
class AttributesPresenter(val mView: AttributesContract.View) : AttributesContract.Presenter {

    init {
        mView.setPresenter(this)
    }

    override fun loadAttributes(id: Int) {
        val list = Select().from(MovementDatum::class.java)
                .where(MovementDatum_Table.ownerId.eq(id))
                .queryList()
        mView.showAttributes(list)
    }
}