package skeptical;

import java.util.ArrayList;
import java.util.List;

/**
 * This class hierarchy represents expressions in the abstract syntax tree
 * manipulated by this interpreter.
 * 
 * @author hridesh
 * 
 */
public interface AST {
    public static abstract class ASTNode implements AST {
        public abstract <T> T accept(Visitor<T> visitor, Env env);
    }

    public static class Program extends ASTNode {
    StaDiv _staticDiv;
    DynDiv _dynamicDiv;

    public Program(StaDiv staticDiv, DynDiv dynamicDiv) {
        _staticDiv = staticDiv;
        _dynamicDiv = dynamicDiv;
    }

    public StaDiv staticDivision() { return _staticDiv; }
    public DynDiv dynamicDivision() { return _dynamicDiv; }

    public <T> T accept(Visitor<T> visitor, Env env) {
        return visitor.visit(this, env);
    }
}

    public static abstract class Division extends ASTNode {}

    public static class StaDiv extends Division {
        List<StaDecl> statements;

        public StaDiv(List<StaDecl> statements) {
            this.statements = statements;
        }

        public List<StaDecl> getStatements() {
            return statements;
        }

        public <T> T accept(Visitor<T> visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    public static class DynDiv extends Division {
        List<Statement> statements;

        public DynDiv(List<Statement> statements) {
            this.statements = statements;
        }

        public List<Statement> getStatements() {
            return statements;
        }

        public <T> T accept(Visitor<T> visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    public static abstract class StaDecl extends ASTNode {}


    public static class ProgId extends StaDecl {
        String name;

        public ProgId(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public <T> T accept(Visitor<T> visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    public static class Auth extends StaDecl {
        String name;

        public Auth(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public <T> T accept(Visitor<T> visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    public static class Date extends StaDecl {
        String date;

        public Date(String date) {
            this.date = date;
        }

        public String getDate() {
            return date;
        }

        public <T> T accept(Visitor<T> visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    public static class Const extends StaDecl {
    	private final String id;      // For example, "LIMIT"
    	private final Exp value;      // For example, a ConstExp(5)

   	 public Const(String id, Exp value) {
        	this.id = id;
        	this.value = value;
    	}

    	public String id() { return id; }
   	public Exp value() { return value; }

    	@Override
    	public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	}
     }
    public static abstract class Exp extends ASTNode {}

    public static class IdExp extends Exp {
    	private final String id;

    	public IdExp(String id) {
        	this.id = id;
    	}

    	public String id() {
        	return id;
    	}

    	@Override
    	public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	}
     }
    public static abstract class LiteralExp extends Exp {}
    public static class NumExp extends LiteralExp {
    	private final double value;

    	public NumExp(double value) {
        	this.value = value;
    	}

    	public double value() {
        	return value;
    	}

    	@Override
    	public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	}
     }
     public static class StrExp extends LiteralExp {
    	private final String value;

    	public StrExp(String value) {
        	this.value = value;
    	}

    	public String value() {
        	return value;
    	}

    	@Override
    	public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	}
     }
    public static abstract class BinaryOpExp extends Exp {
    	protected final Exp left;
    	protected final Exp right;

    	public BinaryOpExp(Exp left, Exp right) {
        	this.left = left;
        	this.right = right;
    	}

    	public Exp left() { return left; }
    	public Exp right() { return right; }
     }
    
     public static class DisjExp extends BinaryOpExp {
    	public DisjExp(Exp left, Exp right) {
        	super(left, right);
    	}

    	public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	}
     }
    public static class ConjExp extends BinaryOpExp {
    	public ConjExp(Exp left, Exp right) {
        	super(left, right);
    	}

    	public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	}
     }

     public static class CompExp extends BinaryOpExp {
    	private final String operator;

    	public CompExp(Exp left, String operator, Exp right) {
        	super(left, right);
        	this.operator = operator;
    	}

    	public String operator() { return operator; }

    	public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	}
      }

     public static class SumExp extends BinaryOpExp {
    	private final String operator;

    	public SumExp(Exp left, String operator, Exp right) {
        	super(left, right);
        	this.operator = operator;
    	}

    	public String operator() { return operator; }

    	public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	}
    }

    public static class TermExp extends BinaryOpExp {
	private final String operator;

    	public TermExp(Exp left, String operator, Exp right) {
        	super(left, right);
        	this.operator = operator;
    	}

    	public String operator() { return operator; }

    	public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	}
     }

    public static class PowExp extends BinaryOpExp {
    	public PowExp(Exp left, Exp right) {
        	super(left, right);
    	}

    	public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	}
    }

    public static class FactorExp extends Exp {
    	private Exp expr;  // This can be a number, identifier, GroupingExp, or UnaryOpExp

    	// Constructor for all types of factors
    	public FactorExp(Exp expr) {
        	this.expr = expr;
    	}

    	public Exp getExpr() {
       	 return expr;
    	}

   	 @Override
    	public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	}
     }
    public static class GroupExp extends Exp {
   	 private Exp expr;  // The expression inside the parentheses

    	// Constructor for grouping expression
    	public GroupExp(Exp expr) {
        	this.expr = expr;
    	}

    	public Exp getExpr() {
        	return expr;
    	}

    	@Override
    	public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	}
     }
    public static class UnaryOpExp extends Exp {
    	private String operator;  // The operator (e.g., "-")
    	private Exp expr;         // The expression to which the unary operator is applied

    	// Constructor for unary operator
    	public UnaryOpExp(String operator, Exp expr) {
        	this.operator = operator;
        	this.expr = expr;
    	}

