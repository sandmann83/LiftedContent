#!/bin/sh
for dir in *; do if [ -d $dir ]; then
	  echo $dir && (cd $dir && rm -rf target)
	fi;
done
