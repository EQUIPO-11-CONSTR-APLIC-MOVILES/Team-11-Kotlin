package com.example.restau.di

import android.app.Application
import com.example.restau.data.repository.RecentsRepositoryImpl
import com.example.restau.data.repository.RestaurantsRepositoryImpl
import com.example.restau.domain.repository.RecentsRepository
import com.example.restau.domain.repository.RestaurantsRepository
import com.example.restau.domain.usecases.DateTimeUseCases
import com.example.restau.domain.usecases.GetCurrentDay
import com.example.restau.domain.usecases.GetCurrentTime
import com.example.restau.domain.usecases.GetOpenRestaurants
import com.example.restau.domain.usecases.GetRecents
import com.example.restau.domain.usecases.GetRestaurants
import com.example.restau.domain.usecases.RecentsUseCases
import com.example.restau.domain.usecases.RestaurantUseCases
import com.example.restau.domain.usecases.SaveRecents
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRecentsRepository(application: Application): RecentsRepository {
        return RecentsRepositoryImpl(application)
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideRestaurantsRepository(db: FirebaseFirestore): RestaurantsRepository {
        return RestaurantsRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideRecentsUseCases(
        recentsRepository: RecentsRepository
    ): RecentsUseCases {
        return RecentsUseCases(
            getRecents = GetRecents(recentsRepository),
            saveRecents = SaveRecents(recentsRepository)
        )
    }

    @Provides
    @Singleton
    fun provideRestaurantUseCases(
        restaurantsRepository: RestaurantsRepository
    ): RestaurantUseCases {
        return RestaurantUseCases(
            getRestaurants = GetRestaurants(restaurantsRepository),
            getOpenRestaurants = GetOpenRestaurants(restaurantsRepository)
        )
    }

    @Provides
    @Singleton
    fun provideDateTimeUseCases(): DateTimeUseCases {
        return DateTimeUseCases(
            getCurrentTime = GetCurrentTime(),
            getCurrentDay = GetCurrentDay()
        )
    }

}