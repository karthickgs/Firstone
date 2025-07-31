package String;

public class StringMatch {
	public int isPrefixOfWord(String sentence, String searchWord) {
        String [] sent = sentence.split(" ");
       for(String a: sent) {
    	   System.out.println(a);
       }
        int a =0;
        while(a<=sent.length-1){
            if(searchWord.equalsIgnoreCase(sent[a])){
            	System.out.println(sent[a]);
            	a++;
                return a;
            }
        }
		return a;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		StringMatch sbuild = new StringMatch();
//		int [] a = {1,5,10,13};
//		sbuild.addSpaces("ILoveIndiaandbriyani", a);
		sbuild.isPrefixOfWord("I love banana", "I");
	


	}
}
