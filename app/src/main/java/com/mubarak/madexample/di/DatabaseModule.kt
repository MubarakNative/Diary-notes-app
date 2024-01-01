package com.mubarak.madexample.di

import android.content.Context
import androidx.room.Room
import com.mubarak.madexample.data.repository.NoteRepository
import com.mubarak.madexample.data.sources.local.NoteDatabase
import com.mubarak.madexample.data.sources.NoteRepositoryImpl
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
    fun provideDatabase(@ApplicationContext context: Context):NoteDatabase{
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "notes_db"
        ).build()
    }

    @Singleton
    @Provides
    fun getRepositoryImpl(noteDatabase: NoteDatabase):NoteRepository{
        return NoteRepositoryImpl(noteDatabase)
    }
}