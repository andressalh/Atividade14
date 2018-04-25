package br.edu.utfpr.bugzilla;

import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

public class BugzillaTest {

    private static String CHROMEDRIVER_LOCATION = "C:\\Users\\andre\\Desktop\\8Sem\\topicos\\SeleniumMavenExemplo\\chromedriver.exe";

    private static int scId = 0;

    WebDriver driver;

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("webdriver.chrome.driver", CHROMEDRIVER_LOCATION);
    }

    @Before
    public void before() {
        ChromeOptions chromeOptions = new ChromeOptions();
        //Opcao headless para MacOS e Linux
        chromeOptions.addArguments("headless");
        chromeOptions.addArguments("window-size=1200x600");
        chromeOptions.addArguments("start-maximized");

        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @After
    public void after() {
        driver.close();
    }

    @Test
    public void testPesquisaAvancadaTotalDeItensEncontrada() {
        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/query.cgi?format=advanced");
        WebElement inputDeBusca = driver.findElement(By.xpath("//*[@id=\"short_desc\"]"));
        WebElement btSubmit = driver.findElement(By.xpath("//*[@id=\"Search_top\"]"));

        inputDeBusca.sendKeys("it ain't accepting the fields of username and password");
        btSubmit.submit();

        WebElement totalDeBugs = driver.findElement(By.xpath("//*[@id=\"bugzilla-body\"]/span"));

        assertEquals("2 bugs found.", totalDeBugs.getText());
    }

    @Test
    public void testPesquisaAvancadaOrdenacaoPorId() {
        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/query.cgi?format=advanced");
        WebElement inputDeBusca = driver.findElement(By.xpath("//*[@id=\"short_desc\"]"));
        WebElement btSubmit = driver.findElement(By.xpath("//*[@id=\"Search_top\"]"));

        inputDeBusca.sendKeys("it ain't accepting the fields of username and password");
        btSubmit.submit();

        WebElement ID = driver.findElement(By.cssSelector("#bugzilla-body > table > tbody > tr.bz_buglist_header.bz_first_buglist_header > th.first-child > a"));
        ID.click();

        WebElement idDoPrimeiroItem = driver.findElement(By.cssSelector("td.first-child.bz_id_column > a"));
        assertEquals("46907", idDoPrimeiroItem.getText());
    }

    @Test
    public void testPesquisaAvancadaSelecionandoUmProduto() {
        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/query.cgi?format=advanced");
        WebElement inputDeBusca = driver.findElement(By.xpath("//*[@id=\"short_desc\"]"));
        WebElement btSubmit = driver.findElement(By.xpath("//*[@id=\"Search_top\"]"));

        inputDeBusca.sendKeys("username");

        Select selectProduto = new Select(driver.findElement(By.id("product")));
        selectProduto.selectByVisibleText("WorldControl");
        btSubmit.submit();

        WebElement textResult = driver.findElement(By.xpath("//*[@id=\"bugzilla-body\"]/span[1]"));
        assertEquals("15 bugs found.", textResult.getText());

    }

    @Test
    public void testAcessaFAQ() throws InterruptedException {
        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/");
        WebElement faqLink = driver.findElement(By.xpath("//*[@id=\"query\"]"));
        faqLink.click();
        
        //*[@id="query"]
        
        WebElement searchInput = driver.findElement(By.xpath("//*[@id=\"content\"]"));
        searchInput.sendKeys("erro de software");
        
        WebElement search = driver.findElement(By.xpath("//*[@id=\"search\"]"));
        search.submit();
       
        WebElement tituloErro = driver.findElement(By.xpath("//*[@id=\"bugzilla-body\"]/ul/li[1]"));
        System.out.println(tituloErro.getText());
        
        assertTrue(tituloErro.getText().contains("erro de software"));
    
    }
    
    @Test
    public void testCriarConta() {
        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/");

        WebElement conta = driver.findElement(By.id("account"));
        conta.click();

        WebElement email = driver.findElement(By.xpath("//*[@id=\"login\"]"));
        email.sendKeys("usuario@user.com");

        WebElement enviarEmail = driver.findElement(By.id("send"));
        enviarEmail.click();

        WebElement resposta = driver.findElement(By.xpath("//*[@id=\"bugzilla-body\"]/p"));

        Assert.assertEquals("A confirmation email has been sent containing a "
                + "link to continue creating an account. The link will expire "
                + "if an account is not created within 3 days.", resposta.getText());
    }

    
    @Test
    public void testTentarCriarContaNovamente() {
        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/");

        WebElement conta = driver.findElement(By.id("account"));
        conta.click();

        WebElement email = driver.findElement(By.xpath("//*[@id=\"login\"]"));
        email.sendKeys("ert@users.com.br");

        WebElement enviarEmail = driver.findElement(By.id("send"));
        enviarEmail.click();

        WebElement resposta = driver.findElement(By.xpath("//*[@id=\"bugzilla-body\"]/p"));

        Assert.assertEquals("A confirmation email has been sent containing a "
                + "link to continue creating an account. The link will expire "
                + "if an account is not created within 3 days.", resposta.getText());
        
        WebElement newConta = driver.findElement(By.xpath("//*[@id=\"new_account_container_top\"]/a"));
        newConta.click();
        
        email = driver.findElement(By.xpath("//*[@id=\"login\"]"));
        email.sendKeys("ert@users.com.br");
        enviarEmail = driver.findElement(By.id("send"));
        enviarEmail.click();
        
        WebElement msgErro = driver.findElement(By.id("error_msg"));
        Assert.assertEquals("You have requested an account token too recently "
                + "to request another. Please wait 10 minutes then try again.",
                msgErro.getText());
    }
    
    @Test
    public void testLoginSemConta() {
        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/");
        
        WebElement btnLogin = driver.findElement(By.id("login_link_top"));
        btnLogin.click();
        
        WebElement campoEmail = driver.findElement(By.id("Bugzilla_login_top"));
        WebElement campoSenha = driver.findElement(By.id("Bugzilla_password_top"));
        
        campoEmail.sendKeys("srdfhgf@sgdfng.com");
        campoSenha.sendKeys("12348");
        
        WebElement btnlogar = driver.findElement(By.id("log_in_top"));
        btnlogar.click();
        
        WebElement msgErro = driver.findElement(By.id("error_msg"));
        Assert.assertEquals("The login or password you entered is not valid.",
                msgErro.getText());
    }
  
    @Test
    public void testPesquisaDetalhada() {
        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/");
        
        WebElement btnPesquisa = driver.findElement(By.id("query"));
        btnPesquisa.click();
        
        
        WebElement status = driver.findElement(By.id("bug_status"));
        Select cbStatus = new Select(status);
        cbStatus.selectByVisibleText("All");
        
        WebElement produtos = driver.findElement(By.id("product"));
        Select cbProdutos = new Select(produtos);
        cbProdutos.selectByVisibleText("WorldControl");
        
        WebElement palavras = driver.findElement(By.id("content"));
        palavras.sendKeys("teste");
        
        WebElement procurar = driver.findElement(By.id("search"));
        procurar.click();
        
        Assert.assertEquals("https://landfill.bugzilla.org/bugzilla-5.0-branch/"
                + "buglist.cgi?bug_status=__all__&content=teste&no_redirect=1"
                + "&order=Importance&product=WorldControl&query_format=specific",
                driver.getCurrentUrl());
        
        
       /*WebElement btnPesquisa = driver.findElement(By.id("query"));
        WebElement btnPesquisa = driver.findElement(By.id("query"));
        WebElement btnPesquisa = driver.findElement(By.id("query"));
        WebElement btnPesquisa = driver.findElement(By.id("query"));
        WebElement btnPesquisa = driver.findElement(By.id("query"));
        WebElement btnPesquisa = driver.findElement(By.id("query"));*/
        
    }
    
     @Test
    public void loginInvalido() {
        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/");
        WebElement signUpButton = driver.findElement(By.xpath("//*[@id=\"login_link_top\"]"));
        signUpButton.click();
        WebElement username = driver.findElement(By.xpath("//*[@id=\"Bugzilla_login_top\"]"));
        username.sendKeys("gabriel@gmail.com");
        WebElement password = driver.findElement(By.xpath("//*[@id=\"Bugzilla_password_top\"]"));
        password.sendKeys("senha");
        WebElement signUpButton2 = driver.findElement(By.xpath("//*[@id=\"log_in_top\"]"));
        signUpButton2.click();
        WebElement errorMsg = driver.findElement(By.xpath("//*[@id=\"error_msg\"]"));
        assertEquals("The login or password you entered is not valid.", errorMsg.getText().trim());
    }
    @Test
        public void loginRealizado() {
        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/");
        WebElement signUpButton = driver.findElement(By.xpath("//*[@id=\"login_link_top\"]"));
        signUpButton.click();
        WebElement username = driver.findElement(By.xpath("//*[@id=\"Bugzilla_login_top\"]"));
        username.sendKeys("papu_nilo@hotmail.com");
        WebElement password = driver.findElement(By.xpath("//*[@id=\"Bugzilla_password_top\"]"));
        password.sendKeys("22ams22");
        WebElement signUpButton2 = driver.findElement(By.xpath("//*[@id=\"log_in_top\"]"));
        signUpButton2.click();
        WebElement msg = driver.findElement(By.xpath("//*[@id=\"common_links\"]/ul/li[9]/a"));
        assertEquals("Log out", msg.getText().trim());
    }
    @Test
        public void realizarBusca() {
        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/");
        WebElement username = driver.findElement(By.xpath("//*[@id=\"quicksearch_main\"]"));
        username.sendKeys("security");
        WebElement signUpButton = driver.findElement(By.xpath("//*[@id=\"find\"]"));
        signUpButton.click();

        WebElement msg = driver.findElement(By.xpath("//*[@id=\"title\"]"));
        assertEquals("Bugzilla – Bug List", msg.getText().trim());
    }
    @Test
        public void realizarBusca24hrs() {
        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/");
        WebElement click1 = driver.findElement(By.xpath("//*[@id=\"common_queries\"]/ul/li[1]/a[1]"));
        click1.click();
        WebElement msg = driver.findElement(By.xpath("//*[@id=\"bugzilla-body\"]/ul/li[2]/strong"));
        assertEquals("Creation date:", msg.getText().trim());
    }
    
    @Test
    public void loginValido() {
        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/");

        WebElement linkLogin = driver.findElement(By.id("login_link_top"));

        linkLogin.click();

        WebElement email = driver.findElement(By.id("Bugzilla_login_top"));

        email.sendKeys("rafaelnsantos1@gmail.com");

        WebElement senha = driver.findElement(By.id("Bugzilla_password_top"));

        senha.sendKeys("abcd1234");

        WebElement login = driver.findElement(By.id("log_in_top"));

        login.click();

        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/userprefs.cgi");

        WebElement user = driver.findElement(By.xpath("//*[@id=\"subtitle\"]"));
        assertEquals("rafaelnsantos1@gmail.com", user.getText().trim());
    }
    
    @Test
    public void newBug(){
        loginValido();
        
        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/token.cgi");
        
        WebElement newBug = driver.findElement(By.xpath("//*[@id=\"common_links\"]/ul/li[2]/a"));
        newBug.click();
        
        WebElement definicao = driver.findElement(By.xpath("//*[@id=\"choose_classification\"]/tbody/tr[3]/th/a"));
        definicao.click();
        
        WebElement food = driver.findElement(By.xpath("//*[@id=\"choose_product\"]/tbody/tr[2]/th/a"));
        food.click();
        
        WebElement component = driver.findElement(By.xpath("//*[@id=\"v10_component\"]"));
        component.click();
        
        WebElement severitySel = driver.findElement(By.xpath("//*[@id=\"bug_severity\"]"));
        Select severity = new Select(severitySel);
        severity.selectByVisibleText("critical");
        
        
        WebElement hardwareSel = driver.findElement(By.xpath("//*[@id=\"rep_platform\"]"));
        Select hardware = new Select(hardwareSel);
        hardware.selectByVisibleText("HP");
        
        WebElement osSel = driver.findElement(By.xpath("//*[@id=\"op_sys\"]"));
        Select os = new Select(osSel);
        os.selectByVisibleText("Windows 95");
        
        WebElement sumary = driver.findElement(By.xpath("//*[@id=\"short_desc\"]"));
        sumary.sendKeys("Teste");
        
        WebElement submit = driver.findElement(By.xpath("//*[@id=\"commit\"]"));
        submit.click();
        
        try{
            WebElement numBug = driver.findElement(By.xpath("//*[@id=\"changeform\"]/div[1]/a/b"));
            assertTrue(numBug.isDisplayed());
        } catch (Exception e){
            fail();
        }
        
    }
    
    
    @Test
    public void usuarioJaRegistrado() {
        driver.get("https://landfill.bugzilla.org/bugzilla-5.0-branch/createaccount.cgi");

        WebElement email = driver.findElement(By.xpath("//*[@id=\"login\"]"));

        email.sendKeys("rafaelnsantos1@gmail.com");

        WebElement login = driver.findElement(By.id("send"));

        login.click();

        WebElement errorMsg = driver.findElement(By.id("error_msg"));

        assertEquals("There is already an account with the login name rafaelnsantos1@gmail.com.", errorMsg.getText().trim());

    }

    @Test
    public void newBugInvalido() {
        
        loginValido();

        WebElement newBug = driver.findElement(By.xpath("//*[@id=\"common_links\"]/ul/li[2]/a"));
        newBug.click();

        WebElement definicao = driver.findElement(By.xpath("//*[@id=\"choose_classification\"]/tbody/tr[3]/th/a"));
        definicao.click();

        WebElement food = driver.findElement(By.xpath("//*[@id=\"choose_product\"]/tbody/tr[2]/th/a"));
        food.click();

        WebElement component = driver.findElement(By.xpath("//*[@id=\"v10_component\"]"));
        component.click();

        WebElement severitySel = driver.findElement(By.xpath("//*[@id=\"bug_severity\"]"));
        Select severity = new Select(severitySel);
        severity.selectByVisibleText("critical");

        WebElement hardwareSel = driver.findElement(By.xpath("//*[@id=\"rep_platform\"]"));
        Select hardware = new Select(hardwareSel);
        hardware.selectByVisibleText("HP");

        WebElement osSel = driver.findElement(By.xpath("//*[@id=\"op_sys\"]"));
        Select os = new Select(osSel);
        os.selectByVisibleText("Windows 95");

        WebElement submit = driver.findElement(By.xpath("//*[@id=\"commit\"]"));
        submit.click();

        WebElement erroMsg = driver.findElement(By.xpath("//*[@id=\"Create\"]/table/tbody[5]/tr[1]/td/div"));
        assertEquals("You must enter a Summary for this bug.", erroMsg.getText());
    }
    
}
