package com.salmakhd.android.forpractice.paging.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [BeerEntity::class],
    version = 1
)
abstract class BeerDatabase: RoomDatabase() {
    abstract val dao: BeerDao

}