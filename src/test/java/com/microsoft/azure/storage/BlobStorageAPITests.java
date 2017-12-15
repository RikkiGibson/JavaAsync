package com.microsoft.azure.storage;

import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.implementation.StorageClientImpl;
import com.microsoft.azure.storage.models.ContainerCreateHeaders;
import com.microsoft.rest.v2.RestResponse;
import com.microsoft.rest.v2.http.*;
import com.microsoft.rest.v2.policy.RequestPolicy;
import com.microsoft.rest.v2.policy.RequestPolicyFactory;
import com.microsoft.rest.v2.policy.RequestPolicyOptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import io.reactivex.Single;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.microsoft.azure.storage.blob.Utility.getGMTTime;

public class BlobStorageAPITests {
    static class AddDatePolicy implements RequestPolicyFactory {

        @Override
        public RequestPolicy create(RequestPolicy next, RequestPolicyOptions options) {
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

        HttpPipelineLogger logger = new HttpPipelineLogger() {
            @Override
            public HttpPipelineLogLevel minimumLogLevel() {
                return HttpPipelineLogLevel.INFO;
            }

            @Override
            public void log(HttpPipelineLogLevel logLevel, String s, Object... objects) {
                if (logLevel == HttpPipelineLogLevel.INFO) {
                    Logger.getGlobal().info(String.format(s, objects));
                }
                else if (logLevel == HttpPipelineLogLevel.WARNING) {
                    Logger.getGlobal().warning(String.format(s, objects));
                }
                else if (logLevel == HttpPipelineLogLevel.ERROR) {
                    Logger.getGlobal().severe(String.format(s, objects));
                }
            }
        };

        HttpClient.Configuration configuration = new HttpClient.Configuration(
                new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8888)));
        LoggingOptions loggingOptions = new LoggingOptions(Level.INFO);
        SharedKeyCredentials creds = new SharedKeyCredentials("xclientfileencryption", "key");
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
        final ContainerURL containerURL = new ContainerURL("http://xclientfileencryption.blob.core.windows.net/" + generateRandomContainerName(), pipeline);
        //containerURL.deleteAsync(null, null).blockingGet();
        //containerURL.createAsync(null, null, null).blockingGet();
        //containerURL.getPropertiesAndMetadataAsync(null, null).blockingGet().headers();

        final CountDownLatch latch = new CountDownLatch(1);
        final boolean valid = true;
        containerURL.createAsync(null, null, null)
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // check if error is something other than container exists
                        //if (throwable.getCause() != null)
                            //valid = false;
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (!valid) {
                            latch.countDown();
                            return;
                        }

                        containerURL.deleteAsync(null, null).doFinally(
                                new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        latch.countDown();
                                    }
                                }
                        );
                    }
                }).subscribe();

        latch.await();
    }

    public static String generateRandomContainerName() {
        String containerName = "container" + UUID.randomUUID().toString();
        return containerName.replace("-", "");
    }
}
