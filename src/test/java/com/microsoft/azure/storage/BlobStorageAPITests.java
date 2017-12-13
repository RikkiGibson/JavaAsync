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
import io.reactivex.Single;

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

        HttpClient.Configuration configuration = new HttpClient.Configuration(
                new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8888)));
        LoggingOptions loggingOptions = new LoggingOptions(Level.INFO);
        SharedKeyCredentials creds = new SharedKeyCredentials("xclientdev", "key");
        //AnonymousCredentials creds = new AnonymousCredentials();
        //RequestRetryFactory requestRetryFactory = new RequestRetryFactory();
        TelemetryOptions telemetryOptions = new TelemetryOptions();
        AddDatePolicy addDate = new AddDatePolicy();


//        builder.withHttpClient(HttpClient.createDefault(configuration))
//                .withLogger(logger)
//                .withRequestPolicies(requestIDFactory, telemetryFactory, addDate, creds, loggingFactory);
        //StorageClientImpl client = new StorageClientImpl(builder.build());

        PipelineOptions pop = new PipelineOptions();
        pop.logger = logger;
        pop.client = HttpClient.createDefault(configuration);
        pop.loggingOptions = loggingOptions;
        pop.telemetryOptions = telemetryOptions;
        HttpPipeline pipeline = StorageURL.CreatePipeline(creds, pop);
        ContainerURL containerURL = new ContainerURL("http://xclientdev.blob.core.windows.net/newautogencontainerr", pipeline);
        //containerURL.deleteAsync("\"http://xclientdev.blob.core.windows.net/newautogencontainer").toBlocking().value();
        containerURL.createAsync(null, null, null).blockingGet();
        containerURL.getPropertiesAndMetadataAsync(null, null).blockingGet().headers();
        //containerURL.deleteAsync().blockingGet();
    }

}
