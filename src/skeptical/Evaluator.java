package skeptical;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static skeptical.AST.*;
import static skeptical.Value.*;
import skeptical.Env.*; 
import skeptical.ASTNode.*;


// Base AST Node
abstract class ASTNode {}

// Statements
class Program extends ASTNode {
    public Division division;

    public Program(Division division) {
        this.division = division;
    }
}

abstract class Division extends ASTNode {}

class StaDiv extends Division {
    public ArrayList<StaDecl> idStatements;

    public StaDiv(ArrayList<StaDecl> idStatements) {
        this.idStatements = idStatements;
    }
}

class DynDiv extends Division {
    public ArrayList<Statement> statements;

    public DynDiv(ArrayList<Statement> statements) {  // Add the constructor
        this.statements = statements;
    }
}


class StaDecl extends ASTNode {
    public String Identifier;

    public StaDecl(String identifier) {
        this.Identifier = Identifier;
    }
}

abstract class Statement extends ASTNode {}

class Assign extends Statement {
    public String identifier;
    public Expression expression;
    public String term;

    public Assign(String identifier, Expression expression, String term) {
        this.Identifier = identifier;
        this.expression = expression;
        this.term = term;
    }
}

class Print extends Statement {
    public Output output;

    public Print(Output output) {
        this.output = output;
    }
}

class Input extends Statement {
    public String identifier;
    public String prompt;

    public Input(String identifier, String prompt) {
        this.identifier = identifier;
        this.prompt = prompt;
    }
}


class IfStmt extends Statement {
    public Expression condition;
    public ArrayList<Statement> trueBranch;
    public ArrayList<Statement> falseBranch;

    public IfStmt(Expression condition, ArrayList<Statement> trueBranch, ArrayList<Statement> falseBranch) {
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }
}

class LoopStmt extends Statement {
    public String Identifier;
    public int start;
    public int end;
    public ArrayList<Statement> body;

    public LoopStmt(String identifier, int start, int end, ArrayList<Statement> body) {
        this.Identifier = Identifier;
        this.start = start;
        this.end = end;
        this.body = body;
    }
}

class CallStmt extends Statement {
    public String functionName;
    public ArrayList<Expression> arguments;

    public CallStmt(String functionName, V<Expression> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }
}

class FuncDef extends Statement {
    public String functionName;
    public ArrayList<Expression> arguments;
    public ArrayList<Statement> body;
    public String returnVar;

    public FuncDef(String functionName, ArrayList<Expression> arguments, ArrayList<Statement> body, String returnVar) {
        this.functionName = functionName;
        this.arguments = arguments;
        this.body = body;
        this.returnVar = returnVar;
    }
}

class Rand extends Statement {
    public String identifier;
    public int min;
    public int max;

    // Constructor
    public Rand(String iIentifier, int min, int max) {
        this.Identifier = identifier;
        this.min = min;
        this.max = max;
    }

    // Method to generate a random number
    public int generateRandom() {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;  // Returns a value between min and max, inclusive
    }
}
// Expressions
abstract class Expression extends ASTNode {}

class Disjunction extends Expression {
    public ArrayList<Conjunction> conjunctions;

    public Disjunction(ArrayList<Conjunction> conjunctions) {
        this.conjunctions = conjunctions;
    }
}

class Conjunction extends Expression {
    public ArrayList<Comparison> comparisons;

    public Conjunction(ArrayList<Comparison> comparisons) {
        this.comparisons = comparisons;
    }
}

class Comparison extends Expression {
    public Sum left;
    public String operator;
    public Sum right;

    public Comparison(Sum left, String operator, Sum right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}

class Sum extends Expression {
    public ArrayList<Term> terms;

    public Sum(ArrayList<Term> terms) {
        this.terms = terms;
    }
}

class Term extends Expression {
    public ArrayList<Power> powers;

    public Term(ArrayList<Power> powers) {
        this.powers = powers;
    }
}

class Power extends Expression {
    public Factor base;
    public Power exponent;

    public Power(Factor base, Power exponent) {
        this.base = base;
        this.exponent = exponent;
    }
}

class Factor extends Expression {
    public String value;

    public Factor(String value) {
        this.value = value;
    }
}

class NumberLiteral extends Expression {
    public double value;

    public NumberLiteral(double value) {
        this.value = value;
    }
}

class Identifier extends Expression {
    public String name;

    public Identifier(String name) {
        this.name = name;
    }
}

class Output extends Expression {
    public String value;

    public Output(String value) {
        this.value = value;
    }
}
