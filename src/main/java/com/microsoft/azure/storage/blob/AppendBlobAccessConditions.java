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
 * Access conditions specific to append blobs
 */
public final class AppendBlobAccessConditions {
    private final Long ifAppendPositionEquals;

    private final Long ifMaxSizeLessThanOrEqual;

    /**
     * Creates a {@link AppendBlobAccessConditions} object
     * @param ifAppendPositionEquals
     *      ensures that the AppendBlock operation succeeds only if the append position is equal to a value.
     * @param ifMaxSizeLessThanOrEqual
     *      ensures that the AppendBlock operation succeeds only if the append blob's size is less than or
     *      equal to a value.
     */
    public AppendBlobAccessConditions(Long ifAppendPositionEquals, Long ifMaxSizeLessThanOrEqual) {
        this.ifAppendPositionEquals = ifAppendPositionEquals;
        this.ifMaxSizeLessThanOrEqual = ifMaxSizeLessThanOrEqual;
    }

    /**
     * @return
     *      A <code>Long</code> for ensuring that the AppendBlock operation succeeds only if the append position
     *      is equal to a value.
     */
    public Long getIfAppendPositionEquals() {
        return ifAppendPositionEquals;
    }

    /**
     * @return
     *      A <code>Long</code> for ensuring that the AppendBlock operation succeeds only if the append blob's size
     *      is less than or equal to a value.
     */
    public Long getIfMaxSizeLessThanOrEqual() {
        return ifMaxSizeLessThanOrEqual;
    }
}
