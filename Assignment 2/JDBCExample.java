// Credits: https://jdbc.postgresql.org/documentation/92/
// More examples: https://jdbc.postgresql.org/documentation/92/ (check under List Examples)      


// Instructions:
// 1) Connect to Prism using a terminal window: "ssh eecs.yorku.ca" using your Prism username and password
// 2) Download the JDBC driver https://jdbc.postgresql.org/download/postgresql-42.2.5.jar and copy jdbc jar file (using sftp) to Prism server. 
// 	  If you need another JDBC driver version look at http://jdbc.postgresql.org/download.html 
// 3) On line 60 DriverManager.getConnection("jdbc:postgresql://db:5432/<dbname>", "<username>", "<password>");
//    -leave as is the host and port number ("db:5432"); 
//    -replace <dbname> with the name of your database, <username> with your Prism "username" and password your student number "21233XXXX". These are the credentials to be used to login into the database.
//    - The default name of your database is the same as your Prism login; 
// 4) Compile the code:
//         javac JDBCExample.java
// 5) Run the code:
//         java -cp /*****path-to-jdbc-directory*****/postgresql-42.2.5.jar:. JDBCExample   
//    where postgresql-42.2.5.jar is the jdbc jar file downloaded in step 2


import java.sql.*;

 
public class JDBCExample {
 
	public static void main(String[] argv) {

		// A connection to the database		
		Connection connection;

		// Statement to run queries
		Statement sql;

		//Prepared Statement
		PreparedStatement ps;

		//Resultset for the query
		ResultSet rs;
 
		System.out.println("-------- PostgreSQL JDBC Connection Testing ------------");
 
		try {
		
 			// Load JDBC driver
			Class.forName("org.postgresql.Driver");
 
		} catch (ClassNotFoundException e) {
 
			System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
			e.printStackTrace();
			return;
 
		}
 
		System.out.println("PostgreSQL JDBC Driver Registered!");
 
		try {
			
 			//Make the connection to the database, ****** but replace <dbname>, <username>, <password> with your credentials ******
			System.out.println("*** Please make sure to replace 'dbname', 'username' and 'password' with your credentials in the jdbc connection string!!!");
			connection = DriverManager.getConnection("jdbc:postgresql://db:5432/gavinsit", "gavinsit", "215043870");
 
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
 
		}
 
		if (connection != null) {
			System.out.println("You made it, take control of your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}

		try{

			//Create a Statement for executing SQL queries
			sql = connection.createStatement(); 

			//---------------------------------------------------------------------------------------
			//Create jdbc_demo table
			String sqlText;
			sqlText = "CREATE TABLE jdbc_demo(                  " +
				  "                       code int,         " +
                	          "                       text varchar(20)  " +
                        	  "                      ) ";

			System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
    			sql.executeUpdate(sqlText);


			//---------------------------------------------------------------------------------------			
			//Insert into jdbc_demo table
			sqlText = "INSERT INTO jdbc_demo " +
                                  "VALUES (1, 'One')";
			System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
			sql.executeUpdate(sqlText);
			//System.exit(1);

 
			sqlText = "INSERT INTO jdbc_demo   " +
                                  "VALUES (4, 'Five')";
			System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
			sql.executeUpdate(sqlText);



			//---------------------------------------------------------------------------------------
			//Upate values of jdbc_demo
			sqlText = "UPDATE jdbc_demo      " +
                                  "   SET text = 'Four'  " +
                                  " WHERE  code = 4      "; 
			System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
			sql.executeUpdate(sqlText);
			System.out.println (sql.getUpdateCount() + " rows were update by this statement.\n");

	

			//---------------------------------------------------------------------------------------
			//Use Prepared Statement for insertion
			System.out.println("\n\nNow demostrating a prepared statement (inserting 5 tuples)...");
		        sqlText = "INSERT INTO jdbc_demo " + 
        			  "VALUES (?,?)          ";
    			System.out.println("Prepared Statement: " + sqlText.replaceAll("\\s+", " ") + "\n");
    			ps = connection.prepareStatement(sqlText);
			for (int i=1; i<=5; i++){
				System.out.println(i + "...\n");
		      		
				//Populate the prepared statement
				//Set column one (code) to i
				ps.setInt(1,i);         
				
				//Column two gets a string
			      	ps.setString(2,"random insertion");

				//Execute insert
				ps.executeUpdate();
		    	}
			ps.close();


			
			//---------------------------------------------------------------------------------------
			//Select from jdbc_demo table
			sqlText = "SELECT *       " +
                                  " FROM jdbc_demo";
			System.out.println("Now executing the command: " + sqlText.replaceAll("\\s+", " ") + "\n");
                       	rs = sql.executeQuery(sqlText);
			if (rs != null){
				while (rs.next()){
					System.out.println("code = " + rs.getInt("code") + "; text = "+rs.getString(2)+"\n");
      				}
    			}

			//Close the resultset
			rs.close();

		

			//---------------------------------------------------------------------------------------
			//Drop jdbc_demo table
			sqlText = "DROP TABLE jdbc_demo";
			System.out.println("Executing this command: \n" + sqlText.replaceAll("\\s+", " ") + "\n");
        	        sql.executeUpdate(sqlText);

			connection.close();
		} catch (SQLException e) {

                        System.out.println("Query Exection Failed!");
                        e.printStackTrace();
                        return;

                }		
	}
 
}