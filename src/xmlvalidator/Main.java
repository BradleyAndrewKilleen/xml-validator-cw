package xmlvalidator;

import static sbcc.Core.*;

import java.io.*;
import java.util.*;
import static java.lang.System.*;
import static org.apache.commons.lang3.StringUtils.*;

public class Main {

	public static void main(String[] args) throws IOException {
		/*
		 * BasicStringStack myStack = new BasicStringStack();
		 * 
		 * ArrayList<String> possibleAttributes = new ArrayList<String>(
		 * Arrays.asList("Version", "default", "pattern", "value", "color", "property",
		 * "name", "outfile"));
		 * 
		 * for (String x : possibleAttributes) { myStack.push(x); }
		 * 
		 * while (myStack.getCount() > 0) { println("Current Count:" +
		 * myStack.getCount()); println("Item at this position:" + myStack.pop());
		 * println("Item in next position:" + myStack.peek(0)); }
		 */

		BasicXmlValidator b = new BasicXmlValidator();

		String testString = readFile("InvalidFile.xml");

		String[] testError = b.validate(testString);

		for (int x = 0; x < testError.length; x++) {
			println(testError[x]);
		}
	}

}
