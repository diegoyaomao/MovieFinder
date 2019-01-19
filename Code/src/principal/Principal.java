package principal;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import javax.swing.JOptionPane;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.google.gson.Gson;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;


public class Principal {

	protected Shell shell;
	private Text text;
	private Label lblInformation;
	private Text text_1;
	private String CurrentMovie;
	private String CurrentMovieID;
	public List list;
	public Label lblPoster;

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		ReadWishlist();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(1350, 900);
		shell.setText("Movie Information Client");
		shell.setLayout(null);
		
		Label lblFilmName = new Label(shell, SWT.NONE);
		lblFilmName.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblFilmName.setBounds(35, 26, 138, 32);
		lblFilmName.setText("FILM NAME");
		
		text = new Text(shell, SWT.BORDER);
		text.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text.setBounds(35, 71, 452, 40);
		
		lblInformation = new Label(shell, SWT.NONE);
		lblInformation.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblInformation.setBounds(35, 134, 166, 30);
		lblInformation.setText("INFORMATION");
		
		text_1 = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text_1.setBounds(35, 180, 453, 634);
		
		lblPoster = new Label(shell, SWT.NONE );
		lblPoster.setBounds(535, 182, 300, 450);
		
		Button btnAddToWishlist = new Button(shell, SWT.NONE);
		btnAddToWishlist.setBounds(532, 673, 303, 54);
		btnAddToWishlist.setText("ADD TO WISHLIST");
		btnAddToWishlist.setEnabled(false);
		Button btnDeleteFromWishlist = new Button(shell, SWT.NONE);
		btnDeleteFromWishlist.setText("DELETE FROM WISHLIST");
		btnDeleteFromWishlist.setBounds(532, 747, 303, 54);
		btnDeleteFromWishlist.setEnabled(false);
		
		Label lblWhishlist = new Label(shell, SWT.NONE);
		lblWhishlist.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblWhishlist.setBounds(909, 134, 130, 30);
		lblWhishlist.setText("WHISHLIST");
		
