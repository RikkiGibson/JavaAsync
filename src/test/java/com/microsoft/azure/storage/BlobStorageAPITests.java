package com.microsoft.azure.storage;

import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.implementation.AzureBlobStorageImpl;
import com.microsoft.azure.v2.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.v2.serializer.AzureJacksonAdapter;
import com.microsoft.azure.storage.implementation.StorageClientImpl;
import com.microsoft.rest.v2.LogLevel;
import com.microsoft.rest.v2.RestClient;
import com.microsoft.rest.v2.RestClient.Builder;
import com.microsoft.rest.v2.http.HttpClient;
import com.microsoft.rest.v2.http.HttpPipeline;
import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.policy.RequestPolicy;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import rx.Single;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlobStorageAPITests {
    static class AddDatePolicy implements RequestPolicy {
        private final DateTimeFormatter format = DateTimeFormat
                .forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
                .withZoneUTC()
                .withLocale(Locale.US);

        private final RequestPolicy next;
        public AddDatePolicy(RequestPolicy next) {
            this.next = next;
        }

        @Override
        public Single<HttpResponse> sendAsync(HttpRequest request) {
            request.headers().set("Date", format.print(DateTime.now()));
            return next.sendAsync(request);
        }

        static class Factory implements RequestPolicy.Factory {
            @Override
            public RequestPolicy create(RequestPolicy next) {
                return new AddDatePolicy(next);
            }
        }
    }

    @Test
    public void testBasic() throws Exception {

        HttpPipeline.Logger logger = new HttpPipeline.Logger() {
            @Override
            public void log(String message) {
                Logger.getGlobal().info(message);
            }
        };
        HttpPipeline.Builder builder = new HttpPipeline.Builder();
        HttpClient.Configuration configuration = new HttpClient.Configuration(
                new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8888)));
        RequestLoggingOptions loggingOptions = new RequestLoggingOptions(Level.INFO);
        LoggingFactory loggingFactory = new LoggingFactory(loggingOptions);
        SharedKeyCredentials creds = new SharedKeyCredentials("xclientdev", "key");
        RequestIDFactory requestIDFactory = new RequestIDFactory();
        //RequestRetryFactory requestRetryFactory = new RequestRetryFactory();
        TelemetryOptions telemetryOptions = new TelemetryOptions();
        TelemetryFactory telemetryFactory = new TelemetryFactory(telemetryOptions);
        builder.withHttpClient(HttpClient.createDefault(configuration))
                .withLogger(logger)
                .withRequestPolicy(loggingFactory)
                .withRequestPolicy(creds)
                .withRequestPolicy(requestIDFactory)
                .withRequestPolicy(telemetryFactory);
        AzureBlobStorageImpl client = new AzureBlobStorageImpl(builder.build());
        client = client.withAccountUrl("http://xclientdev.blob.core.windows.net").withVersion("2016-05-31");
        client.containers().createAsync();

        //System.setProperty("http.proxyHost", "localhost");
        //System.setProperty("http.proxyPort", "8888");
    }
}
