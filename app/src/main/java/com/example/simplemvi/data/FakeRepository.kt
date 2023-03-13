package com.example.simplemvi.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

interface FakeRepository {

    suspend fun getMovies(): Result<List<Movie>>

    suspend fun setFavorite(id: Int, favorite: Boolean)
}

class FakeRepositoryImpl(private val io: CoroutineDispatcher) : FakeRepository {

    override suspend fun getMovies(): Result<List<Movie>> {

        return withContext(io) {
            delay(2000)
            Result.success(
                listOf(
                    Movie(
                        id = 1,
                        name = "The Shawshank Redemption",
                        rating = 9.2,
                        year = 1994,
                        favorite = false
                    ),
                    Movie(
                        id = 2,
                        name = "The Godfather",
                        rating = 9.2,
                        year = 1972,
                        favorite = false
                    ),
                    Movie(
                        id = 3,
                        name = "The Dark Knight",
                        rating = 9.0,
                        year = 2000,
                        favorite = true
                    ),
                    Movie(
                        id = 4,
                        name = "The Godfather Part II ",
                        rating = 9.0,
                        year = 1974,
                        favorite = false
                    ),
                    Movie(id = 5, name = "Angry Men", rating = 9.0, year = 1957, favorite = true)
                )
            )
        }
    }

    override suspend fun setFavorite(id: Int, favorite: Boolean) {
        // set/unset movie favorite
    }

}