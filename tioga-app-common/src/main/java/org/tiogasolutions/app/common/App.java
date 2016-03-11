package org.tiogasolutions.app.common;

import org.tiogasolutions.app.common.status.AppInfo;
import org.tiogasolutions.app.common.status.ChangeAppStatus;
import org.tiogasolutions.app.common.status.ChangeAppTestUser;
import org.tiogasolutions.app.common.status.AppStatus;

public interface App {

  AppInfo shutdown();

  AppInfo getAppInfo();

  AppInfo execute(ChangeAppStatus changeAppStatus);
  AppInfo execute(ChangeAppTestUser changeAppTestUser);
}
