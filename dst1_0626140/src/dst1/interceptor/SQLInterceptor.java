package dst1.interceptor;

import org.hibernate.EmptyInterceptor;

public class SQLInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 3894614218727237142L;
	private static int selectCount = 0;

	public String onPrepareStatement(String sql) {
		if (sql.trim().toLowerCase().startsWith("select"))
			incrementSelectCounter();

		return sql;
	}

	private static synchronized void incrementSelectCounter() {
		SQLInterceptor.selectCount = getSelectCount() + 1;
	}

	public static synchronized void resetSelectCounter() {
		SQLInterceptor.selectCount = 0;
	}

	public static int getSelectCount() {
		return selectCount;
	}

}
