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
    locals [List<Statement> statements]
    @init { $statements = new ArrayList<Statement>(); }
  : 'DYNAMIC' 'DIVISION.' NEWLINE
    (state=statement { $statements.add($state.ast); })*  // Repeatable list of statements in dynamic division
    { $ast = new DynDiv($statements); }
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
