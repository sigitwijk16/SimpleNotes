package com.gitz.simplenotes.di

import android.content.Context
import com.gitz.simplenotes.data.local.AppDatabase
import com.gitz.simplenotes.data.local.NoteDao
import com.gitz.simplenotes.data.remote.ApiService
import com.gitz.simplenotes.data.remote.RetrofitInstance
import com.gitz.simplenotes.repository.NoteRepository
import com.gitz.simplenotes.repository.NoteRepositoryImpl
import com.gitz.simplenotes.repository.QuoteRepository
import com.gitz.simplenotes.repository.QuoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }

    @Provides
    @Singleton
    fun provideNoteDao(appDatabase: AppDatabase): NoteDao {
        return appDatabase.noteDao()
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return RetrofitInstance.api
    }

    @Provides
    @Singleton
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepositoryImpl(noteDao)
    }

    @Provides
    @Singleton
    fun provideQuoteRepository(apiService: ApiService): QuoteRepository {
        return QuoteRepositoryImpl(apiService)
    }
}