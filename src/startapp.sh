#!/usr/bin/env bash

launchctl load $JENKINS_HOME/homebrew.mxcl.jenkins.plist
$NEXUS_HOME/bin/./nexus start