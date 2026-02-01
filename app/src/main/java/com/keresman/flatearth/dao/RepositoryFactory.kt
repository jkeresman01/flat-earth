package com.keresman.flatearth.dao

import android.content.Context
import com.keresman.flatearth.dao.sql.SQLiteRepository

object RepositoryFactory {

    private lateinit var repository: Repository

    fun getInstance(context: Context?): Repository {
        if (!::repository.isInitialized) {
            repository = SQLiteRepository(context)
        }
        return repository
    }
}