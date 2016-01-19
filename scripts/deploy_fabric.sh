#!/usr/bin/env bash
#
# Assemble the app, backup it and upload the apks to crashlytics
#

sh gradlew crashlyticsUploadDistributionProdDebug --daemon