		ListViewer listViewer = new ListViewer(shell, SWT.BORDER | SWT.V_SCROLL);
		list = listViewer.getList();
		list.setItems(new String[] {});
		list.setBounds(909, 179, 365, 634);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				//Enable the delete button because it's contained in the wishlist
			    btnDeleteFromWishlist.setEnabled(true);
			    btnAddToWishlist.setEnabled(false);
	        	//Get input from text field and get the Movie object
				String selected[] = list.getSelection();
				String id = selected[0].substring(selected[0].length()-9,selected[0].length());	          
	        	OMDbMovie Selectedmovie = OMDbQuery(id, "i");
	        	CurrentMovie = Selectedmovie.getTitle();
	        	CurrentMovieID = Selectedmovie.getImdbID();
	        	String content = GetOMDbContent(Selectedmovie);
		        text_1.setText(content);
		        LoadPoster("OMDb", Selectedmovie.getPoster());
			}
		});
		
		btnAddToWishlist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String movieID = CurrentMovie + "                                                                        " + CurrentMovieID;
				list.add(movieID);
				btnAddToWishlist.setEnabled(false);
				btnDeleteFromWishlist.setEnabled(true);
				//Upload wishlist file
				WriteWishlist();
			}
		});
		
		btnDeleteFromWishlist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent f) {
				for(int n=0; n<list.getItemCount(); n++) {
					if(list.getItem(n).toLowerCase().contains(CurrentMovie.toLowerCase())) {
						list.remove(n);
						btnAddToWishlist.setEnabled(true);
			        	btnDeleteFromWishlist.setEnabled(false);
			        	//Upload wishlist file 
			        	WriteWishlist();
			        }
		        };
			}
		});
		
		Combo dbselector = new Combo(shell, SWT.READ_ONLY);
		dbselector.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		dbselector.setItems(new String[] {" OMDb", " TMDb"});
		dbselector.setBounds(487, 71, 116, 40);
		dbselector.select(0);
		
		Button btnRandomSearch = new Button(shell, SWT.NONE);
		btnRandomSearch.setBounds(863, 71, 176, 40);
		btnRandomSearch.setText("RANDOM SEARCH");
		btnRandomSearch.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event ee) {
		        switch (ee.type) {
		        case SWT.Selection:
		        	//Reset the list pointer
		        	list.deselectAll();
		        	//Reset buttons
		        	btnAddToWishlist.setEnabled(false);
		        	btnDeleteFromWishlist.setEnabled(false);
		        	OMDbMovie movie = OMDbQuery("", "i");
		        	//If it's not in the database, then try again
			        while(movie.getResponse().equals("False") || !movie.getType().equals("movie")) {
			        	movie = OMDbQuery("", "i");
			        }
		        	CurrentMovie = movie.getTitle();
		        	CurrentMovieID = movie.getImdbID();
		        	String content = GetOMDbContent(movie);
			        text_1.setText(content);
			        //Check if the movie is on the list or not, if yes then enable delete button, if not, enable add button
			        for(int n=0; n <= list.getItemCount(); n++) {
				        if (n == list.getItemCount()) {
				        	btnAddToWishlist.setEnabled(true);
				        }
				        else {
				        	if(CurrentMovie.equals(list.getItem(n).substring(0, list.getItem(n).indexOf(" ")))) {
				        		btnDeleteFromWishlist.setEnabled(true);
				        	}
				        }
			        }
			        LoadPoster("OMDb", movie.getPoster());
		        break;
		        }
		      }
		    });
		        	
		        	
		Button btnSearch = new Button(shell, SWT.NONE);
		btnSearch.setBounds(659, 71, 176, 40);
		btnSearch.setText("SEARCH");
		btnSearch.addListener(SWT.Selection, new Listener() {
	      public void handleEvent(Event e) {
	        switch (e.type) {
	        case SWT.Selection:
	        	//Reset the list pointer
	        	list.deselectAll();
	        	//Get input from text field and get the Movie object
	        	String input = text.getText();
	        	//If search field is empty
	        	if(input.isEmpty()) {
	        		JOptionPane.showMessageDialog(null, "Please, fill the Search field");
	        		return;
	        	}
	        	//Reset buttons
	        	btnAddToWishlist.setEnabled(false);
	        	btnDeleteFromWishlist.setEnabled(false);
	        	String content = "";
	        	//If it uses OMDb
	        	if(dbselector.getText().equals(" OMDb")) {
	        		OMDbMovie movie = OMDbQuery(input, "t");
			        //If it's not in the database, then show error message
			        if(movie.getResponse().equals("False")) {
			        	JOptionPane.showMessageDialog(null, "Movie not found!");
			        }
			        else {
			        	CurrentMovie = movie.getTitle();
			        	CurrentMovieID = movie.getImdbID();
			        	content = GetOMDbContent(movie);
				        text_1.setText(content);
				        //Check if the movie is on the list or not, if yes then enable delete button, if not, enable add button
				        for(int n=0; n <= list.getItemCount(); n++) {
					        if (n == list.getItemCount()) {
					        	btnDeleteFromWishlist.setEnabled(false);
					        	btnAddToWishlist.setEnabled(true);
					        }
					        else {
					        	if(CurrentMovie.equals(list.getItem(n).substring(0, list.getItem(n).indexOf(" ")))) {
					        		btnAddToWishlist.setEnabled(false);
					        		btnDeleteFromWishlist.setEnabled(true);
					        		break;
					        	}
					        }
				        }
				        LoadPoster("OMDb", movie.getPoster());
			        }
	        	}
	        	//If it uses TMDb
	        	else {
	        		TMDbMovie movie = TMDbQuery(input, "t");
	        		if(!movie.getResponse().equals("false")) {
	        			CurrentMovie = movie.getTitle();
	        			CurrentMovieID = movie.getImdb_id();
			        	content = GetTMDbContent(movie);
				        text_1.setText(content);
				        //Check if the movie is on the list or not, if yes then enable delete button, if not, enable add button
				        for(int n=0; n <= list.getItemCount(); n++) {
				        	if (n == list.getItemCount()) {
					        	btnDeleteFromWishlist.setEnabled(false);
					        	btnAddToWishlist.setEnabled(true);
					        }
					        else {
					        	if(CurrentMovie.equals(list.getItem(n).substring(0, list.getItem(n).indexOf(" ")))) {
					        		btnAddToWishlist.setEnabled(false);
					        		btnDeleteFromWishlist.setEnabled(true);
					        		break;
					        	}
					        }
				        }
				        LoadPoster("TMDb", movie.getPoster_path());
	        		}
	        	}
	        	//Reset text field
	        	text.setText("");
	          break;
	        }
	      }
	    });
	}
	//create an object of Principal
	private static Principal instance = new Principal();

	//make the constructor private so that this class cannot be instantiated from external classes
	private Principal(){}
   
	//Get the Principal object available and return it
	public static Principal getInstance(){
		return instance;
	}
	public OMDbMovie OMDbQuery(String input, String type) {
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
		OMDbMovie movie = OMDbMovie.getInstance();
		movie = (OMDbMovie) gson.fromJson(content, OMDbMovie.class);
		//Close the browser
		driver.quit();
		return movie;
	}
	public TMDbMovie TMDbQuery(String input, String type) {
		if(type.equals("t")) {
			input = input.replaceAll("\\s+","+");
		}
		if(type.equals("i")&& input.isEmpty()) {
			Random r = new Random();
		    int numbers = 1000000 + (int)(r.nextFloat() * 99999);
		    input = "tt" + String.valueOf(numbers);
		}
		ChromeOptions options = new ChromeOptions();
	 	options.addArguments("--headless");
        // Create a new instance of the Chrome driver
	 	WebDriver driver = new ChromeDriver(options);

        // Find the searching results text input element by its name
        driver.get("https://api.themoviedb.org/3/search/movie?api_key=ab941f8215d78901bc538afbb2a9eed9&type=movie&query="+input);
        
        String Resultscontent = driver.findElement(By.tagName("body")).getText();
        //If it's not in the database, then show error message
        if(Resultscontent.indexOf("id") == -1) {
        	JOptionPane.showMessageDialog(null, "Movie not found!");
        	//Close the browser
    		driver.quit();
    		TMDbMovie movie = TMDbMovie.getInstance();
    		movie.setResponse("false");
    		return movie;
        }
        else {
            int beginning1 = Resultscontent.indexOf("id");
    		int ending1 = Resultscontent.indexOf(",\"video\"");
    		String id = Resultscontent.substring(beginning1+4, ending1);
    		 // Find the movie text input element by its name
            driver.get("https://api.themoviedb.org/3/movie/"+id+"?api_key=ab941f8215d78901bc538afbb2a9eed9&append_to_response=credits");
            
            String Moviecontent = driver.findElement(By.tagName("body")).getText();
            Gson gson = new Gson();
            TMDbMovie movie = (TMDbMovie) gson.fromJson(Moviecontent, TMDbMovie.class);
            movie.setResponse("true");
            //Close the browser
    		driver.quit();
    		return movie;
        }
	}
	
	public String GetOMDbContent(OMDbMovie movie) {
		String rating = "";
    	//Not all the movies has 3 ratings
    	if(movie.getRatings().length>0) {
    		rating = "Ratings: " + movie.getRatings()[0].getSource() + " " + movie.getRatings()[0].getValue() + "\n";
    		if(movie.getRatings().length>1) {
    			rating += movie.getRatings()[1].getSource() + " " + movie.getRatings()[1].getValue() + "\n";
    			if(movie.getRatings().length>2) {
    				rating += movie.getRatings()[2].getSource() + " " + movie.getRatings()[2].getValue() + "\n";		        				
    			}
    		}
    		rating += "\n";
    	}
    	else {
    		rating = "N/A";
    	}
        String content = "Title: " + movie.getTitle() + "\n\n"
        + "Year: " + movie.getYear() + "\n\n"
        + "Rated: " + movie.getRated() + "\n\n"
        + "Released: " + movie.getReleased() + "\n\n"
        + "Runtime: " + movie.getRuntime() + "\n\n"
        + "Genre: " + movie.getGenre() + "\n\n"
        + "Director: " + movie.getDirector() + "\n\n"
        + "Writer: " + movie.getWriter() + "\n\n"
        + "Actors: " + movie.getActors() + "\n\n"
        + "Plot: " + movie.getPlot() + "\n\n"
        + "Language: " + movie.getLanguage() + "\n\n"
        + "Country: " + movie.getCountry() + "\n\n"
        + "Awards: " + movie.getAwards() + "\n\n"
        + rating 
        + "Metascore: " + movie.getMetascore() + "\n\n"
        + "Imdb Rating: " + movie.getImdbRating() + "\n\n"
        + "Imdb Votes: " + movie.getImdbVotes() + "\n\n"
        + "ImdbID: " + movie.getImdbID() + "\n\n"
        + "DVD: " + movie.getDVD() + "\n\n"
        + "Box Office: " + movie.getBoxOffice() + "\n\n"
        + "Production: " + movie.getProduction() + "\n\n"
        + "Website: " + movie.getWebsite() + "\n";
        return content;
	}
	public String GetTMDbContent(TMDbMovie movie) {
		String sactors = "";
		int limit = 5;
		if(movie.getCredits().getCast().length < 5) {
			limit = movie.getCredits().getCast().length;
		}
		if(limit == 0) {
			sactors = "N/A";
		}
		else {
			for(int i=0; i < limit ;i++) {
				if(i == limit - 1) {
					sactors += movie.getCredits().getCast()[i].getName() +" (" + movie.getCredits().getCast()[i].getCharacter() + ")";
				}
				else {
					sactors += movie.getCredits().getCast()[i].getName() +" (" + movie.getCredits().getCast()[i].getCharacter() + "), ";
				}
			}
		}

		String director = "";
		for(int i=0, j=0; i <= movie.getCredits().getCrew().length ;i++) {
			if(i != movie.getCredits().getCrew().length && movie.getCredits().getCrew()[i].getJob().equals("Director")) {
				if(j == 0) {
					director += movie.getCredits().getCrew()[i].getName();
					j++;
				}
				else {
					director += ", " + movie.getCredits().getCrew()[i].getName();
					j++;
				}
			}
			if(i == movie.getCredits().getCrew().length) {
				director = "N/A";
			}
		}
		
		String writer = "";
		for(int i=0, j=0; i <= movie.getCredits().getCrew().length ;i++) {
			if(i != movie.getCredits().getCrew().length && movie.getCredits().getCrew()[i].getDepartment().equals("Writing")) {
				if(j == 0) {
					writer += movie.getCredits().getCrew()[i].getName() + " (" + movie.getCredits().getCrew()[i].getJob() + ")";
					j++;
				}
				else {
					writer += ", " + movie.getCredits().getCrew()[i].getName() + " (" + movie.getCredits().getCrew()[i].getJob() + ")";
					j++;
				}
			}
			if(i == movie.getCredits().getCrew().length) {
				writer = "N/A";
			}
		}
		
		String content = "Title: " + movie.getTitle() + "\n\n"
		+ "Status: " + movie.getStatus() + "\n\n"
		+ "Release date: " + movie.getRelease_date() + "\n\n"
		+ "Runtime: " + movie.getRuntime() + "\n\n"
		+ "Genre: " + movie.getGenres() + "\n\n"
	    + "Director: " + director + "\n\n"
	    + "Writer: " + writer + "\n\n"
	    + "Actors: " + sactors + "\n\n"
	    + "Overview: " + movie.getOverview() + "\n\n"
	    + "Original language: " + movie.getOriginal_language() + "\n\n"
	    + "Original title: " + movie.getOriginal_title() + "\n\n"
	    + "Production countries: " + movie.getProduction_countries() + "\n\n"
	    + "Production companies: " + movie.getProduction_companies() + "\n\n"
	    + "Popularity: " + movie.getPopularity() + "\n\n"
	    + "Vote average: " + movie.getVote_average() + "\n\n"
	    + "Vote count: " + movie.getVote_count() + "\n\n"
	    + "ImdbID: " + movie.getImdb_id() + "\n\n"
	    + "Revenue: " + movie.getRevenue() + "\n\n"
	    + "Budget: " + movie.getBudget() + "\n\n"
	    + "Homepage: " + movie.getHomepage() + "\n\n";
	    return content;
	}
	public void WriteWishlist() {
		String tofile = "";
		for(int k=0; k<list.getItemCount(); k++) {
			tofile += list.getItem(k) + ",";
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
	              new FileOutputStream("wishlist.txt"), "utf-8"))) {
			writer.write(tofile);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public void ReadWishlist() {	
		try {
			byte[] encoded = Files.readAllBytes(Paths.get("wishlist.txt"));
			String wishlist = new String(encoded, "utf-8");
			//adds each film to the whislist
			for(String subString: wishlist.split(",")){
			   list.add(subString);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void LoadPoster(String DB, String link){
		//Clear images cache
        SWTResourceManager.disposeImages();
        //If the movie has no poster link, then show message
        if(link.equals("N/A") || link == null)	{
        	lblPoster.setImage(SWTResourceManager.getImage(System.getProperty("user.dir")+"\\images\\No-image-available.jpg"));
        }
        else {
			if(DB == "OMDb") {
		        //If the movie has poster, then show poster
		        try(InputStream in = new URL(link).openStream()){
			        Files.copy(in, Paths.get(System.getProperty("user.dir")+"\\images\\poster.jpg"));
			    }
			    catch (IOException p) {
					p.printStackTrace();
				}
		        lblPoster.setImage(SWTResourceManager.getImage(System.getProperty("user.dir")+"\\images\\poster.jpg"));
				//Delete image to free space
				Path path = Paths.get(System.getProperty("user.dir")+"\\images\\poster.jpg");
				try {
					Files.delete(path);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if(DB == "TMDb") {
		        //If the movie has poster, then show poster
	        	try(InputStream in = new URL("https://image.tmdb.org/t/p/w300/" + link).openStream()){
			        Files.copy(in, Paths.get(System.getProperty("user.dir")+"\\images\\poster.jpg"));
			    }
			    catch (IOException p) {
					p.printStackTrace();
				}
		        lblPoster.setImage(SWTResourceManager.getImage(System.getProperty("user.dir")+"\\images\\poster.jpg"));
				//Delete image to free space
				Path path = Paths.get(System.getProperty("user.dir")+"\\images\\poster.jpg");
				try {
					Files.delete(path);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
        }
	}
}