package org.xapps.apps.filex.core.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import org.xapps.apps.filex.core.repositories.SettingsRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class SettingsModule {

    companion object {
        const val PREFERENCE_NAME = "application_preferences"
    }

    @Singleton
    @Provides
    fun provideSettingsService(@ApplicationContext context: Context): SettingsRepository =
        SettingsRepository(context = context, dispatcher = Dispatchers.IO, PREFERENCE_NAME)

}