/**
 * 
 */
package com.sdcworld.view;

import com.sdcworld.controller.JdbcConnection;
import com.sdcworld.model.Person;

/**
 * @author souro
 *
 */
public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		JdbcConnection<Person> connection = new JdbcConnection<>(Person.class);
		for (Person person : connection.getAll()) {
			System.out.println(person.getAge());
		}
	}

}
