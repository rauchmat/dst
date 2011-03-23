package dst1.interceptor;

import org.hibernate.EmptyInterceptor;

public class SQLInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 3894614218727237142L;

	public String onPrepareStatement(String sql) {
		
		// TODO

		return sql;
	}

}
