import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = "[ \\t,;\\.\\?\\!\\-:@\\[\\]\\(\\)\\{\\}\\_\\*/\\=]+";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }
    
    public String[] process() throws Exception {
        String[] ret = new String[20];
       
        // 1. Read the file line by line.
        File file = new File(this.inputFileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
 
        // 2. Divide each sentence into a list of words.
        String line = null;
        ArrayList<ArrayList<String>> words = new ArrayList<ArrayList<String>>();
        
        while ((line = br.readLine()) != null) {
        	// ([ (and ]) are not standard characters inside a regEx
        	// need to be escaped.
            String[] results = line.split(delimiters);
            
            ArrayList<String> eachLine = new ArrayList<String>();
            for (String word : results) {
            	eachLine.add(word);
            }
            
            words.add(eachLine);
        }
 
        br.close();

        // 3. Make all the tokens lower case and remove any tailing and leading spaces.
        // 4. Ignore all common words provided in the “stopWordsArray” variable.
        // Step 3 & 4 can be done in one step.
        ArrayList<String> stopWordsArrayList = new ArrayList<String>();
        
        for (int i = 0; i < stopWordsArray.length; i++) {
        	stopWordsArrayList.add(stopWordsArray[i]);
        }
        
        ArrayList<ArrayList<String>> processed_words = new ArrayList<ArrayList<String>>();

        for (int i = 0; i < words.size(); i++) {
        	ArrayList<String> eachLine = words.get(i);
        	ArrayList<String> newLine = new ArrayList<String>();
        	
        	for (int j = 0; j < eachLine.size(); j++) {
        		String word = eachLine.get(j).toLowerCase().trim();
        		
        		if (! stopWordsArrayList.contains(word)) {
        			newLine.add(word);
            	}
        	}
        	
        	processed_words.add(newLine);
        }
        
        // 5. Keep track of word frequencies.
        HashMap<String, Integer> wordFreq = new HashMap<String, Integer>();
        
        Integer[] indexes = this.getIndexes();
        
        for (int i = 0; i < indexes.length; i++) {
        	ArrayList<String> eachLine = processed_words.get(indexes[i]);
        	
        	for (int j = 0; j < eachLine.size(); j++) {
        		String word = eachLine.get(j);
        	
        		if (wordFreq.containsKey(word)) {
        			wordFreq.put(word, wordFreq.get(word) + 1);
        		} else {
        			wordFreq.put(word, 1);
        		}
        	}
        }
        
        // 6. Sort the list by frequency in a descending order.
        List<Map.Entry<String, Integer>> hashmapList = new LinkedList<Map.Entry<String, Integer>>(wordFreq.entrySet());
        
        Collections.sort(hashmapList, new Comparator<Map.Entry<String, Integer>>() {
        	public int compare(Map.Entry<String, Integer> left, Map.Entry<String, Integer> right) {
        		if (left.getValue() != right.getValue()) {
        			return right.getValue() - left.getValue();
        		}
        		
        		return left.getKey().compareTo(right.getKey());
        	}
        });
        
        
        // 7. Return the top 20 items from the sorted list as a String Array.
        for (int i = 0; i < 20; i++) {
        	// System.out.println(hashmapList.get(i).getKey() + "  " + hashmapList.get(i).getValue());
        	ret[i] = hashmapList.get(i).getKey();
        }
        
        return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
