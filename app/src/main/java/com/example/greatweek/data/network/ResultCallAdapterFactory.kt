package com.example.greatweek.data.network

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Suppress("UNCHECKED_CAST")
class ResultCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java || returnType !is ParameterizedType) {
            return null
        }
        val upperBound = getParameterUpperBound(0, returnType)
        if (upperBound is ParameterizedType && upperBound.rawType == Result::class.java) {
            return object : CallAdapter<Any, Call<Result<*>>> {
                override fun responseType(): Type =
                    getParameterUpperBound(0, upperBound)
                override fun adapt(call: Call<Any>): Call<Result<*>> =
                    ResultCall(call, retrofit) as Call<Result<*>>
            }
        } else {
            return null
        }
    }
}
