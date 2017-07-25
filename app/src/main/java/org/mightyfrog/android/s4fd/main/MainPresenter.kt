package org.mightyfrog.android.s4fd.main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.util.Log
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.sql.language.Select
import com.tbruyelle.rxpermissions.RxPermissions
import org.mightyfrog.android.s4fd.R
import org.mightyfrog.android.s4fd.data.*
import org.mightyfrog.android.s4fd.util.Const
import org.mightyfrog.android.s4fd.util.KHService
import rx.Observable
import rx.SingleSubscriber
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.io.File
import java.io.InputStream
import javax.inject.Inject

/**
 * TODO: rewrite data retrieval code, needs major refactoring
 * @author Shigehiro Soejima
 */
class MainPresenter @Inject constructor(val view: MainContract.View, val service: KHService) : MainContract.Presenter {
    private val compositeSubscription: CompositeSubscription = CompositeSubscription()

    init {
        view.setPresenter(this)
    }

    override fun loadCharacters() {
        val characterList = Select().from(KHCharacter::class.java)
                .where()
                .queryList()
        if (characterList.size != Const.CHARACTER_COUNT) {
            compositeSubscription.add(service.getCharacters()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : SingleSubscriber<List<KHCharacter>>() {
                        override fun onSuccess(list: List<KHCharacter>?) {
                            if (list == null) {
                                view.showErrorMessage((view as Context).getString(R.string.no_char_data_found))
                                return
                            }
                            FlowManager.getDatabase(AppDatabase::class.java).executeTransaction {
                                for (char in list) {
                                    char.save()
                                }
                            }
                            loadDetails(list)
                        }

                        override fun onError(t: Throwable?) {
                            view.showFallbackDialog()
                            Log.e(Const.TAG, t.toString())
                        }
                    }))
        } else {
            loadDetails(characterList)
        }
    }

    override fun openCharacter(id: Int, position: Int) {
        view.showDetails(id, position)
    }

    override fun fallback() {
        RxPermissions((view as Activity))
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe {
                    if (it) {
                        copyDatabase()
                    } else {
                        view.finish()
                    }
                }
    }

    override fun destroy() {
        if (!compositeSubscription.isUnsubscribed) {
            compositeSubscription.unsubscribe()
        }
    }

    private fun copyDatabase() {
        (view as Activity).assets.open("smash4data.db").use { input ->
            input.toFile(view.getDatabasePath(AppDatabase.NAME + ".db"))
        }
        view.showDatabaseCopiedDialog()
    }

    fun InputStream.toFile(file: File) {
        use { input ->
            file.outputStream().use { input.copyTo(it) }
        }
    }

    private fun loadSmashAttributeTypes() { // TODO: rewrite me
        val test = SQLite.select()
                .from(SmashAttributeType::class.java)
                .where(Move_Table.id.eq(1))
                .querySingle()

        test?.let {
            return
        }

        view.showProgressDialog((view as Activity).getString(R.string.loading_attr_types))
        compositeSubscription.add(service.getSmashAttributeTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleSubscriber<List<SmashAttributeType>>() {
                    override fun onSuccess(attrTypes: List<SmashAttributeType>?) {
                        attrTypes?.let {
                            for (attrType in it) {
                                attrType.save()
                            }
                        }
                        loadMoves()
                    }

                    override fun onError(error: Throwable?) {
                        view.hideActivityCircle()
                    }
                })
        )
    }

    private fun loadMoves() { // TODO: rewrite me
        val test = SQLite.select()
                .from(Move::class.java)
                .where(Move_Table.id.eq(1))
                .querySingle()

        test?.let {
            return
        }

        view.showProgressDialog((view as Activity).getString(R.string.loading_moves))
        compositeSubscription.add(service.getMoves()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleSubscriber<List<Move>>() {
                    override fun onError(error: Throwable?) {
                        view.hideProgressDialog()
                    }

                    override fun onSuccess(moves: List<Move>?) {
                        view.hideProgressDialog()
                        moves?.apply {
                            FlowManager.getDatabase(AppDatabase::class.java).executeTransaction {
                                for (move in this) {
                                    move.save()
                                }
                            }
                        }
                    }
                }))
    }

    private fun loadDetails(list: List<KHCharacter>?) { // TODO: rewrite me
        compositeSubscription.add(Observable.from(list)
                .filter {
                    val metadata = SQLite.select()
                            .from(Metadata::class.java)
                            .where(Metadata_Table.id.eq(it.id))
                            .querySingle()
                    metadata == null
                }
                .concatMap({ character ->
                    val res = service.getDetails(character.id).execute()
                    if (res.isSuccessful && res.code() == 200) {
                        val details = res.body()
                        details?.apply {
                            FlowManager.getDatabase(AppDatabase::class.java).executeTransaction {
                                metadata?.save()
                                movementData?.apply {
                                    for (move in this) {
                                        move.save()
                                    }
                                }
                                characterAttributeData?.apply {
                                    for (attr in this) {
                                        attr.save()
                                    }
                                }
                            }
                        }
                        Observable.just(details)
                    } else {
                        Observable.error(RuntimeException("Unable to retrieve data"))
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<CharacterDetails>() {
                    override fun onCompleted() {
                        view.showCharacters(SQLite.select()
                                .from(KHCharacter::class.java)
                                .queryList())
                        loadSmashAttributeTypes()
                    }

                    override fun onError(e: Throwable?) {
                        view.showErrorMessage(e.toString())
                        view.hideProgressDialog()
                    }

                    override fun onNext(t: CharacterDetails?) {
                        t?.apply {
                            view.showProgressDialog((view as Activity).getString(R.string.loading_chars, metadata?.displayName))
                        }
                    }
                })
        )
    }
}