    	public String getOperator() {
        	return operator;
    	}

    	public Exp getExpr() {
        	return expr;
    	}

    	@Override
    	public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	}
     }




    public static abstract class Statement extends ASTNode {}

    public static class Assign extends Statement { 
    	private String identifier;
    	private Exp expression;
    	private String type; // Optional

    	public Assign() { }

    	public void setIdentifier(String identifier) { this.identifier = identifier; }
    	public void setExpression(Exp expression) { this.expression = expression; }
    	public void setType(String type) { this.type = type; }

    	public String getIdentifier() { return identifier; }
    	public Exp getExpression() { return expression; }
    	public String getType() { return type; }

    	@Override
    	public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	}
     }


	
     public static class Print extends Statement {
    	private String output; // could be a string literal or identifier

    	public Print(String output) {
        	this.output = output;
    	}

    	public String getOutput() { return output; }

    	@Override
    	public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	}
     }

	
     public static class Input extends Statement {
    	private String identifier;
    	private String prompt;

   	 public Input(String identifier, String prompt) {
        	this.identifier = identifier;
        	this.prompt = prompt;
    	}

    	public String getIdentifier() { return identifier; }
    	public String getPrompt() { return prompt; }

    	@Override
    	public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	}
      }

	
      public static class IfStmt extends Statement {
    	private Exp condition;
    	private List<Statement> thenBranch;
    	private List<Statement> elseBranch; // can be null if no else

    	public IfStmt(Exp condition, List<Statement> thenBranch, List<Statement> elseBranch) {
        	this.condition = condition;
        	this.thenBranch = thenBranch;
        	this.elseBranch = elseBranch;
    	}

    	public Exp getCondition() { return condition; }
    	public List<Statement> getThenBranch() { return thenBranch; }
    	public List<Statement> getElseBranch() { return elseBranch; }

    	@Override
    	public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	}
      }

	
      public static class LoopStmt extends Statement {
   	 private String identifier;
    	 private int startValue;
   	 private int endValue;
   	 private List<Statement> body;

   	 public LoopStmt(String identifier, int startValue, int endValue, List<Statement> body) {
         	this.identifier = identifier;
         	this.startValue = startValue;
         	this.endValue = endValue;
         	this.body = body;
    	 }

   	 public String getIdentifier() { return identifier; }
   	 public int getStartValue() { return startValue; }
   	 public int getEndValue() { return endValue; }
   	 public List<Statement> getBody() { return body; }

   	 @Override
   	 public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	 }
       }
	
       public static class CallStmt extends Statement {
    	  private String identifier;
    	  private List<Exp> arguments; // can be empty if no arguments

   	   public CallStmt(String identifier, List<Exp> arguments) {
        	this.identifier = identifier;
        	this.arguments = arguments;
    	   }

   	    public String getIdentifier() { return identifier; }
   	    public List<Exp> getArguments() { return arguments; }

   	   @Override
   	   public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
    	   }
       }

      public static class FuncDef extends Statement {
    	   private String name;
 	   private List<String> parameters;
 	   private List<Statement> body;
 	   private String returnIdentifier;

 	   public FuncDef(String name, List<String> parameters, List<Statement> body, String returnIdentifier) {
        	this.name = name;
        	this.parameters = parameters;
        	this.body = body;
        	this.returnIdentifier = returnIdentifier;
 	   }

 	   public String getName() { return name; }
 	   public List<String> getParameters() { return parameters; }
 	   public List<Statement> getBody() { return body; }
 	   public String getReturnIdentifier() { return returnIdentifier; }

 	   @Override
 	   public <T> T accept(Visitor<T> visitor, Env env) {
        	return visitor.visit(this, env);
 	   }
       }


       public static class Rand extends Statement {
 	   private String identifier;
 	   private int min;
 	   private int max;

 	   public Rand(String identifier, int min, int max) {
        	this.identifier = identifier;
        	this.min = min;
        	this.max = max;
 	   }

  	  public String getIdentifier() { return identifier; }
 	   public int getMin() { return min; }
 	   public int getMax() { return max; }

 	   @Override
 	   public <T> T accept(Visitor<T> visitor, Env env) {
     		return visitor.visit(this, env);
    	    }
        }



    public interface Visitor<T> {
        T visit(AST.Program p, Env env);
    	T visit(AST.StaticDivision d, Env env);
    	T visit(AST.DynamicDivision d, Env env);
        T visit(AST.ProgId sd, Env env);
        T visit(AST.Auth sd, Env env);
        T visit(AST.Date sd, Env env);
        T visit(AST.Const sd, Env env);
	T visit(AST.IdExp e, Env env);
	T visit(AST.NumExp e, Env env);
	T visit(AST.StrExp e, Env env);
	T visit(AST.DisjExp e, Env env);
	T visit(AST.ConjExp e, Env env);
	T visit(AST.CompExp e, Env env);
	T visit(AST.SumExp e, Env env);
	T visit(AST.TermExp e, Env env);
	T visit(AST.PowExp e, Env env);
	T visit(AST.FactorExp e, Env env);
   	T visit(AST.GroupExp e, Env env);
    	T visit(AST.UnaryOpExp e, Env env);
	T visit(AST.Assign dd, Env env);
	T visit(AST.Print dd, Env env);
	T visit(AST.Input dd, Env env);
	T visit(AST.IfStmt dd, Env env);
	T visit(AST.LoopStmt dd, Env env);
	T visit(AST.CallStmt dd, Env env);
	T visit(AST.FuncDef dd, Env env);
	T visit(AST.Rand dd, Env env);
	}
}