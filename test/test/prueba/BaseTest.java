package test.prueba;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import util.Evidencia;
import util.FunctionGeneric;
import util.LeerExcel;

public abstract class BaseTest {
	protected LeerExcel excel;
	protected String[][] matriz;
	protected Evidencia evi;
	protected String nameClass, lab, rutaAlm, estado, pathResultados;
	protected boolean flagState = true;

	@BeforeClass
	public void beforeClass() {
		try {
			excel = new LeerExcel();
			evi = new Evidencia();
			nameClass = this.getClass().getName().substring(this.getClass().getPackage().getName().length() + 1,
					this.getClass().getName().length());
			lab = excel.valorCol("LABORATORIO", matriz);
			rutaAlm = excel.valorCol("RUTA_ALM", matriz);
			pathResultados = rutaAlm + "\\" + lab + "\\";
			matriz = LeerExcel.retornaDatosExcel(this.getClass().getPackage().getName(), nameClass);
			FunctionGeneric.setupDriver("chrome");
			FunctionGeneric.driver.get(excel.valorCol("Url", matriz));
			// FunctionGeneric.driver.manage().window().maximize();

		} catch (Exception e) {
			System.out.println("Error BeforeClass: " + e.getMessage());
		}
	}

	@AfterClass
	public void afterClass() {

		try {

			FunctionGeneric.closeWindows(FunctionGeneric.driver, 0);
			evi.createPDF(FunctionGeneric.arrEvidencia, nameClass, pathResultados, flagState);
			// FunctionGeneric.updateStateTestCase(flagState, nameClass,
			// FunctionGeneric.arrEvidencia);
			// FunctionGeneric.moveFileXLSX(pathResultados, nameClass);
			System.exit(0);

		} catch (Exception e) {
			System.out.println("Error AfterClass: " + e.getMessage());
		}
	}
}
