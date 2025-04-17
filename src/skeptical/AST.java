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

    public static class AssignmentStmt extends Statement {
    	private String identifier;
    	private Exp expression;
    	private String type; // Optional "AS" type term (optional)

    	public AssignmentStmt(String identifier, Exp expression, String type) {
        	this.identifier = identifier;
        	this.expression = expression;
        	this.type = type;
    	}

    	public String getIdentifier() { return identifier; }
    	public Exp getExpression() { return expression; }
    	public String getType() { return type; }

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
	T visit(AST.FactorExp f, Env env);
   	T visit(AST.GroupExp g, Env env);
    	T visit(AST.UnaryOpExp o, Env env);

	}
}
