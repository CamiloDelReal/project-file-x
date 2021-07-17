package org.xapps.apps.filex.core.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import org.xapps.apps.filex.core.repositories.StorageRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Singleton
    @Provides
    fun provideStorageRepository(@ApplicationContext context: Context): StorageRepository =
        StorageRepository(context = context, dispatcher = Dispatchers.IO)

}