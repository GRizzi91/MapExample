package com.wisemotions.mapexaple.network

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import java.lang.Exception

fun <T> Observable<out Iterable<T>>.filterIterable(predicate:(T)->Boolean): Observable<out Iterable<T>> {
    return subscribeOn(Schedulers.newThread()).map{it.filter(predicate)}
}

fun <T> Observable<out Iterable<T>>.takeFirstIterable(size:Int): Observable<out Iterable<T>> {
    return subscribeOn(Schedulers.newThread()).map {it.take(size)}
}

fun <T> Observable<out Iterable<T>>.subIterable(from:Int, to:Int): Observable<List<T>> {
    return subscribeOn(Schedulers.newThread()).map{
        it.filterIndexed { index, t -> index in from..to }
    }
}

fun <T> Retrofit.Builder.build(service: Class<T>) : T{
    val baseUrl = service.annotations.find { it.annotationClass == BaseUrl::class } as? BaseUrl
    baseUrl?.let {
        return baseUrl(it.baseUrl).build().create(service)
    }
    throw MissingBaseUrlAnnotationException(service)
}

class  MissingBaseUrlAnnotationException(passedClass:Any) : Exception("$passedClass non contiene l'annotation BaseUrl")

annotation class BaseUrl(val baseUrl:String)