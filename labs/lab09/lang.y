%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define YYDEBUG 1

#define INT_TYPE 1
#define CHAR_TYPE 2
#define STRING_TYPE 3

double stiva[20];
int sp;

void push(double x)
{ stiva[sp++]=x; }

double pop()
{ return stiva[--sp]; }

%}


%union {
  	int l_val;
	char *p_val;
}


%token <p_val> IDENTIFIER
%token <p_val> INT_LITERAL
%token <p_val> CHAR_LITERAL
%token <p_val> STRING_LITERAL

%token INT
%token CHAR
%token STRING

%token READ
%token WRITE
%token IF
%token ELSE
%token WHILE

%token LBRACE
%token RBRACE
%token LPAREN
%token RPAREN
%token SEMICOLON
%token COMMA

%token ASSIGN

%token EQ
%token NEQ
%token LT
%token LEQ
%token GT
%token GEQ

%token PLUS
%token MINUS
%token MULTIPLY
%token DIVIDE

%left '+' '-'
%left '*' '/'

%type <l_val> expr factor constant

%%

program: /* empty */
    | stmt
    | program ';' stmt
    ;

stmt: '{' stmt_list '}'
    | decl_stmt
    | assign_stmt
    | read_stmt
    | write_stmt
    | if_stmt
    | while_stmt
    ;

stmt_list: /* empty */
    | stmt
    | stmt_list ';' stmt
    ;

decl_stmt: type IDENTIFIER '=' expr ';'
    ;

type: INT
    | CHAR
    | STRING
    ;


/* TODO: expr functions */
expr: factor
    | expr '+' factor {

        }
    | expr '-' factor {

        }
    | expr '*' factor {

        }
    | expr '/' factor {

        }
    | relational_expr {

        }
    ;

relational_expr: expr '<' expr {

        }
    | expr '>' expr {

        }
    | expr '<' '=' expr {

        }
    | expr '>' '=' expr {

        }
    | expr '=' '=' expr {

        }
    | expr '!' '=' expr {

        }
    ;

factor: IDENTIFIER {}
    | constant {}
    | '(' expr ')' { /*$$ = $2;*/}
    ;

constant: INT_LITERAL {
            $$ = INT_TYPE;
            push(atof($1));
        }
    | CHAR_LITERAL {
            $$ = CHAR_TYPE;
            push((double)$1[0]);
        }
    | STRING_LITERAL {
            $$ = STRING_TYPE;
            push((double)$1[0]);
        }
    ;

assign_stmt: IDENTIFIER '=' expr ';'
    ;

read_stmt: READ '(' IDENTIFIER ')' ';'
    ;

write_stmt: WRITE '(' expr ')' ';'
    ;

if_stmt: IF '(' expr ')' stmt
    | IF '(' expr ')' stmt ELSE stmt
    ;

while_stmt: WHILE '(' expr ')' stmt
    ;

%%

/* yyerror has a non-void type just to avoid a compiler warning*/
int yyerror(char *s)
{
  printf("%s\n", s);
  return 0;
}

extern FILE *yyin;

void main(int argc, char **argv)
{
  printf("reading file: %s\n", argv[1]);
  if(argc>1) yyin = fopen(argv[1], "r");
  if((argc>2)&&(!strcmp(argv[2],"-d"))) yydebug = 1;
  if(!yyparse()) fprintf(stderr,"\tO.K.\n");
}
