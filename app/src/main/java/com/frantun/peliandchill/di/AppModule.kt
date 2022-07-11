package com.frantun.peliandchill.di

import com.frantun.peliandchill.BuildConfig
import com.frantun.peliandchill.data.datasource.MoviesLocalDataSource
import com.frantun.peliandchill.data.datasource.MoviesLocalDataSourceImpl
import com.frantun.peliandchill.data.datasource.MoviesRemoteDataSource
import com.frantun.peliandchill.data.datasource.MoviesRemoteDataSourceImpl
import com.frantun.peliandchill.data.datasource.SeriesLocalDataSource
import com.frantun.peliandchill.data.datasource.SeriesLocalDataSourceImpl
import com.frantun.peliandchill.data.datasource.SeriesRemoteDataSource
import com.frantun.peliandchill.data.datasource.SeriesRemoteDataSourceImpl
import com.frantun.peliandchill.data.remote.MoviesApi
import com.frantun.peliandchill.data.remote.SeriesApi
import com.frantun.peliandchill.data.repository.MoviesRepositoryImpl
import com.frantun.peliandchill.data.repository.SeriesRepositoryImpl
import com.frantun.peliandchill.domain.repository.MoviesRepository
import com.frantun.peliandchill.domain.repository.SeriesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ProvidesModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun providesMoviesApi(retrofit: Retrofit): MoviesApi {
        return retrofit.create(MoviesApi::class.java)
    }

    @Singleton
    @Provides
    fun providesSeriesApi(retrofit: Retrofit): SeriesApi {
        return retrofit.create(SeriesApi::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsModule {

    @Binds
    abstract fun providesMoviesRepository(moviesRepository: MoviesRepositoryImpl): MoviesRepository

    @Binds
    abstract fun providesSeriesRepository(seriesRepository: SeriesRepositoryImpl): SeriesRepository

    @Binds
    abstract fun providesMoviesLocalDataSource(moviesLocalDataSource: MoviesLocalDataSourceImpl): MoviesLocalDataSource

    @Binds
    abstract fun providesMoviesRemoteDataSource(moviesRemoteDataSource: MoviesRemoteDataSourceImpl): MoviesRemoteDataSource

    @Binds
    abstract fun providesSeriesLocalDataSource(seriesLocalDataSource: SeriesLocalDataSourceImpl): SeriesLocalDataSource

    @Binds
    abstract fun providesSeriesRemoteDataSource(seriesRemoteDataSource: SeriesRemoteDataSourceImpl): SeriesRemoteDataSource
}
