package org.mightyfrog.android.s4fd.compare

import android.content.SharedPreferences
import com.raizlabs.android.dbflow.sql.language.OperatorGroup
import com.raizlabs.android.dbflow.sql.language.Select
import org.mightyfrog.android.s4fd.data.Move
import org.mightyfrog.android.s4fd.data.Move_Table
import javax.inject.Inject

/**
 * @author Shigehiro Soejima
 */
class ComparePresenter @Inject constructor(val view: CompareContract.View, private val prefs: SharedPreferences) : CompareContract.Presenter {

    init {
        view.setPresenter(this)
    }

    override fun loadMoves(name: String, charId: Int, charToCompareId: Int) {
        val list = if (charToCompareId != 0) {
            (Select().from(Move::class.java)
                    .where()
                    .and(OperatorGroup.clause().or(Move_Table.name.like("$name%")).or(Move_Table.name.like("% $name%")))
                    .and(OperatorGroup.clause().or(Move_Table.ownerId.eq(charId)).or(Move_Table.ownerId.eq(charToCompareId)))
                    .orderBy(Move_Table.id, true)
                    .queryList())
        } else {
            (Select().from(Move::class.java)
                    .where()
                    .and(OperatorGroup.clause().or(Move_Table.name.like("$name%")).or(Move_Table.name.like("% $name%")))
                    .orderBy(Move_Table.id, true)
                    .queryList())
        }
        val count = list
                .takeWhile { it.ownerId != charId }
                .count()
        view.showMoves(list, count)
        if (prefs.getInt("compare_sort_type", DataAdapter.SORT_BY_CHAR) != DataAdapter.SORT_BY_CHAR) {
            sortBy(DataAdapter.SORT_BY_MOVE)
        }
    }

    override fun sortBy(type: Int) {
        prefs.edit().putInt("compare_sort_type", type).apply()
        view.showSortedMoves(type)
    }
}