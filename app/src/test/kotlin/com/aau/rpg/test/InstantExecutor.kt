package com.aau.rpg.test

import androidx.arch.core.executor.TaskExecutor

object InstantExecutor : TaskExecutor() {

    override fun executeOnDiskIO(runnable: Runnable) = runnable.run()

    override fun postToMainThread(runnable: Runnable) = runnable.run()

    override fun isMainThread(): Boolean = true
}
