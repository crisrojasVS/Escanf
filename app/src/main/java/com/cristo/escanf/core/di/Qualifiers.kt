package com.cristo.escanf.core.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenFoodFactsRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EscanfRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenFoodFactsClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EscanfClient