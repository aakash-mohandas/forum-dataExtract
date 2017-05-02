import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class DataproviderClass {

    public WebDriver driver;
    public int limitvar=100;

    public List<String[]> rowList = new ArrayList<String[]>();

    private Workbook getWorkbook(FileInputStream inputStream, String excelFilePath)
            throws IOException {
        Workbook workbook = null;

        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }

    public void setListValue(String workBookName) throws IOException {
        String resourceFilePath = "src/main/resources/inputFiles/" + workBookName;
        String excelFilePath = new File(resourceFilePath).getAbsolutePath();
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        Workbook workbook = getWorkbook(inputStream,excelFilePath);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();

        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();

            String[] str=new String[4];
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                if(cell.getColumnIndex() == 0){
                    str[0] = cell.getStringCellValue();
                    System.out.println("Main Forum: "+ cell.getStringCellValue());
                }else if(cell.getColumnIndex() == 1){
                    str[1] = cell.getStringCellValue();
                    System.out.println("Forum Url: "+cell.getStringCellValue());
                }else if(cell.getColumnIndex() == 2){
                    str[2] = cell.getStringCellValue();
                    System.out.println("Sub Forum Element Locator: "+cell.getStringCellValue());
                }else if(cell.getColumnIndex() == 3){
                    str[3] = cell.getStringCellValue();
                    System.out.println("Text message Locator: "+cell.getStringCellValue());
                }

            }
            rowList.add(str);
        }

        workbook.close();
        inputStream.close();
    }


    public void setText( String siteName) throws Exception {
        for (int i = 0; i <= (rowList.size() - 1); i++) {
            for (String[] reviewElement : rowList) {
                String Main_forum = reviewElement[0];

                String ForumURL = reviewElement[1].toString();
                String data = "";
                String nextURL="";
                driver.manage().window().maximize();
                driver.get(ForumURL);

                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                driver.findElement(By.linkText("Change cookie settings")).click();
                driver.findElement(By.className("submitCookie")).click();


                //To store all the URLS of sub forums
                List<String> sublinkUrls = new ArrayList<String>();
                List<WebElement> allNextLinks1 = driver.findElements(By.xpath("(//a[contains(.,'Next')])[1]"));
                Boolean next1 = true;

                //setting up counter as Script got failed in between
                int cnt=0;

                //Reading all Sub Links from major Category
                while (next1 == true) {
                    if(cnt<limitvar){
                        List<WebElement> forumSubLinks = driver.findElements(By.xpath(reviewElement[2]));

                        for (WebElement forumSubLink : forumSubLinks) {
                            sublinkUrls.add(forumSubLink.getAttribute("href"));

                        }

                        System.out.println("Page URL - "+driver.getCurrentUrl());
                        allNextLinks1 = driver.findElements(By.xpath("(//a[contains(.,'Next')])[1]"));

                        if ((allNextLinks1.size() > 0)) {

                            nextURL = allNextLinks1.get(0).getAttribute("href");
                            //
                            //WaitforElement(driver.findElement(By.xpath("(//table[@class='lia-list-wide']//h2/span/a)[1]")));
                            next1 = true;
                        } else {
                            next1 = false;
                        }



                        //Start of reading Posts data
                        for (String forumSubLinkString : sublinkUrls) {
                            //String WholeURL = BTURL + forumSubLink.getAttribute("href");
                            File file = createCSVFile(siteName, Main_forum);
                            driver.get(forumSubLinkString);
                            //reading sub forums
                            List<WebElement> allNextLinks = driver.findElements(By.xpath("(//a[@rel='next'])[1]"));
                            Boolean next = true;
                            while (next == true) {
                                List<WebElement> posts = driver.findElements(By.xpath(reviewElement[3]));
                                for (WebElement post : posts) {

                                    data += post.getText();
                                    data += "\n------------------------------------------------------\n";
                                }
                                System.out.println("Post : "+driver.getCurrentUrl());

                                allNextLinks = driver.findElements(By.xpath("(//a[@rel='next'])[1]"));

                                if ((allNextLinks.size() > 0)) {
                                    allNextLinks.get(0).click();
                                    //WaitforElement(driver.findElement(By.xpath("(//div[contains(@id,'messagebodydisplay')])[1]")));
                                    next = true;
                                } else {
                                    next = false;
                                }

                            }

                            writeCSVFile(file, data);
                            data = "";
                        }
                        //End of Reading Posts Data

                        //Clearing URL Array
                        sublinkUrls.clear();

                        if(next1 == true){
                            driver.get(nextURL);
                        }
                    }
                    else
                    {
                        break;
                    }
                    cnt++;
                }

            }
        }
    }

    public void setTextDigitalspy( String siteName) throws Exception {
        for (String[] reviewElement : rowList) {

            //ArrayList<String> reviewElement = rowList.get(i);
            String Main_forum = reviewElement[0].toString();

            String ForumURL = reviewElement[1].toString();
            String data="";
            driver.manage().window().maximize();
            driver.get(ForumURL);

            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

            //To store all the URLS of sub forums
            List<String> sublinkUrls = new ArrayList<String>();

            List<WebElement> allNextLinks = driver.findElements(By.xpath("(//a[@class='Next'])[1]"));

            String nextpageURL = driver.findElement(By.xpath("(//a[@class='Next'])[1]")).getAttribute("href");
          /*  while (next == true){

                List<WebElement> forumSubLinks = driver.findElements(By.xpath(reviewElement[2]));
                for (WebElement forumSubLink:forumSubLinks) {
                    sublinkUrls.add(forumSubLink.getAttribute("href"));

                }
                System.out.println("PAGE: --");
                allNextLinks = driver.findElements(By.xpath("(//a[contains(.,'»')])[1]"));
                if(allNextLinks.size() >0){
                    //driver.findElement(By.xpath("(//a[@class='Next'])[1]")).click();
                    //WaitforElement(driver.findElement(By.xpath("(//a[@class='Next'])[1]")));
                    WaitforPageLoad();
                    allNextLinks.get(0).click();
                    WaitforPageLoad();
                    //WaitforElement(driver.findElement(By.xpath("(//a[@class='Title'])[1]")));
                    next=true;
                }else{
                    next=false;
                }

            } */

            int allNextCounter=1;
            int totalpages= Integer.parseInt(driver.findElement(By.xpath("//div[@id='PagerBefore']//a[contains(@class,'Last')]")).getText());
            while(allNextCounter<=totalpages){
                List<WebElement> forumSubLinks = driver.findElements(By.xpath(reviewElement[2]));
                for (WebElement forumSubLink:forumSubLinks) {
                    sublinkUrls.add(forumSubLink.getAttribute("href"));

                }

                nextpageURL = driver.findElement(By.xpath("(//a[@class='Next'])[1]")).getAttribute("href");

                //reading sub forums
                for (String forumSubLinkString :sublinkUrls) {
                    //String WholeURL = BTURL + forumSubLink.getAttribute("href");

                    driver.get(forumSubLinkString);
                    System.out.println("POST : "+forumSubLinkString);

                    File file = createCSVFile(siteName,Main_forum);

                    int subPageNextCounter=1;

                    if(driver.findElements(By.xpath("//span[@id='PagerBefore']//a[contains(@class,'Last')]")).size()>0){
                        int totalSubpages= Integer.parseInt(driver.findElement(By.xpath("//span[@id='PagerBefore']//a[contains(@class,'Last')]")).getText());
                        while(subPageNextCounter<=totalSubpages){
                            List<WebElement> posts = driver.findElements(By.xpath(reviewElement[3]));
                            for (WebElement post :posts) {

                                data += post.getText();
                                data += "\n------------------------------------------------------\n";
                            }
                            subPageNextCounter += 1;
                            driver.get(forumSubLinkString+"/p"+subPageNextCounter);
                            System.out.println("POST : "+forumSubLinkString+"/p"+subPageNextCounter);
                        }
                    }else{
                        List<WebElement> posts = driver.findElements(By.xpath(reviewElement[3]));
                        for (WebElement post :posts) {

                            data += post.getText();
                            data += "\n------------------------------------------------------\n";
                        }

                    }

                    writeCSVFile(file, data);
                    data="";
                }

                sublinkUrls.clear();

                allNextCounter +=1;
                driver.get(nextpageURL);
                System.out.println("Page URL - "+nextpageURL);
                
            }


        }

    }

    public void setTextMoneySaving( String siteName) throws Exception {
        for (int i = 0; i <= (rowList.size() - 1); i++) {
            for (String[] reviewElement : rowList) {
                String Main_forum = reviewElement[0];

                String ForumURL = reviewElement[1].toString();
                String data = "";
                String nextURL = "";
                driver.manage().window().maximize();
                driver.get(ForumURL);



                //To store all the URLS of sub forums
                List<String> sublinkUrls = new ArrayList<String>();
                List<WebElement> allNextLinks1 = driver.findElements(By.xpath("(//a[@rel='next'])[1]"));
                Boolean next1 = true;

                //setting up counter as Script got failed in between
                int cnt = 0;

                //Reading all Sub Links from major Category
                while (next1 == true) {

                    if(cnt<limitvar){

                        List<WebElement> forumSubLinks = driver.findElements(By.xpath(reviewElement[2]));


                        for (WebElement forumSubLink : forumSubLinks) {
                            sublinkUrls.add(forumSubLink.getAttribute("href"));

                        }
                        System.out.println("Page URL - " + driver.getCurrentUrl());
                        allNextLinks1 = driver.findElements(By.xpath("(//a[@rel='next'])[1]"));

                        if ((allNextLinks1.size() > 0)) {

                            nextURL = allNextLinks1.get(0).getAttribute("href");

                            WaitforElement(driver.findElement(By.xpath("(//*[@class=\"threadbit list-hover-item\"]//a[contains(@id,'td_threadtitle')])[1]")));
                            next1 = true;
                        } else {
                            next1 = false;
                        }

                        //Start of reading Posts data
                        for (String forumSubLinkString : sublinkUrls) {
                            //String WholeURL = BTURL + forumSubLink.getAttribute("href");
                            File file = createCSVFile(siteName, Main_forum);
                            driver.get(forumSubLinkString);
                            //reading sub forums
                            List<WebElement> allNextLinks = driver.findElements(By.xpath("(//a[@rel='next'])[1]"));
                            Boolean next = true;
                            while (next == true) {
                                List<WebElement> posts = driver.findElements(By.xpath(reviewElement[3]));
                                for (WebElement post : posts) {

                                    data += post.getText();
                                    data += "\n------------------------------------------------------\n";
                                }
                                System.out.println("Post : "+driver.getCurrentUrl());

                                allNextLinks = driver.findElements(By.xpath("(//a[@rel='next'])[1]"));

                                if ((allNextLinks.size() > 0)) {
                                    allNextLinks.get(0).click();
                                    WaitforElement(driver.findElement(By.xpath("(//*[@class=\"post-message\"])[1]")));
                                    next = true;
                                } else {
                                    next = false;
                                }

                            }

                            writeCSVFile(file, data);
                            data="";
                        }

                        //End of Reading Posts Data

                        //Clearing URL Array
                        sublinkUrls.clear();
                        if (next1 == true) {
                            driver.get(nextURL);
                        }


                    }else{
                        break;
                    }
                    cnt++;
                }
            }
        }
    }


    public File createCSVFile(String siteName, String Main_forum) throws Exception{

            String fileName = siteName + "_" + Main_forum + "_" + System.currentTimeMillis();
            String temp = "src/main/resources/outputFiles/";
            String excelFilePath = new File(temp).getAbsolutePath();
            File file = new File(excelFilePath + "/" + fileName + ".csv");
            file.createNewFile();
            return file;
        }

    public void writeCSVFile(File file, String data) throws Exception{
        //FileWriter fileWritter = new FileWriter(file.getName(),true);
        BufferedWriter bufferWritter = new BufferedWriter(new FileWriter(file));
        bufferWritter.write(data);
        bufferWritter.close();
    }

    public void WaitforElement(WebElement element) throws Exception{
        new WebDriverWait(driver,20).
                until(ExpectedConditions.elementToBeClickable(element));
    }

    public void WaitforPageLoad() throws Exception{
        new WebDriverWait(driver, 30).until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }

}
