/*
 *  Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.base.seed.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zz 2019-04-08
 */
public class NamedThreadFactory implements ThreadFactory {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final String prefix;
    private final int totalSize;
    private final boolean makeDaemons;
    private final ThreadGroup group;

    /**
     * Instantiates a new Named thread factory.
     *
     * @param prefix      the prefix
     * @param totalSize   the total size
     * @param makeDaemons the make daemons
     */
    public NamedThreadFactory(String prefix, int totalSize, boolean makeDaemons) {
        this.prefix = prefix;
        this.makeDaemons = makeDaemons;
        this.totalSize = totalSize;

        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
    }

    /**
     * Instantiates a new Named thread factory.
     *
     * @param prefix    the prefix
     * @param totalSize the total size
     */
    public NamedThreadFactory(String prefix, int totalSize) {
        this(prefix, totalSize, true);
    }

    @Override
    public Thread newThread(Runnable r) {
        String name = prefix + "_" + counter.incrementAndGet();
        if (totalSize > 1) {
            name += "_" + totalSize;
        }
        Thread thread = new Thread(group, r,
                name, 0);

        thread.setDaemon(makeDaemons);
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
