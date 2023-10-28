package com.salmakhd.android.forpractice.paging.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.salmakhd.android.forpractice.paging.network.BeerApi
import com.salmakhd.android.forpractice.paging.data.BeerDatabase
import com.salmakhd.android.forpractice.paging.data.BeerEntity
import com.salmakhd.android.forpractice.paging.network.BeerRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesBeerDatabase(
        @ApplicationContext context: Context
    ): BeerDatabase =
        Room.databaseBuilder(
            context,
            BeerDatabase::class.java,
            "beer_db"
        ).build()


    @Provides
    @Singleton
    fun providesBeerApi(): BeerApi =
       Retrofit.Builder()
           .baseUrl(BeerApi.BASE_URL)
           .addConverterFactory(MoshiConverterFactory.create())
           .build()
           .create()

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun providesBeerPager(beerDatabase: BeerDatabase, beerApi: BeerApi): Pager<Int, BeerEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20 ),
            remoteMediator = BeerRemoteMediator(
                beerDatabase = beerDatabase,
                beerApi = beerApi
            ),
            pagingSourceFactory = {
                beerDatabase.dao.pagingSource()
            }
        )
    }
}