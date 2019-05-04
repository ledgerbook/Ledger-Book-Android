package com.android.ledgerbook.storage.remote;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Pair;

import com.android.ledgerbook.BuildConfig;
import com.android.ledgerbook.models.User;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.CookieCache;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.jakewharton.picasso.OkHttp3Downloader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteRepositoryConfig {
    protected Context appContext;
    private OkHttpClient httpClient;
    private GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson gson;
    private Gson errorGson;
    protected PersistentCookieJar persistentCookieJar;
    private List<Pair<String, String>> permanentHeaders;

    private static final List<String> CERTIFICATE_PINS = Arrays.asList(
            "sha1/arlQf81yyDNMC2GRFPYxwrdhvfA=", // CN=*.gopaysense.com,OU=Domain Control Validated
            "sha1/18TCBACN73tOG2w0sXDG4HryncI=", // CN=*.sandbox.gopaysense.com,OU=Domain Control Validated
            "sha1/tFVQFINFH+6MoKEM9a/eOkxeEVk=", // CN=Go Daddy Secure Certificate Authority - G2,OU=http://certs.godaddy.com/repository/,O=GoDaddy.com\, Inc.,L=Scottsdale,ST=Arizona,C=US
            "sha1/IQ8siffEzV0bgl441sZZO6aTda4=", // CN=Go Daddy Root Certificate Authority - G2,O=GoDaddy.com\, Inc.,L=Scottsdale,ST=Arizona,C=US
            "sha1/7uWfHiqlRMPLJUOmmlvUaiW8u44=" // OU=Go Daddy Class 2 Certification Authority,O=The Go Daddy Group\, Inc.,C=US
    );

    public static final String HEADER_ANDROID_ID = "x-ps-android-id";
    public static final String HEADER_DEVICE_ID = "x-ps-device-id";
    public static final String HEADER_MOBILE_ID = "x-ps-mobile-id-1";
    public static final String HEADER_PACKAGE_NAME = "x-ps-app-package-name";
    public static final String HEADER_APP_VERSION_NAME = "x-ps-app-version";
    public static final String HEADER_APP_VERSION_CODE = "x-ps-app-version-code";
    public static final String HEADER_REGISTRATION_TOKEN = "x-ps-app-registration-token";
    public static final String HEADER_INSTALLATION_ID = "x-ps-install-id";

    /**
     * @param appContext Should be Application Context
     */
    public RemoteRepositoryConfig(Context appContext) {
        this.appContext = appContext;
        if (!(appContext instanceof Application)) {
            throw new RuntimeException();
        }
    }

    protected OkHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = getHttpClientBuilder().build();
        }
        return httpClient;
    }

    public OkHttp3Downloader getDownloader() {
        return new OkHttp3Downloader(getHttpClient());
    }

    public Map<String, String> getCookies(String url) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        CookieJar cookieJar = getHttpClient().cookieJar();
        Map<String, String> cookiesNameVal = new HashMap<>();
        if (cookieJar != null && httpUrl != null) {
            List<Cookie> cookies = cookieJar.loadForRequest(httpUrl);
            if (cookies != null && !cookies.isEmpty()) {
                for (Cookie cookie : cookies) {
                    cookiesNameVal.put(cookie.name(), cookie.value());
                }
            }
        }
        return cookiesNameVal;
    }

    protected OkHttpClient.Builder getHttpClientBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        String baseUrl = getBaseUrl();
        if (baseUrl.startsWith("https") &&
                baseUrl.matches(".*gopaysense\\.com$")) {
            CertificatePinner.Builder certificatePinnerBuilder = new CertificatePinner.Builder();
            for (String pin : CERTIFICATE_PINS) {
                certificatePinnerBuilder.add("gopaysense.com", pin);
            }
            builder.certificatePinner(certificatePinnerBuilder.build());
        }

        CookieCache cookieCache = new SetCookieCache();
        persistentCookieJar =
                new PersistentCookieJar(cookieCache, new SharedPrefsCookiePersistor(appContext));
        builder.cookieJar(persistentCookieJar);

        builder.addNetworkInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder();

            //Set Mandatory Headers
            List<Pair<String, String>> headers = getHeaders();
            for (Pair<String, String> header : headers) {
                if (!TextUtils.isEmpty(header.second)) {
                    requestBuilder.header(header.first, header.second);
                }
            }

            //Set Cookie Header
            for (Cookie cookie : cookieCache) {
                if ("sessionid".equals(cookie.name())) {
                    requestBuilder.header("X-sessionid", cookie.value());
                    break;
                }
            }

            Response response = chain.proceed(requestBuilder.build());
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                MediaType contentType = responseBody != null ? responseBody.contentType() : null;
                if (contentType != null) {
                    String strContentType = contentType.toString();
                    if (!TextUtils.isEmpty(strContentType) && strContentType.contains("image")) {
                        return response;
                    }
                }
                String strResponseBody = responseBody.string();
                if (strResponseBody.length() == 0) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("message", "ok");
                        ResponseBody body = ResponseBody.create(contentType, jsonObject.toString());
                        return response.newBuilder().body(body).build();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ResponseBody newResponseBody = ResponseBody.create(contentType, strResponseBody);
                    return response.newBuilder().body(newResponseBody).build();
                }
            }
            return response;
        });

        builder.connectTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS);
        return builder;
    }

    protected String getBaseUrl() {
        return BuildConfig.BASE_URL;
    }

    protected Retrofit getApiClient() {
        return new Retrofit.Builder().baseUrl(getBaseUrl()).client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create(getGsonInstance()))
                .build();
    }

    public RemoteDataRepository getApiService() {
        return getApiClient().create(RemoteDataRepository.class);
    }

    public Gson getGsonInstance() {
        if (gson == null) {
            gsonBuilder.registerTypeAdapter(
                    User.class, (InstanceCreator<User>) type -> User.getInstance());
            gson = gsonBuilder.create();
        }
        return gson;
    }

    public Gson getErrorGsonInstance() {
        if (errorGson == null) {
            gsonBuilder
                    //This adapter converts the array of errors to a single String object
                    .registerTypeAdapter(String.class,
                            (JsonDeserializer<String>) (json, typeOfSrc, context) -> {
                                StringBuilder strBuilder = new StringBuilder();
                                if (json.isJsonArray()) {
                                    JsonArray strArr = json.getAsJsonArray();
                                    int size = strArr.size();
                                    for (int i = 0; i < size; i++) {
                                        if (i != 0) {
                                            strBuilder.append("\n");
                                        }
                                        strBuilder.append((String) context.deserialize(strArr.get(i), String.class));
                                    }
                                } else {
                                    strBuilder.append(json.getAsString());
                                }
                                return strBuilder.toString();
                            });
            errorGson = gsonBuilder.create();
        }
        return errorGson;
    }

    protected List<Pair<String, String>> getHeaders() {
        List<Pair<String, String>> headers = new ArrayList<>();
        if (permanentHeaders == null) {
            permanentHeaders = new ArrayList<>(2);
            String packageName = appContext.getPackageName();
            permanentHeaders.add(new Pair<>(HEADER_PACKAGE_NAME, packageName));
            try {
                PackageInfo pInfo = appContext.getPackageManager().getPackageInfo(packageName, 0);
                permanentHeaders.add(new Pair<>(HEADER_APP_VERSION_NAME, pInfo.versionName));
                permanentHeaders.add(new Pair<>(HEADER_APP_VERSION_CODE, getAppVersionCode(pInfo)));
                permanentHeaders.add(new Pair<>("Accept-Encoding", "identity"));//https://github.com/square/okio/issues/299
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        headers.addAll(permanentHeaders);
        return headers;
    }

    protected String getAppVersionCode(PackageInfo pInfo) {
        return String.valueOf(pInfo.versionCode);
    }

    public void clearCache() {
        if (persistentCookieJar != null) {
            persistentCookieJar.clear();
        }
    }
}
