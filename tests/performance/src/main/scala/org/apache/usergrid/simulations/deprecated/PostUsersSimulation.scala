/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.usergrid.simulations.deprecated

import io.gatling.core.Predef._
import org.apache.usergrid.helpers.Setup
import org.apache.usergrid.scenarios.UserScenarios
import org.apache.usergrid.settings.Settings

/**
 * Posts application users continually to an application.  Expects the following parameters
 *
 * -DmaxPossibleUsers : The maximum number of users to be making requests as fast as possible.  Think of this as conccurrent users in the system
 * -DrampTime: The amount of time (in seconds) to allow for maxPossibleUsers to be reached.  This will add new users linearlly
 * -Dduration: The amount of time (in seconds) to continue to perform requests up with the maxPossibleUsers
 */
class PostUsersSimulation extends Simulation {

  println("Begin setup")
  Setup.setupOrg()
  Setup.setupApplication()
  println("End Setup")


  setUp(
    UserScenarios.postUsersInfinitely
      .inject(
        /**
         * injection steps take from this forum post
         * https://groups.google.com/forum/#!topic/gatling/JfYHaWCbA-w
         */
        rampUsers(Settings.rampUsers) over Settings.rampTime,
        constantUsersPerSec(Settings.constantUsersPerSec) during Settings.constantUsersDuration

      )).protocols(Settings.httpAppConf.acceptHeader("application/json"))

}
