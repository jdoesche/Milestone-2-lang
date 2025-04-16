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


    public static abstract class Statement extends ASTNode {}

    public interface Visitor<T> {
        T visit(AST.Program p, Env env);
    	T visit(AST.StaticDivision d, Env env);
    	T visit(AST.DynamicDivision d, Env env);
        T visit(AST.ProgId is, Env env);
        T visit(AST.Auth is, Env env);
        T visit(AST.Date is, Env env);
        T visit(AST.Const e, Env env);
	T visit(AST.IdExp v, Env env);
	}
}
