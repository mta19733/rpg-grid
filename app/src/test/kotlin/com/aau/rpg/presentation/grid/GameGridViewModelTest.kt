package com.aau.rpg.presentation.grid

import com.aau.rpg.data.grid.Grid
import com.aau.rpg.data.grid.Tile
import com.aau.rpg.test.InstantExecutorExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ListAssert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.function.Function

@ExtendWith(InstantExecutorExtension::class)
class GameGridViewModelTest {

    private val view = GameGridViewModel(
        idDelimiter = ",",
        viewSize = VIEW_SIZE,
        gridSize = GRID_SIZE
    )

    private val grid = Grid(
        tiles = listOf(
            listOf(
                Tile(0, true), Tile(1, false), Tile(2, false)
            ),
            listOf(
                Tile(3, false), Tile(4, true), Tile(5, false)
            ),
            listOf(
                Tile(6, false), Tile(7, false), Tile(8, true)
            )
        ),
        size = GRID_SIZE,
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
            Tile(5, false).asGrid()
        )
    }

    @Test
    fun `should move right and left`() {
        view.move(Direction.RIGHT)
        view.move(Direction.LEFT)

        assertThat(view.viewGrid.value).isEqualTo(
            Tile(4, true).asGrid()
        )
    }

    @Test
    fun `should move down`() {
        view.move(Direction.DOWN)

        assertThat(view.viewGrid.value).isEqualTo(
            Tile(7, false).asGrid()
        )
    }

    @Test
    fun `should move down and up`() {
        view.move(Direction.DOWN)
        view.move(Direction.UP)

        assertThat(view.viewGrid.value).isEqualTo(
            Tile(4, true).asGrid()
        )
    }


    @Test
    fun `should move right and down`() {
        view.move(Direction.RIGHT)
        view.move(Direction.DOWN)

        assertThat(view.viewGrid.value).isEqualTo(
            Tile(8, true).asGrid()
        )
    }

    @Test
    fun `should set bottom right tile`() {
        view.set(8, false)

        view.move(Direction.RIGHT)
        view.move(Direction.DOWN)

        val tile = Tile(8, false)
        assertThat(view.viewGrid.value).isEqualTo(
            tile.asGrid()
        )
    }

    @Test
    fun `should load view ids`() {
        val view = GameGridViewModel(
            idDelimiter = ",",
            viewSize = GRID_SIZE,
            gridSize = GRID_SIZE
        )

        view.loadGrid(grid)
        view.loadViewIds()

        assertThat(view.viewIds.value).isEqualTo("0,4,8")
    }

    @Test
    fun `should move bottom right and load view ids`() {
        view.move(Direction.RIGHT)
        view.move(Direction.DOWN)

        view.loadViewIds()

        assertThat(view.viewIds.value).isEqualTo("0")
    }

    @Test
    fun `should disable up`() {
        val values = observeDirectionStates()
        view.move(Direction.UP)
        assertThat(values).isEqualTo(up = false)
    }

    @Test
    fun `should disable up left`() {
        val values = observeDirectionStates()
        view.move(Direction.UP)
        view.move(Direction.LEFT)

        assertThat(values).isEqualTo(up = false, left = false)
    }

    @Test
    fun `should disable up right`() {
        val values = observeDirectionStates()
        view.move(Direction.UP)
        view.move(Direction.RIGHT)

        assertThat(values).isEqualTo(up = false, right = false)
    }

    @Test
    fun `should disable down`() {
        val values = observeDirectionStates()
        view.move(Direction.DOWN)
        assertThat(values).isEqualTo(down = false)
    }

    @Test
    fun `should disable down left`() {
        val values = observeDirectionStates()
        view.move(Direction.DOWN)
        view.move(Direction.LEFT)

        assertThat(values).isEqualTo(down = false, left = false)
    }

    @Test
    fun `should disable down right`() {
        val values = observeDirectionStates()
        view.move(Direction.DOWN)
        view.move(Direction.RIGHT)

        assertThat(values).isEqualTo(down = false, right = false)
    }

    @Test
    fun `should disable left`() {
        val values = observeDirectionStates()
        view.move(Direction.LEFT)
        assertThat(values).isEqualTo(left = false)
    }

    @Test
    fun `should disable right`() {
        val values = observeDirectionStates()
        view.move(Direction.RIGHT)
        assertThat(values).isEqualTo(right = false)
    }

    @Test
    fun `should create grid`() {
        val grid = view.createGrid(NAME)
        val tiles = grid.tiles.flatten()

        assertThat(tiles)
            .extracting(Function<Tile, Boolean>(Tile::value))
            .containsOnly(false)

        assertThat(tiles)
            .extracting(Function<Tile, Int>(Tile::id))
            .containsExactlyElementsOf(
                (0 until (GRID_SIZE * GRID_SIZE))
            )

        assertThat(grid.size).isEqualTo(GRID_SIZE)
        assertThat(grid.name).isEqualTo(NAME)
    }

    private fun observeDirectionStates(): List<DirectionState> {
        val states = mutableListOf<DirectionState>()
        view.directionState.observeForever { state ->
            states += state
        }

        return states
    }
}

private const val VIEW_SIZE = 1
private const val GRID_SIZE = 3

private const val NAME = "test name"

private fun Tile.asGrid() = Grid(
    tiles = listOf(listOf(this)),
    size = 1,
    name = NAME
)

private fun ListAssert<DirectionState>.isEqualTo(
    up: Boolean = true,
    down: Boolean = true,
    left: Boolean = true,
    right: Boolean = true
) = satisfies { states ->
    val directions = states
        .takeLast(Direction.values().size)
        .associateBy(DirectionState::direction)
        .mapValues { entry -> entry.value.enabled }

    assertThat(directions[Direction.UP]).isEqualTo(up)
    assertThat(directions[Direction.DOWN]).isEqualTo(down)
    assertThat(directions[Direction.LEFT]).isEqualTo(left)
    assertThat(directions[Direction.RIGHT]).isEqualTo(right)
}
