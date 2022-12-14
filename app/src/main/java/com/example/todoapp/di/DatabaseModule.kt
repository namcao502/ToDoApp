package com.example.todoapp.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        Database::class.java,
        "Database"
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideTaskDao(database: Database) = database.taskDao()
}