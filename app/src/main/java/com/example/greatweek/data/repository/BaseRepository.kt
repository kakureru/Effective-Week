package com.example.greatweek.data.repository

import com.example.greatweek.data.SyncManager
import com.example.greatweek.domain.utils.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseRepository(private val syncManager: SyncManager? = null) {
    protected fun <T> doRequest(request: suspend () -> T) = flow<Either<String, T>> {
        emit(Either.Right(value = request()))
    }.flowOn(Dispatchers.IO).catch { exception ->
        emit(Either.Left(value = exception.localizedMessage ?: "Error Occurred!"))
    }

    protected suspend fun <T> doEntry(entry: suspend () -> T) {
        entry()
        syncManager?.addDataVersion()
    }
}