package com.microsoft.azure.storage;

import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.implementation.StorageClientImpl;
import com.microsoft.rest.v2.http.HttpClient;
import com.microsoft.rest.v2.http.HttpPipeline;
import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.policy.RequestPolicy;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import rx.Single;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.microsoft.azure.storage.blob.Utility.getGMTTime;

public class BlobStorageAPITests {
    static class AddDatePolicy implements RequestPolicy.Factory {

        @Override
        public RequestPolicy create(RequestPolicy next, RequestPolicy.Options options) {
            return new AddDate(next);
        }

        public final class AddDate implements RequestPolicy {
            private final DateTimeFormatter format = DateTimeFormat
                    .forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
                    .withZoneUTC()
                    .withLocale(Locale.US);

            private final RequestPolicy next;
            public AddDate(RequestPolicy next) {
                this.next = next;
            }

            @Override
            public Single<HttpResponse> sendAsync(HttpRequest request) {
                request.headers().set(Constants.HeaderConstants.DATE, getGMTTime(new Date()));
                return this.next.sendAsync(request);
            }
        }
    }

    @Test
    public void testBasic() throws Exception {

        HttpPipeline.Logger logger = new HttpPipeline.Logger() {
            @Override
            public HttpPipeline.LogLevel minimumLogLevel() {
                return HttpPipeline.LogLevel.INFO;
            }

            @Override
            public void log(HttpPipeline.LogLevel logLevel, String s, Object... objects) {
                if (logLevel == HttpPipeline.LogLevel.INFO) {
                    Logger.getGlobal().info(String.format(s, objects));
                }
                else if (logLevel == HttpPipeline.LogLevel.WARNING) {
                    Logger.getGlobal().warning(String.format(s, objects));
                }
                else if (logLevel == HttpPipeline.LogLevel.ERROR) {
                    Logger.getGlobal().severe(String.format(s, objects));
                }
            }
        };
        HttpPipeline.Builder builder = new HttpPipeline.Builder();
        HttpClient.Configuration configuration = new HttpClient.Configuration(
                new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8888)));
        RequestLoggingOptions loggingOptions = new RequestLoggingOptions(Level.INFO);
        LoggingFactory loggingFactory = new LoggingFactory(loggingOptions);
        SharedKeyCredentials creds = new SharedKeyCredentials("xclientdev", "key");
        //AnonymousCredentials creds = new AnonymousCredentials();
        RequestIDFactory requestIDFactory = new RequestIDFactory();
        //RequestRetryFactory requestRetryFactory = new RequestRetryFactory();
        TelemetryOptions telemetryOptions = new TelemetryOptions();
        TelemetryFactory telemetryFactory = new TelemetryFactory(telemetryOptions);
        AddDatePolicy addDate = new AddDatePolicy();
        builder.withHttpClient(HttpClient.createDefault(configuration))
                .withLogger(logger)
                .withRequestPolicy(requestIDFactory)
                .withRequestPolicy(telemetryFactory)
                .withRequestPolicy(addDate)
                .withRequestPolicy(creds)
                .withRequestPolicy(loggingFactory);
        StorageClientImpl client = new StorageClientImpl(builder.build());
        client = client.withVersion("2016-05-31");
        client.containers().createAsync("http://xclientdev.blob.core.windows.net/newautogencontainerr").toBlocking().value();

        //System.setProperty("http.proxyHost", "localhost");
        //System.setProperty("http.proxyPort", "8888");
    }
}
