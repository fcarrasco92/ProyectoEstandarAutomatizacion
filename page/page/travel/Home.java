package page.travel;

import org.openqa.selenium.WebDriver;

import util.FunctionGeneric;

public class Home {
	WebDriver driver;

	public Home(WebDriver driver) {
		this.driver = driver;
	}

	public String login(String email, String password) {
		String msg = "OK";
		try {
			// System.out.println(this.driver.getPageSource());
			msg = FunctionGeneric.setTextObject("Campo Usuario", "name", "email", email, "set", false, this.driver);
			if (!msg.equals("OK"))
				return msg;
			msg = FunctionGeneric.setTextObject("Campo Password", "name", "password", password, "set", false,
					this.driver);
			if (!msg.equals("OK"))
				return msg;

			FunctionGeneric.addScreenEvi("Login", "Pass");
			msg = FunctionGeneric.clickObject("Botón Login", "css",
					"button.btn.btn-primary.btn-block.ladda-button.fadeIn.animated", "click", this.driver);
			if (!msg.equals("OK"))
				return msg;

			// Validar que realizó Login correctamente
			if (!FunctionGeneric.findObject("link", "Profile", driver))
				msg = "No se ha desplagado pantalla inicio Correctamente";

		} catch (Exception e) {
			System.out.println("Error en el login " + e.toString());
			msg = "Error en el login";
		}
		return msg;
	}

	public String validarURL(String title) {
		String msg = "OK";
		try {
			if (!title.equals(driver.getTitle()))
				msg = "No se ha logrado acceder a la URL " + title;
		} catch (Exception e) {
			System.out.println("Error al validar titulo " + e.toString());
			msg = "Error al validar titulo de la pagina";
		}

		return msg;
	}

}
