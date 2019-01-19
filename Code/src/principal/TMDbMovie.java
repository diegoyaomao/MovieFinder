package principal;

import java.text.NumberFormat;
import java.util.Locale;

public class TMDbMovie {
	private String title;
	private String status;
	private String release_date;
	private String runtime;
	private Genres genres [];
	private String overview;
	private String original_language;
	private String original_title;
	private String vote_average;
	private String vote_count;
	private String popularity;
	private String imdb_id;
	private String revenue;
	private String budget;
	private Production_countries production_countries [];
	private Production_companies production_companies [];
	private String homepage;
	private String id;
	private String poster_path;
	private Credits credits;
	private String response;
	
	public String getGenres() {
		String sgenres = "";
		if(genres.length == 0) {
			return "N/A";
		}
		for(int i=0; i<genres.length ;i++) {
			if(i == genres.length - 1) {
				sgenres += genres[i].getName();
			}
			else {
				sgenres += genres[i].getName() + ", ";
			}
		}
		return sgenres;
	}
	public String getProduction_countries() {
		String sproduction_countries = "";
		if(production_countries.length == 0) {
			return "N/A";
		}
		for(int i=0; i<production_countries.length ;i++) {
			if(i == production_countries.length - 1) {
				sproduction_countries += production_countries[i].getName();
			}
			else {
				sproduction_countries += production_countries[i].getName() + ", ";
			}
		}
		return sproduction_countries;
	}
	public String getProduction_companies() {
		String sproduction_companies = "";
		if(production_companies.length == 0) {
			return "N/A";
		}
		for(int i=0; i<production_companies.length ;i++) {
			if(i == production_companies.length - 1) {
				sproduction_companies += production_companies[i].getName();
			}
			else {
				sproduction_companies += production_companies[i].getName() + ", ";
			}
		}
		return sproduction_companies;
	}
	public Credits getCredits() {
		return credits;
	}
	
	public String getVote_count() {
		if (vote_count == null) {
			return "N/A";
		}
		return vote_count;
	}
	public String getStatus() {
		if (status == null) {
			return "N/A";
		}
		return status;
	}
	public String getRuntime() {
		if (runtime == null) {
			return "N/A";
		}
		return runtime + "min";
	}
	public String getImdb_id() {
		return imdb_id;
	}
	public String getRevenue() {
		if (revenue == null) {
			return "N/A";
		}
		return "$"+NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(revenue));
	}
	public String getBudget() {
		if (budget == null) {
			return "N/A";
		}
		return "$"+NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(budget));
	}
	public String getHomepage() {
		if (homepage == null) {
			return "N/A";
		}
		return homepage;
	}
	public String getId() {
		return id;
	}
	public String getVote_average() {
		if (vote_average == null) {
			return "N/A";
		}
		return vote_average;
	}
	public String getTitle() {
		return title;
	}
	public String getPopularity() {
		if (popularity == null) {
			return "N/A";
		}
		return popularity;
	}
	public String getPoster_path() {
		return poster_path;
	}
	public String getOriginal_language() {
		if (original_language == null) {
			return "N/A";
		}
		return original_language;
	}
	public String getOriginal_title() {
		if (original_title == null) {
			return "N/A";
		}
		return original_title;
	}
	public String getOverview() {
		if (overview == null || overview.isEmpty()) {
			return "N/A";
		}
		return overview;
	}
	public String getRelease_date() {
		return release_date;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	//create an object of OMDbMovie
	private static TMDbMovie instance = new TMDbMovie();

	//make the constructor private so that this class cannot be instantiated from external classes
	private TMDbMovie(){}
   
	//Get the OMDbMovie object available and return it
	public static TMDbMovie getInstance(){
		return instance;
	}
}