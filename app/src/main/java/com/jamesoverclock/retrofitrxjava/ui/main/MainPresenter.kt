package com.jamesoverclock.retrofitrxjava.ui.main

import com.jamesoverclock.retrofitrxjava.BuildConfig
import com.jamesoverclock.retrofitrxjava.models.UserResponse
import com.jamesoverclock.retrofitrxjava.services.Constants
import com.jamesoverclock.retrofitrxjava.services.GithubAPI
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainPresenter(private val view: MainView) {

    private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return interceptor
    }

    private fun provideOkHttpClientDefault(interceptor: HttpLoggingInterceptor): OkHttpClient {
        val okBuilder = OkHttpClient.Builder()
        okBuilder.addInterceptor(interceptor)

        val timeout = 30
        okBuilder.connectTimeout(timeout.toLong(), TimeUnit.SECONDS)
        okBuilder.readTimeout(timeout.toLong(), TimeUnit.SECONDS)
        okBuilder.writeTimeout(timeout.toLong(), TimeUnit.SECONDS)

        return okBuilder.build()
    }


    var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(provideOkHttpClientDefault(provideHttpLoggingInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    fun searchUser(q: String?) {
        retrofit.create<GithubAPI>(GithubAPI::class.java)
                .searchUsers(q)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<UserResponse> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(userResponse: UserResponse) {
                        view.setAdapterData(userResponse.users)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
    }
}