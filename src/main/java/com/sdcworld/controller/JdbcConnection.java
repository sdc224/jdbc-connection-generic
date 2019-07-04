/**
 * 
 */
package com.sdcworld.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sdcworld.controller.helper.EnumDatabaseCase;
import com.sdcworld.utility.ConvertUtility;
import com.sdcworld.utility.StringUtility;

/**
 * @author souro
 *
 */
public class JdbcConnection<T> {
	
	private String url = "jdbc:mysql://localhost/sdc";
	private String username = "devuser";
	private String password = "Demo@123";
	private String driver = "com.mysql.jdbc.Driver";
	private String tableName;
	private Map<String, String> columnsMap = new HashMap<String, String>();
	
	private EnumDatabaseCase databaseCase = EnumDatabaseCase.SENTENCECASE_UNDERSCORE;
	
	private static final String SELECT_ALL = "SELECT * FROM ${table}";
	private static final String INSERT = "INSERT INTO ${table}(${keys}) VALUES(${values})";
	private static final String TABLE_NAME = "\\$\\{table\\}";
	private static final String KEYS = "\\$\\{keys\\}";
	private static final String VALUES = "\\$\\{values\\}";
	
	private Class<T> genericType;
	
	private boolean isInitializationCompleted;
	private boolean hasAutomaticField;
	private boolean hasPrimaryKey;
	private boolean hasForeignKey;
	private boolean isUnique;
	
	private Map<String, Field> privateFields = new LinkedHashMap<>();
	
	private Connection dbConnection = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	// Constructors
	public JdbcConnection(final Class<T> type)
	{
		this.genericType = type;
		connectToDb();
		initialize();
	}
	
	public JdbcConnection(final Class<T> type, EnumDatabaseCase databaseCase)
	{
		this.genericType = type;
		connectToDb();
		this.databaseCase = databaseCase;
		initialize();
	}

	public JdbcConnection(final Class<T> type, String url, String username, String password)
	{
		this.genericType = type;
		this.url = url;
		this.username = username;
		this.password = password;
		
		connectToDb();
		initialize();
	}
	
	public JdbcConnection(final Class<T> type, String url, String username, String password, EnumDatabaseCase databaseCase)
	{
		this.genericType = type;
		this.url = url;
		this.username = username;
		this.password = password;
		this.databaseCase = databaseCase;
		
		connectToDb();
		initialize();
	}
	
	public JdbcConnection(final Class<T> type, String url, String username, String password, String driver)
	{
		this.genericType = type;
		this.url = url;
		this.username = username;
		this.password = password;
		this.driver = driver;
		
		connectToDb();
		initialize();
	}
	
	public JdbcConnection(final Class<T> type, String url, String username, String password, String driver, EnumDatabaseCase databaseCase)
	{
		this.genericType = type;
		this.url = url;
		this.username = username;
		this.password = password;
		this.driver = driver;
		this.databaseCase = databaseCase;
		
		connectToDb();
		initialize();
	}
	
	private void initialize()
	{
		if (!this.isInitializationCompleted) {
			// this section is only for finding private fields
			// in a class to add them in field class
			Field[] fields = genericType.getDeclaredFields();
			for (Field field : fields) {
				if (Modifier.isPrivate(field.getModifiers())) {
					privateFields.put(field.getName(), field);
				}
			}
			
			try
			{				
				// getSimpleName() will not work for static methods
				this.tableName = genericType.newInstance().getClass().getSimpleName().toLowerCase();
				
				List<String> listOfFieldNames = new ArrayList<>(privateFields.keySet());
				
				for (String string : listOfFieldNames) {
					this.columnsMap.put(string, StringUtility.toSentenceUnderScore(string));
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			this.isInitializationCompleted = true;
		}
	}
	
	/**
	 * A method for connecting to the database
	 */
	private void connectToDb() {
		try
		{
			Class.forName(driver);
			dbConnection = DriverManager.getConnection(url, username, password);			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() 
	{
        return dbConnection;
    }
	
	public List<T> getAll()
	{		
		List<T> list = new ArrayList<>();
		
		try
		{
			// List<String> columns = new ArrayList<String>(columnsMap.values());
			String query = SELECT_ALL.replaceFirst(TABLE_NAME, tableName);
			statement = dbConnection.createStatement();
			resultSet = statement.executeQuery(query);
			
			while (resultSet.next()) {
				T refObject = genericType.newInstance();
                this.loadResultSetIntoObject(resultSet, refObject);
                list.add(refObject);
			}
			
			return list;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void loadResultSetIntoObject(ResultSet resultSet2, T refObject) throws IllegalArgumentException, IllegalAccessException, SQLException {
		Class<?> classDenoter = refObject.getClass();
		for (Field field : classDenoter.getDeclaredFields()) {
			String name = field.getName();
			field.setAccessible(true);
			Object value = resultSet2.getObject(columnsMap.get(name));
			Class<?> type = field.getType();
			if (ConvertUtility.isPrimitive(type)) {
				Class<?> boxed = ConvertUtility.boxPrimitiveClass(type);
				value = boxed.cast(value);
			}
			field.set(refObject, value);
		}
	}

	public T getAll(Object condition)
	{
		return null;
	}
}
