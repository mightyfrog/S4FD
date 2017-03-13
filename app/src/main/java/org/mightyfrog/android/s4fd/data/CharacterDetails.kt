package org.mightyfrog.android.s4fd.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Shigehiro Soejima
 */
class CharacterDetails { // TODO: dbflow list<T> converter?
    @SerializedName("metadata")
    @Expose
    var metadata: Metadata? = null

    @SerializedName("movementData")
    @Expose
    var movementData: List<MovementDatum>? = null

    @SerializedName("characterAttributeData")
    @Expose
    var characterAttributeData: List<CharacterAttributeDatum>? = null

    override fun toString(): String {
        return "CharacterDetails(metadata=$metadata, movementData=$movementData, characterAttributeData=$characterAttributeData)"
    }
}