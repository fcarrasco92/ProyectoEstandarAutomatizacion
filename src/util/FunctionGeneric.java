package util;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FunctionGeneric {
	public static ArrayList<String> arrEvidencia = new ArrayList<String>();
	// protected static final String CUR_DIR = System.getProperty("user.dir");
	public static WebDriver driver;

	// static String curDir = System.getProperty("user.dir");
	private static KeyboardClass keyBoa;

	public void deleteFile(String nameFile, String path) {
		File file = new File("C:\\desarrollos\\Ejecucion_Automatizada\\" + path + "HTML\\" + nameFile + ".html");
		if (file.exists())
			file.delete();
	}

	public static void closeWindows(WebDriver driver, int winEsperadas) {

		try {

			closeAlert(driver);
			Set<String> winSet = driver.getWindowHandles();
			List<String> winList = new ArrayList<String>(winSet);

			while (winList.size() != winEsperadas) {
				driver.switchTo().window(winList.get(winList.size() - 1));
				driver.close();
				winSet = driver.getWindowHandles();
				winList = new ArrayList<String>(winSet);
			}

		} catch (Exception e) {

		}
	}

	public static void closeAlert(WebDriver driver) {
		try {
			if (validaAlert(driver)) {
				driver.switchTo().alert().accept();
			}
		} catch (Exception e) {
			System.out.println("Close Alert: " + e.getMessage());
		}
	}

	public static void dismissAlert(WebDriver driver) {
		try {
			if (validaAlert(driver)) {
				driver.switchTo().alert().dismiss();
			}
		} catch (Exception e) {
			System.out.println("Close Alert: " + e.getMessage());
		}
	}

	public static boolean validaAlert(WebDriver driver) {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException Ex) {
			return false;
		}
	}

	public static void setupDriver(String browserType, String appURL) {
		executeCMD("taskkill -f -im cmd.exe");
		if (browserType.equals("firefox")) {
			driver = initFirefoxDriver(appURL);
		} else if (browserType.equals("chrome")) {
			driver = initChromeDriver(appURL);
		} else if (browserType.equals("ie")) {
			driver = initIExplorerDriver(appURL);
		} else {
			throw new RuntimeException("Browser type unsupported : " + browserType);
		}
	}

	public static void setupDriver(String browserType) {
		FunctionGeneric.executeCMD("taskkill -f -im cmd.exe");
		try {
			if (browserType.equalsIgnoreCase("chrome")) {
				executeCMD("taskkill -f -im chromedriver.exe");
				// System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");
				System.setProperty("webdriver.chrome.driver", "C:\\desarrollos\\Drivers/chromedriver.exe");
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--start-maximized");
				options.addArguments("--disable-popup-blocking");
				driver = new ChromeDriver(options);
			} else if (browserType.equalsIgnoreCase("phantom")) {
				// executeCMD("taskkill -f -im chromedriver.exe");
				System.setProperty("phantomjs.binary.path", "./driver/phantomjs.exe");
				driver = new PhantomJSDriver();
			} else if (browserType.equalsIgnoreCase("ie")) {
				// executeCMD("taskkill -f -im IEDriverServer.exe");
				System.setProperty("webdriver.ie.driver", "./driver/IEDriverServer.exe");
				DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
				capabilities.setCapability("requireWindowFocus", true);
				capabilities.setCapability("ignoreZoomSetting", true);
				driver = new InternetExplorerDriver(capabilities);
			} else if (browserType.equalsIgnoreCase("firefox")) {
				FirefoxOptions options = new FirefoxOptions().setLegacy(true);
				System.setProperty("webdriver.gecko.driver", "C:/desarrollos/driver/geckodriver.exe");
				driver = new FirefoxDriver(options);
			} else {
				throw new Exception("Opcion no valida");
			}
		} catch (Exception e) {
			System.out.println("Error al abrir navegador " + e.toString());
			throw new RuntimeException("Browser type unsupported : " + browserType);

		}
	}

	public static WebDriver initFirefoxDriver(String appURL) {
		executeCMD("taskkill -f -im firefox.exe");
		// String path = CUR_DIR + "\\Drivers\\geckodriver.exe";
		// System.setProperty("webdriver.gecko.driver", path);
		System.out.println("Launching Firefox browser..");
		WebDriver driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.navigate().to(appURL);
		return driver;
	}

	public static WebDriver initChromeDriver(String appURL) {
		executeCMD("taskkill -f -im chrome.exe");
		System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		// driver.manage().window().maximize();
		driver.navigate().to(appURL);
		return driver;
	}

	public static WebDriver initIExplorerDriver(String appURL) {
		try {
			executeCMD("taskkill -f -im iexplore.exe");
			executeCMD("taskkill -f -im IEDriverServer.exe");
			// String path = CUR_DIR + "\\Drivers\\IEDriverServer.exe";
			// String path = "c:\\desarrollos\\Drivers\\IEDriverServer.exe";
			// System.setProperty("webdriver.ie.driver", path);
			// System.setProperty("webdriver.ie.driver",
			// "C:\\desarrollos\\Drivers\\IEDriverServer.exe");
			System.out.println("Launching IExplorer browser..");
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			capabilities.setCapability("requireWindowFocus", true);
			capabilities.setCapability("ignoreZoomSetting", true);
			capabilities.setCapability("nativeEvents", false);

			System.out.println("Antes de crear el obj driver");

			WebDriver driver = new InternetExplorerDriver(capabilities);
			Thread.sleep(60000);
			System.out.println("despues de crear el objdriver");
			driver.manage().window().maximize();
			driver.navigate().to(appURL);
			System.out.println("PASA AAAAA");
		} catch (Exception e) {
			System.out.println("Error abrir url " + e.getMessage());
		}
		// driver.get(appURL);
		return driver;
	}

	public static Boolean validatePopUp(WebDriver driver) {

		Boolean exists = false;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5 /* timeout in seconds */);
			if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
				// System.out.println("alert was present");
				exists = true;
			}
		} catch (Exception e) {

		}
		return exists;
	}

	public static void executeCMD(String cmd) {
		try {

			Runtime.getRuntime().exec(cmd);
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

	public static void waitTOC(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje);
	}

	public static boolean waitWindows(int window, List<String> winList, WebDriver driver) throws InterruptedException {

		Set<String> winSet = driver.getWindowHandles();
		int contador = 0;
		boolean flag = true;

		while (winList.size() != window) {
			Thread.sleep(2000);
			winSet = driver.getWindowHandles();
			winList = new ArrayList<String>(winSet);
			contador++;
			if (contador == 20) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	public void table() {
		WebElement table = driver.findElement(By.id("BarraMenuBarraMenuAdmisión"));
		List<WebElement> childTable = table.findElements(By.tagName("td"));
		for (WebElement trElement : childTable) {
			if (trElement.getText().equals("Evaluador")) {
				trElement.click();
			} else if (trElement.getText().equals("Evaluador de Evrie.Solicitudes_Evrie")) {
				trElement.click();
			}
		}
	}

	public static WebDriver waitWindows(int windowsWait, WebDriver driver) {

		try {

			Set<String> winSet = driver.getWindowHandles();
			List<String> winList = new ArrayList<String>(winSet);

			if (windowsWait > 0) {
				if (!waitWindows(windowsWait, winList, driver))
					return driver;

				winSet = driver.getWindowHandles();
				winList = new ArrayList<String>(winSet);
			}

			driver.switchTo().window(winList.get(winList.size() - 1));

		} catch (Exception e) {
			System.out.println("Las ventanas esperadas no se han desplegado correctamente");
		}

		return driver;
	}

	public static String Screenshot() {

		String ruta = "C:\\desarrollos\\Ejecucion_Automatizada\\Print\\";
		File folder = new File(ruta);

		if (!folder.exists())
			folder.mkdirs();

		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String strDate = sdf.format(cal.getTime());
			Thread.sleep(1500);
			BufferedImage image = new Robot()
					.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			strDate = strDate.replaceAll("/", "");
			strDate = strDate.replaceAll(":", "");
			ruta = ruta + strDate + ".png";
			ImageIO.write(image, "png", new File(ruta));

		} catch (Exception e) {
			System.out.println("Error al crear Screenshot: " + e.getMessage());
		}

		return ruta;
	}

	public static String validaMensajeAlert(String mensaje, WebDriver driver) {

		String flag = "OK";

		try {

			WebDriverWait wait = new WebDriverWait(driver, 30);
			Alert myAlert = wait.until(ExpectedConditions.alertIsPresent());

			if (!myAlert.getText().contains(mensaje)) {
				flag = "Mensaje recibido : " + myAlert.getText();
				return flag;
			}
			myAlert.accept();

		} catch (Exception e) {
			flag = "La ventana parav alidar mensaje no se encuentra visible";
			System.out.println("Error Validar Mensaje Alert");
		}

		return flag;
	}

	public static int returnPositionDataCBO(Select combobox, String value) {
		int position = 0;
		try {
			List<WebElement> options = ((WebDriver) combobox).findElements(By.tagName("option"));

			for (WebElement option : options) {

				if (value.equalsIgnoreCase(option.getText().trim())) {
					System.out.println(option.getText().trim());
					position++;
					break;
				} else {
					position++;
				}

			}
		} catch (Exception e) {
			System.out.println("Error Combobox indice " + e.toString());
		}
		return position - 1;
	}

	public static int returnPositionDataCBOEquals(WebElement combobox, String value) {

		int position = 0;
		boolean flag = false;

		try {

			List<WebElement> options = combobox.findElements(By.tagName("option"));

			for (int i = 0; i < options.size(); i++) {
				// System.out.println(options.get(i).getText().trim());
				if (options.get(i).getText().trim().equals(value.trim())) {
					position = i;
					flag = true;
					break;
				}
			}

			if (flag == false)
				position = -1;

		} catch (Exception e) {
			System.out.println("Error Combobox indice " + e.toString());
		}
		return position;
	}

	public static int returnPositionDataCBO(WebElement combobox, String value) {

		int position = 0;
		boolean flag = false;

		try {

			List<WebElement> options = combobox.findElements(By.tagName("option"));

			for (int i = 0; i < options.size(); i++) {
				// System.out.println(options.get(i).getText());
				if (options.get(i).getText().trim().contains(value.trim())) {
					position = i;
					flag = true;
					break;
				}
			}

			if (flag == false)
				position = -1;

		} catch (Exception e) {
			System.out.println("Error Combobox indice " + e.toString());
		}
		return position;
	}

	public static String returnItemActual(WebElement combobox) {

		String valor = "";

		try {
			Select option = new Select(combobox);
			WebElement optionElment = option.getFirstSelectedOption();
			valor = optionElment.getText();
		} catch (Exception e) {
			System.out.println("Error Item Acutal Combobox " + e.toString());
		}
		return valor;
	}

	public static String validarTexto(String valor, String resultado, WebDriver driver) {

		String msg = "OK";

		try {

			if (!driver.getPageSource().contains(valor))
				msg = "Error en " + resultado;

		} catch (Exception ex) {
			msg = "Error en " + resultado;
		}
		return msg;
	}

	public static void cerraALTF4() {
		try {

			keyBoa = new KeyboardClass();
			Robot robot = new Robot();

			keyBoa.KeyPressTecl("ALT");
			keyBoa.KeyPressTecl("F4");

			robot.keyRelease(KeyEvent.VK_ALT);
			robot.keyRelease(KeyEvent.VK_F4);
			Thread.sleep(2000);

		} catch (Exception e) {
			System.out.println("Error Cerrar ALT F4: " + e.getMessage());
		}
	}

	/*
	 * public static boolean stateStep(String paso, String estado, ITestCaseRun
	 * ITestCaseRun, ALMServiceWrapper wrapper) {
	 * 
	 * alm = new ALM(); boolean flag = true;
	 * 
	 * try {
	 * 
	 * if (estado.equals("OK")) { arrEvidencia.add(paso + "-" + Screenshot() + "-" +
	 * "Pass"); alm.addExecutionSteps(wrapper, "Pass", paso, ITestCaseRun); } else {
	 * arrEvidencia.add(paso + "-" + Screenshot() + "-" + "Fail" + "-" + estado);
	 * alm.addExecutionSteps(wrapper, "Fail", paso, ITestCaseRun); flag = false; }
	 * 
	 * } catch (Exception e) { System.out.println("Error en el estado del paso"); }
	 * return flag; }
	 */

	public static String clickObject(String nombreObj, String buscarPor, String value, String accion,
			WebDriver driver) {

		String msg = "OK";
		WebElement obj = null;

		try {

			switch (buscarPor) {
			case "name":
				obj = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By.name(value)));
				break;

			case "id":
				obj = (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.id(value)));
				break;

			case "value":
				obj = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By.tagName(value)));
				break;

			case "link":
				obj = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By.linkText(value)));
				break;

			case "css":
				obj = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(value)));
			default:
				break;
			}

			if (obj.isDisplayed()) {
				if (accion.equals("click")) {
					obj.click();
				} else if (accion.equals("dbclick")) {
					//
					Actions ac = new Actions(driver);
					ac.doubleClick(obj).perform();
				} else if (accion.equals("enter")) {
					obj.sendKeys(Keys.ENTER);
				}
			} else {
				msg = "El objeto '" + nombreObj + "' no se ha encontrado";
				return msg;
			}

		} catch (Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado";
		}

		return msg;
	}

	public static String clickObjectByXpath(String nombreObj, String tipoObj, String propiedad, String value,
			String accion, WebDriver driver) {

		String msg = "OK";
		WebElement objByXpath;

		try {

			if (propiedad.equals("text")) {
				objByXpath = (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//" + tipoObj + "[" + propiedad + "() = '" + value + "']")));
			} else {
				objByXpath = (new WebDriverWait(driver, 30)).until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//" + tipoObj + "[@" + propiedad + "='" + value + "']")));
			}

			if (objByXpath.isDisplayed()) {
				if (accion.equals("click")) {
					objByXpath.click();
				} else if (accion.equals("dbclick")) {
					//
					Actions ac = new Actions(driver);
					ac.doubleClick(objByXpath).perform();
				} else if (accion.equals("enter")) {
					objByXpath.sendKeys(Keys.ENTER);
				}
			} else {
				msg = "El objeto '" + nombreObj + "' no se ha encontrado";
				return msg;
			}

		} catch (Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado";
		}

		return msg;
	}

	public static String setTextObject(String nombreObj, String buscarPor, String value, String valorSet, String accion,
			boolean vacio, WebDriver driver) {
		String msg = "OK";
		WebElement obj = null;

		try {

			switch (buscarPor) {
			case "name":
				obj = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By.name(value)));
				break;

			case "id":
				obj = (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.id(value)));
			default:
				break;
			}

			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", obj);

			if (obj.isDisplayed()) {
				if (accion.equals("click")) {
					obj.click();
				} else if (accion.equals("set")) {
					if (vacio) {
						if (obj.getAttribute("value").equals(""))
							obj.sendKeys(valorSet);
					} else {
						obj.sendKeys(valorSet);
					}
				} else if (accion.equals("enter")) {
					obj.sendKeys(Keys.ENTER);
				} else if (accion.equals("clean")) {
					obj.clear();
					// deleteAllText(obj);
				}
			} else {
				msg = "El objeto '" + nombreObj + "' no se ha encontrado";
				return msg;
			}

		} catch (Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado";
		}

		return msg;
	}

	public static String setTextObjectByXpath(String nombreObj, String tipoObj, String propiedad, String value,
			String valorSet, String accion, WebDriver driver) {

		String msg = "OK";

		try {

			WebElement objByXpath = (new WebDriverWait(driver, 30)).until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//" + tipoObj + "[@" + propiedad + "='" + value + "'")));

			if (objByXpath.isDisplayed()) {
				if (accion.equals("click")) {
					objByXpath.click();
				} else if (accion.equals("set")) {
					objByXpath.sendKeys(valorSet);
				} else if (accion.equals("enter")) {
					objByXpath.sendKeys(Keys.ENTER);
				}
			} else {
				msg = "El objeto '" + nombreObj + "' no se ha encontrado";
				return msg;
			}

		} catch (Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado";
		}

		return msg;
	}

	public static String setTextObjectByXpath(String nombreObj, String xpath, String valorSet, String accion,
			WebDriver driver) {

		String msg = "OK";

		try {

			WebElement objByXpath = (new WebDriverWait(driver, 30))
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

			if (objByXpath.isDisplayed()) {
				if (accion.equals("click")) {
					objByXpath.click();
				} else if (accion.equals("set")) {
					objByXpath.sendKeys(valorSet);
				}
			} else {
				msg = "El objeto '" + nombreObj + "' no se ha encontrado";
				return msg;
			}

		} catch (Exception e) {
			msg = "El objeto '" + nombreObj + "' no se ha encontrado";
		}

		return msg;
	}

	public static boolean findObject(String buscarPor, String value, WebDriver driver) {

		boolean msg = true;
		WebElement obj = null;

		try {

			switch (buscarPor) {
			case "name":
				obj = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By.name(value)));
				break;

			case "id":
				obj = (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.id(value)));
				break;

			case "tagname":
				obj = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By.tagName(value)));
				break;

			case "link":
				obj = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By.linkText(value)));
				break;
			case "class":
				obj = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By.className(value)));
				break;
			default:
				break;
			}

			if (!obj.isDisplayed()) {
				msg = false;
				return msg;
			}

		} catch (Exception e) {
			System.out.println("El objeto no se ha encontrado");
			msg = false;
		}

		return msg;
	}

	public static void addScreenEvi(String paso, String estado) {
		arrEvidencia.add(paso + "-" + FunctionGeneric.Screenshot() + "-" + estado);
	}

	public static String selectCBOEquals(String nombreObj, String buscarPor, String value, String dato, boolean vacio,
			WebDriver driver) {

		String msg = "OK";
		WebElement cbo = null;

		try {

			switch (buscarPor) {
			case "name":
				cbo = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By.name(value)));
				break;
			case "id":
				cbo = (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.id(value)));
				break;

			default:
				break;
			}

			if (cbo.isDisplayed()) {
				Select cboSelect = new Select(cbo);
				if (vacio) {
					if (returnItemActual(cbo).equals("")) {
						int index = FunctionGeneric.returnPositionDataCBOEquals(cbo, dato);
						cboSelect.selectByIndex(index);
					}
				} else {
					int index = FunctionGeneric.returnPositionDataCBOEquals(cbo, dato);
					cboSelect.selectByIndex(index);
				}

			} else {
				msg = "No se ha logrado seleccionar el dato '" + dato + "' en el combobox " + nombreObj;
			}

		} catch (Exception e) {
			msg = "No se ha logrado seleccionar el dato '" + dato + "' en el combobox " + nombreObj;
		}

		return msg;
	}

	public static String selecCBO(String nombreObj, String buscarPor, String value, String dato, boolean vacio,
			WebDriver driver) {

		String msg = "OK";
		WebElement cbo = null;

		try {

			switch (buscarPor) {
			case "name":
				cbo = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By.name(value)));
				break;
			case "id":
				cbo = (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.id(value)));
				break;
			default:
				break;
			}

			if (cbo.isDisplayed()) {
				Select cboSelect = new Select(cbo);
				if (vacio) {
					if (returnItemActual(cbo).equals("")) {
						int index = FunctionGeneric.returnPositionDataCBO(cbo, dato);
						cboSelect.selectByIndex(index);
					}
				} else {
					int index = FunctionGeneric.returnPositionDataCBO(cbo, dato);
					cboSelect.selectByIndex(index);
				}

			} else {
				msg = "No se ha logrado seleccionar el dato '" + dato + "' en el combobox " + nombreObj;
			}

		} catch (Exception e) {
			msg = "No se ha logrado seleccionar el dato '" + dato + "' en el combobox " + nombreObj;
		}

		return msg;
	}

	public static String selecCBOByVisibleText(String nombreObj, String buscarPor, String value, String dato,
			boolean vacio, WebDriver driver) {
		String msg = "OK";
		WebElement cbo = null;
		try {
			switch (buscarPor) {
			case "name":
				cbo = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By.name(value)));
				break;
			case "id":
				cbo = (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.id(value)));
				break;

			case "xpath":
				cbo = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By.xpath(value)));
				break;
			default:
				break;
			}

			if (cbo.isDisplayed()) {
				Select cboSelect = new Select(cbo);
				// if (vacio) {
				// if (returnItemActual(cbo).equals("")) {
				// int index = FunctionGeneric.returnPositionDataCBO(cbo, dato);
				// cboSelect.selectByIndex(index);
				// }
				// } else {
				// int index = FunctionGeneric.returnPositionDataCBO(cbo, dato);
				// cboSelect.selectByIndex(index);
				// }

				cboSelect.selectByVisibleText(dato);

			} else {
				msg = "No se ha logrado seleccionar el dato '" + dato + "' en el combobox " + nombreObj;
			}
		} catch (Exception e) {

		}
		return msg;
	}

	public static String selecCBOByIndex(String nombreObj, String buscarPor, String value, int index, boolean vacio,
			WebDriver driver) {
		String msg = "OK";
		WebElement cbo = null;
		try {
			switch (buscarPor) {
			case "name":
				cbo = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By.name(value)));
				break;
			case "id":
				cbo = (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.id(value)));
				break;

			case "xpath":
				cbo = (new WebDriverWait(driver, 30))
						.until(ExpectedConditions.presenceOfElementLocated(By.xpath(value)));
				break;
			default:
				break;
			}

			if (cbo.isDisplayed()) {
				Select cboSelect = new Select(cbo);
				// if (vacio) {
				// if (returnItemActual(cbo).equals("")) {
				// int index = FunctionGeneric.returnPositionDataCBO(cbo, dato);
				// cboSelect.selectByIndex(index);
				// }
				// } else {
				// int index = FunctionGeneric.returnPositionDataCBO(cbo, dato);
				// cboSelect.selectByIndex(index);
				// }

				cboSelect.selectByIndex(index);

			} else {
				msg = "No se ha logrado seleccionar el dato '" + index + "' en el combobox " + nombreObj;
			}
		} catch (Exception e) {

		}
		return msg;
	}

	public static String selecCBOXpath(String nombreObj, String tipoObj, String propiedad, String value, String dato,
			boolean vacio, WebDriver driver) {

		String msg = "OK";
		WebElement cbo = null;

		try {

			cbo = (new WebDriverWait(driver, 30)).until(ExpectedConditions
					.presenceOfElementLocated(By.xpath("//" + tipoObj + "[@" + propiedad + " ='" + value + "']")));

			if (cbo.isDisplayed()) {
				Select cboSelect = new Select(cbo);
				if (vacio) {
					if (returnItemActual(cbo).equals("")) {
						int index = FunctionGeneric.returnPositionDataCBO(cbo, dato);
						cboSelect.selectByIndex(index);
					}
				} else {
					int index = FunctionGeneric.returnPositionDataCBO(cbo, dato);
					cboSelect.selectByIndex(index);
				}

			} else {
				msg = "No se ha logrado seleccionar el dato '" + dato + "' en el combobox " + nombreObj;
			}

		} catch (Exception e) {
			msg = "No se ha logrado seleccionar el dato '" + dato + "' en el combobox " + nombreObj;
		}

		return msg;
	}

	public static void moveFileXLSX(String path, String nameFile) {
		String pathFinal = "C:\\desarrollos\\Ejecucion_Automatizada\\" + path + "DATOS\\" + nameFile + ".xlsx";
		try {
			File rutaOriginalFichero = new File(
					"C:\\desarrollos\\Ejecucion_Automatizada\\Datos_Temporales\\" + nameFile + ".xlsx");
			File rutaDestinoFichero = new File(pathFinal.replace(" ", "_"));

			if (rutaDestinoFichero.exists())
				rutaDestinoFichero.delete();

			boolean estatus = rutaOriginalFichero.renameTo(rutaDestinoFichero);

			if (!estatus)
				System.out.println("Error no se ha podido mover el  archivo del directorio");

		} catch (Exception e) {
			System.out.println("Error al mover el archivo");
		}
	}

	public static void updateStateTestCase(boolean estado, String caso) {
		if (estado) {
			LeerExcel.setTextRow("ESTADO", "Pass", caso);
		} else {
			LeerExcel.setTextRow("ESTADO", "Fail", caso);
		}

	}

	public static void deleteAllText(WebElement element) {
		try {
			Robot robot = new Robot();
			// element.click();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_A);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyRelease(KeyEvent.VK_A);
			robot.keyPress(KeyEvent.VK_BACK_SPACE);
			robot.keyRelease(KeyEvent.VK_BACK_SPACE);
		} catch (Exception e) {
			System.out.println("ERROR AL ELIMINAR TODO EL TEXTO DEL CAMPO  " + e.getMessage());
		}
	}

	public static boolean findObjectXpath(String tipoObj, String propiedad, String value, WebDriver driver) {

		boolean flag = true;
		WebElement objByXpath;

		try {

			if (propiedad.equals("text")) {
				objByXpath = (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(
						By.xpath("//" + tipoObj + "[" + propiedad + "() = '" + value + "']")));
			} else {
				objByXpath = (new WebDriverWait(driver, 30)).until(ExpectedConditions
						.presenceOfElementLocated(By.xpath("//" + tipoObj + "[@" + propiedad + " ='" + value + "']")));
			}

		} catch (Exception e) {
			flag = false;
		}

		return flag;
	}

	public static boolean stateStep(String paso, String estado) {

		boolean flag = true;

		try {

			if (estado.equals("OK")) {
				arrEvidencia.add(paso + "-" + Screenshot() + "-" + "Pass");
			} else {
				arrEvidencia.add(paso + "-" + Screenshot() + "-" + "Fail" + "-" + estado);
				flag = false;
			}

		} catch (Exception e) {
			System.out.println("Error en el estado del paso");
		}
		return flag;
	}

	public static void updateStateTestCase(boolean estado, String caso, ArrayList<String> arrStep) {

		String pasos = "";

		for (int i = 0; i < arrStep.size(); i++) {
			pasos = pasos + "&" + arrStep.get(i);

		}

		if (estado) {
			LeerExcel.setTextRow("ESTADO", "Pass", caso);
		} else {
			LeerExcel.setTextRow("ESTADO", "Fail", caso);
		}

		LeerExcel.setTextRow("PASOS", pasos, caso);

	}

	public static void irAlFondoDeLaPagina(WebDriver driver) {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	public static void irArribaDeLaPagina(WebDriver driver) {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(document.body.scrollHeight, 0)");
	}

	public static Actions reconocerObjetosDinamicos(String nombreObj, String buscarPor, String value,
			WebDriver driver) {
		Actions actions = null;
		WebElement obj = null;
		try {
			switch (buscarPor) {
			case "name":
				obj = (new WebDriverWait(driver, 15))
						.until(ExpectedConditions.presenceOfElementLocated(By.name(value)));
				break;

			case "id":
				obj = (new WebDriverWait(driver, 15)).until(ExpectedConditions.presenceOfElementLocated(By.id(value)));
				break;

			case "value":
				obj = (new WebDriverWait(driver, 15))
						.until(ExpectedConditions.presenceOfElementLocated(By.tagName(value)));
				break;

			case "link":
				obj = (new WebDriverWait(driver, 15))
						.until(ExpectedConditions.presenceOfElementLocated(By.linkText(value)));
				break;
			case "xpath":
				obj = (new WebDriverWait(driver, 15))
						.until(ExpectedConditions.presenceOfElementLocated(By.xpath(value)));
				break;
			default:
				break;
			}

			if (obj.isDisplayed()) {
				actions = new Actions(driver);
				actions.moveToElement(obj).perform();
			}
		} catch (Exception e) {
			System.out.println("ERROR no se reconoce el objeto " + nombreObj);
		}
		return actions;
	}

	public static boolean validarFecha(String fecha) {
		try {
			SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
			formatoFecha.setLenient(false);
			formatoFecha.parse(fecha);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public static boolean validarGeneracionDePDF(String buscarPor, String value) {
		boolean flag = true;
		try {
			Thread.sleep(2000);
			flag = FunctionGeneric.findObject(buscarPor, value, driver);
		} catch (Exception e) {
			return false;
		}
		return flag;
	}

	/**
	 * @author Rodrigo Miranda
	 * @descripcion este metodo lee si un frame que despliega un pdf contiene una
	 *              ruta (http://), en caso de que si lo haga el resultado es
	 *              correcto, sino envia un mensaje de fallido
	 * @param numeroFrame
	 * @return
	 */
	public static String validarGeneracionDePDF(int numeroFrame) {
		String msg = "OK";
		try {
			List<WebElement> iframe = driver.findElements(By.tagName("iframe"));
			String src = iframe.get(numeroFrame).getAttribute("src");

			if (!src.contains("http") || !src.contains("html")) {
				msg = "No se encuentra desplegado el PDF en pantalla";
			}
		} catch (Exception e) {
			msg = "Error no se encuentra ningun elemento de tipo frame";
		}
		return msg;
	}

	public static String buscarResultadoPorRegEx(String texto, String regEx, int nroGrupo) {
		String resultado = "";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(texto);

		while (matcher.find()) {
			resultado = matcher.group(nroGrupo);
		}
		return resultado;
	}

	public static int extraerMontoDisponible(By by) {
		int monto = 0;
		try {
			WebElement elemento = (new WebDriverWait(driver, 15))
					.until(ExpectedConditions.presenceOfElementLocated(by));
			String msg = elemento.getText();
			msg = msg.replaceAll("[.$]", "").trim();
			monto = Integer.parseInt(msg);
		} catch (Exception e) {
			return 0;
		}
		return monto;
	}

	public static int calcularNuevoMontoDisponible(int montoAntes, int montoSolicitado) {
		int monto;
		try {
			monto = montoAntes - montoSolicitado;
		} catch (Exception e) {
			return 0;
		}
		return monto;
	}

	public static String compararMontoDisponibleActualizado(int nuevoMontoDiponible, int montoActualizado) {
		String msg = "OK";
		try {
			Thread.sleep(3000);
			if (nuevoMontoDiponible != montoActualizado) {
				msg = "el Monto disponible no ha sido actualizado | nuevo monto = " + nuevoMontoDiponible
						+ " | monto actualizado = " + montoActualizado + "";
			}
		} catch (Exception e) {
			msg = "Error no coinciden la cantidad de los montos";
		}
		return msg;
	}

}
