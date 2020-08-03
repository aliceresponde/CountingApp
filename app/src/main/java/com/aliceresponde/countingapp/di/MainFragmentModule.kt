package com.aliceresponde.countingapp.di

import com.aliceresponde.countingapp.domain.usecase.getcounters.GetCountersUseCase
import com.aliceresponde.countingapp.domain.usecase.getcounters.GetCountersUseCaseImp
import com.aliceresponde.countingapp.repository.CounterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

@InstallIn()
@Module
object MainFragmentModule {
    @Provides
    fun provideGetCountersUseCase(repository: CounterRepository): GetCountersUseCase =
        GetCountersUseCaseImp(repository)
}