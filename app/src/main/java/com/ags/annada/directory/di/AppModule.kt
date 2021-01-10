package com.ags.annada.directory.di

import android.content.Context
import androidx.room.Room
import com.ags.annada.directory.datasource.room.DirectoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): DirectoryDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            DirectoryDatabase::class.java,
            "Directory.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}