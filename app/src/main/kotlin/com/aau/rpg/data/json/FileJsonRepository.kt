package com.aau.rpg.data.json

import android.content.Context
import android.content.Context.MODE_PRIVATE
import io.reactivex.Observable
import org.json.JSONObject
import java.io.FileInputStream
import java.io.FileNotFoundException

class FileJsonRepository(
    private val fileName: String,
    private val context: Context
) : JsonRepository {

    override fun load(): Observable<JSONObject> =
        Observable
            .fromCallable {
                context
                    .openFileInput(fileName)
                    .use(FileInputStream::readBytes)
                    .let { bytes -> String(bytes) }
                    .let(::JSONObject)
            }
            .onErrorResumeNext { error: Throwable ->
                if (error is FileNotFoundException) {
                    Observable.just(JSONObject())
                } else {
                    Observable.error(error)
                }
            }

    override fun save(json: JSONObject): Observable<Unit> =
        Observable.fromCallable {
            val bytes = json
                .toString()
                .toByteArray()

            context
                .openFileOutput(fileName, MODE_PRIVATE)
                .use { out -> out.write(bytes) }
        }
}
