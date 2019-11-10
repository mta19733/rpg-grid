package com.aau.rpg.ui.grid

import com.aau.rpg.core.grid.gridOf
import com.aau.rpg.test.InstantExecutorExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
class GameGridViewModelTest {

    private val view = GameGridViewModel(
        idDelimiter = ",",
        viewSize = 1
    )

    private val grid = gridOf(
        tiles = listOf(
            listOf(
                Tile(0, true), Tile(1, false)
            ),
            listOf(
                Tile(2, false), Tile(3, true)
            )
        ),
        size = SIZE,
        name = NAME
    )

    @BeforeEach
    fun setUp() {
        view.loadGrid(grid)
    }

    @Test
    fun `should move right`() {
        view.move(Direction.RIGHT)

        assertThat(view.viewGrid.value).isEqualTo(
            Tile(1, false).asGrid()
        )
    }

    @Test
    fun `should move right and left`() {
        view.move(Direction.RIGHT)
        view.move(Direction.LEFT)

        assertThat(view.viewGrid.value).isEqualTo(
            Tile(0, true).asGrid()
        )
    }

    @Test
    fun `should move down`() {
        view.move(Direction.DOWN)

        assertThat(view.viewGrid.value).isEqualTo(
            Tile(2, false).asGrid()
        )
    }

    @Test
    fun `should move down and up`() {
        view.move(Direction.DOWN)
        view.move(Direction.UP)

        assertThat(view.viewGrid.value).isEqualTo(
            Tile(0, true).asGrid()
        )
    }


    @Test
    fun `should move right and down`() {
        view.move(Direction.RIGHT)
        view.move(Direction.DOWN)

        assertThat(view.viewGrid.value).isEqualTo(
            Tile(3, true).asGrid()
        )
    }

    @Test
    fun `should set bottom right tile`() {
        view.set(3, false)

        view.move(Direction.RIGHT)
        view.move(Direction.DOWN)

        val tile = Tile(3, false)
        assertThat(view.viewGrid.value).isEqualTo(
            tile.asGrid()
        )
    }

    @Test
    fun `should load view ids`() {
        val view = GameGridViewModel(
            idDelimiter = ",",
            viewSize = SIZE
        )

        view.loadGrid(grid)
        view.loadViewIds()

        assertThat(view.viewIds.value).isEqualTo("0,3")
    }

    @Test
    fun `should move bottom right and load view ids`() {
        view.move(Direction.RIGHT)
        view.move(Direction.DOWN)

        view.loadViewIds()

        assertThat(view.viewIds.value).isEqualTo("0")
    }

    private fun Tile.asGrid() = gridOf(
        tiles = listOf(listOf(this)),
        size = 1,
        name = NAME
    )
}

private const val SIZE = 2
private const val NAME = "test id"