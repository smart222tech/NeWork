package ru.netology.nmedia.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.db.AppDatabase
import ru.netology.nmedia.db.EventDao
import ru.netology.nmedia.db.JobDao
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.db.UserDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePostDao(
        database: AppDatabase
    ): PostDao = database.postDao()

    @Provides
    fun provideEventDao(
        database: AppDatabase
    ): EventDao = database.eventDao()

    @Provides
    fun provideUserDao(
        database: AppDatabase
    ): UserDao = database.userDao()

    @Provides
    fun provideJobDao(
        database: AppDatabase
    ): JobDao = database.jobDao()
}
