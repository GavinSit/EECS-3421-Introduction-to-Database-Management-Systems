import java.sql.*;

public class Assignment2 {
    
  // A connection to the database  
  Connection connection;
  
  // Statement to run queries
  Statement sql;
  
  // Prepared Statement
  PreparedStatement ps;
  
  // Resultset for the query
  ResultSet rs;
  
	//Store the instruction for the sql query
	String sqlInstruction;
	
  //CONSTRUCTOR
  Assignment2(){
		//load the JDBC driver
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			//System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
			//e.printStackTrace();
		}
  }
  
  //Using the input parameters, establish a connection to be used for this session. Returns true if connection is sucessful
  public boolean connectDB(String URL, String username, String password){
		try { //try connecting
			connection = DriverManager.getConnection(URL, username, password);
		}catch (SQLException e) {
			//System.out.println("Connection Failed! Check output console");
		}
		
		if (connection == null) { //couldnt connect
			return false;
		}
		
		try {
			//create statement that will generate result set
			sql = connection.createStatement(); 
		}catch (SQLException e) {
			//System.out.println("Failed createStatement");
			return false;
		}
		return true; //return true after finishing everything
	}
  
  //Closes the connection. Returns true if closure was sucessful
  public boolean disconnectDB(){
			try {
				connection.close();
				//System.out.println("Successfully disconnected");
			} catch (SQLException e) {
				//System.out.println("Disconnect failed!");
				return false;
			}
			return true;
  }
    
  public boolean insertCountry (int cid, String name, int height, int population) {
		int result;
		sqlInstruction = "insert into country (cid, cname, height, population) " +
										"	values ( " + cid + " , " + name + " , " + height + " , " + population + ")";
										
		try {
			result = sql.executeUpdate(sqlInstruction);
		} catch (SQLException e) {
			return false;
		}
		return true;
  }
  
	//double int error?
  public int getCountriesNextToOceanCount(int oid) {
		int result = 0; //store the result of the query
		sqlInstruction = "select count(cid) as numCountry " + 
										" from oceanAccess " + 
										" where oid = " + oid;
	
		try {
			rs = sql.executeQuery(sqlInstruction);
			
			if (rs != null) {
				rs.next(); //move it to the first position
				result = rs.getInt("numCountry"); //get the first column
			}
			rs.close();
		} catch (SQLException e) {
			//return -1 on error
			return -1; 
		}
		return result;
	}
   
  public String getOceanInfo(int oid){
		String result = ""; 
		sqlInstruction = "select oid, oname, depth " + 
										" from ocean " + 
										" where oid = " + oid;
				
		try {
			rs = sql.executeQuery(sqlInstruction);
			
			if (rs != null){
				rs.next(); //move to the first spot, should only have one result
				
				//append the result in specific format
				result += rs.getString("oid");
				result += ":";
				result += rs.getString("oname");
				result += ":";
				result += rs.getString("depth");
			}else { //if there are no results 
				return "";
			}
			rs.close();
		} catch (SQLException e) {
			return "";
		}
		return result;
  }

  public boolean chgHDI(int cid, int year, float newHDI){
   	sqlInstruction = "update hdi " + 
										" set hdi_score = " + newHDI + 
										" where cid = " + cid + " and year = " + year;
										
		try {
			sql.executeUpdate(sqlInstruction);
		
			if (sql.getUpdateCount() == 1) { //one change should be made
				return true;
			} else { //anything other that 1 change is wrong
				return false;
			}
		} catch (SQLException e) { //change unsuccessful
			return false;
		} 
  }

  public boolean deleteNeighbour(int c1id, int c2id){
		int result; //0 means nothing was done
		sqlInstruction = "delete from neighbours " + 
										" where ( " + c1id + " = neighbours and " + c2id + " = country) " + 
										" or ( " + c2id + " = neighbours and " + c1id + " = country)"; 
		try {
			result = sql.executeUpdate(sqlInstruction);
			return result > 0; //greater than 0 means delete was done
		}catch (SQLException e) {
			//System.out.println("Delete Failed");
			return false;
		}     
  }
  
  public String listCountryLanguages(int cid){
		String result = "";
		sqlInstruction = "select lid, lname, floor(lpercentage * country.population) as population" + 
										" from country join language " + 
										" on country.cid = language.cid and country.cid = " + cid +
										" order by population";
										
		try {
			rs = sql.executeQuery(sqlInstruction);
			if (rs != null) {
				while(rs.next()) {
					result += rs.getInt("lid") + ":" + rs.getString("lname") + ":" + rs.getInt("population");
					result += "\n"; //new line for next entry
				}
			}
			rs.close();
		} catch (SQLException e){
			return "";
		}			
		return result;
  }
  
  public boolean updateHeight(int cid, int decrH){
		int result;
		sqlInstruction = "update country " + 
										" set height = height - " + decrH +
										" where cid = " + cid;
										
    try {
			result = sql.executeUpdate(sqlInstruction);
			
			if (sql.getUpdateCount() == 1) { //one change should be made
				return true;
			} else { //anything other that 1 change is wrong
				return false;
			}
		} catch (SQLException e) {
			return false;
		}
  }
    
  public boolean updateDB(){
		sqlInstruction = "create table mostPopulousCountries (" + 
										" cid integer " + 
										"cname varchar(20))";

		//try creating the table
		try {
			sql.executeUpdate(sqlInstruction);
		} catch (SQLException e) {
			return false;
		}	
		
		sqlInstruction = "from country " + 
										" select cid, cname " +
										" where population > 100000000 ";
				
		//try getting the queries 
		try {
			rs = sql.executeQuery(sqlInstruction);
		} catch (SQLException e) {
			return false;
		}
		
		//try updating mostPopulousCountries with the correct queries
		sqlInstruction = "insert into mostPopulousCountries(cid, cname) " + 
										" values (?,?)";
		
		try {
			if (rs != null) { //if there are countries with pop > 100m, then update the table
				ps = connection.prepareStatement(sqlInstruction);
				
				while (rs.next()) {
					//get each entry from the query and input it
					ps.setInt(1, rs.getInt("cid"));
					ps.setString(2, rs.getString("cname"));
					ps.executeUpdate(); //update each time
				}
				ps.close(); //close after finished with it
				rs.close();
		
			}
		} catch (SQLException e) {
			return false;
		}
	
		return true; //after everything is done, return true
  }
  
}
