#!/bin/bash
mkdir -p res
for file in data/*.ci; do
	echo $file
	filename=$(basename $file .${file##*.})
	java -cp gen Parser "res/$filename".dot "res/$filename".ci < $file
done
