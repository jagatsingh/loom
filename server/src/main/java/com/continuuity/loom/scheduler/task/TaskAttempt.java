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
package com.continuuity.loom.scheduler.task;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;

/**
 * A task attempt is one run of an action on a node. Attempts can be retried, in which case a {@link ClusterTask} will
 * contain multiple task attempts.
 */
class TaskAttempt {
  private final int id;
  private long submitTime;
  private ClusterTask.Status status;
  private int statusCode;
  private String statusMessage;
  private long statusTime;

  private JsonObject config;

  private ClusterTask rollbackTask;

  TaskAttempt(int id, JsonObject config) {
    this.id = id;
    this.status = ClusterTask.Status.NOT_SUBMITTED;
    this.config = config;
  }

  /**
   * Get the id of the task attempt.
   *
   * @return Id of the task attempt.
   */
  public int getId() {
    return id;
  }

  /**
   * Get the timestamp in milliseconds that the task attempt was submitted.
   *
   * @return Timestamp in milliseconds that the task attempt was submitted.
   */
  public long getSubmitTime() {
    return submitTime;
  }

  /**
   * Set the timestamp in milliseconds when the task attempt was submitted.
   *
   * @param submitTime Timestamp in milliseconds to set when the task attempt was submitted.
   */
  public void setSubmitTime(long submitTime) {
    this.submitTime = submitTime;
  }

  /**
   * Get the status of the task attempt.
   *
   * @return Status of the task attempt.
   */
  public ClusterTask.Status getStatus() {
    return status;
  }

  /**
   * Set the status of the task attempt.
   *
   * @param status Status of the task attempt.
   */
  public void setStatus(ClusterTask.Status status) {
    this.status = status;
  }

  /**
   * Get the status code of the task attempt.
   *
   * @return Status code of the task attempt.
   */
  public int getStatusCode() {
    return statusCode;
  }

  /**
   * Set the status code of the task attempt.
   *
   * @param statusCode Status code to set for the task attempt.
   */
  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  /**
   * Get the configuration to send to the provisioner executing the task attempt.
   *
   * @return Configuration to send to the provisioner executing the task attempt.
   */
  public JsonObject getConfig() {
    return config;
  }

  /**
   * Set the configuration to send to the provisioner executing the task attempt.
   *
   * @param config Configuration to set to send to the provisioner executing the task attempt.
   */
  public void setConfig(JsonObject config) {
    this.config = config;
  }

  /**
   * Get the status message for the task attempt.
   *
   * @return Status message for the task attempt.
   */
  public String getStatusMessage() {
    return statusMessage;
  }

  /**
   * Set the status message for the task attempt.
   *
   * @param statusMessage Status message to set for the task attempt.
   */
  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  /**
   * Get the timestamp in milliseconds of when the status was last updated.
   *
   * @return Timestamp in milliseconds of when the status was last updated.
   */
  public long getStatusTime() {
    return statusTime;
  }

  /**
   * Set the timestamp in milliseconds of when the status was last updated.
   *
   * @param statusTime Timestamp in milliseconds to set of when the status was last updated.
   */
  public void setStatusTime(long statusTime) {
    this.statusTime = statusTime;
  }

  /**
   * Get the rollback task associated with this attempt should the task fail.
   *
   * @return Rollback task associated with this attempt should the task fail.
   */
  public ClusterTask getRollbackTask() {
    return rollbackTask;
  }

  /**
   * Set the rollback task that should run should this task fail.
   *
   * @param rollbackTask Rollback task to run should this task fail.
   */
  public void setRollbackTask(ClusterTask rollbackTask) {
    this.rollbackTask = rollbackTask;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add("id", id)
      .add("submitTime", submitTime)
      .add("status", status)
      .add("statusCode", statusCode)
      .add("statusMessage", statusMessage)
      .add("statusTime", statusTime)
      .add("config", config)
      .add("rollbackTask", rollbackTask)
      .toString();
  }
}
