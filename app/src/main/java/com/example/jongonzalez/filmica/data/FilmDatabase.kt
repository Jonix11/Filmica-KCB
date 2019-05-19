package com.example.jongonzalez.filmica.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [Film::class], version = 1)
abstract class FilmDatabase: RoomDatabase() {
    abstract fun filmDao(): FilmDao
}