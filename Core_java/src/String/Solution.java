package String;

public class Solution {
	
    public boolean canTransform(String start, String target) {
        // If the lengths are different, it's not possible to transform
        if (start.length() != target.length()) {
            return false;
        }
        
        // Loop through the strings to check each character's position
        for (int i = 0; i < start.length(); i++) {
            char sChar = start.charAt(i);
            char tChar = target.charAt(i);
            
            // If both characters are the same, continue
            if (sChar == tChar) {
                continue;
            }
            
            // If there is a mismatch, check for the validity of movement
            if (sChar == 'L' && tChar == '_') {
                // 'L' can only move left, so if the target is not 'L' return false
              return false;
            }
            if (sChar == '_' && tChar == 'R') {
                // 'R' can only move right, so if the target is not 'R' return false
                return false;
            }
        }
        
        // After considering all positions, if all pieces can be moved to form the target
        return true;
    }
    public static void main(String[] args) {
		Solution s = new Solution();
		Boolean g=s.canTransform("L__R", "_L_R");
		System.out.println(g);
	}
}








