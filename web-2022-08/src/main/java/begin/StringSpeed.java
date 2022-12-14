package begin;

public class StringSpeed {
	
	String str = "a";
	
	public StringSpeed() {
		long sTime=0, eTime=0, rTime=0, rTime2=0;
		
		// 고정 문자열
		sTime = System.currentTimeMillis();
		for(int i=0; i<99000; i++) {
			str += "a";
		}
		eTime = System.currentTimeMillis();
		rTime = eTime - sTime;
		System.out.println("Time1 : " + (rTime/1000D) + "초");
		
		// 가변 문자열
		StringBuilder sb = new StringBuilder(1024);
		sTime = System.currentTimeMillis();
		for(int i=0; i<99000; i++) {
			sb.append("a");
		}
		eTime = System.currentTimeMillis();
		rTime2 = eTime - sTime;
		System.out.println("Time2 : " + (rTime2/1000d) + "초");
		System.out.println(rTime / rTime2 + "배");
	}
	
	public static void main(String[] args) {
		new StringSpeed();
	}
	
}
