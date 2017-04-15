#!/bin/bash

PATH=$PATH:$HOME/javacc-5.0/bin

mkdir -p gen

cp LangageH17.jjt gen
cp PrintMachineCodeVisitor.java gen
cp ast/*.java gen

cd gen
jjtree LangageH17.jjt
javacc LangageH17.jj

javac *.java
echo Compilation javac terminee

cd ..
