package com.choice.feature.module

import com.choice.feature.navigation.navigator.AppNavigator
import com.choice.feature.navigation.navigator.IAppNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object AppNavigatorModule {

    @ViewModelScoped
    @Provides
    fun providesAppNavigator() : AppNavigator {
        return IAppNavigator()
    }


}