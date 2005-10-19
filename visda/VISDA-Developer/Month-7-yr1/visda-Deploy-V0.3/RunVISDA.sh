#!/bin/sh
export LD_LIBRARY_PATH="${LD_LIBRARY_PATH}:`pwd`"
echo $LD_LIBRARY_PATH
java -Xms512m -Xmx512m -jar visda.jar


