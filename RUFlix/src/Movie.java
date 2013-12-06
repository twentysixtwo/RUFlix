import java.util.*;

public class Movie {
	private int MovieID;
	private Vector<Rating> Rated = new Vector<Rating>();
	private String Title;
	private int YearOfRelease;
	private boolean hasRate = false;
	
	public Movie(int movieID,int yearRelease, String title) {
		setMovieID(movieID);
		setYear(yearRelease);
		setTitle(title);
		}
	
	void printInfo() {
		//System.out.println(MovieID + "\t\t" + YearOfRelease + "\t" + Title + "\t" + avgRating());
		System.out.printf("%4d,%-20.20s,%4d,%3.2f\n",getMovieID(),getTitle(),getYear(),avgRating());
		}
	
	void setMovieID(int a){
		MovieID=a;
		}
	
	int getMovieID() {
		return MovieID;
		}
	
	void setTitle(String a) {
		Title=a;
		}
	
	String getTitle() {
		return Title;
		}
	
	void setYear(int a) {
		YearOfRelease=a;
		}
	
	int getYear() {
		return YearOfRelease;
		}
	
	boolean hasRating() {
		return hasRate;
		}
	
	void hasRating(boolean hasRates) {
		hasRate=hasRates;
		}
	
	void addRating(int a,int b,String c) {
		Rated.add(new Rating(a,b,c));
		hasRating(true);
		}
	
	float avgRating() {
		int count = Rated.size();
		int total=0;
		for (Rating i : Rated) {
			total += i.getRate();
			}
		if((float)total/count>0)
			return (float)total/count;
		else return -2;
	}
	
	int getNumRatings()
	{
		return Rated.size();
	}
	
	void printRating()
	{
		
		if(hasRating()){
		Iterator<Rating> j = Rated.iterator();
		if(j.hasNext())
			{
			System.out.println("\n\tRating Data");
			System.out.println("\tMovieID\t\tCustomerID\t\tRating(1-5)\tDate");
			}
		for (Rating i : Rated)
			{
			System.out.println("\t"+MovieID + "\t\t" + i.getCustomer() + "\t\t\t" + i.getRate() + "\t\t" + i.getDate());
			}
		System.out.println();
		}
		else 
		{
			System.out.println("This movie has no ratings.\n");
		}
	}
	
	Vector<Integer> returnFiveStars()
	{
		Vector<Integer> temp = new Vector<Integer>();
		for (Rating i : Rated)
		{
			if(i.getRate()==5)
			{
				int tempCID = i.getCustomer();
				temp.add(tempCID);
			}
		}
		return temp;
	}
	
	//Conditional Probability Classes/Variables
	
	private Comparator<CondProb> comp = new CondProbComp();
	private PriorityQueue<CondProb> condQueue = new PriorityQueue<CondProb>(10,Collections.reverseOrder(comp));
		
	void addProb(float prob, int idb)
	{
		CondProb temp = new CondProb(prob,idb);
		condQueue.add(temp);
	}
	
	/*
	 * retProb returns a Vector<Integer> containing, in order,
	 * the list of top 10 conditional probability movies. 
	 */
	
	Vector<Integer> retProb()
	{
		Vector<Integer> temp = new Vector<Integer>();
		int count = 1;
		while((!condQueue.isEmpty())&&(count<=10))
			{
				CondProb i = condQueue.remove();
				temp.add(i.movieID);
				count++;
			}
		return temp;
	}
	
	private class CondProb{
		private float condProb;
		private int movieID;
		
		CondProb(float prob,int idb){
			condProb = prob;
			movieID = idb;
		}
	}
	
	private class CondProbComp implements Comparator<CondProb>{
		public int compare(CondProb o1, CondProb o2) {
			return Float.compare(o1.condProb, o2.condProb);
		}
	}	
}
