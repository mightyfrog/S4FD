package org.mightyfrog.android.s4fd.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

/**
 * @author Shigehiro Soejima
 */
@Table(database = AppDatabase::class)
class SmashAttributeType : BaseModel() {
    @Column
    @SerializedName("name")
    @Expose
    var name: String? = null

    @PrimaryKey
    @Column
    @SerializedName("id")
    @Expose
    var id: Int = 0

    override fun toString(): String {
        return "SmashAttributeType(id=$id, name=$name)"
    }
}