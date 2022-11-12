grammar Formula;

// a formula always start with '='
formula: startFormulaSymbol expr EOF #zFormula ;

// an expression always returns something
expr: invokation #invokeExpr
    | openParen expr closeParen #parenExpr
    | lit #literal
    | op=SUB expr #unSubExpr
    | op=NOT expr #notExpr
    | expr op=EXP expr # powExpr
    | expr op=(MUL|DIV|MOD) expr #mulDivModExpr
    | expr op=(ADD|SUB) expr #addSubExpr
    | expr op=(AND|OR) expr # andOrExpr
    | expr op=(EQUAL|NOT_EQUAL|LARGER|LARGER_OR_EQUAL|SMALLER|SMALLER_OR_EQUAL) expr #boolOperation
    ;

invokation:functionCall
          | fullRangeAddress
          ;

functionCall:functionName openParen (expr)?(comma expr)* comma? closeParen;

functionName:noSpaceId ;

fullRangeAddress:rangeAddress sheetPrefix? wbPrefix?;
sheetPrefix:'@' sheetNameWithSpace
           |'@' sheetName
           ;
sheetNameWithSpace:WITH_SPACE_ID;
sheetName:noSpaceId;

rangeAddress:cellAddress ':' cellAddress  #rangeAsPairCellAddress
            | cellAddress  #rangeAsOneCellAddress
            | ID_LETTERS ':' ID_LETTERS  #rangeAsColAddress
            | INT':'INT #rangeAsRowAddress
            |openParen rangeAddress closeParen #rangeInparens
            ;

wbPrefix: wbPrefixNoPath
        | wbPrefixWithPath;
wbPrefixNoPath:'@' wbName;
wbPrefixWithPath:'@' wbName '@' wbPath ;

wbName: noSpaceId | WITH_SPACE_ID ;

// A1,A123, ABC123, $A1, A$1, $A$1
cellAddress: CELL_LIKE_ADDRESS;

// wbPath is encased in single quotes: 'path/to/wb.abc'
wbPath:WITH_SPACE_ID;
lit: (FLOAT_NUMBER | BOOLEAN | STRING | INT );

openParen:'(';
closeParen:')';
comma:',';
startFormulaSymbol:'=';

// Boolean must be prioritized over id so that it can be parsed correctly
BOOLEAN: 'TRUE' | 'FALSE';

// eg: A1, $A1, A$1, $A$1
noSpaceId:CELL_LIKE_ADDRESS|NO_SPACE_ID;
CELL_LIKE_ADDRESS:'$'?ID_LETTERS '$'?INT;
NO_SPACE_ID:ID_LETTERS(INT|ID_LETTERS)*;
WITH_SPACE_ID:SINGLE_QUOTE_STRING;

ID_LETTERS:LETTER(LETTER)*;
fragment LETTER:'a'..'z'|'A'..'Z'|'_';

FLOAT_NUMBER: DIGIT+ '.' DIGIT*
        |'.' DIGIT+
        ;

// an Int may start with 0, to preserve the text integrity
INT:DIGIT+;
fragment DIGIT:[0-9] ;

// string
STRING: '"' (ESC_CHAR|.)*? '"' ;// match anything in "..."
SINGLE_QUOTE_STRING:'\'' (ESC_CHAR|.)*? '\'';
ESC_CHAR : '\\"' | '\\\\' ; // 2-char sequences \" and \\

// operator
MUL: '*';
DIV: '/';
ADD: '+';
SUB: '-';
//modulo
MOD: '%';
//exponential
EXP: '^';
// boolean operators
AND: '&&';
OR: '||';
NOT:'!';
EQUAL:'==';
NOT_EQUAL:'!=';
LARGER:'>';
LARGER_OR_EQUAL:'>=';
SMALLER:'<';
SMALLER_OR_EQUAL:'<=';

NEWLINE:'\r'?'\n'->skip;
// white space
WS:(' '|'\t')+ -> skip;
