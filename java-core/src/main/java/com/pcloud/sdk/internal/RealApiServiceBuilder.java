/*
 * Copyright (c) 2017 pCloud AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pcloud.sdk.internal;

import com.pcloud.sdk.ApiClient;
import com.pcloud.sdk.Authenticator;
import okhttp3.*;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

class RealApiServiceBuilder implements ApiClient.Builder {

    private Cache cache;
    private Executor callbackExecutor;
    private ConnectionPool connectionPool;
    private Dispatcher dispatcher;
    private int readTimeoutMs;
    private int writeTimeoutMs;
    private int connectTimeoutMs;
    private long progressCallbackThresholdBytes;
    private String apiServerHostname;
    private Authenticator authenticator;

    RealApiServiceBuilder(OkHttpClient okHttpClient, Executor callbackExecutor, long progressCallbackThresholdBytes, Authenticator authenticator) {
        this.cache = okHttpClient.cache();
        this.callbackExecutor = callbackExecutor;
        this.connectionPool = okHttpClient.connectionPool();
        this.dispatcher = okHttpClient.dispatcher();
        this.readTimeoutMs = okHttpClient.readTimeoutMillis();
        this.writeTimeoutMs = okHttpClient.writeTimeoutMillis();
        this.connectTimeoutMs = okHttpClient.connectTimeoutMillis();
        this.progressCallbackThresholdBytes = progressCallbackThresholdBytes;
        this.authenticator = authenticator;
    }

    RealApiServiceBuilder() {
    }

    @Override
    public ApiClient.Builder cache(Cache cache) {
        this.cache = cache;
        return this;
    }

    @Override
    public ApiClient.Builder connectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        return this;
    }

    @Override
    public ApiClient.Builder dispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        return this;
    }

    @Override
    public ApiClient.Builder withClient(OkHttpClient client) {
        return cache(client.cache())
                .connectionPool(client.connectionPool())
                .dispatcher(client.dispatcher())
                .readTimeout(client.readTimeoutMillis(), TimeUnit.MILLISECONDS)
                .writeTimeout(client.writeTimeoutMillis(), TimeUnit.MILLISECONDS)
                .connectTimeout(client.connectTimeoutMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public ApiClient.Builder readTimeout(long timeout, TimeUnit timeUnit) {
        this.readTimeoutMs = (int) timeUnit.toMillis(timeout);
        return this;
    }

    @Override
    public ApiClient.Builder writeTimeout(long timeout, TimeUnit timeUnit) {
        this.writeTimeoutMs = (int) timeUnit.toMillis(timeout);
        return this;
    }

    @Override
    public ApiClient.Builder connectTimeout(long timeout, TimeUnit timeUnit) {
        this.connectTimeoutMs = (int) timeUnit.toMillis(timeout);
        return this;
    }

    @Override
    public ApiClient.Builder authenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
        return this;
    }

    @Override
    public ApiClient.Builder callbackExecutor(Executor callbackExecutor) {
        this.callbackExecutor = callbackExecutor;
        return this;
    }

    @Override
    public ApiClient.Builder progressCallbackThreshold(long bytes) {
        if (bytes < 0) {
            throw new IllegalArgumentException("Threshold parameter must a positive number.");
        }
        this.progressCallbackThresholdBytes = bytes;
        return this;
    }

    @Override
    public ApiClient.Builder apiServerHostname(String hostname) {
        this.apiServerHostname = hostname;
        return this;
    }

    @Override
    public ApiClient create() {
        return new RealApiClient(this);
    }

    public Cache cache() {
        return cache;
    }

    public Executor callbackExecutor() {
        return callbackExecutor;
    }

    public ConnectionPool connectionPool() {
        return connectionPool;
    }

    public Dispatcher dispatcher() {
        return dispatcher;
    }

    public int readTimeoutMs() {
        return readTimeoutMs;
    }

    public int writeTimeoutMs() {
        return writeTimeoutMs;
    }

    public int connectTimeoutMs() {
        return connectTimeoutMs;
    }

    public long progressCallbackThresholdBytes() {
        return progressCallbackThresholdBytes;
    }

    public String apiServerHostname() {
        return apiServerHostname;
    }

    public Authenticator authenticator() {
        return authenticator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RealApiServiceBuilder builder = (RealApiServiceBuilder) o;

        if (readTimeoutMs != builder.readTimeoutMs) return false;
        if (writeTimeoutMs != builder.writeTimeoutMs) return false;
        if (connectTimeoutMs != builder.connectTimeoutMs) return false;
        if (progressCallbackThresholdBytes != builder.progressCallbackThresholdBytes) return false;
        if (cache != null ? !cache.equals(builder.cache) : builder.cache != null) return false;
        if (callbackExecutor != null ? !callbackExecutor.equals(builder.callbackExecutor) : builder.callbackExecutor != null)
            return false;
        if (connectionPool != null ? !connectionPool.equals(builder.connectionPool) : builder.connectionPool != null)
            return false;
        if (dispatcher != null ? !dispatcher.equals(builder.dispatcher) : builder.dispatcher != null) return false;
        return authenticator != null ? authenticator.equals(builder.authenticator) : builder.authenticator == null;

    }

    @Override
    public int hashCode() {
        int result = cache != null ? cache.hashCode() : 0;
        result = 31 * result + (callbackExecutor != null ? callbackExecutor.hashCode() : 0);
        result = 31 * result + (connectionPool != null ? connectionPool.hashCode() : 0);
        result = 31 * result + (dispatcher != null ? dispatcher.hashCode() : 0);
        result = 31 * result + readTimeoutMs;
        result = 31 * result + writeTimeoutMs;
        result = 31 * result + connectTimeoutMs;
        result = 31 * result + (int) (progressCallbackThresholdBytes ^ (progressCallbackThresholdBytes >>> 32));
        result = 31 * result + (authenticator != null ? authenticator.hashCode() : 0);
        return result;
    }
}
