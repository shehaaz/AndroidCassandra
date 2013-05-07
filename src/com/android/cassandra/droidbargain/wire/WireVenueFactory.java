package com.android.nitelights.wire;

public class WireVenueFactory implements Comparable<WireFactory>, WireFactory {
	
	private String element1;
	private String element2;
	private String wireType;
	private String wire_timestamp;

	
	public WireVenueFactory(String pElement1, String pElement2, String pWireType, String pTimestamp){
		element1 = pElement1;
		element2 = pElement2;
		wireType = pWireType;
		wire_timestamp = pTimestamp;
	}
	
	@Override
	public int compareTo(WireFactory comparedWireObject) {
		
		int compareQuantity;
		
		if(comparedWireObject.getTimestamp() != null){
			compareQuantity = Integer.parseInt(((WireFactory) comparedWireObject).getTimestamp()); 
			//descending order
			return compareQuantity - Integer.parseInt(this.getTimestamp());
		}
		else{
			compareQuantity = 0;
			return compareQuantity;
		}
		  	
	}

	@Override
	public String getElement1() {
		return element1;
	}

	@Override
	public String getElement2() {
		return element2;
	}

	@Override
	public String getWireType() {
		return wireType;
	}

	@Override
	public String getTimestamp() {
		return wire_timestamp;
	}

}

