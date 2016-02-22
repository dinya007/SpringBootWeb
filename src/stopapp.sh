#!/usr/bin/env bash

launchctl unload $JENKINS_HOME/homebrew.mxcl.jenkins.plist
$NEXUS_HOME/bin/./nexus stop