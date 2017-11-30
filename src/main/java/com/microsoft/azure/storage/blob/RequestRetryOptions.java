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

/**
 * Options for retrying requests
 */
public final class RequestRetryOptions {

    /**
     * A {@link RetryPolicyType} telling the pipeline what kind of retry policy to use.
     */
    RetryPolicyType retryPolicyType;

    // MaxTries specifies the maximum number of attempts an operation will be tried before producing an error (0=default).
    // A value of zero means that you accept our default policy. A value of 1 means 1 try and no retries.
    int maxRetries;

    // TryTimeout indicates the maximum time allowed for any single try of an HTTP request.
    // A value of zero means that you accept our default timeout. NOTE: When transferring large amounts
    // of data, the default TryTimeout will probably not be sufficient. You should override this value
    // based on the bandwidth available to the host machine and proximity to the Storage service. A good
    // starting point may be something like (60 seconds per MB of anticipated-payload-size).
    Long tryTimeout;

    // RetryDelay specifies the amount of delay to use before retrying an operation (0=default).
    // The delay increases (exponentially or linearly) with each retry up to a maximum specified by
    // MaxRetryDelay. If you specify 0, then you must also specify 0 for MaxRetryDelay.
    Long retryDelay;

    // MaxRetryDelay specifies the maximum delay allowed before retrying an operation (0=default).
    // If you specify 0, then you must also specify 0 for RetryDelay.
    Long maxRetryDelay;

    // RetryReadsFromSecondaryHost specifies whether the retry policy should retry a read operation against another host.
    // If RetryReadsFromSecondaryHost is "" (the default) then operations are not retried against another host.
    // NOTE: Before setting this field, make sure you understand the issues around reading stale & potentially-inconsistent
    // data at this webpage: https://docs.microsoft.com/en-us/azure/storage/common/storage-designing-ha-apps-with-ragrs
    Long retryReadsFromSecondaryHost;
}
