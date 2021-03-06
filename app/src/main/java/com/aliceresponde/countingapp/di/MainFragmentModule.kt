package com.aliceresponde.countingapp.di

import com.aliceresponde.countingapp.domain.usecase.decrease.DecreaseCounterUseCase
import com.aliceresponde.countingapp.domain.usecase.delete.DeleteCounterUseCase
import com.aliceresponde.countingapp.domain.usecase.filter.FilterDataUseCase
import com.aliceresponde.countingapp.domain.usecase.getcounters.GetCountersUseCase
import com.aliceresponde.countingapp.domain.usecase.increase.IncreaseCounterUseCase
import com.aliceresponde.countingapp.presentation.main.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.CoroutineDispatcher

@InstallIn(ActivityRetainedComponent::class)
@Module
object MainFragmentModule {

    @Provides
    fun provideMainViewModel(
        getCountersUC: GetCountersUseCase,
        increaseCounterUC: IncreaseCounterUseCase,
        decreaseCounterUC: DecreaseCounterUseCase,
        deleteCounterUC: DeleteCounterUseCase,
        coroutineDispatcher: CoroutineDispatcher
    ) =
        MainViewModel(
            getCountersUC,
            increaseCounterUC,
            decreaseCounterUC,
            deleteCounterUC,
            coroutineDispatcher
        )

}