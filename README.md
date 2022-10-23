# Build Switcher Basics

### This plugin was put together relatively quickly and has some somewhat specific requirements.

Saved builds use the items directly from your inventory, no new items are made. This means you can't use saved builds on other accounts.

If you delete the items from your inventory then the build won't load the missing items.

 If you need more than 52 builds then you should optimize your builds :)

## Installation
**Prebuilt JAR:** 
- Get latest BuildSwitcher.jar release from [releases](https://github.com/NotThorny/BuildSwitcher/releases) and place it in your `\grasscutter\plugins` folder.
 
 Restart the server if it was already running.
 
 ## Usage
 
`/set save [a - z]` to save current artifacts

- eg. /set save c
 
`/set load [a - z]` to load saved artifacts

- eg. /set load p

Builds can use a - z for first 26 slots, and a1 - z1 for 26 more slots.

## Issues

None found yet

## TODO

- Store items instead of inventory entries, so that builds can be used across accounts
