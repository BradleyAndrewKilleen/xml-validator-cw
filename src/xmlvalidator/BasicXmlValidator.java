package xmlvalidator;

import static java.lang.System.*;
import static sbcc.Core.*;

import java.util.*;
import java.util.regex.*;

public class BasicXmlValidator implements XmlValidator {

	BasicStringStack tagStack = new BasicStringStack();
	BasicStringStack lineStack = new BasicStringStack();

	ArrayList<String> alTags = new ArrayList();
	ArrayList<Integer> alLines = new ArrayList();

	int errorCode = -1;
	String[] errorData = null;
	String currentMatch;
	int currentLine;
	int loopCursor = 0;


	@Override
	public String[] validate(String xmlDocument) {

		// Testing xmlDocument matches String array after split
		println(xmlDocument);

		// Parse data into array. May need to rewrite to consider multi-line open tags
		String[] myText;
		myText = xmlDocument.split("\n");

		// Testing correct file parsing
		for (int x = 0; x < myText.length; x++) {
			println(myText[x]);
		}

		// Build regex pattern for xml tags
		String regex = "<[^<>]+>";
		Pattern p = Pattern.compile(regex);

		// Populate ArrayLists with regex matches and associated line numbers. LINE
		// NUMBERS ARE CORRECT - NOT OFF BY ONE.
		for (int x = 1; x <= myText.length; x++) {
			String myLine = myText[x - 1];
			Matcher m = p.matcher(myLine);

			while (m.find()) {
				alTags.add(m.group());
				alLines.add(x);
			}
		}

		// Begin evaluating for tag errors
		for (int x = 0; x < alTags.size(); x++) {
			currentMatch = alTags.get(x);
			currentLine = alLines.get(x);

			// Error code updated by validateTag. Continues to be -1 if no error.
			errorCode = validateTag(currentMatch);

			if (errorCode != -1) {
				x = alTags.size(); // Exit for loop upon errorCode find!
			}

			println(loopCursor);
			println(alTags.size());

			loopCursor++;
		}

		errorData = buildErrorData(errorCode);

		return errorData;
	}


	private int validateTag(String tag) {
		if (tag.charAt(1) == '?' || tag.charAt(1) == '!' || tag.charAt(tag.length() - 2) == '/') {
			return -1;
		}

		else if (tag.charAt(1) == '/') {

			// Evaluate for orphan close tag
			if (tagStack.getCount() == 0) {
				return 1;
			}

			// Evaluate for tag mismatch
			else {
				println("Close Tag: " + currentMatch);
				println("Close Tag: " + tagStack.peek(0));

				if (cleanTag(currentMatch).equals(cleanTag(tagStack.peek(0))) == false) {
					return 2;
				}

				else {
					tagStack.pop();
					lineStack.pop();

					println("Popping");
				}
			}
		}

		else if (loopCursor == alTags.size() - 1 && tagStack.getCount() != 0 && alTags.size() > 1) {
			return 3;
		}

		else {
			tagStack.push(currentMatch);
			lineStack.push(Integer.toString(currentLine)); // Off by one handled previously in Arraylist

			println("Pushing: " + currentMatch);
		}

		return -1;
	}


	private String[] buildErrorData(int errorCode) {
		String[] myErrorData = null;

		if (errorCode == 1) {
			myErrorData = new String[3];

			myErrorData[0] = "Orphan closing tag";
			myErrorData[1] = cleanTag(currentMatch);
			myErrorData[2] = Integer.toString(currentLine);
		}

		else if (errorCode == 2) {
			myErrorData = new String[5];

			myErrorData[0] = "Tag mismatch";
			myErrorData[1] = cleanTag(tagStack.peek(0));
			myErrorData[2] = lineStack.peek(0);
			myErrorData[3] = cleanTag(currentMatch);
			myErrorData[4] = Integer.toString(currentLine);
		}

		else if (errorCode == 3) {
			myErrorData = new String[3];

			myErrorData[0] = "Unclosed tag at end";
			myErrorData[1] = cleanTag(currentMatch);
			myErrorData[2] = Integer.toString(currentLine);
		}

		else if (errorCode == 4) {
			myErrorData = new String[5];

			myErrorData[0] = "Attribute not quoted";
			myErrorData[1] = cleanTag(currentMatch);
			myErrorData[2] = Integer.toString(currentLine);
			// myErrorData[3] = Attribute Name
			// myErrorData[4] = Attribute Line
		}

		return myErrorData;
	}


	private String cleanTag(String xmlTag) {
		String bracketsRemoved = xmlTag.substring(1, xmlTag.length() - 1);
		String returnString;

		if (bracketsRemoved.contains(" ")) {
			returnString = bracketsRemoved.substring(0, bracketsRemoved.indexOf(" "));
		}

		else if (bracketsRemoved.contains("/")) {
			returnString = bracketsRemoved.substring(1);
		}

		else {
			returnString = bracketsRemoved;
		}

		return returnString;
	}
}
