import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;


public class ReadDataClass extends DataproviderClass{

    public synchronized void startDriver(){
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
     }

     @Test(enabled = false)
     public void getBTText() throws Exception {

         ReadDataClass d = new ReadDataClass();
         d.startDriver();
         d.rowList.clear();
         String excelFile = "BT_forum_ADSLCopper4.xlsx";
         d.setListValue(excelFile);
         d.setText("BT_Forum");
         d.shutDriver();
     }


    @Test(enabled = false)
    public void getMoneySavingData() throws Exception {

        ReadDataClass d = new ReadDataClass();
        d.startDriver();
        d.rowList.clear();
        String excelFile = "MoneySaving3.xlsx";
        d.setListValue(excelFile);
        d.setTextMoneySaving("MoneySaving");
        d.shutDriver();
    }


    @Test(enabled = false)
    public void getDigitalspyData() throws Exception {

        ReadDataClass d = new ReadDataClass();
        d.startDriver();
        d.rowList.clear();
        String excelFile = "DIGITALSPY.xlsx";
        d.setListValue(excelFile);
        d.setTextDigitalspy("DIGITALSPY");
        d.shutDriver();
    }

    @Test(enabled = false)
    public void getDigitalspyData2() throws Exception {

        ReadDataClass d = new ReadDataClass();
        d.startDriver();
        d.rowList.clear();
        String excelFile = "DIGITALSPY2.xlsx";
        d.setListValue(excelFile);
        d.setTextDigitalspy("DIGITALSPY");
        d.shutDriver();
    }

    @Test(enabled = false)
    public void getDigitalspyData3() throws Exception {

        ReadDataClass d = new ReadDataClass();
        d.startDriver();
        d.rowList.clear();
        String excelFile = "DIGITALSPY3.xlsx";
        d.setListValue(excelFile);
        d.setTextDigitalspy("DIGITALSPY");
        d.shutDriver();
    }

    @Test(enabled = false)
    public void getDigitalspyData4() throws Exception {

        ReadDataClass d = new ReadDataClass();
        d.startDriver();
        d.rowList.clear();
        String excelFile = "DIGITALSPY4.xlsx";
        d.setListValue(excelFile);
        d.setTextDigitalspy("DIGITALSPY");
        d.shutDriver();
    }

    @Test(enabled = true)
    public void getDigitalspyData5() throws Exception {

        ReadDataClass d = new ReadDataClass();
        d.startDriver();
        d.rowList.clear();
        String excelFile = "DIGITALSPY5.xlsx";
        d.setListValue(excelFile);
        d.setTextDigitalspy("DIGITALSPY");
        d.shutDriver();
    }

    /*
    @Test(enabled = false)
    public static void getFlipkartText() throws IOException {
        phoneName.clear();
        phoneUrl.clear();
        reviewElement.clear();
        String excelFile = "flipkartFile.xlsx";
        setListValue(excelFile);
        setText(phoneName,phoneUrl,reviewElement,"flipkart");
    }

    @Test(enabled = false)
    public static void getAmazonText() throws IOException {
        phoneName.clear();
        phoneUrl.clear();
        reviewElement.clear();
        String excelFile = "amazonFile.xlsx";
        setListValue(excelFile);
        setText(phoneName,phoneUrl,reviewElement,"amazon");
    }

    @Test(enabled = false)
    public static void getGsmArenaText() throws IOException {
        phoneName.clear();
        phoneUrl.clear();
        reviewElement.clear();
        String excelFile = "gsmFile.xlsx";
        setListValue(excelFile);
        setText(phoneName,phoneUrl,reviewElement,"gsm");
    }

*/
    //@AfterClass
    public synchronized void shutDriver(){
        driver.close();
        driver.quit();
    }
}
