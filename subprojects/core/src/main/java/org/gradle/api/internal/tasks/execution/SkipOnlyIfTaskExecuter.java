/*
 * Copyright 2010 the original author or authors.
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

package org.gradle.api.internal.tasks.execution;

import org.gradle.api.GradleException;
import org.gradle.api.internal.TaskInternal;
import org.gradle.api.internal.tasks.TaskExecuter;
import org.gradle.api.internal.tasks.TaskExecuterResult;
import org.gradle.api.internal.tasks.TaskExecutionContext;
import org.gradle.api.internal.tasks.TaskExecutionOutcome;
import org.gradle.api.internal.tasks.TaskStateInternal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link org.gradle.api.internal.tasks.TaskExecuter} which skips tasks whose onlyIf predicate evaluates to false
 */
public class SkipOnlyIfTaskExecuter implements TaskExecuter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SkipOnlyIfTaskExecuter.class);
    private final TaskExecuter executer;

    public SkipOnlyIfTaskExecuter(TaskExecuter executer) {
        this.executer = executer;
    }

    @Override
    public TaskExecuterResult execute(TaskInternal task, TaskStateInternal state, TaskExecutionContext context) {
        SelfDescribingSpec<? super TaskInternal> unsatisfiedSpec;
        try {
            unsatisfiedSpec = task.getOnlyIf().findUnsatisfiedSpec(task);
        } catch (Throwable t) {
            state.setOutcome(new GradleException(String.format("Could not evaluate onlyIf predicate for %s.", task), t));
            return TaskExecuterResult.WITHOUT_OUTPUTS;
        }

        if (unsatisfiedSpec != null) {
            LOGGER.info("Skipping {} as task onlyIf '{}' is false.", task, unsatisfiedSpec.getDisplayName());
            state.setOutcome(TaskExecutionOutcome.SKIPPED);
            state.setSkipReasonMessage("'" + unsatisfiedSpec.getDisplayName() + "' not satisfied");
            return TaskExecuterResult.WITHOUT_OUTPUTS;
        }

        return executer.execute(task, state, context);
    }
}
