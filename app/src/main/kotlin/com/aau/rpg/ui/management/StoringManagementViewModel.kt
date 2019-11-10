package com.aau.rpg.ui.management

import androidx.lifecycle.MutableLiveData
import com.aau.rpg.core.grid.Grid
import com.aau.rpg.core.grid.GridInfo
import com.aau.rpg.core.grid.GridStorageService
import com.aau.rpg.core.grid.gridOf
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class StoringManagementViewModel(
    private val gridStorage: GridStorageService,
    private val gridSize: Int
) : ManagementViewModel() {

    override val gridNameValid = MutableLiveData<Boolean>(false)

    override val updated = MutableLiveData<UpdatedGridInfo>()

    override val loading = MutableLiveData<Boolean>()

    override val loaded = MutableLiveData<Grid>()

    override val infos = MutableLiveData<List<GridInfo>>()

    override val error = MutableLiveData<Throwable>()

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.clear()
    }

    override fun createGrid(name: String) {
        val grid = gridOf(
            name = name,
            size = gridSize
        )

        disposables += gridStorage
            .saveGrid(grid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { loadGrids() },
                ::onError
            )
    }

    override fun loadGrids() {
        loadGridsInternal(initialLoading = true)
    }

    override fun loadGrid(name: String) {
        disposables += gridStorage
            .loadGrid(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { grid -> loaded.value = grid },
                ::onError
            )
    }

    override fun isGridNameValid(oldName: String?, newName: String) {
        if (oldName != null && newName == oldName) {
            gridNameValid.value = true
            return
        }

        if (newName.isBlank()) {
            gridNameValid.value = false
            return
        }

        disposables += gridStorage
            .loadGridInfos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { infos ->
                    gridNameValid.value = infos.none { info ->
                        when {
                            info.name == newName -> true
                            info.name == oldName -> false
                            else -> false
                        }
                    }
                },
                ::onError
            )
    }

    override fun updateGridName(oldName: String, newName: String) {
        loading.value = true

        disposables += gridStorage
            .loadGrid(oldName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { grid ->
                gridStorage
                    .deleteGrid(oldName)
                    .flatMap {
                        gridStorage.saveGrid(
                            grid.copy(
                                name = newName
                            )
                        )
                    }
            }
            .doFinally {
                loading.value = false
            }
            .subscribe(
                {
                    updated.value = UpdatedGridInfo(
                        oldName = oldName,
                        newName = newName
                    )

                    loadGridsInternal()
                },
                ::onError
            )
    }

    override fun saveGrid(grid: Grid) {
        loading.value = true

        disposables += gridStorage
            .saveGrid(grid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                loading.value = false
            }
            .subscribe(
                { loadGridsInternal() },
                ::onError
            )
    }

    override fun deleteGrid(name: String) {
        loading.value = true

        disposables += gridStorage
            .deleteGrid(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                loading.value = false
            }
            .subscribe(
                { loadGridsInternal() },
                ::onError
            )
    }

    private fun loadGridsInternal(initialLoading: Boolean? = null) {
        if (initialLoading != null) {
            loading.value = initialLoading
        }

        disposables += gridStorage
            .loadGridInfos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                loading.value = false
            }
            .subscribe(
                { infos -> this.infos.value = infos },
                ::onError
            )
    }

    private fun onError(error: Throwable) {
        this.error.value = error
    }
}
