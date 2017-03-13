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
class Autocancel : BaseModel() {
    @Column
    @SerializedName("cancel1")
    @Expose
    var cancel1: String? = null

    @Column
    @SerializedName("cancel2")
    @Expose
    var cancel2: String? = null

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
    @SerializedName("lastModified")
    @Expose
    var lastModified: String? = null
}