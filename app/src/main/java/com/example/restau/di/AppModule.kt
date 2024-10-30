package com.example.restau.di

import android.app.Application
import android.content.Context
import com.example.restau.data.remote.AnalyticsAPI
import com.example.restau.data.repository.AnalyticsRepositoryImpl
import com.example.restau.data.repository.AuthRepositoryImpl
import com.example.restau.data.repository.FeaturesInteractionsEventsRepositoryImpl
import com.example.restau.data.repository.ImageRepositoryImpl
import com.example.restau.data.repository.LikeDateRestaurantRepositoryImpl
import com.example.restau.data.repository.LocationRepositoryImpl
import com.example.restau.data.repository.NavPathsRepositoryImpl
import com.example.restau.data.repository.RecentsRepositoryImpl
import com.example.restau.data.repository.RestaurantsRepositoryImpl
import com.example.restau.data.repository.ReviewsRepositoryImpl
import com.example.restau.data.repository.ScreenTimeEventsRepositoryImpl
import com.example.restau.data.repository.SearchedCategoriesRepositoryImpl
import com.example.restau.data.repository.TagsRepositoryImpl
import com.example.restau.data.repository.UsersRepositoryImpl
import com.example.restau.domain.repository.AnalyticsRepository
import com.example.restau.domain.repository.AuthRepository
import com.example.restau.domain.repository.FeaturesInteractionsEventsRepository
import com.example.restau.domain.repository.ImageRepository
import com.example.restau.domain.repository.LikeDateRestaurantRepository
import com.example.restau.domain.repository.LocationRepository
import com.example.restau.domain.repository.NavPathsRepository
import com.example.restau.domain.repository.RecentsRepository
import com.example.restau.domain.repository.RestaurantsRepository
import com.example.restau.domain.repository.ReviewsRepository
import com.example.restau.domain.repository.ScreenTimeEventsRepository
import com.example.restau.domain.repository.SearchedCategoriesRepository
import com.example.restau.domain.repository.TagsRepository
import com.example.restau.domain.repository.UsersRepository
import com.example.restau.domain.usecases.pathUseCases.CreatePath
import com.example.restau.domain.usecases.pathUseCases.NavPathsUseCases
import com.example.restau.domain.usecases.pathUseCases.UpdatePath
import com.example.restau.domain.usecases.authUseCases.AuthUseCases
import com.example.restau.domain.usecases.analyticsUseCases.AnalyticsUseCases
import com.example.restau.domain.usecases.analyticsUseCases.GetLikeReviewWeek
import com.example.restau.domain.usecases.analyticsUseCases.GetPercentageCompletion
import com.example.restau.domain.usecases.imagesUseCases.DownloadImages
import com.example.restau.domain.usecases.imagesUseCases.DownloadSingleImage
import com.example.restau.domain.usecases.authUseCases.ExecuteSignIn
import com.example.restau.domain.usecases.authUseCases.ExecuteSignUp
import com.example.restau.domain.usecases.authUseCases.GetCurrentUser
import com.example.restau.domain.usecases.restaurantUseCases.GetFilterRestaurantsByNameAndCategories
import com.example.restau.domain.usecases.restaurantUseCases.GetIsNewRestaurantArray
import com.example.restau.domain.usecases.locationUseCases.GetLocation
import com.example.restau.domain.usecases.restaurantUseCases.GetOpenRestaurants
import com.example.restau.domain.usecases.recentsUseCases.GetRecents
import com.example.restau.domain.usecases.restaurantUseCases.GetRestaurants
import com.example.restau.domain.usecases.restaurantUseCases.GetRestaurantsInRadius
import com.example.restau.domain.usecases.restaurantUseCases.GetRestaurantsLiked
import com.example.restau.domain.usecases.tagsUseCases.GetTags
import com.example.restau.domain.usecases.userUseCases.GetUserObject
import com.example.restau.domain.usecases.restaurantUseCases.HasLikedCategoriesArray
import com.example.restau.domain.usecases.imagesUseCases.ImageDownloadUseCases
import com.example.restau.domain.usecases.locationUseCases.LocationUseCases
import com.example.restau.domain.usecases.recentsUseCases.RecentsUseCases
import com.example.restau.domain.usecases.restaurantUseCases.RestaurantUseCases
import com.example.restau.domain.usecases.recentsUseCases.SaveRecents
import com.example.restau.domain.usecases.userUseCases.SaveTags
import com.example.restau.domain.usecases.analyticsUseCases.SendFeatureInteractionEvent
import com.example.restau.domain.usecases.analyticsUseCases.SendLikeDateRestaurantEvent
import com.example.restau.domain.usecases.userUseCases.SendLike
import com.example.restau.domain.usecases.userUseCases.SetUserInfo
import com.google.android.gms.location.LocationServices
import com.example.restau.domain.usecases.analyticsUseCases.SendScreenTimeEvent
import com.example.restau.domain.usecases.analyticsUseCases.SendSearchedCategoriesEvent
import com.example.restau.domain.usecases.locationUseCases.LaunchMaps
import com.example.restau.domain.usecases.restaurantUseCases.GetFeaturedArray
import com.example.restau.domain.usecases.restaurantUseCases.GetRestaurant
import com.example.restau.domain.usecases.restaurantUseCases.IsOpen
import com.example.restau.domain.usecases.reviewsUseCases.AddReview
import com.example.restau.domain.usecases.reviewsUseCases.GetRestaurantsReviews
import com.example.restau.domain.usecases.reviewsUseCases.ReviewsUseCases
import com.example.restau.domain.usecases.tagsUseCases.TagsUseCases
import com.example.restau.domain.usecases.userUseCases.UserUseCases
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
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
    fun provideNavPathsRepository(db: FirebaseFirestore): NavPathsRepository {
        return NavPathsRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideReviewsRepository(db: FirebaseFirestore): ReviewsRepository {
        return ReviewsRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideReviewsUseCases(
        reviewsRepository: ReviewsRepository
    ) = ReviewsUseCases(
        getRestaurantsReviews = GetRestaurantsReviews(reviewsRepository),
        addReview = AddReview(reviewsRepository)
    )

    @Provides
    @Singleton
    fun provideNavPathsUseCases(
        navPathsRepository: NavPathsRepository
    ) = NavPathsUseCases(
        createPath = CreatePath(navPathsRepository),
        updatePath = UpdatePath(navPathsRepository)
    )

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
            hasLikedCategoriesArray = HasLikedCategoriesArray(),
            getRestaurant = GetRestaurant(restaurantsRepository),
            isOpen = IsOpen(restaurantsRepository),
            getFeaturedArray = GetFeaturedArray()
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
    fun provideAnalyticsAPI(): AnalyticsAPI {
        return Retrofit
            .Builder()
            .baseUrl("http://35.239.202.192:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnalyticsAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideAnalyticsRepository(
        analyticsAPI: AnalyticsAPI
    ): AnalyticsRepository = AnalyticsRepositoryImpl(analyticsAPI)

    @Provides
    @Singleton
    fun provideLocationUseCases(
        locationRepository: LocationRepository
    ): LocationUseCases = LocationUseCases(
        GetLocation(locationRepository),
        LaunchMaps()
    )

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
    fun provideSearchedCategoriesRepository(db: FirebaseFirestore): SearchedCategoriesRepository =
        SearchedCategoriesRepositoryImpl(db)

    @Provides
    @Singleton
    fun provideLikeDateRestaurantRepository(db: FirebaseFirestore): LikeDateRestaurantRepository =
        LikeDateRestaurantRepositoryImpl(db)

    @Provides
    @Singleton
    fun provideAnalyticsUseCases(
        screenTimeEventsRepository: ScreenTimeEventsRepository,
        featuresInteractionsEventsRepository: FeaturesInteractionsEventsRepository,
        searchedCategoriesRepository: SearchedCategoriesRepository,
        likeDateRestaurantRepository: LikeDateRestaurantRepository,
        analyticsRepository: AnalyticsRepository
    ) = AnalyticsUseCases(
        sendScreenTimeEvent = SendScreenTimeEvent(screenTimeEventsRepository),
        sendFeatureInteraction = SendFeatureInteractionEvent(featuresInteractionsEventsRepository),
        sendSearchedCategoriesEvent = SendSearchedCategoriesEvent(searchedCategoriesRepository),
        sendLikeDateRestaurantEvent = SendLikeDateRestaurantEvent(likeDateRestaurantRepository),
        getLikeReviewWeek = GetLikeReviewWeek(analyticsRepository),
        getPercentageCompletion = GetPercentageCompletion(analyticsRepository)
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



}