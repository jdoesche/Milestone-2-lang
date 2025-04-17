grammar Skeptical;

program returns [Program ast]
    locals [StaDiv staticDiv; DynDiv dynamicDiv]
    : s=static_division
      d=dynamic_division
    {$ast = new Program($s.ast, $d.ast); }
  ;








Comment :  '#' ~[\r\n]* -> skip;
