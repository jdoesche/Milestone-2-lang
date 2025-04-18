package skeptical;

import java.util.*;
import static skeptical.AST.*;
import static skeptical.Value.*;

public class Evaluator {
    private final Map<String, Double> env = new HashMap<>();
    private final Map<String, FuncDef> functions = new HashMap<>();

    public void evaluate(Program program) {
        if (program.division instanceof StaDiv) {
            StaDiv staDiv = (StaDiv) program.division;
            for (StaDecl decl : staDiv.idStatements) {
                env.put(decl.Identifier, 0.0);
            }
        } else if (program.division instanceof DynDiv) {
            DynDiv dynDiv = (DynDiv) program.division;
            for (Statement stmt : dynDiv.statements) {
                evaluateStatement(stmt);
            }
        }
    }

    private void evaluateStatement(Statement stmt) {
        if (stmt instanceof Assign) {
            Assign assign = (Assign) stmt;
            double val = evaluateExpression(assign.expression);
            env.put(assign.Identifier, val);
        } else if (stmt instanceof Print) {
            Print print = (Print) stmt;
            System.out.println(print.output.value);
        } else if (stmt instanceof Input) {
            Input input = (Input) stmt;
            Scanner scanner = new Scanner(System.in);
            System.out.print(input.prompt + ": ");
            double val = scanner.nextDouble();
            env.put(input.identifier, val);
        } else if (stmt instanceof IfStmt) {
            IfStmt ifStmt = (IfStmt) stmt;
            if (evaluateExpression(ifStmt.condition) != 0) {
                for (Statement s : ifStmt.trueBranch) evaluateStatement(s);
            } else {
                for (Statement s : ifStmt.falseBranch) evaluateStatement(s);
            }
        } else if (stmt instanceof LoopStmt) {
            LoopStmt loop = (LoopStmt) stmt;
            for (int i = loop.start; i <= loop.end; i++) {
                env.put(loop.Identifier, (double) i);
                for (Statement s : loop.body) evaluateStatement(s);
            }
        } else if (stmt instanceof Rand) {
            Rand rand = (Rand) stmt;
            int val = rand.generateRandom();
            env.put(rand.Identifier, (double) val);
        } else if (stmt instanceof FuncDef) {
            FuncDef def = (FuncDef) stmt;
            functions.put(def.functionName, def);
        } else if (stmt instanceof CallStmt) {
            CallStmt call = (CallStmt) stmt;
            FuncDef def = functions.get(call.functionName);
            if (def == null) throw new RuntimeException("Function not found: " + call.functionName);

            Map<String, Double> savedEnv = new HashMap<>(env);

            for (int i = 0; i < def.arguments.size(); i++) {
                Expression argExpr = call.arguments.get(i);
                double argVal = evaluateExpression(argExpr);
                env.put(((Identifier) def.arguments.get(i)).name, argVal);
            }

            for (Statement s : def.body) evaluateStatement(s);

            double retVal = env.getOrDefault(def.returnVar, 0.0);
            env.clear();
            env.putAll(savedEnv);
            env.put(def.functionName, retVal);
        }
    }

    private double evaluateExpression(Expression expr) {
        if (expr instanceof NumberLiteral) {
            return ((NumberLiteral) expr).value;
        } else if (expr instanceof Identifier) {
            return env.getOrDefault(((Identifier) expr).name, 0.0);
        } else if (expr instanceof Comparison) {
            Comparison cmp = (Comparison) expr;
            double left = evaluateExpression(cmp.left);
            double right = evaluateExpression(cmp.right);
            switch (cmp.operator) {
                case "==": return left == right ? 1 : 0;
                case "!=": return left != right ? 1 : 0;
                case ">": return left > right ? 1 : 0;
                case "<": return left < right ? 1 : 0;
                case ">=": return left >= right ? 1 : 0;
                case "<=": return left <= right ? 1 : 0;
                default: throw new RuntimeException("Unknown comparison: " + cmp.operator);
            }
        } else if (expr instanceof Sum) {
            double sum = 0;
            for (Term term : ((Sum) expr).terms) {
                sum += evaluateExpression(term);
            }
            return sum;
        } else if (expr instanceof Term) {
            double product = 1;
            for (Power power : ((Term) expr).powers) {
                product *= evaluateExpression(power);
            }
            return product;
        } else if (expr instanceof Power) {
            Power p = (Power) expr;
            double base = evaluateExpression(p.base);
            double exponent = p.exponent != null ? evaluateExpression(p.exponent) : 1;
            return Math.pow(base, exponent);
        } else if (expr instanceof Factor) {
            String val = ((Factor) expr).value;
            try {
                return Double.parseDouble(val);
            } catch (NumberFormatException e) {
                return env.getOrDefault(val, 0.0);
            }
        }
        return 0;
    }
}
