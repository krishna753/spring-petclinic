package org.springframework.samples.petclinic.model;

import java.util.Arrays;

public enum TimeSlots {

	 EIGHT("8:00am"),
	 EIGHTTHIRTY("8:30am"),
	 NINE("9:00am"),
	 NINETHIRTY("9:30am"),
	 TEN("10:00am"),
	 TENTHIRTY("10:30am"),
	 ELEVEN("11:00am"),
	 ELEVENTHIRTY("11:30am"),
	 TWELVE("12:00pm"),
	 TWELVETHIRTY("12:30pm"),
	 ONE("1:00pm"),
	 ONETHIRTY("1:30pm"),
	 TWO("2:00pm"),
	 TWOTHIRTY("2:30pm"),
	 THREE("3:00pm"),
	 THREETHIRTY("3:30pm"),
	 FOUR("4:00pm"),
	 FOURHIRTY("4:30pm");

	 public final String label;
	public String getLabel(){
		return this.label;
	}
	 private TimeSlots(String label) {
	 this.label = label;
	 }
	 public static String[] stream() {
	 return Arrays.stream(TimeSlots.values())
		 .map(c->new String[] {c.getLabel()}).toArray(String[]::new);

	 }
	@Override
	public String toString() {
		return this.label;
	}

	public static String[] getList(){
		TimeSlots[] values = TimeSlots.values();

		String[] result = new String[values.length];

		for (int i = 0; i < values.length; ++i) {
			result[i] = values[i].toString();
		}
		return result;
	}
}
