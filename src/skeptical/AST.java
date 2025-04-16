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
        List<Division> divisions;

        public Program(List<Division> divisions) {
		this.divisions = divisions;
        }

        public List<Division> getDivisions() {
            return divisions;
        }

        public <T> T accept(Visitor<T> visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    public static abstract class Division extends ASTNode {}

    public static class StaDiv extends Division {
        List<IDStatement> statements;

        public StaDiv(List<IDStatement> statements) {
            this.statements = statements;
        }

        public List<IDStatement> getStatements() {
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

    public static abstract class IDStatement extends ASTNode {}


    public static class ProgId extends IDStatement {
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

    public static class Auth extends IDStatement {
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

    public static class Date extends IDStatement {
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

    public static class Const extends IDStatement {
        String identifier;
        Exp expression;

        public Const(String identifier, Exp expression) {
            this.identifier = identifier;
            this.expression = expression;
        }

        public String getIdentifier() {
            return identifier;
        }

        public Exp getExpression() {
            return expression;
        }

        public <T> T accept(Visitor<T> visitor, Env env) {
            return visitor.visit(this, env);
        }
    }
    public static abstract class Exp extends ASTNode {}

    public static class Ident extends Exp {
        String name;

        public Ident(String name) {
            this.name = name;
        }

        public String  getName() {
            return name;
        }

        public <T> T accept(Visitor<T> visitor, Env env) {
            return visitor.visit(this, env);
        }
    }

    public static abstract class Statement extends ASTNode {}

    public interface Visitor<T> {
        T visit(Program p, Env env);
        T visit(StaDiv d, Env env);
        T visit(DynDiv d, Env env);
        T visit(ProgId is, Env env);
        T visit(Auth is, Env env);
        T visit(Date is, Env env);
        T visit(Const e, Env env);
	T visit(Ident v, Env env);
	}
}
