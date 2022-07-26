/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.jvm.toolchain.internal;

import org.gradle.api.toolchain.management.JavaToolchainRepositoryRegistration;
import org.gradle.jvm.toolchain.JavaToolchainRepository;
import org.gradle.jvm.toolchain.JavaToolchainRepositoryRegistry;

import java.util.List;

public interface JavaToolchainRepositoryRegistryInternal extends JavaToolchainRepositoryRegistry {

    void request(String... registrationNames);

    void request(JavaToolchainRepositoryRegistration... registrations);

    boolean hasExplicitRequests();

    List<JavaToolchainRepository> requestedRepositories();

}
