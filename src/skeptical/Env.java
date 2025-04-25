package skeptical;

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
	 * Uses a fixed-size array to simulate a map structure.
	 */
	public static class GlobalEnv implements Env {
		private static final int MAX_BINDINGS = 100;
		private final String[] keys;
		private final Value[] values;
		private int size;

		public GlobalEnv() {
			keys = new String[MAX_BINDINGS];
			values = new Value[MAX_BINDINGS];
			size = 0;
		}

		public synchronized Value get(String searchVar) {
			for (int i = 0; i < size; i++) {
				if (keys[i].equals(searchVar)) {
					return values[i];
				}
			}
			throw new LookupException("No binding found for name: " + searchVar);
		}

		public synchronized void extend(String var, Value val) {
			for (int i = 0; i < size; i++) {
				if (keys[i].equals(var)) {
					values[i] = val; // overwrite existing
					return;
				}
			}
			if (size < MAX_BINDINGS) {
				keys[size] = var;
				values[size] = val;
				size++;
			} else {
				throw new RuntimeException("GlobalEnv capacity exceeded.");
			}
		}

		public boolean isEmpty() {
			return size == 0;
		}
	}
}
