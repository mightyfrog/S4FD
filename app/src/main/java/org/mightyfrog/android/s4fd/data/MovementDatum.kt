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
class MovementDatum : BaseModel() {
    @Column
    @SerializedName("name")
    @Expose
    var name: String? = null

    @Column
    @SerializedName("ownerId")
    @Expose
    var ownerId: Int = 0

    @PrimaryKey
    @Column
    @SerializedName("id")
    @Expose
    var id: Int = 0

    @Column
    @SerializedName("value")
    @Expose
    var value: String? = null

    override fun toString(): String {
        return "MovementDatum(id=$id, name=$name, ownerId=$ownerId, value=$value)"
    }
}