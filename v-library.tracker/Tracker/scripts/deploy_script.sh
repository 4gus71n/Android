#!/bin/bash

LECTORIUM_DESTINY_PATH=/home/lectorium/deploy/test/webapps/marginalia/tracker/;
LOCAL_APK_FILE=/home/astinx/workspace/android/v-library.tracker/Deploy/;
LOCAL_APK_FILENAME=tracker.apk;

scp -P 4500 $LOCAL_APK_FILE$LOCAL_APK_FILENAME  lectorium@168.96.255.168:$LECTORIUM_DESTINY_PATH$LOCAL_APK_FILENAME;
