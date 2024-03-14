package com.example.planmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TaskItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskItemDao() : TaskItemDao

    companion object { // make sure only one database instance ever
        @Volatile private var instance: AppDatabase? = null // if one thread change
        // a volatile var, it will make the change visible immediately to all
        // the threads
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "task-item-db"
            ).build()

        fun getInstance(context: Context) : AppDatabase {
            return instance ?: synchronized(this) {
                buildDatabase(context).also {
                    instance = it
                }
            }
        }

    }
}