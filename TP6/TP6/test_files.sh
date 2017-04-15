#!/bin/bash
mkdir -p res
for file in data/*.ci; do
	echo $file
	filename=$(basename $file .${file##*.})
	java -cp gen Parser $file "res/$filename".asm
done
