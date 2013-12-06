public class Rating {
	private String Date;
	private int Customer;
	private int Rate;
	
	public Rating(int CustomerID, int rateVal, String dateRate) {
		Customer = CustomerID;
		Rate = rateVal;
		Date = dateRate;
		}
	
	int getRate() {
		return Rate;
		}
	
	void setRate(int a) {
		Rate=a;
		}
	
	int getCustomer() {
		return Customer;
		}
	
	void setCustomer(int a) {
		Customer=a;
		}
	
	String getDate() {
		return Date;
		}
	
	void setDate(String a) {
		Date=a;
		}
}
