package com.aliceresponde.countingapp.di

import com.aliceresponde.countingapp.domain.usecase.create.CreateCounterUseCase
import com.aliceresponde.countingapp.presentation.createcounter.CreateCounterViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.CoroutineDispatcher

@InstallIn(ActivityRetainedComponent::class)
@Module
object CreateCounterFragment {
    @Provides
    fun providesCreateCounterViewModel(
        useCase: CreateCounterUseCase,
        coroutineDispatcher: CoroutineDispatcher
    ) =
        CreateCounterViewModel(useCase, coroutineDispatcher)
}