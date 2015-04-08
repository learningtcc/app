/*
 * Copyright 2012 Jacob D Parr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tiogasolutions.dev.domain.hibernate;

import org.jadira.usertype.dateandtime.joda.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JpaUtilsTest {

  @Test
  public void testStatics() throws Exception {
    Assert.assertEquals(JpaUtils.PERSISTENT_MONEY, PersistentMoney.class.getName());

    Assert.assertEquals(JpaUtils.PERSISTENT_LOCAL_DATE, PersistentLocalDate.class.getName());
    Assert.assertEquals(JpaUtils.PERSISTENT_LOCAL_TIME, PersistentLocalTime.class.getName());
    Assert.assertEquals(JpaUtils.PERSISTENT_LOCAL_DATE_TIME, PersistentLocalDateTime.class.getName());
  }
}
