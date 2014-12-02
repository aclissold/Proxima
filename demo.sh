#!/bin/bash

# case $@ in
#     kresge)
#         echo k
#         ;;
#     ec)
#         echo e
#         ;;
#     dodge)
#         echo d
#         ;;
#     elliot)
#         echo e
#         ;;
# esac

# Library
echo Emulating Kresge…
adb emu geo fix -83.2157830 42.6729790
adb emu geo fix -83.2157830 42.6729790
echo Pan around the map!
echo Zoom out before emulating EC.
read

# EC
echo Emulating the Engineering Center…
adb emu geo fix -83.21542 42.6716
echo What happened here? Requires investigation and commentation!
read

echo '(Enable undiscovered filter.)'
echo Zoom out before emulating Dodge.
read

# Dodge
echo Emulating Dodge Hall…
adb emu geo fix -83.2162 42.6716
echo 'My selfie senses are tingling! Must investigate.'
echo '(Meanwhile, Rachel takes a nearby photo, triggering a notification…)'
read

echo '(Reset the filter and take a look at the new cluster.)'
echo Zoom out before emulating Elliot Tower.
read

# Elliot Tower
echo Emulating Elliot Tower…
adb emu geo fix -83.2151 42.6732
echo "Another cluster! Let's investigate."
echo Show New indication and Discovered indication.
echo Zoom out before emulating Dodge.
read

echo Back to Dodge Hall to take my first photo!
adb emu geo fix -83.2162 42.6716
read

echo "Let's check it out in my profile!"
echo '(Pause for more comments.)'
read

echo Look at my improved profile!
read

echo Happy Proximating!
read
