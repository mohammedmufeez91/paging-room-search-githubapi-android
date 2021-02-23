/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.mufeez.paging

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.android.mufeez.paging.api.GithubService
import com.example.android.mufeez.paging.data.GithubRepository
import com.example.android.mufeez.paging.db.GithubLocalCache
import com.example.android.mufeez.paging.db.RepoDatabase
import com.example.android.mufeez.paging.ui.ViewModelFactory
import java.util.concurrent.Executors

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [GithubLocalCache] based on the database DAO.
     */
    private fun provideCache(context: Context): GithubLocalCache {
        val database = RepoDatabase.getInstance(context) // create Room db
        return GithubLocalCache(database.reposDao(),// Dao implementation
                Executors.newSingleThreadExecutor()// single thread sequential executor
        )
    }

    /**
     * Creates an instance of [GithubRepository] based on the [GithubService] and a
     * [GithubLocalCache]
     */
    private fun provideGithubRepository(context: Context): GithubRepository {
        return GithubRepository(GithubService.create(),//create retrofit service call for github with implementation of endpoints
                provideCache(context)// set cache
        )
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     *
     * has instance of the GithubRepository in constructor and context
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideGithubRepository(context))
    }
}
