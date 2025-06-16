package sample.cafekiosk;

import java.util.*;

public class Main{
    public static void main(String[] args){

        ArrayDeque<Character> ad = new ArrayDeque<>();

        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        int total_count = 0;

        for(int i = 0; i< line.length(); i++){
            if(line.charAt(i) == '(')
                ad.push('(');
            if(line.charAt(i) == ')'){
                if(line.charAt(i-1) == '('){
                    ad.pop();
                    total_count+=ad.size();
                }
                else{
                    ad.pop();
                    total_count+=1;
                }
            }
        }

        System.out.println(total_count);

    }
}