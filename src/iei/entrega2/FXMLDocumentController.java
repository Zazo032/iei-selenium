package iei.entrega2;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author sergisanz
 */
public class FXMLDocumentController implements Initializable {

    private Label label;
    @FXML
    private JFXCheckBox checkAmazon;
    @FXML
    private JFXCheckBox checkElCorteIngles;
    @FXML
    private JFXCheckBox checkFnac;
    @FXML
    private JFXTextField t_libro;
    @FXML
    private JFXTextField autor;

    private static WebDriver driver = null;

    @FXML
    ScrollPane scroll = new ScrollPane();
    private ArrayList<String> listFnac = new ArrayList<String>();
    int tamaño;
    @FXML
    private VBox vbox_identificador;
    private GridPane grid_identificador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // grid_identificador.setVisible(false);
        grid_identificador = new GridPane();
    }

    @FXML
    private void Buscar(ActionEvent event) {
        if (checkFnac.isSelected()) {
            Fnac(t_libro.getText(), autor.getText());
        }
        if (checkAmazon.isSelected()) {
            Amazon(t_libro.getText(), autor.getText());
        }

    }

    public void Fnac(String libro, String autor) {
        String exePath = "lib/chromedriver";
        System.setProperty("webdriver.chrome.driver", exePath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.get("http://www.fnac.es");
        driver.findElement(By.className("Header__aisle")).click();
        driver.findElements(By.cssSelector("ul.select-options>li.select-option")).get(1).click();
        WebElement element = driver.findElement(By.id("Fnac_Search"));
        element.sendKeys(libro);
        element.submit();

        tamaño = driver.findElements(By.className("Article-desc")).size();

        do {
            WebDriverWait waiting = new WebDriverWait(driver, 10);
            waiting.until(ExpectedConditions.presenceOfElementLocated(By.className("Article-desc")));
            waiting.until(ExpectedConditions.invisibilityOf(driver.findElement(By.id("ajaxLoader"))));
            int i = 0;
            for (WebElement el : driver.findElements(By.className("Article-desc"))) {
                List<WebElement> title = el.findElements(By.className("js-minifa-title"));
                if (title.size() >= 1) {
                    listFnac.add(title.get(0).getText());
                    System.out.println(title.get(0).getText());

                    //grid.add(new Label(dato),0,0);
                    //   grid.add(new Label(listFnac.get(i)), i, 0);
                }
            }
            try {
                driver.findElement(By.className("nextLevel1")).click();
            } catch (Exception e) {
                break;
            }
        } while (driver.findElement(By.className("nextLevel1")) != null);
        RellenarGridPane();
        //vbox.getChildren().addAll(grid);

        System.out.println("END");
    }

    private void Amazon(String libro, String autor) {

    }

    private void RellenarGridPane() {
        grid_identificador.setVisible(false);
        grid_identificador.add(new Label("Libro"), 1,0);
        for (int i = 1; i < tamaño; i++) {
            grid_identificador.add(new Label(listFnac.get(i)), 1, i);
        }

        grid_identificador.setAlignment(Pos.CENTER);
        grid_identificador.setPadding(new Insets(50, 20, 20, 20));
      //  double tamaño2 = tamaño*50;
        //vbox_identificador.setPrefSize(900, 100000);
        vbox_identificador.getChildren().addAll(grid_identificador);
        
        grid_identificador.setVisible(true);
    }
}
