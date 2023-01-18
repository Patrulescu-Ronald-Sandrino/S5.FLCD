#!/bin/bash

clear; lex lang.lxi && gcc -o lang.out lex.yy.c -ll && ./lang.out < "$@"
# clear; lex lang.lxi && gcc -Wnone -o lang.out lex.yy.c -ll && ./lang.out < p1.txt