package org.mightyfrog.android.s4fd.data

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.QueryModel
import com.raizlabs.android.dbflow.structure.BaseQueryModel

/**
 * @author Shigehiro Soejima
 */
@QueryModel(database = AppDatabase::class, allFields = false)
class MiscAttribute : BaseQueryModel() {
    @Column(name = "id")
    var id: Int = 0

    @Column
    var ownerId: Int = 0

    @Column
    var rank: String? = null

    @Column
    var value: String? = null

    @Column(name = "cName")
    var cName: String? = null

    @Column(name = "sName")
    var sName: String? = null

    override fun toString(): String {
        return "MiscAttribute(ownerId=$ownerId, rank=$rank, value=$value, cName=$cName, sName=$sName)"
    }
}