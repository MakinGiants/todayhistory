#!/usr/bin/env bash
#
# Execute random actions over the ui
#

echo "Number of interactions:"
read INPUT_NUMBER

adb shell monkey -p com.makingiants.todayhistory -v INPUT_NUMBER