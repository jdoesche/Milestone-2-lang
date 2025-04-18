grammar Skeptical;

program returns [Program ast]
    locals [StaDiv staticDiv; DynDiv dynamicDiv]
    : s=static_division
      d=dynamic_division
      {$ast = new Program($s.ast, $d.ast);}
  ;

static_division returns [StaDiv ast]
    locals [ArrayList<StaDecl> decls]
    @init { $decls = new ArrayList<StaDecl>(); }
  : 'STATIC' 'DIVISION.' NEWLINE
    (decl=stadecl { $decls.add($decl.ast); })*  
    { $ast = new StaDiv($decls); }
  ;

dynamic_division returns [DynDiv ast]
    locals [ArrayList<Statement> statements]
    @init { $statements = new ArrayList<Statement>(); }
  : 'DYNAMIC' 'DIVISION.' NEWLINE
    (state=statement { $statements.add($state.ast); })* 
    { $ast = new DynDiv($statements); }
  ;

stadecl returns [StaDecl ast]
  : 'PROGRAM-ID.' id=Identifier '.' NEWLINE { $ast = new ProgId($id.text); }
  | 'AUTHOR.' s=STRING '.' NEWLINE { $ast = new Auth($s.text); }
  | 'DATE-WRITTEN.' s=STRING '.' NEWLINE { $ast = new Date($s.text); }
  | constant { $ast = $constant.ast; }
  ;

constant returns [StaDecl ast]
@init { Exp valueExp = null; }
  : 'FIX' id=Identifier 'TO' e=expression
    {
      if (e.ast instanceof IdExp) {
        throw new RuntimeException("Cannot assign constant to a variable reference like '" + ((IdExp)e.ast).id() + "'"); 
      }
      $ast = new Const($id.text, e.ast);
    }
    '.'
  ;

statement returns [Statement ast]
  : assign { $ast = $assign.ast; }
  | print { $ast = $print.ast; }
  | input { $ast = $input.ast; }
  | ifstmt { $ast = $ifstmt.ast; }
  | loop_stmt { $ast = $loop_stmt.ast; }
  | callstmt { $ast = $callstmt.ast; }
  | funcdef { $ast = $funcdef.ast; }
  | rand { $ast = $rand.ast; }
  ;

assign returns [Statement ast]
    locals [Assign assignment]
    @init { $assignment = new Assign(); }
  : 'SET' id=Identifier 'TO' expr=expression 
    {
        $assignment.setIdentifier($id.text);
        $assignment.setExpression($expr.ast);
    }
    ('AS' value= (STRING | Number)
        { $assignment.setType($value.text); }
    )? 
    '.'
    { $ast = $assignment; }
  ;

print returns [Statement ast]
  : 'DISPLAY' expr=expression '.' { $ast = new Print($expr.ast); }
  ;

input returns [Statement ast]
  : 'PROMPT' id=Identifier 'WITH' str=STRING '.' { $ast = new Input($id.text, $str.text); }
  ;

ifstmt returns [Statement ast]
    locals [ArrayList<Statement> thenStmts = new ArrayList<>(), elseStmts = new ArrayList<>(); int indentLevel = 0;]
  : 'IF' cond=expression 'THEN' NEWLINE
    INDENT { indentLevel++; }
    (t=statement { thenStmts.add($t.ast); })* 
    ('ELSE' NEWLINE 
      INDENT { indentLevel++; }
      (e=statement { elseStmts.add($e.ast); })*
      DEDENT
    )? DEDENT
    { $ast = new IfStmt($cond.ast, thenStmts, elseStmts); }
  ;

loop_stmt returns [Statement ast]
    locals [ArrayList<Statement> body = new ArrayList<>(); int indentLevel = 0;]
  : 'START' id=Identifier 'AS' start=expression 'TO' end=expression 'DOING' NEWLINE
    INDENT { indentLevel++; }
    (stmt=statement { body.add($stmt.ast); })* DEDENT
    { $ast = new LoopStmt($id.text, $start.ast, $end.ast, body); }
  ;

