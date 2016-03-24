package org.tiogasolutions.app.standard.execution;

/**
 * User: Harlan
 * Date: 2/9/2015
 * Time: 11:21 PM
 */
public interface ExecutionAccessor<T> {

  /** Indicates if a context is bound to the current context */
  boolean isContextSet();

  /** Returns the current context bound to the current thread. */
  ExecutionContext<T> getContext();
}
