#!/bin/bash

INPUT="small_sep.txt"
K=3
TIGHT=3
LOOSE=6
ITERATIONS=10
RESULTS="../results.csv"

python process_manual.py $INPUT $K $TIGHT $LOOSE $ITERATIONS $RESULTS plot


