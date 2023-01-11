package org.ovi.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject
import org.ovi.BuildConfig
import org.ovi.data.pref.OVIPreferences
import org.ovi.data.remote.*
import org.ovi.network.jsonadapter.NullToEmptyStringAdapter
import org.ovi.network.jsonadapter.StringToBooleanAdapter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

val networkModule = module {
    single { provideHttpClient(get(), get()) }
    single(named("noAuth")) { provideUploadHttpClient(get(), get()) }
    single { provideLoggingInterceptor() }
    single { provideAuthInterceptor() }
    single { providesMoshi() }
    single { provideRetrofit(get(), get()) }
    single(named("noAuth")) { provideUploadRetrofit(get(), get((named("noAuth")))) }

    single { provideAuthService(get()) }
    single { provideRegisterService(get()) }
    single { provideProfileService(get()) }
    single { provideEventService(get()) }
    single { provideValidationService(get()) }
    single { provideNotificationService(get()) }
    single { provideSurveyService(get()) }
    single { provideFileUploadService(get((named("noAuth")))) }
    single { provideNewFileUploadService(get((named("noAuth")))) }

}

private fun provideAuthService(retrofit: Retrofit): AuthService =
    retrofit.create(AuthService::class.java)

private fun provideRegisterService(retrofit: Retrofit): RegisterService =
    retrofit.create(RegisterService::class.java)

private fun provideProfileService(retrofit: Retrofit): ProfileServices =
    retrofit.create(ProfileServices::class.java)

private fun provideEventService(retrofit: Retrofit): EventService =
    retrofit.create(EventService::class.java)

private fun provideFileUploadService(retrofit: Retrofit): FileUploadService =
    retrofit.create(FileUploadService::class.java)

private fun provideNotificationService(retrofit: Retrofit): NotificationService =
    retrofit.create(NotificationService::class.java)

private fun provideNewFileUploadService(retrofit: Retrofit): NewFileUploadService =
    retrofit.create(NewFileUploadService::class.java)

private fun provideValidationService(retrofit: Retrofit): ValidationService =
    retrofit.create(ValidationService::class.java)

private fun provideSurveyService(retrofit: Retrofit): SurveyService =
    retrofit.create(SurveyService::class.java)


fun providesMoshi(): Moshi {
    return Moshi.Builder()
        .add(StringToBooleanAdapter())
        .add(NullToEmptyStringAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()
}

fun provideHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor,
    authInterceptor: Interceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        //.certificatePinner(certificatePinner)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(authInterceptor)
        .ignoreAllSSLErrors()
        .build()
}


fun provideUploadHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor,
    authInterceptor: Interceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(3000, TimeUnit.SECONDS)
        .writeTimeout(3000, TimeUnit.SECONDS)
        .readTimeout(3000, TimeUnit.SECONDS)
        //.certificatePinner(certificatePinner)
        .addInterceptor(httpLoggingInterceptor)
        .ignoreAllSSLErrors()
        .build()
}


fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        level = if (org.ovi.BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }
}

fun provideAuthInterceptor(): Interceptor {

    return object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {

            val pref: OVIPreferences by inject(OVIPreferences::class.java)

//            Log.d("Network", "intercept: accessToken ${pref.token} isLoggedIn $isLoggedIn")

            val newRequest = if (pref.token?.isNotEmpty() == true) {
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${pref.token}")
                    .build()
            } else
                chain.request().newBuilder()
                    .addHeader("Origin","mobile_app")
                    .build()

            return chain.proceed(newRequest)
        }
    }
}

fun provideUploadRetrofit(factory: Moshi, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(factory).withNullSerialization())
        .client(client)
        .build()
}

fun provideRetrofit(factory: Moshi, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(factory))
        .client(client)
        .build()
}

// TODO:- Change certificate
//private val certificatePinner = CertificatePinner.Builder()
//    .add(
//        "",
//        ""
//    )
//    .build()

fun OkHttpClient.Builder.ignoreAllSSLErrors(): OkHttpClient.Builder {
    val naiveTrustManager = object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) = Unit
        override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) = Unit
    }

    val insecureSocketFactory = SSLContext.getInstance("SSL").apply {
        val trustAllCerts = arrayOf<TrustManager>(naiveTrustManager)
        init(null, trustAllCerts, SecureRandom())
    }.socketFactory

    sslSocketFactory(insecureSocketFactory, naiveTrustManager)
    hostnameVerifier { _, _ -> true }
    return this
}

fun convertResponse(className: Any, value: Any): Any {
    val moshi = providesMoshi()
    val jsonAdapter =
        moshi.adapter(className as Class<*>)
    if (value is Buffer)
        return jsonAdapter.fromJson(value)!!
    else
        return jsonAdapter.fromJsonValue(value)!!
}
