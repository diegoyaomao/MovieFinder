package principal;

public class OMDbMovie {
	private String Title;
	private String Year;
	private String Rated;
	private String Released;
	private String Runtime;
	private String Genre;
	private String Director;
	private String Writer;
	private String Actors;
	private String Plot;
	private String Language;
	private String Country;
	private String Awards;
	private String Poster;
	private Ratings Ratings [];
	private String Metascore;
	private String imdbRating;
	private String imdbVotes;
	private String imdbID;
	private String Type;
	private String DVD;
	private String BoxOffice;
	private String Production;
	private String Website;
	private String Response;
	
	public String getTitle() {
		return Title;
	}
	public String getYear() {
		return Year;
	}
	public String getRated() {
		return Rated;
	}
	public String getReleased() {
		return Released;
	}
	public String getRuntime() {
		return Runtime;
	}
	public String getGenre() {
		return Genre;
	}
	public String getDirector() {
		return Director;
	}
	public String getWriter() {
		return Writer;
	}
	public String getActors() {
		return Actors;
	}
	public String getPlot() {
		return Plot;
	}
	public String getLanguage() {
		return Language;
	}
	public String getCountry() {
		return Country;
	}
	public String getAwards() {
		return Awards;
	}
	public String getPoster() {
		return Poster;
	}
	public Ratings[] getRatings() {
		return Ratings;
	}
	public String getMetascore() {
		return Metascore;
	}
	public String getImdbRating() {
		return imdbRating;
	}
	public String getImdbVotes() {
		return imdbVotes;
	}
	public String getImdbID() {
		return imdbID;
	}
	public String getType() {
		return Type;
	}
	public String getDVD() {
		return DVD;
	}
	public String getBoxOffice() {
		return BoxOffice;
	}
	public String getProduction() {
		return Production;
	}
	public String getWebsite() {
		return Website;
	}
	public String getResponse() {
		return Response;
	}
	//create an object of OMDbMovie
	private static OMDbMovie instance = new OMDbMovie();

	//make the constructor private so that this class cannot be instantiated from external classes
	private OMDbMovie(){}
   
	//Get the OMDbMovie object available and return it
	public static OMDbMovie getInstance(){
		return instance;
	}
}