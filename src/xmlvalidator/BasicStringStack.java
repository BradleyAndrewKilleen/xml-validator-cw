package xmlvalidator;

import static java.lang.System.*;
import static sbcc.Core.*;

public class BasicStringStack implements StringStack {
	private int count = 0;
	private String[] strings = new String[1000];


	public BasicStringStack() {
	}


	public BasicStringStack(int initialSize) {
		strings = new String[initialSize];
	}


	@Override
	public void push(String item) {
		if (count == strings.length) {
			int newLength = (int) (strings.length * 1.25) + 1;
			String[] tempArray = new String[newLength];
			arraycopy(strings, 0, tempArray, 0, strings.length);
			strings = tempArray;
		}
		strings[count + 1] = item;
		count++;
	}


	@Override
	public String pop() {
		if (count == 0) {
			return null;
		} else {
			String myString = strings[count];
			count--;
			return myString;
		}
	}


	@Override
	public String peek(int position) {
		if (position < 0 || position > count - 1) {
			// println("The position you requested is outside the bounds of the stack");
			return null;
		} else {
			return strings[count - position];
		}
	}


	@Override
	public int getCount() {
		return count;
	}

}
