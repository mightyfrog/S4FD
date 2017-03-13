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
class KHCharacter : BaseModel() {
    @Column
    @SerializedName("fullUrl")
    @Expose
    var fullUrl: String? = null

    @Column
    @SerializedName("style")
    @Expose
    var style: String? = null

    @Column
    @SerializedName("mainImageUrl")
    @Expose
    var mainImageUrl: String? = null

    @Column
    @SerializedName("thumbnailUrl")
    @Expose
    var thumbnailUrl: String? = null

    @Column
    @SerializedName("description")
    @Expose
    var description: String? = null

    @Column
    @SerializedName("colorTheme")
    @Expose
    var colorTheme: String? = null

    @Column
    @SerializedName("name")
    @Expose
    var name: String? = null

    @Column
    @SerializedName("displayName")
    @Expose
    var displayName: String? = null

    @PrimaryKey
    @Column
    @SerializedName("id")
    @Expose
    var id: Int = 0

    override fun toString(): String {
        return "KHCharacter(fullUrl=$fullUrl, style=$style, mainImageUrl=$mainImageUrl, thumbnailUrl=$thumbnailUrl, description=$description, colorTheme=$colorTheme, name=$name, displayName=$displayName, id=$id)"
    }
}