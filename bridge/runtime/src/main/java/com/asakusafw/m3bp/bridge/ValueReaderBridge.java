/**
 * Copyright 2011-2017 Asakusa Framework Team.
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
package com.asakusafw.m3bp.bridge;

import java.io.IOException;

import com.asakusafw.dag.api.common.Deserializer;
import com.asakusafw.dag.api.processor.ObjectReader;
import com.asakusafw.lang.utils.common.Arguments;
import com.asakusafw.m3bp.mirror.InputReaderMirror;
import com.asakusafw.m3bp.mirror.PageDataInput;

/**
 * M3BP bridge implementation of {@link ObjectReader}.
 */
public class ValueReaderBridge implements ObjectReader {

    private final InputReaderMirror reader;

    private final PageDataInput values;

    private final Deserializer deserialzier;

    private Object next = null;

    /**
     * Creates a new instance.
     * @param reader the reader
     * @param deserializer the deserializer
     */
    public ValueReaderBridge(InputReaderMirror reader, Deserializer deserializer) {
        Arguments.requireNonNull(reader);
        Arguments.requireNonNull(deserializer);
        this.reader = reader;
        this.values = reader.getValueInput();
        this.deserialzier = deserializer;
    }

    @Override
    public boolean nextObject() throws IOException, InterruptedException {
        if (values.next()) {
            next = deserialzier.deserialize(values);
            return true;
        } else {
            next = null;
            return false;
        }
    }

    @Override
    public Object getObject() throws IOException, InterruptedException {
        assert next != null;
        return next;
    }

    @Override
    public void close() throws IOException, InterruptedException {
        reader.close();
    }
}
