#!/bin/bash
find . -type f -name "*.kt" -exec grep -Hn "//" {} \; | head -n 30
