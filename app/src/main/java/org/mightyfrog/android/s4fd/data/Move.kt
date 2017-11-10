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
class Move : BaseModel() {
    @Column
    @SerializedName("hitboxActive")
    @Expose
    var hitboxActive: String? = null

    @Column
    @SerializedName("firstActionableFrame")
    @Expose
    var firstActionableFrame: String? = null

    @Column
    @SerializedName("baseDamage")
    @Expose
    var baseDamage: String? = null

    @Column
    @SerializedName("angle")
    @Expose
    var angle: String? = null

    @Column
    @SerializedName("baseKnockBackSetKnockback")
    @Expose
    var baseKnockBackSetKnockback: String? = null

    @Column
    @SerializedName("landingLag")
    @Expose
    var landingLag: String? = null

    @Column
    @SerializedName("autoCancel")
    @Expose
    var autoCancel: String? = null

    @Column
    @SerializedName("knockbackGrowth")
    @Expose
    var knockbackGrowth: String? = null

    @Column
    @SerializedName("type")
    @Expose
    var type: Int = 0

    @Column
    @SerializedName("name")
    @Expose
    var name: String = ""

    @Column
    @SerializedName("ownerId")
    @Expose
    var ownerId: Int = 0

    @PrimaryKey
    @Column
    @SerializedName("id")
    @Expose
    var id: Int = 0

    override fun toString(): String {
        return "Move(hitboxActive=$hitboxActive, firstActionableFrame=$firstActionableFrame, baseDamage=$baseDamage, angle=$angle, baseKnockBackSetKnockback=$baseKnockBackSetKnockback, landingLag=$landingLag, autoCancel=$autoCancel, knockbackGrowth=$knockbackGrowth, type=$type, name='$name', ownerId=$ownerId, id=$id)"
    }
}