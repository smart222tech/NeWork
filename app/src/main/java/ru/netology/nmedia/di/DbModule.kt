package ru.netology.nmedia.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.db.EventDao
import ru.netology.nmedia.db.JobDao
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.db.UserDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideAppDb(
        @ApplicationContext context: Context
    ): AppDb {
        return Room.databaseBuilder(context, AppDb::class.java, "app.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePostDao(db: AppDb): PostDao = db.postDao()

    @Provides
    fun provideEventDao(db: AppDb): EventDao = db.eventDao()

    @Provides
    fun provideUserDao(db: AppDb): UserDao = db.userDao()

    @Provides
    fun provideJobDao(db: AppDb): JobDao = db.jobDao()
}
