package skeptical;

import java.util.Hashtable;

/**
 * Representation of an environment for the Skeptical language,
 * which maps variable names to runtime values.
 * 
 * Compatible with the AST structure and visitor-based interpretation.
 */
public interface Env {
	Value get(String searchVar);
	boolean isEmpty();

	@SuppressWarnings("serial")
	public static class LookupException extends RuntimeException {
		public LookupException(String message) {
			super(message);
		}
	}

	/**
	 * Represents an empty environment (base case for scoping).
	 */
	public static class EmptyEnv implements Env {
		public Value get(String searchVar) {
			throw new LookupException("No binding found for name: " + searchVar);
		}

		public boolean isEmpty() {
			return true;
		}
	}

	/**
	 * Represents an environment extended with a single variable binding.
	 */
	public static class ExtendEnv implements Env {
		private final Env savedEnv;
		private final String var;
		private final Value val;

		public ExtendEnv(Env savedEnv, String var, Value val) {
			this.savedEnv = savedEnv;
			this.var = var;
			this.val = val;
		}

		public synchronized Value get(String searchVar) {
			if (searchVar.equals(var))
				return val;
			return savedEnv.get(searchVar);
		}

		public boolean isEmpty() {
			return false;
		}

		public Env savedEnv() {
			return savedEnv;
		}

		public String var() {
			return var;
		}

		public Value val() {
			return val;
		}
	}

	/**
	 * Represents a global environment with mutable bindings.
	 */
	public static class GlobalEnv implements Env {
		private final Hashtable<String, Value> map;

		public GlobalEnv() {
			map = new Hashtable<>();
		}

		public synchronized Value get(String searchVar) {
			if (map.containsKey(searchVar))
				return map.get(searchVar);
			throw new LookupException("No binding found for name: " + searchVar);
		}

		public synchronized void extend(String var, Value val) {
			map.put(var, val);
		}

		public boolean isEmpty() {
			return map.isEmpty();
		}
	}
}
