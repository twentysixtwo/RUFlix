import java.io.*;
import java.util.*;

	public class RUFlix {

		private static Vector<Movie> movies = new Vector<Movie>(); 
		
		public static void main(String args[]) throws IOException
		{	
			String fileName = "movie_titles.txt";
			load(fileName); // Only works with text files in the working directory
			printAll();
			//boolean parse = true;
			while(parseInput());
		}
		
		static boolean parseInput()
		{
				Scanner scan = new Scanner(System.in);
				
				System.out.print("\nCommand Input : ");
				String line = scan.nextLine();
				String[] part = line.split(" ");
					
				scan.close();
				RUFlix.Reader read = (new RUFlix()).new Reader((new RUFlix()).new searchCmd(part),(new RUFlix()).new recommendCmd(part));
				return read.parse(part);
					
		}
		
		static void printAll()
		{
			System.out.printf("%7s,%-20.20s,%4s,%6s\n","MovieID","Title","Year","Rating");
			for (Movie i: movies)
			{
				i.printInfo();
			}	
		}
		
		static void printMoviesInYear(int year)  //FOR EACH
		{
			System.out.println("Searching for movies from year " + year + "\n");
			System.out.printf("%-20.20s\tYear\tRating\n","MovieID Title");
			System.out.println("--------------------------------------");
			for (Movie i: movies)
					if(i.getYear()==year)
						i.printInfo();
		}
		
		static void printMoviesWithKeyword(String keyword) //ITERATOR
		{
			Iterator<Movie> i = movies.iterator();
			System.out.println("\nSearching for movies with keyword " + keyword + "\n");
			System.out.printf("%-20.20s\tYear\tRating\n","MovieID Title");
			System.out.println("--------------------------------------");
			while(i.hasNext())
			{
				Movie a = i.next();
				if(a.getTitle().toLowerCase().indexOf(keyword.toLowerCase())!=-1)
				a.printInfo();
			}
		}
		
		static float averageRating(int movieID) 
		{
			for (Movie i: movies)
			{
				if(i.getMovieID()==movieID)
				{
					return i.avgRating();
				}
			}
			return -1;
		}

		static void printMoviesWithRating(Collection<Movie> movies)
		{
			Vector<Movie> movie = (Vector<Movie>)movies;
			Collections.sort(movie, Collections.reverseOrder(comp1));
			System.out.printf("%-20.20s\tYear\tRating\n","MovieID Title");
			System.out.println("--------------------------------------");
			for (Movie i: movie)
			{
				if ((i.avgRating()!=-2)&&(i.hasRating()))
				System.out.printf("%-20.20s\t%4d\t%3.2f\n",i.getTitle(),i.getYear(),i.avgRating());
			}
		}
		
		static void printMoviesWithRating(float rate)
		{
			Vector<Movie> movie = movies;
			System.out.printf("%-20.20s\tYear\tRating\n","MovieID Title");
			System.out.println("--------------------------------------");
			for (Movie i: movie)
			{
				if ((i.avgRating()==rate)&&(i.hasRating()))
				System.out.printf("%-20.20s\t%4d\t%3.2f\n",i.getTitle(),i.getYear(),i.avgRating());
			}
		}
		
		static void printMoviesWithMinRating(float rate)
		{
			Vector<Movie> movie = movies;
			System.out.printf("%-20.20s\tYear\tRating\n","MovieID Title");
			System.out.println("--------------------------------------");
			for (Movie i: movie)
			{
				if ((i.avgRating()>=rate)&&(i.hasRating()))
				System.out.printf("%-20.20s\t%4d\t%3.2f\n",i.getTitle(),i.getYear(),i.avgRating());
			}
		}
		
		static void load(String fileName) throws IOException
		{
	         	File file = new File(fileName);	
	        		Scanner scan = new Scanner(new FileReader(file));
	        		while(scan.hasNext())
	        		{
	        			String temp = scan.nextLine(); // Read the movie info (MovieID, YearOfRelease, Title)
	        			String []t = temp.split(",");
	        			
	        			int a = Integer.parseInt(t[0]); // MovieID
	        			//System.out.println(a);
	        			int b =0;
	        			try{
	        				b=Integer.parseInt(t[1]); // YearOfRelease
	        			}
	        			catch(NumberFormatException e)
	        			{
	        				b = 0;
	        			}
	        			String c=t[2]; // Title
	        			
	        			movies.add(new Movie(a,b,c));
	        			loadRatings(a,b,c);	
	        		}
	        		scan.close();
		}
		
		static void loadRatings(int a, int b, String c) throws FileNotFoundException
		{
			String ratingName = String.format("mv_%07d.txt",a);
			File ratingFile = new File(ratingName);
			if(ratingFile.exists())
			{
				Scanner rateScan = new Scanner(new FileReader(ratingFile));
				rateScan.nextLine(); // Skip the movieID line
				while(rateScan.hasNext()){
				String temp = rateScan.nextLine(); // Read the rating info (CustomerID, Rating, Date)
				String []t=temp.split(",");
				int x = Integer.parseInt(t[0]); // CustomerID
				int y = Integer.parseInt(t[1]); // Rating
				String z = t[2]; // Date 
				movies.lastElement().addRating(x,y,z); // After adding the new movie, the new movie is at the end of the vector.
				}	
			}
			else
				System.out.printf("No rating data for %s available\n",c);
	
		}
		
		static float loveMovie(int movieIDA, int movieIDB) 
		{	
			/*
			 * loveMovie compares two Vector<Integers> which contain
			 * the customer ids of those who rated 5 stars for the
			 * respective movies. The probability is then loaded 
			 * with the reference movie into the movie's condProb
			 * priorityQueue. The float value of the probability is
			 * returned for output.
			 */
			
			Vector<Integer> numFiveA = new Vector<Integer>();
			Vector<Integer> numFiveB = new Vector<Integer>();
			
			int numRatingsA=0,numRatingsB=0; // Used in the probability calculation.
			
			for(Movie i: movies)
			{
				if (i.getMovieID()==movieIDA)
				{
					numFiveA = i.returnFiveStars();
					numRatingsA = i.getNumRatings();
				}
				if (i.getMovieID()==movieIDB)
				{
					numFiveB = i.returnFiveStars();
					numRatingsB = i.getNumRatings();
				}
			}
			numFiveA.retainAll(numFiveB); // numFiveA = P(A intersect B)
			float pab = ((float)numFiveA.size())/numRatingsA;
			float pb = ((float)numFiveB.size())/numRatingsB;
			
			for (Movie i: movies)
			{
				if (i.getMovieID()==movieIDA)
				{
					i.addProb(pab/pb,movieIDB);
				}
			}
			return pab/pb;
		}

		static Vector<Movie> recommendMovies(int movieIDA)
		{ 
			Vector<Movie> a = new Vector<Movie>();
			for (Movie i : movies)
			{
				if(i.getMovieID()!=movieIDA) // ignore movieIDA since condProb would equal 1
				{
				loveMovie(movieIDA,i.getMovieID());	
				}
			}
			Vector<Integer> temp = new Vector<Integer>();
			for (Movie i : movies)
			{
				if(i.getMovieID()==movieIDA)
					temp = i.retProb();
			}
			
			for (int i : temp)
			{
				for(Movie j : movies)
				{
					if(j.getMovieID() == i)
					{
						a.add(j);
					}
				}
			}
			return a;
		}
		
		//Sorts movie list by average rating
		static Comparator<Movie> comp1 = new Comparator<Movie>() {
			public int compare(Movie arg0, Movie arg1) {
				return Float.compare(arg0.avgRating(), arg1.avgRating());
			}
		};	
		//Sorts movie list by year and then average rating
		static Comparator<Movie> comp2 = new Comparator<Movie>() {
			public int compare(Movie arg0, Movie arg1) {
				int i =((Integer)arg0.getYear()).compareTo((Integer)arg1.getYear());
				if (i != 0) return i;
				return Float.compare(arg0.avgRating(), arg1.avgRating());
			}
		};
		
		static void printSortedMovieList()
		{
			//reverseOrder so that it lists top down.
			Collections.sort(movies, Collections.reverseOrder(comp1));
			System.out.println("Printing movies sorted by average rating.");
			printAll();
			Collections.sort(movies, Collections.reverseOrder(comp2));
			System.out.println("Printing movies sorted by year and then average rating.");
			printAll();
		}
		
		public class Reader{
			
			private Command searchCmd;
			private Command recommendCmd;
			
			public Reader(Command search, Command recommend){
				this.searchCmd = search;
				this.recommendCmd = recommend;
			}
			
			public boolean parse(String[] part) 
			{
				if(part[0].equalsIgnoreCase("search"))
				{
					search();
					return true;
				}
				else if(part[0].equalsIgnoreCase("recommend"))
				{
					recommend();
					return true;
				}
				else if(part[0].equalsIgnoreCase("exit"))
				{
					System.out.println("Goodbye.\n\n");
					return false;
				}
				else
				{
					System.out.println("\nInvalid command parameter : " + part[0] + "\n");
					return true;
				}
			}
			
			public void search() {
				new Thread(searchCmd).start();
				//searchCmd.execute();
			}
			
			public void recommend() {
				new Thread(recommendCmd).start();
				//recommendCmd.execute();
			}
		}
		
		public interface Command extends Runnable{
			void execute();
		}
		
		public class searchCmd implements Command {
			public searchCmd(String[] part) {
				this.temp = part;
			}

			public void execute() 
			{
				if(temp[1].equalsIgnoreCase("year"))
				{
					printMoviesInYear(new Integer(temp[2]));
				}
				else if(temp[1].equalsIgnoreCase("title"))
				{
					printMoviesWithKeyword(temp[2]);
				}
				else if(temp[1].equalsIgnoreCase("rating"))
				{
					printMoviesWithMinRating(new Float(temp[2]));
				}
				else
				{
				System.out.println("Invalid search parameter : " + temp[1].toLowerCase());	
				}
			}
			private String[] temp;
			
			public void run() {
				this.execute();
			}
		}
		
		public class recommendCmd implements Command {
			public recommendCmd(String[] part) {
				this.temp = part;
			}

			public void execute()
			{
				printMoviesWithRating(recommendMovies(new Integer(temp[1])));
			}
			
			private String[] temp;

			public void run() {
				this.execute();
			}
		}
		
		
	}