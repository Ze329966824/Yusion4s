#!/usr/bin/env bash
adb devices | cut -sf 1 | xargs -I devicesId adb -s devicesId install -r ../app/keystore/apks/alpha-yusion4s-V1.5.0-1122.apk