package com.aau.rpg.presentation.management

import com.aau.rpg.data.grid.Grid
import com.aau.rpg.data.grid.GridInfo
import com.aau.rpg.data.grid.GridRepository
import com.aau.rpg.test.InstantExecutorExtension
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
class StoringManagementViewModelTest {

    private val gridRepository = mockk<GridRepository>()
    private val view = StoringManagementViewModel(gridRepository)

    @Test
    fun `should load grids`() {
        val info = GridInfo(size = 1, name = "any")

        every {
            gridRepository.loadGridInfos()
        } answers {
            Observable.just(
                listOf(info)
            )
        }

        view.loadGrids()

        assertThat(view.loading.value).isFalse()
        assertThat(view.infos.value).containsOnly(info)
    }

    @Test
    fun `should load grid`() {
        val grid = createGrid()

        every {
            gridRepository.loadGrid(grid.name)
        } answers {
            Observable.just(grid)
        }

        view.loadGrid(grid.name)

        assertThat(view.loaded.value).isEqualTo(grid)
    }

    @Test
    fun `should validate same grid name`() {
        val name = "any"
        view.isGridNameValid(name, name)
        assertThat(view.gridNameValid.value).isTrue()
    }

    @Test
    fun `should validate blank grid name`() {
        val name = " "
        view.isGridNameValid(newName = name)
        assertThat(view.gridNameValid.value).isFalse()
    }

    @Test
    fun `should validate existing grid name`() {
        val info = GridInfo(size = 1, name = "any")

        every {
            gridRepository.loadGridInfos()
        } answers {
            Observable.just(
                listOf(info)
            )
        }

        view.isGridNameValid(newName = info.name)

        assertThat(view.gridNameValid.value).isFalse()
    }

    @Test
    fun `should update grid name`() {
        val grid = createGrid()
        val updatedGrid = grid.copy(name = "new name")

        every {
            gridRepository.loadGrid(grid.name)
        } answers {
            Observable.just(grid)
        }

        every {
            gridRepository.deleteGrid(grid.name)
        } answers {
            Observable.just(Unit)
        }

        every {
            gridRepository.saveGrid(updatedGrid)
        } answers {
            Observable.just(Unit)
        }

        view.updateGridName(grid.name, updatedGrid.name)

        assertThat(view.loading.value).isFalse()
        assertThat(view.updated.value).isEqualTo(
            UpdatedGridInfo(
                oldName = grid.name,
                newName = updatedGrid.name
            )
        )
    }

    @Test
    fun `should save grid`() {
        val grid = createGrid()

        every {
            gridRepository.saveGrid(grid)
        } answers {
            Observable.just(Unit)
        }

        view.saveGrid(grid)

        assertThat(view.loading.value).isFalse()
    }

    @Test
    fun `should delete grid`() {
        val name = "any"

        every {
            gridRepository.deleteGrid(name)
        } answers {
            Observable.just(Unit)
        }

        view.deleteGrid(name)

        assertThat(view.loading.value).isFalse()
    }
}

private fun createGrid() = Grid(
    name = "old name",
    size = 1,
    tiles = emptyList()
)
