package test.prueba;

import org.testng.annotations.Test;

import page.travel.Home;
import util.FunctionGeneric;

public class PruebaLogin extends BaseTest {
	public PruebaLogin() {
		super();
	}

	@Test
	public void test() {
		try {
			String estado;
			Home home = new Home(FunctionGeneric.driver);
			Thread.sleep(5000);
			// System.out.println(FunctionGeneric.driver.getPageSource());
			estado = home.validarURL("Administator Login");
			if (!FunctionGeneric.stateStep("Validar el titulo de la pagina", estado)) {
				flagState = false;
				afterClass();
			}
			estado = home.login(excel.valorCol("Usuario", matriz), excel.valorCol("Password", matriz));
			if (!FunctionGeneric.stateStep("Login", estado)) {
				flagState = false;
				afterClass();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error en el caso " + e.toString());
		}
	}
}
