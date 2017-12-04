package com.microsoft.azure.storage;

import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.implementation.StorageClientImpl;
import com.microsoft.rest.v2.http.HttpClient;
import com.microsoft.rest.v2.http.HttpPipeline;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlobStorageAPITests {
//    static class AddDatePolicy implements RequestPolicy {
//        private final DateTimeFormatter format = DateTimeFormat
//                .forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
//                .withZoneUTC()
//                .withLocale(Locale.US);
//
//        private final RequestPolicy next;
//        public AddDatePolicy(RequestPolicy next) {
//            this.next = next;
//        }
//
//        @Override
//        public Single<HttpResponse> sendAsync(HttpRequest request) {
//            request.headers().set("Date", format.print(DateTime.now()));
//            return next.sendAsync(request);
//        }
//
//        static class Factory implements RequestPolicy.Factory {
//            @Override
//            public RequestPolicy create(RequestPolicy next) {
//                return new AddDatePolicy(next);
//            }
//        }
//    }

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
        StorageClientImpl client = new StorageClientImpl(builder.build());
        client = client.withAccountUrl("http://xclientdev.blob.core.windows.net").withVersion("2016-05-31");
        client.containers().createAsync("http://xclientdev.blob.core.windows.net/autogencontainer");

        //System.setProperty("http.proxyHost", "localhost");
        //System.setProperty("http.proxyPort", "8888");
    }
}
