#!/bin/bash

clear; lex lang.lxi && yacc -d lang.y && gcc -o lang-yacc.out y.tab.* lex.yy.c -ll && ./lang-yacc.out < "$@"