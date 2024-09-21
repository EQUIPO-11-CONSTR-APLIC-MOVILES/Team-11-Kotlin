package com.example.restau.di

import android.app.Application
import android.content.Context
import com.example.restau.data.repository.ImageRepositoryImpl
import com.example.restau.data.repository.LocationRepositoryImpl
import com.example.restau.data.repository.RecentsRepositoryImpl
import com.example.restau.data.repository.RestaurantsRepositoryImpl
import com.example.restau.domain.repository.ImageRepository
import com.example.restau.domain.repository.LocationRepository
import com.example.restau.domain.repository.RecentsRepository
import com.example.restau.domain.repository.RestaurantsRepository
import com.example.restau.domain.usecases.DownloadImages
import com.example.restau.domain.usecases.DownloadSingleImage
import com.example.restau.domain.usecases.GetIsNewRestaurantArray
import com.example.restau.domain.usecases.GetLocation
import com.example.restau.domain.usecases.GetOpenRestaurants
import com.example.restau.domain.usecases.GetRecents
import com.example.restau.domain.usecases.GetRestaurants
import com.example.restau.domain.usecases.GetRestaurantsInRadius
import com.example.restau.domain.usecases.ImageDownloadUseCases
import com.example.restau.domain.usecases.LocationUseCases
import com.example.restau.domain.usecases.RecentsUseCases
import com.example.restau.domain.usecases.RestaurantUseCases
import com.example.restau.domain.usecases.SaveRecents
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
            getOpenRestaurants = GetOpenRestaurants(restaurantsRepository),
            getIsNewRestaurantArray = GetIsNewRestaurantArray(),
            getRestaurantsInRadius = GetRestaurantsInRadius()
        )
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        @ApplicationContext context: Context
    ): LocationRepository {
        return LocationRepositoryImpl(
            context,
            LocationServices.getFusedLocationProviderClient(context)
        )
    }

    @Provides
    @Singleton
    fun provideLocationUseCases(
        locationRepository: LocationRepository
    ): LocationUseCases = LocationUseCases(GetLocation(locationRepository))

    @Provides
    @Singleton
    fun provideImageRepository(@ApplicationContext context: Context): ImageRepository =
        ImageRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideImageDownloadUseCases(
        imageRepository: ImageRepository
    ): ImageDownloadUseCases = ImageDownloadUseCases(
        downloadImages = DownloadImages(imageRepository),
        downloadSingleImage = DownloadSingleImage(imageRepository)
    )
}