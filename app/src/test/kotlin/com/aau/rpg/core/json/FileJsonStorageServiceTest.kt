package com.aau.rpg.core.json

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.json.JSONObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException
import java.lang.Exception

class FileJsonStorageServiceTest {

    private val storageFile = createTempFile().apply {
        deleteOnExit()
    }

    private val context = mockk<Context>()
    private val service = FileJsonStorageService(
        fileName = storageFile.name,
        context = context
    )

    @AfterEach
    fun tearDown() {
        storageFile.delete()
    }

    @Test
    fun `should load json`() {
        val json = """
            {
                "message": "hello"
            }
        """.trimIndent()

        every {
            context.openFileInput(storageFile.name)
        } answers {
            storageFile.writeText(json)
            storageFile.inputStream()
        }

        val res = service
            .load()
            .blockingFirst()

        assertThat(res.toString())
            .isEqualTo(JSONObject(json).toString())
    }

    @Test
    fun `should load empty json due to missing file`() {
        every {
            context.openFileInput(storageFile.name)
        } throws FileNotFoundException()

        val res = service
            .load()
            .blockingFirst()

        assertThat(res.length()).isZero()
    }

    @Test
    fun `should not load json due to error`() {
        val message = "very bad"
        every {
            context.openFileInput(storageFile.name)
        } throws Exception(message)

        assertThatThrownBy { service.load().blockingFirst() }
            .hasMessageContaining(message)
    }

    @Test
    fun `should save json`() {
        every {
            context.openFileOutput(storageFile.name, Context.MODE_PRIVATE)
        } returns storageFile.outputStream()

        val json = """
            {
                "message": "hello"
            }
        """.trimIndent()

        service
            .save(JSONObject(json))
            .blockingFirst()

        assertThat(storageFile.readText())
            .isEqualTo(JSONObject(json).toString())
    }
}
