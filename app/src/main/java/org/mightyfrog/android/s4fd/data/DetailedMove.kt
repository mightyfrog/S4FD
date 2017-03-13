package org.mightyfrog.android.s4fd.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.ForeignKey
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel


/**
 * @author Shigehiro Soejima
 */
@Table(database = AppDatabase::class)
class DetailedMove : BaseModel() {
    @PrimaryKey
    @Column
    var _id: Int = 0

    @Column
    @SerializedName("moveId")
    @Expose
    var moveId: Int = 0

    @Column
    @SerializedName("moveName")
    @Expose
    var moveName: String? = null

    @ForeignKey(saveForeignKeyModel = false)
    @Column
    @SerializedName("angle")
    @Expose
    var angle: Angle? = null

    @ForeignKey(saveForeignKeyModel = false)
    @Column
    @SerializedName("hitbox")
    @Expose
    var hitbox: Hitbox? = null

    @ForeignKey(saveForeignKeyModel = false)
    @Column
    @SerializedName("baseDamage")
    @Expose
    var baseDamage: BaseDamage? = null

    @ForeignKey(saveForeignKeyModel = false)
    @Column
    @SerializedName("baseKnockback")
    @Expose
    var baseKnockback: BaseKnockback? = null

    @ForeignKey(saveForeignKeyModel = false)
    @Column
    @SerializedName("setKnockback")
    @Expose
    var knockback: Knockbacks? = null

    @Column
    @SerializedName("firstActionableFrame")
    @Expose
    var firstActionableFrame: String? = null

    @ForeignKey(saveForeignKeyModel = false)
    @Column
    @SerializedName("autocancel")
    @Expose
    var autocancel: Autocancel? = null

    @ForeignKey(saveForeignKeyModel = false)
    @Column
    @SerializedName("landingLag")
    @Expose
    var landingLag: LandingLag? = null
}