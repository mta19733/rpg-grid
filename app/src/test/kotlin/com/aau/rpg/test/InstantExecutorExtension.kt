package com.aau.rpg.test

import androidx.arch.core.executor.ArchTaskExecutor
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class InstantExecutorExtension : BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler {
            Schedulers.trampoline()
        }

        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }

        ArchTaskExecutor
            .getInstance()
            .setDelegate(InstantExecutor)
    }

    override fun afterEach(context: ExtensionContext?) {
        ArchTaskExecutor
            .getInstance()
            .setDelegate(null)
    }
}
