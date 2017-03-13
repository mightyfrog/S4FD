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
class CharacterAttributeDatum : BaseModel() {
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
    @SerializedName("rank")
    @Expose
    var rank: String? = null

    @Column
    @SerializedName("value")
    @Expose
    var value: String? = null

    @Column
    @SerializedName("name")
    @Expose
    var name: String? = null

    @Column
    @SerializedName("smashAttributeTypeId")
    @Expose
    var smashAttributeTypeId: Int = 0

    @Column
    @SerializedName("characterAttributeTypeId")
    @Expose
    var characterAttributeTypeId: Int = 0

    override fun toString(): String {
        return "CharacterAttributeDatum(id=$id, ownerId=$ownerId, rank=$rank, value=$value, name=$name, smashAttributeTypeId=$smashAttributeTypeId, characterAttributeTypeId=$characterAttributeTypeId)"
    }
}