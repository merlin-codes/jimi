#!/usr/bin/env sh
set -eu

g++ -std=c++17 Renderer.cpp -o output.bin -lglfw -lGL -lGLU -lm

./output.bin
