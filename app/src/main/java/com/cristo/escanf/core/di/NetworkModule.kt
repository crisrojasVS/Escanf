package com.cristo.escanf.core.di

import com.cristo.escanf.core.network.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.google.android.datatransport.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private fun baseClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "Escanf/1.0 (Android; proyecto escolar)")
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
    }

    // ---- API pública: Open Food Facts (sin token) ----

    @Provides
    @Singleton
    @OpenFoodFactsClient
    fun provideOpenFoodFactsClient(): OkHttpClient = baseClientBuilder().build()

    @Provides
    @Singleton
    @OpenFoodFactsRetrofit
    fun provideOpenFoodFactsRetrofit(@OpenFoodFactsClient client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.org/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ---- Tu servidor en EC2 (con token) ----

    @Provides
    @Singleton
    @EscanfClient
    fun provideEscanfClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return baseClientBuilder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @EscanfRetrofit
    fun provideEscanfRetrofit(@EscanfClient client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}