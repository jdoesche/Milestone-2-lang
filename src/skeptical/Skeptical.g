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








Comment :  '#' ~[\r\n]* -> skip;
