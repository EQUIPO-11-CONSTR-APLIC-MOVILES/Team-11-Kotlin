package com.example.restau.di

import android.app.Application
import com.example.restau.data.repository.AuthRepositoryImpl
import com.example.restau.data.repository.RecentsRepositoryImpl
import com.example.restau.data.repository.RestaurantsRepositoryImpl
import com.example.restau.domain.repository.AuthRepository
import android.content.Context
import com.example.restau.data.repository.ImageRepositoryImpl
import com.example.restau.data.repository.LocationRepositoryImpl
import com.example.restau.data.repository.ScreenTimeEventsRepositoryImpl
import com.example.restau.data.repository.UsersRepositoryImpl
import com.example.restau.domain.repository.ImageRepository
import com.example.restau.domain.repository.LocationRepository
import com.example.restau.domain.repository.RecentsRepository
import com.example.restau.domain.repository.RestaurantsRepository
import com.example.restau.domain.repository.ScreenTimeEventsRepository
import com.example.restau.domain.repository.UsersRepository
import com.example.restau.domain.usecases.AnalyticsUseCases
import com.example.restau.domain.usecases.GetFilterRestaurantsByNameAndCategories
import com.example.restau.domain.usecases.DownloadImages
import com.example.restau.domain.usecases.DownloadSingleImage
import com.example.restau.domain.usecases.GetIsNewRestaurantArray
import com.example.restau.domain.usecases.GetLocation
import com.example.restau.domain.usecases.GetOpenRestaurants
import com.example.restau.domain.usecases.GetRecents
import com.example.restau.domain.usecases.GetRestaurants
import com.example.restau.domain.usecases.AuthUseCases
import com.example.restau.domain.usecases.ExecuteSignIn
import com.example.restau.domain.usecases.GetCurrentUser
import com.example.restau.domain.usecases.RecentsUseCases
import com.example.restau.domain.usecases.RestaurantUseCases
import com.example.restau.domain.usecases.SaveRecents
import com.google.firebase.auth.FirebaseAuth
import com.example.restau.domain.usecases.GetRestaurantsInRadius
import com.example.restau.domain.usecases.GetRestaurantsLiked
import com.example.restau.domain.usecases.GetUserObject
import com.example.restau.domain.usecases.HasLikedCategoriesArray
import com.example.restau.domain.usecases.ImageDownloadUseCases
import com.example.restau.domain.usecases.LocationUseCases
import com.example.restau.domain.usecases.SendLike
import com.example.restau.domain.usecases.SendScreenTimeEvent
import com.example.restau.domain.usecases.UserUseCases
import com.google.android.gms.location.LocationServices
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
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
    fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics

    @Provides
    @Singleton
    fun provideRestaurantsRepository(db: FirebaseFirestore): RestaurantsRepository {
        return RestaurantsRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
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
            getFilterRestaurantsByNameAndCategories = GetFilterRestaurantsByNameAndCategories(),
            getRestaurantsInRadius = GetRestaurantsInRadius(),
            getRestaurantsLiked = GetRestaurantsLiked(),
            hasLikedCategoriesArray = HasLikedCategoriesArray()
        )
    }

    @Provides
    @Singleton
    fun provideSignUpUseCases(
        authRepository: AuthRepository
    ): AuthUseCases {
        return AuthUseCases(
            executeSignIn = ExecuteSignIn(authRepository),
            getCurrentUser = GetCurrentUser(authRepository)
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

    @Provides
    @Singleton
    fun provideUsersRepository(db: FirebaseFirestore): UsersRepository =
        UsersRepositoryImpl(db)

    @Provides
    @Singleton
    fun provideScreenTimeEventsRepository(db: FirebaseFirestore): ScreenTimeEventsRepository =
        ScreenTimeEventsRepositoryImpl(db)

    @Provides
    @Singleton
    fun provideAnalyticsUseCases(
        screenTimeEventsRepository: ScreenTimeEventsRepository
    ) = AnalyticsUseCases(
        sendScreenTimeEvent = SendScreenTimeEvent(screenTimeEventsRepository)
    )

    @Provides
    @Singleton
    fun provideUserUseCases(
        usersRepository: UsersRepository,
        authRepository: AuthRepository
    ): UserUseCases =
        UserUseCases(
            getUserObject = GetUserObject(
                usersRepository,
                authRepository
            ),
            sendLike = SendLike(usersRepository)
        )


}