package com.aditya.appstoryaditya.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aditya.appstoryaditya.data.Story


@Database(
    entities = [Story::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao() : StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}