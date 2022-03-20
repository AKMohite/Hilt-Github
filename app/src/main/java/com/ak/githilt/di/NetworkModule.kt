package com.ak.githilt.di

import com.ak.githilt.remote.GithubAPIService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "https://api.github.com/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule{

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson = GsonBuilder()
//        .excludeFieldsWithoutExposeAnnotation()
        .create()

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun provideOkHttpBuilder(logging: HttpLoggingInterceptor): OkHttpClient.Builder = OkHttpClient.Builder()
        .addInterceptor(logging)

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, httpClient: OkHttpClient.Builder): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient.build())
        .addConverterFactory(GsonConverterFactory.create(gson))

    @Singleton
    @Provides
    fun provideGithubService(retrofit: Retrofit.Builder): GithubAPIService = retrofit
        .build()
        .create(GithubAPIService::class.java)

}