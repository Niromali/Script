package Package1;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemplissageFormulaireWeb {
	
	
	private static String extractPhoneNumber(String text) {
	    String phoneNumber = "";
	    Pattern pattern = Pattern.compile("\\b\\d{10,12}\\b");
	    Matcher matcher = pattern.matcher(text);
	    if (matcher.find()) {
	        phoneNumber = matcher.group();
	    }
	    return phoneNumber;
	}

	private static String extractEmail(String text) {
	    String email = "";
	    Pattern pattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,7}\\b");
	    Matcher matcher = pattern.matcher(text);
	    if (matcher.find()) {
	        email = matcher.group();
	    }
	    return email;
	}


    private static String generateRandomMobileNumber() {
        StringBuilder mobileNumber = new StringBuilder();
        Random rand = new Random();

        int firstDigit = rand.nextInt(4) + 6;
        mobileNumber.append(firstDigit);

        for (int i = 1; i < 12; i++) {
            int digit = rand.nextInt(12);
            mobileNumber.append(digit);
        }
        return mobileNumber.toString();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
    	
    	/*!!--replace the paths whit your paths--*/
    	/*!!!!!!  -this code or links just for exemple */
    	
        String fichierInfos = "C:/Users/abdo1/OneDrive/Bureau/chromedriver-win64 (1)/chromedriver-win64/data.txt";

        System.setProperty("webdriver.chrome.driver", "C:/Users/abdo1/OneDrive/Bureau/chromedriver-win64 (1)/chromedriver-win64/chromedriver.exe");
        WebDriver driver = (WebDriver)new ChromeDriver();

        driver.get("https://www.timesjobs.com/candidate/register.html?pageFlow=TJ_HOME");

        String motDePasseFixe = "1q2w3e4r@";
        String annees = "3";
        String Mois="5";
        try (BufferedReader br = new BufferedReader(new FileReader(fichierInfos))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] champs = line.split("\\s+");

                try {
                    String email = extractEmail(line);
                    String phoneNumber = extractPhoneNumber(line);

                    if (email.isEmpty()) {
                        System.out.println("Pas d'adresse e-mail trouvée dans la ligne. Skipping...");
                        line = br.readLine();
                        continue;
                    }

                    if (phoneNumber.isEmpty()) {
                        System.out.println("Pas de numéro de téléphone trouvé dans la ligne.");
                    } else {
                        System.out.println("Numéro de téléphone : " + phoneNumber);
                    }
                    

                    
                    Thread.sleep(2000);
                    WebElement emailInput = driver.findElement(By.id("emailAdd"));
                    emailInput.clear();
                    emailInput.sendKeys(email);
                    emailInput.sendKeys(Keys.TAB);
                    Thread.sleep(2000);

                    WebElement errorMessage = driver.findElement(By.xpath("//div[@class='error-block cll']"));
                    String errorMessageText = errorMessage.getText();

                    if (errorMessageText.contains("is already registered with us..") || errorMessageText.contains("Please enter a valid email address")) {
                        System.out.println(email + " est déjà enregistré. Skipping...");
                        line = br.readLine();
                        continue;
                    }

                } catch (Exception e) {

                }
                
                driver.findElement(By.id("passwordField")).sendKeys(motDePasseFixe);
                driver.findElement(By.id("retypePassword")).sendKeys(motDePasseFixe);
                Thread.sleep(1000);
                
                String mobileNumber = "";
                if (champs.length > 0) {
                    mobileNumber = extractPhoneNumber(champs[0]);
                }
                if (mobileNumber.isEmpty() && champs.length > 1) {
                    mobileNumber = extractPhoneNumber(champs[1]);
                }
                if (mobileNumber.isEmpty() && champs.length > 2) {
                    mobileNumber = extractPhoneNumber(champs[2]);
                }
                if (mobileNumber.isEmpty() && champs.length > 3) {
                    mobileNumber = extractPhoneNumber(champs[3]);
                }
                if (mobileNumber.isEmpty() && champs.length > 4) {
                    mobileNumber = extractPhoneNumber(champs[4]);
                }

                if (mobileNumber.isEmpty() || !mobileNumber.matches("[0-9]+")) {
                    mobileNumber = generateRandomMobileNumber();
                }

                
                
                WebElement mobilInput = driver.findElement(By.id("mobNumber"));
                mobilInput.clear();
                Thread.sleep(1000);
                mobilInput.sendKeys(mobileNumber);
                Thread.sleep(1000);
                WebElement funcAreaSelect = driver.findElement(By.id("funcArea"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", funcAreaSelect);
                Select select = new Select(funcAreaSelect);
                select.selectByVisibleText("Finance and Insurance");
                
                Thread.sleep(1000);
                
                driver.findElement(By.id("cboWorkExpYear")).sendKeys(annees);
                driver.findElement(By.id("cboWorkExpMonth")).sendKeys(Mois);
                
                Thread.sleep(1000);
                WebElement curLocationSelect = driver.findElement(By.id("curLocation"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", curLocationSelect);
                Select locationSelect = new Select(curLocationSelect);
                locationSelect.selectByVisibleText("Pune");
                
                Thread.sleep(1000);
                curLocationSelect.sendKeys(Keys.TAB);
                
                Thread.sleep(1000);

                String skillsToAdd = "html"; 
                WebElement keySkillsInput = driver.findElement(By.id("token-input-keySkills"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", keySkillsInput);

                Thread.sleep(2000);

                keySkillsInput.click();
                Thread.sleep(1000);
                keySkillsInput.sendKeys(skillsToAdd);
                Thread.sleep(1000);
                keySkillsInput.sendKeys(Keys.ARROW_DOWN);
                keySkillsInput.sendKeys(Keys.ENTER);
                Thread.sleep(1000);
                keySkillsInput.sendKeys(Keys.TAB);
                Thread.sleep(2000);

                
                
                
                WebElement dontHaveResumeButton = driver.findElement(By.id("dontHaveResume"));
                Thread.sleep(1000);
                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("arguments[0].click();", dontHaveResumeButton);


                
                String optionValueToSelect = "1-110";
                Select qualificationSelect = new Select(driver.findElement(By.id("categorisedDegree")));
                qualificationSelect.selectByValue(optionValueToSelect);

                driver.findElement(By.id("lacSalary")).sendKeys(annees);
                driver.findElement(By.id("thousandSalary")).sendKeys(Mois);
                Thread.sleep(2000);
             
                
                WebElement emInput = driver.findElement(By.id("curEmpName"));
                Thread.sleep(1000);
                emInput.click();
                Thread.sleep(1000);

                emInput.sendKeys(skillsToAdd);
                Thread.sleep(1000);

                emInput.sendKeys(Keys.ARROW_DOWN);
                emInput.sendKeys(Keys.ENTER);


                WebElement nput = driver.findElement(By.id("curEmpDesignation"));
                Thread.sleep(1000);
                nput.sendKeys("Desgnation1");
                Thread.sleep(1000);
                
              
                
        
                
                
                
                WebElement Button = driver.findElement(By.id("basicSubmit"));
                Thread.sleep(1000);
                JavascriptExecutor jsEx = (JavascriptExecutor) driver;
                jsEx.executeScript("arguments[0].click();", Button);
                
                
                
                Thread.sleep(1000);
                
                WebElement logoutButton = driver.findElement(By.linkText("Logout"));
                Thread.sleep(1000);
                JavascriptExecutor jsExe = (JavascriptExecutor) driver;
                jsExe.executeScript("arguments[0].click();", logoutButton);


                Thread.sleep(3000);
                
                driver.get("https://www.timesjobs.com/candidate/register.html?pageFlow=TJ_HOME");
                
                
                line = br.readLine();
            }
        }

      
        driver.quit();
    }


}