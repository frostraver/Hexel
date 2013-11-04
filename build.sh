#!/bin/bash
set -e

./compile.sh

#Hexel jar
    cd bin
    jar cmf ../manifest.txt Hexel.jar Hexel/*
    cd ..

build(){
    native=$1
    rm -rf builds/$native/bin/*
    rm -rf builds/$native/lib/*

    mkdir -p builds/$native/bin/
    mkdir -p builds/$native/lib/
    mkdir -p builds/$native/img/
    mkdir -p builds/$native/state/chunks/
    mkdir -p builds/$native/state/shmcs/

    cp bin/Hexel.jar builds/$native/bin/

    cp img/* builds/$native/img/
    cp hexel.config.default builds/$native/hexel.config

    cp lib/jogamp-all-platforms/jar/jogl-all.jar builds/$native/lib/
    cp lib/jogamp-all-platforms/jar/jogl-all-natives-$native.jar builds/$native/lib/
    cp lib/jogamp-all-platforms/jar/gluegen-rt.jar builds/$native/lib/gluegen.jar
    cp lib/jogamp-all-platforms/jar/gluegen-rt-natives-$native.jar builds/$native/lib/gluegen-natives-$native.jar
    cp lib/guava-14.0.1.jar builds/$native/lib/guava-14.0.1.jar

    cp -r lib/jogamp-all-platforms/lib/$native builds/$native/lib/
}

build linux-amd64
build linux-i586
build macosx-universal
build windows-i586
build windows-amd64

cd builds
for file in *.zip; do rm $file; done
for file in *; do zip -r $file $file; done
cd ..

#cleanup
    rm bin/Hexel.jar

