package package2;

import java.util.Arrays;
import java.util.Random;

public class TrainLine {

	private TrainStation leftTerminus;
	private TrainStation rightTerminus;
	private String lineName;
	private boolean goingRight;
	public TrainStation[] lineMap;
	public static Random rand;

	public TrainLine(TrainStation leftTerminus, TrainStation rightTerminus, String name, boolean goingRight) {
		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		this.lineMap = this.getLineArray();
	}

	public TrainLine(TrainStation[] stationList, String name, boolean goingRight)
	/*
	 * Constructor for TrainStation input: stationList - An array of TrainStation
	 * containing the stations to be placed in the line name - Name of the line
	 * goingRight - boolean indicating the direction of travel
	 */
	{
		TrainStation leftT = stationList[0];
		TrainStation rightT = stationList[stationList.length - 1];

		stationList[0].setRight(stationList[stationList.length - 1]);
		stationList[stationList.length - 1].setLeft(stationList[0]);

		this.leftTerminus = stationList[0];
		this.rightTerminus = stationList[stationList.length - 1];
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		for (int i = 1; i < stationList.length - 1; i++) {
			this.addStation(stationList[i]);
		}

		this.lineMap = this.getLineArray();
	}

	public TrainLine(String[] stationNames, String name,
			boolean goingRight) {/*
									 * Constructor for TrainStation. input: stationNames - An array of String
									 * containing the name of the stations to be placed in the line name - Name of
									 * the line goingRight - boolean indicating the direction of travel
									 */
		TrainStation leftTerminus = new TrainStation(stationNames[0]);
		TrainStation rightTerminus = new TrainStation(stationNames[stationNames.length - 1]);

		leftTerminus.setRight(rightTerminus);
		rightTerminus.setLeft(leftTerminus);

		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;
		for (int i = 1; i < stationNames.length - 1; i++) {
			this.addStation(new TrainStation(stationNames[i]));
		}

		this.lineMap = this.getLineArray();

	}

	// adds a station at the last position before the right terminus
	public void addStation(TrainStation stationToAdd) {
		TrainStation rTer = this.rightTerminus;
		TrainStation beforeTer = rTer.getLeft();
		rTer.setLeft(stationToAdd);
		stationToAdd.setRight(rTer);
		beforeTer.setRight(stationToAdd);
		stationToAdd.setLeft(beforeTer);

		stationToAdd.setTrainLine(this);

		this.lineMap = this.getLineArray();
	}

	public String getName() {
		return this.lineName;
	}

	public int getSize() {
		TrainStation station = leftTerminus;
		int count = 0;
		while (!station.isRightTerminal()) {
			count++;
			station = station.getRight();
		}

		return count + 1;
	}

	public void reverseDirection() {
		this.goingRight = !this.goingRight;
	}

	// You can modify the header to this method to handle an exception. You cannot
	// make any other change to the header.
	public TrainStation travelOneStation(TrainStation current, TrainStation previous) {
		if (!current.getLine().equals(this))
			throw new StationNotFoundException(lineName);

		if ((current.hasConnection) && (previous == null)) {

			return current.getTransferStation();
		} else if ((current.hasConnection) && (!previous.equals(current.getTransferStation()))) {
			return current.getTransferStation();
		} else {
			return getNext(current);
		}

	}

	// You can modify the header to this method to handle an exception. You cannot
	// make any other change to the header.
	public TrainStation getNext(TrainStation station) {
		if (!station.getLine().equals(this)) {
			throw new StationNotFoundException(lineName);
		}
		if ((goingRight && station.isRightTerminal()) || (!goingRight && station.isLeftTerminal())) {
			reverseDirection();
		}

		if (goingRight)
			return station.getRight();
		else
			return station.getLeft();

	}

	// You can modify the header to this method to handle an exception. You cannot
	// make any other change to the header.
	public TrainStation findStation(String name) {
		TrainStation found = lineMap[0];
		for (int i = 0; i < lineMap.length - 1; i++) {
			found = lineMap[i];
			if (name.equalsIgnoreCase(found.getName()) == true) {

				return found;

			}
		}
		throw new StationNotFoundException(name);

	}

