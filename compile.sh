#!/bin/bash
set -e

files=
for file in `find src/`
do
    if [[ `basename $file` != `basename $file .java` ]];
    then
      files=$files\ $file
    fi
done


javac -target 1.6 -source 1.6 -cp bin/:lib/guava-14.0.1.jar:lib/jogamp-all-platforms/jar/gluegen.jar:lib/jogamp-all-platforms/jar/jogl-all.jar -Xlint:unchecked -d bin/ $files;

