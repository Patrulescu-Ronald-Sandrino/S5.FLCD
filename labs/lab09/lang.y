%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define YYDEBUG 1
%}


%union {
  	int l_val;
	char *p_val;
}


%token READ
%token WRITE
%token IF
%token ELSE
%token WHILE

%token <p_val> IDENTIFIER
%token <p_val> LITERAL_INT
%token <p_val> LITERAL_CHAR
%token <p_val> LITERAL_STRING

%token INT
%token CHAR
%token STRING

%left '+' '-'
%left '*' '/' '%'

//%type <lvalue> expr_stmt factor_stmt const

%%

program: /* empty */
//    | stmt
    ;

//stmt: expr_stmt ';'

/* TODO: grammar rules */

%%

void yyerror(char *s)
{
  printf("%s\n", s);
}

extern FILE *yyin;

void main(int argc, char **argv)
{
  if(argc>1) yyin = fopen(argv[1], "r");
  if((argc>2)&&(!strcmp(argv[2],"-d"))) yydebug = 1;
  if(!yyparse()) fprintf(stderr,"\tO.K.\n");
}
