package dst3.util;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

public class FacesUtil {
	public static void setResult(String messageKey) {
		Flash flash = FacesContext.getCurrentInstance().getExternalContext()
				.getFlash();
		flash.put("result", getMessage(messageKey));
	}

	public static String getMessage(String key) {
		return getMessages().getString(key);
	}

	private static ResourceBundle getMessages() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().getResourceBundle(context, "m");
	}

	public static void displayResult(String message) {
		displayResult(message, null);
	}

	public static void displayResult(String message, String nextView) {
		try {
			FacesUtil.setResult(message);
			if (nextView != null) {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(nextView);
			}
		} catch (IOException e) {
		}
	}
}
