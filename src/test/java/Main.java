import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("http://ec2-54-208-152-154.compute-1.amazonaws.com/");
        driver.manage().window().maximize();

        //dividing the weights into 3 lots
        //insert the first 2 lots into the board
        for(int i = 0; i < 3; i++) {
            driver.findElement(By.id("left_" + i)).sendKeys(String.valueOf(i));
            driver.findElement(By.id("right_" + i)).sendKeys(String.valueOf(i + 3));
        }

        driver.findElement(By.id("weigh")).click();
        Thread.sleep(2000);

        //read the result of the first Weighing and choose the next lot accordingly
        WebElement gameInfo = driver.findElement(By.className("game-info"));
        String result = gameInfo.findElement(By.tagName("li")).getText();
        int nextLot = -1;
        if(result.contains("=")) {
            nextLot = 2;
        }else if(result.contains("<")) {
            nextLot = 0;
        }else {
            nextLot = 1;
        }

        //reset the values, there is a duplicate of reset in the html
        driver.findElement(By.xpath("(//*[@id=\"reset\"])[2]")).click();
        Thread.sleep(1000);

        //enter the next weights and weigh
        driver.findElement(By.id("left_1")).sendKeys(String.valueOf(nextLot * 3));
        driver.findElement(By.id("right_1")).sendKeys(String.valueOf(nextLot * 3 + 1));


        driver.findElement(By.id("weigh")).click();
        Thread.sleep(2000);

        //now the second weighing gives us the final result
        String finalResult = gameInfo.findElements(By.tagName("li")).get(1).getText();

        char defectiveWeight;

        if(finalResult.contains("=")) {
            defectiveWeight = Character.forDigit(nextLot * 3 + 2, 10);
        }else if(finalResult.contains("<")) {
            defectiveWeight = finalResult.split("<")[0].trim().charAt(1);
        }else {
            defectiveWeight = finalResult.split(">")[1].trim().charAt(1);
        }

        driver.findElement(By.id("coin_" + defectiveWeight)).click();

    }

}

