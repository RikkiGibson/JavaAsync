package com.microsoft.azure.storage;

import com.microsoft.azure.storage.blob.HttpAccessConditions;
import com.microsoft.azure.storage.blob.SharedKeyCredentials;
import com.microsoft.azure.v2.credentials.ApplicationTokenCredentials;
import com.microsoft.azure.v2.serializer.AzureJacksonAdapter;
import com.microsoft.azure.storage.implementation.StorageClientImpl;
import com.microsoft.rest.v2.LogLevel;
import com.microsoft.rest.v2.RestClient;
import com.microsoft.rest.v2.RestClient.Builder;
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
        //final File credFile = new File(System.getenv("AZURE_AUTH_LOCATION"));
        QueryStringDecoder decoder = new QueryStringDecoder("/hello?key=ab+cd&key2=josh");
        //final HttpAccessConditions ac = new HttpAccessConditions() {
        //};

        System.setProperty("http.proxyHost", "localhost");
        System.setProperty("http.proxyPort", "8888");

        RestClient config = new Builder()
                //.withCredentials(ApplicationTokenCredentials.fromFile(credFile))
                .withBaseUrl("127.0.0.1")//"http://xclientdev.blob.core.windows.net")
                .withLogLevel(LogLevel.BODY_AND_HEADERS)
                .addRequestPolicy(new SharedKeyCredentials("dfs", "dfs"))
                .addRequestPolicy(new AddDatePolicy.Factory())
                .withSerializerAdapter(new AzureJacksonAdapter())
                .withProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8888)))
                .build();

        //config.httpClient().
        StorageClientImpl client = new StorageClientImpl(config)
                .withAccountName("azstoragetodelete2")
                .withVersion("2016-05-31");

        client.containers().create("javacontainer");
    }
}
