#!/bin/bash
i=1
while :
do
 let i++
 if [ $i == 100000 ]; then
  i=1
 fi
done