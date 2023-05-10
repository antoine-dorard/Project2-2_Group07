package Spelling_Checker;

// Soundex encoding class.

public class Soundex {
    public String getCode(String word){
        char [] wordArray = word.toUpperCase().toCharArray();

        for (int i = 0 ;  i < wordArray.length ; i++){
            switch (wordArray[i]){
                case 'B':
                case 'F':
                case 'P':
                case 'V':
                case 'W':{
                    wordArray[i] = '2';
                    break ;
                    }
                case 'C':
                case 'G':
                case 'J':
                case 'K':
                case 'Q':
                case 'S':
                case 'X':
                case 'Z': {
                    wordArray[i] = '3';
                    break ;
                    }
                case 'D':
                case 'T':{
                    wordArray[i] = '4';
                    break;
                }
                case 'L':{
                    wordArray[i] = '5';
                    break;
                }
                case 'M':
                case 'N':{
                    wordArray[i] = '6';
                    break;
                }
                case 'R':{
                    wordArray[i] = '7';
                    break;
                }
                case 'O':
                case 'A':{
                    wordArray[i] = '8';
                    break;
                }
                default:{
                    wordArray[i] = '1';
                    break;
                }

            }
        }
        String output = wordArray[0] == 1? "" : "" + wordArray[0];

        for (int i = 1; i < wordArray.length; i++) {
            if(wordArray[i] != wordArray[i - 1] && wordArray[i] != '1')
                output += wordArray[i];
            
        }
        return output;
    }

    public static void main(String[] args) {
        Soundex test = new Soundex();
        String value = test.getCode("kat");
        System.out.println(value);
        String value2 = test.getCode("cat");
        System.out.println(value2);
        String value3 = test.getCode("bat");
        System.out.println(value3);
    }
}
