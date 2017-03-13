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
class Knockbacks : BaseModel() {
    @PrimaryKey
    @Column
    @SerializedName("id")
    @Expose
    var id: Int = 0

    @Column
    @SerializedName("ownerId")
    @Expose
    var ownerId: Int = 0

    @Column
    @SerializedName("moveId")
    @Expose
    var moveId: Int = 0

    @Column
    @SerializedName("moveName")
    @Expose
    var moveName: String? = null

    @Column
    @SerializedName("notes")
    @Expose
    var notes: String? = null

    @Column
    @SerializedName("rawValue")
    @Expose
    var rawValue: String? = null

    @Column
    @SerializedName("hitbox1")
    @Expose
    var hitbox1: String? = null

    @Column
    @SerializedName("hitbox2")
    @Expose
    var hitbox2: String? = null

    @Column
    @SerializedName("hitbox3")
    @Expose
    var hitbox3: String? = null

    @Column
    @SerializedName("hitbox4")
    @Expose
    var hitbox4: String? = null

    @Column
    @SerializedName("hitbox5")
    @Expose
    var hitbox5: String? = null

    @Column
    @SerializedName("hitbox6")
    @Expose
    var hitbox6: String? = null

    override fun toString(): String {
        return "Knockbacks(id=$id, ownerId=$ownerId, moveId=$moveId, moveName=$moveName, notes=$notes, rawValue=$rawValue, hitbox1=$hitbox1, hitbox2=$hitbox2, hitbox3=$hitbox3, hitbox4=$hitbox4, hitbox5=$hitbox5, hitbox6=$hitbox6)"
    }
}