package com.example.restau.di

import android.app.Application
import android.content.Context
import com.example.restau.data.repository.AuthRepositoryImpl
import com.example.restau.data.repository.FeaturesInteractionsEventsRepositoryImpl
import com.example.restau.data.repository.ImageRepositoryImpl
import com.example.restau.data.repository.LocationRepositoryImpl
import com.example.restau.data.repository.NavPathsRepositoryImpl
import com.example.restau.data.repository.RecentsRepositoryImpl
import com.example.restau.data.repository.RestaurantsRepositoryImpl
import com.example.restau.data.repository.ScreenTimeEventsRepositoryImpl
import com.example.restau.data.repository.TagsRepositoryImpl
import com.example.restau.data.repository.UsersRepositoryImpl
import com.example.restau.domain.repository.AuthRepository
import com.example.restau.domain.repository.FeaturesInteractionsEventsRepository
import com.example.restau.domain.repository.ImageRepository
import com.example.restau.domain.repository.LocationRepository
import com.example.restau.domain.repository.NavPathsRepository
import com.example.restau.domain.repository.RecentsRepository
import com.example.restau.domain.repository.RestaurantsRepository
import com.example.restau.domain.repository.ScreenTimeEventsRepository
import com.example.restau.domain.repository.TagsRepository
import com.example.restau.domain.repository.UsersRepository
import com.example.restau.domain.usecases.AnalyticsUseCases
import com.example.restau.domain.usecases.AuthUseCases
import com.example.restau.domain.usecases.CreatePath
import com.example.restau.domain.usecases.DownloadImages
import com.example.restau.domain.usecases.DownloadSingleImage
import com.example.restau.domain.usecases.ExecuteSignIn
import com.example.restau.domain.usecases.ExecuteSignUp
import com.example.restau.domain.usecases.GetCurrentUser
import com.example.restau.domain.usecases.GetFilterRestaurantsByNameAndCategories
import com.example.restau.domain.usecases.GetIsNewRestaurantArray
import com.example.restau.domain.usecases.GetLocation
import com.example.restau.domain.usecases.GetOpenRestaurants
import com.example.restau.domain.usecases.GetRecents
import com.example.restau.domain.usecases.GetRestaurants
import com.example.restau.domain.usecases.GetRestaurantsInRadius
import com.example.restau.domain.usecases.GetRestaurantsLiked
import com.example.restau.domain.usecases.GetTags
import com.example.restau.domain.usecases.GetUserObject
import com.example.restau.domain.usecases.HasLikedCategoriesArray
import com.example.restau.domain.usecases.ImageDownloadUseCases
import com.example.restau.domain.usecases.LocationUseCases
import com.example.restau.domain.usecases.NavPathsUseCases
import com.example.restau.domain.usecases.RecentsUseCases
import com.example.restau.domain.usecases.RestaurantUseCases
import com.example.restau.domain.usecases.SaveRecents
import com.example.restau.domain.usecases.SaveTags
import com.example.restau.domain.usecases.SendFeatureInteractionEvent
import com.example.restau.domain.usecases.SendLike
import com.example.restau.domain.usecases.SendScreenTimeEvent
import com.example.restau.domain.usecases.SetUserInfo
import com.example.restau.domain.usecases.TagsUseCases
import com.example.restau.domain.usecases.UpdatePath
import com.example.restau.domain.usecases.UserUseCases
import com.google.android.gms.location.LocationServices
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
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
    fun provideNavPathsRepository(db: FirebaseFirestore): NavPathsRepository {
        return NavPathsRepositoryImpl(db)
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
            getCurrentUser = GetCurrentUser(authRepository),
            executeSignUp = ExecuteSignUp(authRepository)
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
    fun provideTagsRepository(db: FirebaseFirestore): TagsRepository =
        TagsRepositoryImpl(db)


    @Provides
    @Singleton
    fun provideScreenTimeEventsRepository(db: FirebaseFirestore): ScreenTimeEventsRepository =
        ScreenTimeEventsRepositoryImpl(db)

    @Provides
    @Singleton
    fun provideFeaturesInteractionsEventsRepository(db: FirebaseFirestore): FeaturesInteractionsEventsRepository =
        FeaturesInteractionsEventsRepositoryImpl(db)

    @Provides
    @Singleton
    fun provideAnalyticsUseCases(
        screenTimeEventsRepository: ScreenTimeEventsRepository,
        featuresInteractionsEventsRepository: FeaturesInteractionsEventsRepository
    ) = AnalyticsUseCases(
        sendScreenTimeEvent = SendScreenTimeEvent(screenTimeEventsRepository),
        sendFeatureInteraction = SendFeatureInteractionEvent(featuresInteractionsEventsRepository)
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
            sendLike = SendLike(usersRepository),
            saveTags = SaveTags(usersRepository),
            setUserInfo = SetUserInfo(usersRepository)
        )

    @Provides
    @Singleton
    fun provideTagsUseCases(
        tagsRepository: TagsRepository
    ) = TagsUseCases(
        getTags = GetTags(tagsRepository)
    )

    @Provides
    @Singleton
    fun provideNavPathsUseCases(
        navPathsRepository: NavPathsRepository
    ) = NavPathsUseCases(
        createPath = CreatePath(navPathsRepository),
        updatePath = UpdatePath(navPathsRepository)
    )

}