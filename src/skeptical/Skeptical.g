grammar Skeptical;

program returns [Program ast]
    locals [StaDiv staticDiv; DynDiv dynamicDiv]
    : s=static_division // has to be at least one static division
      d=dynamic_division  // has to be at least one dynamic division
    {$ast = new Program($s.ast, $d.ast); }
  ;
static_division returns [StaDiv ast]
    locals [ArrayList<StaDecl> decls]
    @init { $decls = new ArrayList<StaDecl>(); }
  : 'STATIC' 'DIVISION.' NEWLINE
    (decl=stadecl { $decls.add($decl.ast); })*  // Repeatable list of statements in static division
    { $ast = new StaDiv($decls);}
  ;
dynamic_division returns [DynDiv ast]
    locals [ArrayList<Statement> statements]
    @init { $statements = new ArrayList<Statement>(); }
  : 'DYNAMIC' 'DIVISION.' NEWLINE
    (state=statement { $statements.add($state.ast); })*  // Repeatable list of statements in dynamic division
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




 Identifier :   Letter LetterOrDigit*;

 Letter :   [a-zA-Z$_]
	|   ~[\u0000-\u00FF\uD800-\uDBFF] 
		{Character.isJavaIdentifierStart(_input.LA(-1))}?
	|   [\uD800-\uDBFF] [\uDC00-\uDFFF] 
		{Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}? ;

 LetterOrDigit: [a-zA-Z0-9$_]
	|   ~[\u0000-\u00FF\uD800-\uDBFF] 
		{Character.isJavaIdentifierPart(_input.LA(-1))}?
	|    [\uD800-\uDBFF] [\uDC00-\uDFFF] 
		{Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?;

NEWLINE : '\r'? '\n' ;
Comment :  '#' ~[\r\n]* -> skip;
