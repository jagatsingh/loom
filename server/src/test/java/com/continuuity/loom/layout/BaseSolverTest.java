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
package com.continuuity.loom.layout;

import com.continuuity.loom.BaseTest;
import com.continuuity.loom.Entities;
import com.continuuity.loom.admin.Administration;
import com.continuuity.loom.admin.ClusterDefaults;
import com.continuuity.loom.admin.ClusterTemplate;
import com.continuuity.loom.admin.Compatibilities;
import com.continuuity.loom.admin.Constraints;
import com.continuuity.loom.admin.HardwareType;
import com.continuuity.loom.admin.ImageType;
import com.continuuity.loom.admin.LayoutConstraint;
import com.continuuity.loom.admin.Provider;
import com.continuuity.loom.admin.Service;
import com.continuuity.loom.admin.ServiceConstraint;
import com.continuuity.loom.codec.json.JsonSerde;
import com.continuuity.utils.ImmutablePair;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.BeforeClass;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class BaseSolverTest extends BaseTest {
  protected static ClusterTemplate reactorTemplate;
  protected static ClusterTemplate reactorTemplate2;

  @BeforeClass
  public static void setupBaseSolverTests() throws Exception {

    Set<String> services = ImmutableSet.of("namenode", "datanode", "resourcemanager", "nodemanager",
                                           "hbasemaster", "regionserver", "zookeeper", "reactor");
    reactorTemplate = new ClusterTemplate(
      "reactor-medium",
      "medium reactor cluster template",
      new ClusterDefaults(services, "joyent", null, null, Entities.ClusterTemplateExample.clusterConf),
      new Compatibilities(null, null, services),
      new Constraints(
        ImmutableMap.<String, ServiceConstraint>of(
          "namenode",
          new ServiceConstraint(
            ImmutableSet.of("large-mem"),
            ImmutableSet.of("centos6", "ubuntu12"), 1, 1, 1, null),
          "datanode",
          new ServiceConstraint(
            ImmutableSet.of("medium", "large-cpu"),
            ImmutableSet.of("centos6", "ubuntu12"), 1, 50, 1, null),
          "zookeeper",
          new ServiceConstraint(
            ImmutableSet.of("small", "medium"),
            ImmutableSet.of("centos6"), 1, 5, 2, ImmutablePair.of(1, 20)),
          "reactor",
          new ServiceConstraint(
            ImmutableSet.of("medium", "large"),
            null, 1, 5, 1, ImmutablePair.of(1, 10))
        ),
        new LayoutConstraint(
          ImmutableSet.<Set<String>>of(
            ImmutableSet.of("datanode", "nodemanager", "regionserver"),
            ImmutableSet.of("namenode", "resourcemanager", "hbasemaster")
          ),
          ImmutableSet.<Set<String>>of(
            ImmutableSet.of("datanode", "namenode"),
            ImmutableSet.of("datanode", "zookeeper"),
            ImmutableSet.of("namenode", "zookeeper"),
            ImmutableSet.of("datanode", "reactor"),
            ImmutableSet.of("namenode", "reactor")
          )
        )
      ),
      Administration.EMPTY_ADMINISTRATION
    );
    reactorTemplate2 =
      new JsonSerde().getGson().fromJson(Entities.ClusterTemplateExample.REACTOR2_STRING, ClusterTemplate.class);

    // create providers
    entityStore.writeProvider(new Provider("joyent", "joyent provider", Provider.Type.JOYENT, Collections.EMPTY_MAP));
    // create hardware types
    entityStore.writeHardwareType(
      new HardwareType(
        "small",
        "small hardware",
        ImmutableMap.<String, Map<String, String>>of("joyent", ImmutableMap.<String, String>of("flavor", "Small 2GB"))
      )
    );
    entityStore.writeHardwareType(
      new HardwareType(
        "medium",
        "medium hardware",
        ImmutableMap.<String, Map<String, String>>of("joyent", ImmutableMap.<String, String>of("flavor", "Medium 4GB"))
      )
    );
    entityStore.writeHardwareType(
      new HardwareType(
        "large-mem",
        "hardware with a lot of memory",
        ImmutableMap.<String, Map<String, String>>of("joyent", ImmutableMap.<String, String>of("flavor", "Large 32GB"))
      )
    );
    entityStore.writeHardwareType(
      new HardwareType(
        "large-cpu",
        "hardware with a lot of cpu",
        ImmutableMap.<String, Map<String, String>>of("joyent", ImmutableMap.<String, String>of("flavor", "Large 16GB"))
      )
    );
    // create image types
    entityStore.writeImageType(
      new ImageType(
        "centos6",
        "CentOS 6.4 image",
        ImmutableMap.<String, Map<String, String>>of(
          "joyent", ImmutableMap.<String, String>of("image", "joyent-hash-of-centos6.4"))
      )
    );
    entityStore.writeImageType(
      new ImageType(
        "ubuntu12",
        "Ubuntu 12 image",
        ImmutableMap.<String, Map<String, String>>of(
          "joyent", ImmutableMap.<String, String>of("image", "joyent-hash-of-ubuntu12"))
      )
    );
    // create services
    entityStore.writeService(new Service("namenode", "", ImmutableSet.<String>of(), Collections.EMPTY_MAP));
    entityStore.writeService(new Service("datanode", "", ImmutableSet.<String>of("namenode"), Collections.EMPTY_MAP));
    entityStore.writeService(new Service("resourcemanager", "", ImmutableSet.<String>of("datanode"), Collections.EMPTY_MAP));
    entityStore.writeService(new Service(
      "nodemanager", "", ImmutableSet.<String>of("resourcemanager"), Collections.EMPTY_MAP));
    entityStore.writeService(new Service(
      "hbasemaster", "", ImmutableSet.<String>of("zookeeper", "datanode"), Collections.EMPTY_MAP));
    entityStore.writeService(new Service("regionserver", "", ImmutableSet.<String>of("hbasemaster"), Collections.EMPTY_MAP));
    entityStore.writeService(new Service("zookeeper", "", ImmutableSet.<String>of(), Collections.EMPTY_MAP));
    entityStore.writeService(new Service(
      "reactor", "", ImmutableSet.<String>of("zookeeper", "regionserver", "nodemanager"), Collections.EMPTY_MAP));
    entityStore.writeClusterTemplate(reactorTemplate);
  }
}
