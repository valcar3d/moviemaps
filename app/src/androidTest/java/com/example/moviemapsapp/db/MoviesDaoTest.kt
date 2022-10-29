package com.example.moviemapsapp.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.moviemapsapp.utilities.TestingMocks
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class MoviesDaoTest {
    private lateinit var database: MoviesDatabase
    private lateinit var dao: MovieDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MoviesDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.dao

    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertMovieItem() = runBlockingTest {
        val movieItem = TestingMocks.getMockMoviesList()
        dao.insertMovieItem(movieItem)

        val allMovieItems = dao.getMovieItems()

        assertThat(allMovieItems).contains(movieItem)
    }
}