package skeptical;

import java.util.ArrayList;
import java.util.List;


public interface AST {
	public static abstract class ASTNode {
		public abstract <T> T accept(Visitor<T> visitor);
	}
    public static class Program extends ASTNode {
        

