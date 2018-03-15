package org.mightyfrog.android.s4fd.util

import org.mightyfrog.android.s4fd.data.KHCharacter

/**
 * @author Shigehiro Soejima
 */
object KHCharacterCreator {
    private val list: MutableList<KHCharacter> = mutableListOf()

    init {
        for (i in 1..58) {
            val char = KHCharacter()
            char.fullUrl = i.toString()
            char.style = i.toString()
            char.mainImageUrl = i.toString()
            char.thumbnailUrl = i.toString()
            char.description = i.toString()
            char.colorTheme = i.toString()
            char.name = i.toString()
            char.displayName = i.toString()
            char.id = i
            list.add(char)
        }
    }

    fun getKHCharacterList(): List<KHCharacter> {
        return list
    }
}