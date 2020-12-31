package com.harsh.todoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.harsh.todoapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(version = 1, entities = [Task::class])
abstract class TaskDatabase : RoomDatabase() {

    //use this function to get taskdao to perform databse operations
    abstract fun taskDao(): TaskDao


    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>, //to avoid circular dependency
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task("Learn service component"))
                dao.insert(Task("Go get the milk"))
                dao.insert(Task("Learn Kotlin Flow", important = true))
                dao.insert(Task("Learn Espresso Testing", completed_state = true))
                dao.insert(Task("Learn rxjava"))
                dao.insert(Task("Learn rxandroid"))
                dao.insert(Task("Listen music", completed_state = true))
                dao.insert(Task("Go get some sugar from the local shop", important = true))
            }

        }
    }

}