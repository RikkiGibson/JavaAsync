/**
 * Copyright Microsoft Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.microsoft.azure.storage.blob;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public final class UrlParser {

    // UrlParser parses a URL initializing BlobURLParts' fields including any SAS-related & snapshot query parameters. Any other
    // query parameters remain in the UnparsedParams field. This method overwrites all fields in the BlobURLParts object.
    public BlobURLParts ParseURL(String urlString) throws URISyntaxException, MalformedURLException, ParseException, UnsupportedEncodingException {

        URL url = new URL(urlString);

        String scheme = url.getProtocol();
        String host = url.getHost();

        String containerName = null;
        String blobName = null;

        // find the container & blob names (if any)
        String path = url.getPath();
        if (!Utility.isNullOrEmpty(path)) {
            // if the path starts with a slash remove it
            if (path.charAt(0) == '/') {
                path = path.substring(1);
            }


            int containerEndIndex = path.indexOf('/');
            if (containerEndIndex == -1) {
                // path contains only a container name and no blob name
                containerName = path;
            }
            else
            {
                // path contains the container name up until the slash and blob name is everything after the slash
                containerName = path.substring(0, containerEndIndex);
                blobName = path.substring(containerEndIndex + 1);
            }
        }

        Map<String, String[]> queryParamsMap = Utility.parseQueryString(urlString, true);

        Date snapshot = null;
        String[] snapshotArray = queryParamsMap.get("snapshot");
        if (snapshotArray != null) {
            snapshot = Utility.parseDate(snapshotArray[0]);
            queryParamsMap.remove("snapshot");
        }

        SASQueryParameters sasQueryParameters = new SASQueryParameters(queryParamsMap, true);

        return new BlobURLParts(scheme, host, containerName, blobName, snapshot, sasQueryParameters, queryParamsMap);
    }
}
