package com.example.moviemapsapp.utilities

import com.example.moviemapsapp.db.entities.MovieResultItemEntity

class TestingMocks {
    companion object{
        fun getMockMoviesList():MutableList<MovieResultItemEntity>{
            return mutableListOf(
                MovieResultItemEntity(
                    1, "some overview",
                    "En",
                    "The Matrix",
                    false,
                    "Enter the Matrix",
                    "none",
                    "none",
                    "22-02-1999",
                    9.0,
                    8.5,
                    false,
                    999
                ),
                MovieResultItemEntity(
                    1, "some overview",
                    "En",
                    "Shrek",
                    false,
                    "Shrek",
                    "none",
                    "none",
                    "2-02-2001",
                    7.0,
                    8.5,
                    false,
                    999
                ),
                MovieResultItemEntity(
                    1, "some overview",
                    "En",
                    "Fight Club",
                    false,
                    "Club de la pelea",
                    "none",
                    "none",
                    "1-02-2013",
                    9.0,
                    5.5,
                    false,
                    223
                ),
                MovieResultItemEntity(
                    1, "some overview",
                    "En",
                    "Lord of the rings",
                    false,
                    "senor de los anillos",
                    "none",
                    "none",
                    "18-05-2012",
                    3.0,
                    6.5,
                    false,
                    999
                ), MovieResultItemEntity(
                    1, "some overview",
                    "En",
                    "Hoonigans",
                    false,
                    "Hoonigans",
                    "none",
                    "none",
                    "15-02-2010",
                    9.0,
                    8.5,
                    false,
                    999
                )
            )
        }
    }
}