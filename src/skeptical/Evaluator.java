package skeptical;
import static skeptical.AST.*;
import static skeptical.Value.*;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

import skeptical.Env.*;


public class Evaluator implements Visitor<Value> {
	
	Printer.Formatter ts = new Printer.Formatter();

	Env initEnv = initialEnv(); //New for definelang
	
	Value valueOf(Program p) {
			return (Value) p.accept(this, initEnv);
	}

  private Env initialEnv() {
		GlobalEnv initEnv = new GlobalEnv();
		
		/* Procedure: (read <filename>). Following is same as (define read (lambda (file) (read file))) */
		List<String> formals = new ArrayList<>();
		formals.add("file");
		Exp body = new AST.ReadExp(new VarExp("file"));
		Value.FunVal readFun = new Value.FunVal(initEnv, formals, body);
		initEnv.extend("read", readFun);

		/* Procedure: (require <filename>). Following is same as (define require (lambda (file) (eval (read file)))) */
		formals = new ArrayList<>();
		formals.add("file");
		body = new EvalExp(new AST.ReadExp(new VarExp("file")));
		Value.FunVal requireFun = new Value.FunVal(initEnv, formals, body);
		initEnv.extend("require", requireFun);
		
		/* Add new built-in procedures here */ 
		
		return initEnv;
	}

	@Override
	public Value visit(AST.AddExp e, Env env) {
		NumValue left = (NumValue) e.left().accept(this, env);
		NumValue right = (NumValue) e.right().accept(this, env);
		return new NumValue(left.v() + right.v());
	}

	@Override
	public Value visit(AST.SubExp e, Env env) {
		NumValue left = (NumValue) e.left().accept(this, env);
		NumValue right = (NumValue) e.right().accept(this, env);
		return new NumValue(left.v() - right.v());
	}

	@Override
	public Value visit(AST.MultExp e, Env env) {
		NumValue left = (NumValue) e.left().accept(this, env);
		NumValue right = (NumValue) e.right().accept(this, env);
		return new NumValue(left.v() * right.v());
	}

	@Override
	public Value visit(AST.DivExp e, Env env) {
		NumValue left = (NumValue) e.left().accept(this, env);
		NumValue right = (NumValue) e.right().accept(this, env);
		return new NumValue(left.v() / right.v());
	}

	@Override
	public Value visit(AST.PowExp e, Env env) {
		NumValue left = (NumValue) e.left().accept(this, env);
		NumValue right = (NumValue) e.right().accept(this, env);
		return new NumValue(Math.pow(left.v(), right.v()));
	}

	@Override
	public Value visit(AST.EqExp e, Env env) {
		NumValue left = (NumValue) e.left().accept(this, env);
		NumValue right = (NumValue) e.right().accept(this, env);
		return new NumValue(left.v() == right.v());
	}

	@Override
	public Value visit(AST.NeqExp e, Env env) {
		NumValue left = (NumValue) e.left().accept(this, env);
		NumValue right = (NumValue) e.right().accept(this, env);
		return new NumValue(left.v() != right.v());
	}

	@Override
	public Value visit(AST.LessExp e, Env env) {
		NumValue left = (NumValue) e.left().accept(this, env);
		NumValue right = (NumValue) e.right().accept(this, env);
		return new NumValue(left.v() < right.v());
	}

	@Override
	public Value visit(AST.GreaterExp e, Env env) {
		NumValue left = (NumValue) e.left().accept(this, env);
		NumValue right = (NumValue) e.right().accept(this, env);
		return new NumValue(left.v() > right.v());
	}
	
	Reader _reader; 
	public Evaluator(Reader reader) {
		_reader = reader;
	}
}
