package com.aliceresponde.countingapp.di

import com.aliceresponde.countingapp.domain.usecase.getcounters.GetCountersUseCase
import com.aliceresponde.countingapp.presentation.main.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
object MainFragmentModule {

    @Provides
    fun provideMainViewModel(
        getCountersUC: GetCountersUseCase
//        ,
//        increaseCounterUC: IncreaseCounterUseCase,
//        decreaseCounterUC: DecreaseCounterUseCase,
//        deleteCounterUC: DeleteCounterUseCase
    ) = MainViewModel(
        getCountersUC //, increaseCounterUC, decreaseCounterUC, deleteCounterUC
    )

}