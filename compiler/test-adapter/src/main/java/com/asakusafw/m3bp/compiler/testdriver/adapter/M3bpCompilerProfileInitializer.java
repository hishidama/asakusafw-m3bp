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
package com.asakusafw.m3bp.compiler.testdriver.adapter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.asakusafw.lang.compiler.common.Location;
import com.asakusafw.lang.compiler.extension.redirector.RedirectorParticipant;
import com.asakusafw.lang.compiler.testdriver.adapter.CompilerProfileInitializer;
import com.asakusafw.lang.compiler.tester.CompilerProfile;
import com.asakusafw.m3bp.compiler.common.M3bpTask;
import com.asakusafw.testdriver.compiler.CompilerConfiguration;

/**
 * {@link CompilerProfileInitializer} for M3BP.
 * @since 0.1.0
 * @version 0.2.0
 */
public class M3bpCompilerProfileInitializer implements CompilerProfileInitializer {

    private static final Map<String, String> REDIRECT_MAP;
    static {
        Map<String, String> map = new LinkedHashMap<>();
        putRedirect(map,
                "com.asakusafw.runtime.core.BatchContext", //$NON-NLS-1$
                "com.asakusafw.m3bp.custom.M3bpBatchContext"); //$NON-NLS-1$
        putRedirect(map,
                "com.asakusafw.runtime.core.Report", //$NON-NLS-1$
                "com.asakusafw.bridge.api.Report"); //$NON-NLS-1$
        putRedirect(map,
                "com.asakusafw.runtime.directio.api.DirectIo", //$NON-NLS-1$
                "com.asakusafw.bridge.directio.api.DirectIo"); //$NON-NLS-1$
        REDIRECT_MAP = Collections.unmodifiableMap(map);
    }

    private static void putRedirect(Map<String, String> target, String from, String to) {
        target.put(RedirectorParticipant.KEY_RULE_PREFIX + from, to);
    }

    @Override
    public Collection<Location> getLauncherPaths() {
        return Arrays.asList(M3bpTask.PATH_COMMAND);
    }

    @Override
    public void initialize(CompilerProfile profile, CompilerConfiguration configuration) {
        installOptions(REDIRECT_MAP, profile, configuration);
    }

    private static void installOptions(
            Map<String, String> options,
            CompilerProfile profile,
            CompilerConfiguration configuration) {
        options.forEach((k, v) -> {
            if (configuration.getOptions().containsKey(k) == false) {
                profile.forCompilerOptions().withProperty(k, v);
            }
        });
    }
}
