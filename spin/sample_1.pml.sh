#!/bin/sh

cd "$BUCK_PROJECT_ROOT"
./spin-bin -run sample_1.pml
if [ -f sample_1.pml.trail ]; then
  ./spin-bin -g -l -s -r -p -t sample_1.pml
  exit 1
fi
