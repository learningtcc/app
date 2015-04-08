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

import java.util.Map;
import javax.persistence.TypedQuery;

public class JpaUtils {

  public static final String PERSISTENT_MONEY = "org.tiogasolutions.dev.domain.hibernate.PersistentMoney";

  public static final String PERSISTENT_LOCAL_DATE = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate";
  public static final String PERSISTENT_LOCAL_TIME = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime";
  public static final String PERSISTENT_LOCAL_DATE_TIME = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime";

  public static void updateParameters(TypedQuery<?> query, Map<String, Object> parameters) {
    for (Map.Entry<String,Object> entry : parameters.entrySet()) {
      query.setParameter(entry.getKey(), entry.getValue());
    }
  }
}
