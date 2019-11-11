package com.aau.rpg.presentation.management

import androidx.lifecycle.MutableLiveData
import com.aau.rpg.data.grid.Grid
import com.aau.rpg.data.grid.GridInfo
import com.aau.rpg.data.grid.GridRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class StoringManagementViewModel(
    private val gridRepository: GridRepository
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

    override fun loadGrids() {
        loadGridsInternal(initialLoading = true)
    }

    override fun loadGrid(name: String) {
        disposables += gridRepository
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

        disposables += gridRepository
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

        disposables += gridRepository
            .loadGrid(oldName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { grid ->
                gridRepository
                    .deleteGrid(oldName)
                    .flatMap {
                        gridRepository.saveGrid(
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

        disposables += gridRepository
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

        disposables += gridRepository
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

        disposables += gridRepository
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