	public void sortLine() {
		TrainStation[] lineArray = this.getLineArray();
		boolean sorted = false;

		int arraySize = getSize();

		int numSorted = 0;
		while (!sorted) {
			sorted = true;
			for (int i = 0; i < arraySize - 1 - numSorted; i++) {
				if (lineArray[i].getName().compareToIgnoreCase(lineArray[i + 1].getName()) < 0) {
					TrainStation temp = lineArray[i];
					lineArray[i] = lineArray[i + 1];
					lineArray[i + 1] = temp;
					sorted = false;
				}
			}
			numSorted++;
		}

		for (int i = 0; i < arraySize - 1; i++) {
			lineArray[i].setRight(lineArray[i + 1]);
			lineArray[i + 1].setLeft(lineArray[i]);
		}
		leftTerminus.setNonTerminal();
		rightTerminus.setNonTerminal();
		leftTerminus = lineArray[0];
		leftTerminus.setLeftTerminal();
		leftTerminus.setLeft(null);
		rightTerminus = lineArray[arraySize - 1];
		rightTerminus.setRightTerminal();
		rightTerminus.setRight(null);

		lineMap = lineArray;

		// YOUR CODE GOES HERE
	}

	public TrainStation[] getLineArray() {
		TrainStation station = leftTerminus;
		int arraySize = getSize();
		TrainStation lineArray[] = new TrainStation[arraySize];
		for (int i = 0; i < arraySize; i++) {
			lineArray[i] = station;

			if (!station.isRightTerminal()) {
				station = station.getRight();
			}

		}

		return lineArray; // change this
	}

	private TrainStation[] shuffleArray(TrainStation[] array) {
		Random rand = new Random();
		rand.setSeed(11);
		for (int i = 0; i < array.length; i++) {
			int randomIndexToSwap = rand.nextInt(array.length);
			TrainStation temp = array[randomIndexToSwap];
			array[randomIndexToSwap] = array[i];
			array[i] = temp;
		}
		this.lineMap = array;
		return array;
	}

	public void shuffleLine() {

		// you are given a shuffled array of trainStations to start with
		TrainStation[] lineArray = this.getLineArray();
		TrainStation[] shuffledArray = shuffleArray(lineArray);
		int arraySize = getSize();
		leftTerminus.setNonTerminal();
		rightTerminus.setNonTerminal();

		leftTerminus = lineMap[0];
		leftTerminus.setLeftTerminal();
		leftTerminus.setLeft(null);
		leftTerminus.setRight(lineMap[1]);

		for (int i = 1; i < arraySize - 1; i++) {
			lineMap[i].setRight(lineMap[i + 1]);
			lineMap[i].setLeft(lineMap[i - 1]);
		}

		rightTerminus = lineMap[arraySize - 1];
		rightTerminus.setRightTerminal();
		rightTerminus.setLeft(lineMap[arraySize - 2]);
		rightTerminus.setRight(null);

	}

	public String toString() {
		TrainStation[] lineArr = this.getLineArray();
		String[] nameArr = new String[lineArr.length];
		for (int i = 0; i < lineArr.length; i++) {
			nameArr[i] = lineArr[i].getName();
		}
		return Arrays.deepToString(nameArr);
	}

	public boolean equals(TrainLine line2) {

		// check for equality of each station
		TrainStation current = this.leftTerminus;
		TrainStation curr2 = line2.leftTerminus;

		try {
			while (current != null) {
				if (!current.equals(curr2))
					return false;
				else {
					current = current.getRight();
					curr2 = curr2.getRight();
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public TrainStation getLeftTerminus() {
		return this.leftTerminus;
	}

	public TrainStation getRightTerminus() {
		return this.rightTerminus;
	}
}

//Exception for when searching a line for a station and not finding any station of the right name.
class StationNotFoundException extends RuntimeException {
	String name;

	public StationNotFoundException(String n) {
		name = n;
	}

	public String toString() {
		return "StationNotFoundException[" + name + "]";
	}
}
