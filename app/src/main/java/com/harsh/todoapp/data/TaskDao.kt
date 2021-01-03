package com.harsh.todoapp.data

import androidx.room.*
import com.harsh.todoapp.ui.tasks.SortOrder
import kotlinx.coroutines.flow.Flow


@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM task_table WHERE (completed_state != :hideCompleted OR completed_state = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC,name")
    fun getTasksSortedByName(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (completed_state != :hideCompleted OR completed_state = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC,created")
    fun getTasksSortedByDateCreated(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    fun getTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>> {
        return when (sortOrder) {
            SortOrder.BY_DATE -> getTasksSortedByDateCreated(query, hideCompleted)
            SortOrder.BY_NAME -> getTasksSortedByName(query, hideCompleted)
        }
    }

    @Query("DELETE FROM task_table WHERE completed_state = 1")
    suspend fun deleteCompletedTasks()

}