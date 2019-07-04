/**
 * 
 */
package com.sdcworld.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author souro
 *
 */
public class StringUtility {

	private StringUtility()
	{		
	}
	
	public static String toCamelCase(final String init) {
	    if (init == null)
	    {
	    	return null;
	    }

	    final StringBuilder ret = new StringBuilder(init.length());
	    
	    int count = 0;

	    for (final String word : init.split(" ")) {
	        if (!word.isEmpty()) 
	        {
	            if (count == 0)
	            {
	            	ret.append(Character.toLowerCase(init.charAt(0)));	            	
	            }
	            else 
	            {
	            	ret.append(Character.toUpperCase(word.charAt(0)));
	            }
	            
	            ret.append(word.substring(1).toLowerCase());
	        }
	        
	        ++count;
	    }

	    return ret.toString();
	}
	
	public static String toSentenceUnderScore(final String init) {
		Matcher m = Pattern.compile("(?<=[a-z])[A-Z]").matcher(init);

		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "_" + m.group().toLowerCase());
		}
		m.appendTail(sb);

		return capitailizeWord(sb.toString());
	}
	
	// From GeeksForGeeks.org
	private static String capitailizeWord(String str) { 
        StringBuffer s = new StringBuffer(); 
  
        // Declare a character of space 
        char ch = '_';
        for (int i = 0; i < str.length(); i++) { 
              
            // If previous character is space and current 
            // character is not space then it shows that 
            // current letter is the starting of the word
            if (ch == '_' && str.charAt(i) != ' ') 
                s.append(Character.toUpperCase(str.charAt(i))); 
            else
                s.append(str.charAt(i)); 
            ch = str.charAt(i); 
        } 
  
        // Return the string with trimming 
        return s.toString().trim();
    }    
}
