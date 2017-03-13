package org.mightyfrog.android.s4fd.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

/**
 * @author Shigehiro Soejima
 */
@Table(database = AppDatabase::class)
class CharacterAttributeType {
    @PrimaryKey
    @Column
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @Column
    @SerializedName("name")
    @Expose
    var name: String? = null

    @Column
    @SerializedName("notationId")
    @Expose
    var notationId: Int? = null
}