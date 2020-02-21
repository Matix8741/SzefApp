package com.example.szefapp.persistence.task

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable


@Dao
interface TaskDao {

    @Query("select * from Task where id = :id")
    fun getTaskById(id: String): Flowable<TaskEntity>

    @Query("select * from Task order by Task.creationTime")
    fun getTasks(): Observable<Array<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: TaskEntity): Completable

    @Query("delete from Task")
    fun deleteAllTasks()

    @Query("delete from Task where id = :id")
    fun deleteTask(id: String): Completable
}