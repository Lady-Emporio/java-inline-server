package app;

public class Test {
	public static void indexOfArray() {
		byte[]first={1,2,3,4,5,6};
		byte[]second;
		int beginFirst=0;
		
		boolean[]results=new boolean[4];
		
		first=new byte[]{1,2,3,4,5,6};
		second=new byte[]{1,2,3,4,5,6};
		results[0]=ClearSky.indexOfArray(first,second,beginFirst)==0;
		
		first=new byte[]{1,2,3,4,5,6};
		second=new byte[]{1,2,3,4,5,6,7};
		results[1]=ClearSky.indexOfArray(first,second,beginFirst)==-1;
		
		first=new byte[]{1,2,3,4,5,6};
		second=new byte[]{2,3,4};
		results[2]=ClearSky.indexOfArray(first,second,beginFirst)==1;
		
		first=new byte[]{1,2,3,4,5,6,7};
		second=new byte[]{7};
		results[3]=ClearSky.indexOfArray(first,second,beginFirst)==6;
		
		System.out.println("Test 'indexOfArray'.");
		for(boolean r:results) {
			System.out.println(r);
		}
	}
}
