package org.mightyfrog.android.s4fd.util

import org.mightyfrog.android.s4fd.data.CharacterDetails
import org.mightyfrog.android.s4fd.data.KHCharacter
import org.mightyfrog.android.s4fd.data.Move
import org.mightyfrog.android.s4fd.data.SmashAttributeType
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Single

/**
 * @author Shigehiro Soejima
 */
interface KHService {
    @GET("/api/Characters")
    fun getCharacters(): Single<List<KHCharacter>>

    @GET("/api/Characters/{id}/details")
    fun getDetails(@Path("id") id: Int): Call<CharacterDetails>

    @GET("/api/moves")
    fun getMoves(): Single<List<Move>>

    @GET("/api/smashattributetypes")
    fun getSmashAttributeTypes(): Single<List<SmashAttributeType>>
}