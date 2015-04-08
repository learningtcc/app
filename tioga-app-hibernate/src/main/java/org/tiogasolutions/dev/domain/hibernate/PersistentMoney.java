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

import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.tiogasolutions.dev.domain.money.Money;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.HibernateException;import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;

/**
 * Persist {@link org.tiogasolutions.dev.domain.money.Money} via hibernate.
 */
public class PersistentMoney implements UserType, Serializable {

  public static final PersistentMoney INSTANCE = new PersistentMoney();

  private static final int[] SQL_TYPES = new int[] { Types.DECIMAL };

  @Override
  public int[] sqlTypes() {
      return SQL_TYPES;
  }

  @Override
  public java.lang.Class returnedClass() {
      return Money.class;
  }

  @Override
  public boolean equals(Object x, Object y) throws HibernateException {
    if (x == y) {
        return true;
    }
    if (x == null || y == null) {
        return false;
    }
    Money dtx = (Money) x;
    Money dty = (Money) y;

    return dtx.equals(dty);
  }

  @Override
  public int hashCode(Object object) throws HibernateException {
    return object.hashCode();
  }

  @Override
  public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
    BigDecimal value = (BigDecimal) StandardBasicTypes.BIG_DECIMAL.nullSafeGet(resultSet, names, session, owner);
    if (value == null) {
      return null;
    }
    return new Money(value);
  }

  @Override
  public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
    if (value == null) {
      StandardBasicTypes.BIG_DECIMAL.nullSafeSet(preparedStatement, null, index, session);
    } else {
      Money amount = (Money)value;
      StandardBasicTypes.BIG_DECIMAL.nullSafeSet(preparedStatement, amount.getBigDecimalValue(), index, session);
    }
  }

  @Override
  public Object deepCopy(Object value) throws HibernateException {
    return value;
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public Serializable disassemble(Object value) throws HibernateException {
    return (Serializable) value;
  }

  @Override
  public Object assemble(Serializable cached, Object value) throws HibernateException {
    return cached;
  }

  @Override
  public Object replace(Object original, Object target, Object owner) throws HibernateException {
    return original;
  }
}
