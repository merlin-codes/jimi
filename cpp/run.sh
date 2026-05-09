#!/usr/bin/env sh

gcc -lglfw -lGL -lGLU -o output.bin window.cpp

./output.bin
