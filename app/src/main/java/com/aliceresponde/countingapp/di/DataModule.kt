package com.aliceresponde.countingapp.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.aliceresponde.countingapp.data.dataSource.LocalDataSource
import com.aliceresponde.countingapp.data.dataSource.RemoteDataSource
import com.aliceresponde.countingapp.data.local.AppDatabase
import com.aliceresponde.countingapp.data.local.RoomDataSource
import com.aliceresponde.countingapp.data.remote.CornerApiService
import com.aliceresponde.countingapp.data.remote.NetworkConnection
import com.aliceresponde.countingapp.data.remote.NetworkConnectionInterceptor
import com.aliceresponde.countingapp.data.remote.RetrofitDataSource
import com.aliceresponde.countingapp.domain.usecase.create.CreateCounterUseCase
import com.aliceresponde.countingapp.domain.usecase.create.CreateCounterUseCaseImp
import com.aliceresponde.countingapp.domain.usecase.decrease.DecreaseCounterUseCase
import com.aliceresponde.countingapp.domain.usecase.decrease.DecreaseCounterUseCaseImp
import com.aliceresponde.countingapp.domain.usecase.delete.DeleteCounterUseCase
import com.aliceresponde.countingapp.domain.usecase.delete.DeleteCounterUseCaseImp
import com.aliceresponde.countingapp.domain.usecase.getcounters.GetCountersUseCase
import com.aliceresponde.countingapp.domain.usecase.getcounters.GetCountersUseCaseImp
import com.aliceresponde.countingapp.domain.usecase.increase.IncreaseCounterUseCase
import com.aliceresponde.countingapp.domain.usecase.increase.IncreaseCounterUseCaseImp
import com.aliceresponde.countingapp.repository.CounterRepository
import com.aliceresponde.countingapp.repository.CounterRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "database-name").build()

    @Provides
    @Singleton
    fun provideNetworkConnectionInterceptor(@ApplicationContext context: Context): Interceptor =
        NetworkConnectionInterceptor(context)

    @Provides
    @Singleton
    fun provideApiService(interceptor: Interceptor) = CornerApiService.invoke(interceptor)

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideNetworkConnection(
        @ApplicationContext context: Context,
        connectivityManager: ConnectivityManager
    ) = NetworkConnection(context, connectivityManager)

//    ----------------------- Repository----------------------

    @Provides
    @Singleton
    fun provideLocalDataSource(db: AppDatabase): LocalDataSource = RoomDataSource(db)

    @Provides
    @Singleton
    fun provideRemoteDataSource(service: CornerApiService): RemoteDataSource =
        RetrofitDataSource(service)

    @Provides
    @Singleton
    fun provideCounterRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): CounterRepository = CounterRepositoryImp(localDataSource, remoteDataSource)

    //    ----------------------- UseCase-------------------------
    @Provides
    fun provideGetCountersUseCase(repository: CounterRepository): GetCountersUseCase =
        GetCountersUseCaseImp(repository)

    @Provides
    fun provideIncreaseCounterUseCase(repository: CounterRepository): IncreaseCounterUseCase =
        IncreaseCounterUseCaseImp(repository)

    @Provides
    fun provideDecreaseCounterUseCase(repository: CounterRepository): DecreaseCounterUseCase =
        DecreaseCounterUseCaseImp(repository)

    @Provides
    fun provideDeleteCounterUseCase(repository: CounterRepository): DeleteCounterUseCase =
        DeleteCounterUseCaseImp(repository)

    @Provides
    fun provideCreateCounterUseCase(repository: CounterRepository): CreateCounterUseCase = CreateCounterUseCaseImp(repository)

}