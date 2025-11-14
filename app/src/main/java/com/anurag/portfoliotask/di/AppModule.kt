package com.anurag.portfoliotask.di

import android.content.Context
import androidx.room.Room
import com.anurag.portfoliotask.data.local.dao.HoldingsDao
import com.anurag.portfoliotask.data.local.db.HoldingsDatabase
import com.anurag.portfoliotask.data.remote.api.PortfolioApiService
import com.anurag.portfoliotask.data.repository.PortfolioRepository
import com.anurag.portfoliotask.domain.calculator.PortfolioCalculator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                try {
                    chain.proceed(chain.request())
                } catch (e: Exception) {
                    throw IOException("Network error: ${e.message}") as Throwable
                }
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): PortfolioApiService {
        return Retrofit.Builder()
            .baseUrl("https://35dee773a9ec441e9f38d5fc249406ce.api.mockbin.io/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PortfolioApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HoldingsDatabase {
        return Room.databaseBuilder(context, HoldingsDatabase::class.java, "holdings_db").build()
    }

    @Provides
    fun provideHoldingsDao(db: HoldingsDatabase): HoldingsDao = db.holdingsDao()

    @Provides
    @Singleton
    fun providePortfolioRepository(api: PortfolioApiService, dao: HoldingsDao, calculator: PortfolioCalculator): PortfolioRepository {
        return PortfolioRepository(api, dao, calculator)
    }
}