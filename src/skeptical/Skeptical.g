grammar SkeptiCal;

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
  : "PROGRAM-ID." id=Identifier "." NEWLINE { $ast = new ProgId($id.text); }
  | "AUTHOR." s=String "." NEWLINE { $ast = new Auth($s.text); }
  | "DATE-WRITTEN." s=String "." NEWLINE { $ast = new Date($s.text); }
  | constant { $ast = $constant.ast; }
  ;

constant returns [StaDecl ast]
@init { Exp valueExp = null; }
  : "FIX" id=Identifier "TO" e=exp
    {
      if (e.ast instanceof IdExp) {
        throw new RuntimeException("Cannot assign constant to a variable reference like '" + ((IdExp)e.ast).id() + "'"); 
      }
      $ast = new Const($id.text, e.ast);
    }
    "."
  ;

statement returns [Statement ast]
  : as=assign { $ast = $as.ast; }
  | pr=print { $ast = $pr.ast; }
  | is=input { $ast = $is.ast; }
  | ifs=ifstmt { $ast = $ifs.ast; }
  | ls=loop_stmt { $ast = $ls.ast; }
  | cs=callstmt { $ast = $cs.ast; }
  | fd=funcdef { $ast = $fd.ast; }
  | rs=rand { $ast = $rs.ast; }
  ;

assign returns [Statement ast]
    locals [Assign assignment]
    @init {
        $assignment = new Assign(); // Start with a blank Assign object
    }
  : 'SET' id=Identifier 'TO' expr=exp 
    {
        $assignment.setIdentifier($id.text);
        $assignment.setExpression($expr.ast);
    }
    ('AS' value=(String | Number)
        { $assignment.setType($value.text); }
    )? 
    '.'
    { $ast = $assignment; }
  ;

print returns [Statement ast]
  : 'DISPLAY' expr=exp '.' { $ast = new Print($expr.ast); }
  ;

input returns [Statement ast]
  : 'PROMPT' id=Identifier 'WITH' str=String '.' { $ast = new Input($id.text, $str.text); }
  ;

ifstmt returns [Statement ast]
    locals [ArrayList<Statement> thenStmts = new ArrayList<>(), elseStmts = new ArrayList<>();]
    locals [int indentLevel = 0;]
  : 'IF' cond=exp 'THEN' NEWLINE
    INDENT { indentLevel++; }
    (t=statement { 
        thenStmts.add($t.ast);
        $t.indentLevel = indentLevel;
    })* 
    ('ELSE' NEWLINE 
    INDENT { indentLevel++; }
    (e=statement { 
        elseStmts.add($e.ast);
        $e.indentLevel = indentLevel;
    })*
    )? DEDENT
    DEDENT
    { $ast = new IfStmt($cond.ast, thenStmts, elseStmts); }
  ;

loop_stmt returns [Statement ast]
    locals [int indentLevel = 0;]
  : 'START' id=Identifier 'AS' start=exp 'TO' end=exp 'DOING' NEWLINE
    INDENT { indentLevel++; }
    (stmt=statement { 
        $ast.add(stmt.ast); 
        stmt.indentLevel = indentLevel;
    })* DEDENT
    { $ast = new LoopStmt($id.text, $start.ast, $end.ast, $ast); }
  ;

funcdef returns [Statement ast]
    locals [int indentLevel = 0;]
  : 'FUNCTION' id=Identifier ('WITH' args=arglist)? '.' NEWLINE
    INDENT { indentLevel++; }
    (stmt=statement { 
        $ast.add(stmt.ast); 
        stmt.indentLevel = indentLevel;
    })* DEDENT
    'RETURN' returnExpr=exp '.' { 
        $ast = new FuncDef($id.text, $args, $ast, $returnExpr.ast); 
        $returnExpr.indentLevel = indentLevel;
    }
  ;

rand returns [Statement ast]
  : 'SET' id=Identifier 'TO RANDOM' start=exp 'TO' end=exp '.' { $ast = new RandStmt($id.text, $start.ast, $end.ast); }
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
    ( ('==' | '!=' | '<' | '<=' | '>' | '>=') s=sum 
        { $ast = new BinOp($ast, $s.ast, $inputText); }
    )*
  ;

sum returns [Exp ast]
  : term { $ast = $term.ast; }
    ( ('+' | '-') t=term { $ast = new BinOp($ast, $t.ast, $inputText); } )*
  ;

term returns [Exp ast]
  : power { $ast = $power.ast; }
    ( ('*' | '/' | 'MOD') p=power { $ast = new BinOp($ast, $p.ast, $inputText); } )*
  ;

power returns [Exp ast]
  : factor { $ast = $factor.ast; }
    ( '**' p=power { $ast = new BinOp($ast, $p.ast, "**"); } )?
  ;

factor returns [Exp ast]
  : number { $ast = new NumExp($number.text); }
  | identifier { $ast = new IdExp($identifier.text); }
  | '(' e=expression ')' { $ast = $e.ast; }
  | '-' f=factor { $ast = new NegExp($f.ast); }
  ;

number returns [Token text]
  : [0-9]+ { $text = $input.text; }
  ;

identifier returns [Token text]
  : [a-zA-Z_][a-zA-Z_0-9]* { $text = $input.text; }
  ;

NEWLINE : '\r'? '\n' ;
Comment :  '#' ~[\r\n]* -> skip;
