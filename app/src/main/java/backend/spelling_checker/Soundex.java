package backend.spelling_checker;

// Soundex encoding class.

public class Soundex {
    public String getCode(String word){
        char [] wordArray = word.toUpperCase().toCharArray();

        char firstLetter = wordArray[0];

        //RULE [ 2 ]
        //Convert letters to numeric code
        for (int i = 0; i < wordArray.length; i++) {
            switch (wordArray[i]) {
                case 'B':
                case 'F':
                case 'P':
                case 'V': {
                    wordArray[i] = '1';
                    break;
                }

                case 'C':
                case 'G':
                case 'J':
                case 'K':
                case 'Q':
                case 'S':
                case 'X':
                case 'Z': {
                    wordArray[i] = '2';
                    break;
                }

                case 'D':
                case 'T': {
                    wordArray[i] = '3';
                    break;
                }

                case 'L': {
                    wordArray[i] = '4';
                    break;
                }

                case 'M':
                case 'N': {
                    wordArray[i] = '5';
                    break;
                }

                case 'R': {
                    wordArray[i] = '6';
                    break;
                }

                default: {
                    wordArray[i] = '0';
                    break;
                }
            }
        }

        //Remove duplicates
        //RULE [ 1 ]
        String output = "" + firstLetter;

        //RULE [ 3 ]
        for (int i = 1; i < wordArray.length; i++)
            if (wordArray[i] != wordArray[i - 1] && wordArray[i] != '0')
                output += wordArray[i];

        //RULE [ 4 ]
        //Pad with 0's or truncate
        output = output + "0000";
        return output.substring(0, 4);
    }

    public static void main(String[] args) {
        Soundex test = new Soundex();
        String value = test.getCode("ram");
        System.out.println(value);
        String value2 = test.getCode("ream");
        System.out.println(value2);
        String value3 = test.getCode("real");
        System.out.println(value3);

        //If code is the same return 1, if different return 0
    }
}
