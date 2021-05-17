public class Main {
	public static void main(String []args){
		Assignment2 db = new Assignment2();
		boolean attempt;
		int num;
		String s;
		
		attempt = db.connectDB("jdbc:postgresql://db:5432/gavinsit", "gavinsit", "215043870");
		 
		attempt = db.insertCountry(1234, "Valorant", 3333, 987654);
		System.out.println("attempt to add: " + attempt);
		
		num = db.getCountriesNextToOceanCount(1);
		System.out.println("attempt get next to ocean: " + num);
		
		s = db.getOceanInfo(1);
		System.out.println("attempt to get ocean info: " + s + "eof");
		
		attempt = db.chgHDI(1,2009, 123);
		System.out.println("attempt to change hdi: " + attempt);
		
		attempt = db.deleteNeighbour(1,2);
		System.out.println("attempt to delete neighbour: " + attempt);
		
		s = db.listCountryLanguages(12);
		System.out.println("attempt to listLanguage: \n" + s);
		
		attempt = db.updateHeight(2, 2);
		System.out.println("attempt to decrease height: " + attempt);
		
		attempt = db.updateDB();
		System.out.println("attempt to update: " + attempt);
		
		attempt = db.disconnectDB();
		
	}
}