package testing;

import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.google.gson.Gson;

import principal.OMDbMovie;

public class Testing {
	public static void main(String[] args) {
		long start = System.nanoTime();
		test();
		long finish = System.nanoTime();
		long timeElapsed = finish - start;
		System.out.println(timeElapsed);
	}
	public static void test() {
		OMDbMovie Movie1 = OMDbQuery("Inception", "t");
		OMDbMovie Movie2 = OMDbQuery("Wolf of wall street", "t");
		OMDbMovie Movie3 = OMDbQuery("Pokemon", "t");
		OMDbMovie Movie4 = OMDbQuery("V for vendetta", "t");
		OMDbMovie Movie5 = OMDbQuery("Pulp fiction", "t");
		OMDbMovie Movie6 = OMDbQuery("Kill bill", "t");
		OMDbMovie Movie7 = OMDbQuery("Runner", "t");
		OMDbMovie Movie8 = OMDbQuery("Blade", "t");
		OMDbMovie Movie9 = OMDbQuery("Star wars", "t");
		OMDbMovie Movie10 = OMDbQuery("Lord of the rings", "t");
		OMDbMovie Movie11 = OMDbQuery("Ronaldo", "t");
		OMDbMovie Movie12 = OMDbQuery("No strings attached", "t");
		OMDbMovie Movie13 = OMDbQuery("Jumanji", "t");
		OMDbMovie Movie14 = OMDbQuery("Dead poets society", "t");
		OMDbMovie Movie15 = OMDbQuery("American beauty", "t");
	}
	public static OMDbMovie OMDbQuery(String input, String type) {
		//Title search
		if(type.equals("t")) {
			input = input.replaceAll("\\s+","+");
		}
		//Random search
		if(type.equals("i") && input.isEmpty()) {
			Random r = new Random();
		    int numbers = 1000000 + (int)(r.nextFloat() * 99999);
		    input = "tt" + String.valueOf(numbers);
		}
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--disable-extensions");
		options.addArguments("--disable-gpu");
		options.setPageLoadStrategy(PageLoadStrategy.NONE);
		// Create a new instance of the Chrome driver
		WebDriver driver = new ChromeDriver(options);
		// Find the text input element by its name
		driver.get("http://www.omdbapi.com/?apikey=5341da14&type=movie&" + type + "=" + input);
		String content = driver.findElement(By.tagName("body")).getText();
		Gson gson = new Gson();
		OMDbMovie movie = (OMDbMovie) gson.fromJson(content, OMDbMovie.class);
		//Close the browser
		driver.quit();
		return movie;
	}
}
