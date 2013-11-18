mkdir -p bin

javac -d bin -cp .:packages/commons-logging-1.1.1.jar:packages/dom4j.jar:packages/gis2.jar:packages/handy.jar:packages/human.jar:packages/ignition.jar:packages/jaxen-1.1.1.jar:packages/jcommon-1.0.16.jar:packages/jfreechart-1.0.13.jar:packages/jscience-4.3.jar:packages/jsi-1.0b2p1.jar:packages/jts-1.11.jar:packages/junit-4.5.jar:packages/kernel.jar:packages/log4j-1.2.17.jar:packages/maps.jar:packages/misc.jar:packages/rescuecore.jar:packages/rescuecore2.jar:packages/resq-fire.jar:packages/sample.jar:packages/standard.jar:packages/traffic3.jar:packages/trove-0.1.8.jar:packages/uncommons-maths-1.2.jar:packages/xml-0.0.6.jar $(find ./src/* | grep .java$)

cp -r packages bin/packages