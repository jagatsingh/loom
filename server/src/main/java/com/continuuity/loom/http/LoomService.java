/*
 * Copyright 2012-2014, Continuuity, Inc.
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
package com.continuuity.loom.http;

import com.continuuity.http.HttpHandler;
import com.continuuity.http.NettyHttpService;
import com.continuuity.loom.conf.Constants;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * Netty Loom service.
 */
public class LoomService extends AbstractIdleService {

  private static final Logger LOG  = LoggerFactory.getLogger(LoomService.class);
  private final NettyHttpService httpService;

  @Inject
  public LoomService(@Named("loom.host") String host, @Named("loom.port") int port, Set<HttpHandler> handlers,
                     @Named(Constants.NETTY_EXEC_NUM_THREADS) int nettyExecNumThreads,
                     @Named(Constants.NETTY_WORKER_NUM_THREADS) int nettyWorkerNumThreads) {
    NettyHttpService.Builder builder = NettyHttpService.builder();
    builder.addHttpHandlers(handlers);

    builder.setHost(host);
    builder.setPort(port);

    builder.setConnectionBacklog(20000);
    builder.setExecThreadPoolSize(nettyExecNumThreads);
    builder.setBossThreadPoolSize(1);
    builder.setWorkerThreadPoolSize(nettyWorkerNumThreads);

    this.httpService = builder.build();
  }

  @Override
  protected void startUp() throws Exception {
    httpService.startAndWait();
    LOG.info("Loom started successfully on {}", httpService.getBindAddress());
  }

  @Override
  protected void shutDown() throws Exception {
    httpService.stopAndWait();
  }

  /**
   * Get the address the service has bound to.
   *
   * @return Address the service has bound to.
   */
  public InetSocketAddress getBindAddress() {
    return httpService.getBindAddress();
  }
}
