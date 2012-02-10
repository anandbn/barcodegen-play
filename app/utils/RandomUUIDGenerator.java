package utils;

import java.util.UUID;

public class RandomUUIDGenerator {

	public static UUID getUUID(){
		return java.util.UUID.randomUUID();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(Integer i=0;i<5;i++){
			System.out.println(String.format(">>>>>>>>>>> UUID = %s",getUUID()));
		}
	}

}
