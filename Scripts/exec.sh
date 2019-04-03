#!/bin/bash
for num in 1 2 3 4 5 6
do
{
 sh /data/a.sh
}&
done
while :
do
 top -n 1
 sleep $7s
done