funcdef returns [Statement ast]
    locals [ArrayList<Statement> body = new ArrayList<>(); int indentLevel = 0;]
  : 'FUNCTION' id=Identifier ('WITH' args=arglist)? '.' NEWLINE
    INDENT { indentLevel++; }
  (stmt=statement { body.add($stmt.ast); })* DEDENT
  'RETURN' returnExpr=expression '.' 
  { $ast = new FuncDef($id.text, $args.ast, body, $returnExpr.ast); }
;

callstmt returns [Statement ast]
: 'CALL' id=Identifier (args=arglist)? '.' 
  { $ast = new CallStmt($id.text, $args.ast); }
;
arglist returns [Exp ast]
    locals [ArrayList<Exp> argsList = new ArrayList<Exp>();]
  : e=expression { argsList.add($e.ast); }
    (',' e2=expression { argsList.add($e2.ast); })*
    { $ast = argsList; }
  ;

rand returns [Statement ast]
  : 'SET' id=Identifier 'TO RANDOM' start=expression 'TO' end=expression '.' { $ast = new RandStmt($id.text, $start.ast, $end.ast); }
  ;

expression returns [Exp ast]
  : disjunction { $ast = $disjunction.ast; }
  ;

disjunction returns [Exp ast]
  : conjunction { $ast = $conjunction.ast; }
    ( 'OR' c=conjunction { $ast = new BinOp($ast, $c.ast, "OR"); } )*
  ;

conjunction returns [Exp ast]
  : comparison { $ast = $comparison.ast; }
    ( 'AND' c=comparison { $ast = new BinOp($ast, $c.ast, "AND"); } )*
  ;

comparison returns [Exp ast]
  : sum { $ast = $sum.ast; }
    ( op=('==' | '!=' | '<' | '<=' | '>' | '>=') s=sum 
        { $ast = new BinOp($ast, $s.ast, $op.text); }
    )*
  ;

sum returns [Exp ast]
  : term { $ast = $term.ast; }
    ( op=('+' | '-') t=term { $ast = new BinOp($ast, $t.ast, $op.text); } )*
  ;

term returns [Exp ast]
  : power { $ast = $power.ast; }
    ( op=('*' | '/' | 'MOD') p=power { $ast = new BinOp($ast, $p.ast, $op.text); } )*
  ;

power returns [Exp ast]
  : factor { $ast = $factor.ast; }
    ( '**' p=power { $ast = new BinOp($ast, $p.ast, "**"); } )?
  ;

factor returns [Exp ast]
  : n=Number { $ast = new NumExp($n.text); }
  | id=Identifier { $ast = new IdExp($id.text); }
  | '(' e=expression ')' { $ast = $e.ast; }
  | '-' f=factor { $ast = new NegExp($f.ast); }
  ;

Number : DIGIT+ ;
Identifier : Letter LetterOrDigit*;

fragment DIGIT : [0-9];
fragment Letter : [a-zA-Z$_] | ~[\u0000-\u00FF\uD800-\uDBFF] {Character.isJavaIdentifierStart(_input.LA(-1))}? | [\uD800-\uDBFF] [\uDC00-\uDFFF] {Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?;
fragment LetterOrDigit : [a-zA-Z0-9$_] | ~[\u0000-\u00FF\uD800-\uDBFF] {Character.isJavaIdentifierPart(_input.LA(-1))}? | [\uD800-\uDBFF] [\uDC00-\uDFFF] {Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?;

STRING: '"' (~['\\']|('\\' .))* '"';
INDENT: '\t'; // assuming 1 tab character for Indent (adjust as necessary)
DEDENT: '\t'; // assuming 1 tab character for Dedent (adjust as necessary)


AT : '@';
ELLIPSIS : '...';
WS : [ \t\u000C]+ -> skip;
NEWLINE : '\r'? '\n' ;
Comment : '#' ~[\r\n]* -> skip;