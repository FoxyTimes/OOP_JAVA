import java.io.*;
import java.util.*;


public class Main {
    public static HashMap<String, Integer> sort(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> listToSort = new ArrayList<>(map.entrySet()); //sets
        listToSort.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        HashMap<String, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : listToSort) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static String countPercent(Map<String, Integer> statistic, int index, int allWords) {
        double temp = (double) (statistic.get(statistic.keySet().toArray()[index]) * 100) / allWords;
        return String.format(Locale.US, "%.2f", temp);
    }

    public static void writeToCsv(Map<String, Integer> statistic) {
        Writer writer = null;
        try {
            int allWords = 0;
            writer = new OutputStreamWriter(new FileOutputStream("output.csv"));
            for (int i = 0; i < statistic.size(); i++) {
                allWords += statistic.get(statistic.keySet().toArray()[i]);
            }
            for (int i=0; i<statistic.size(); i++) {
                writer.write(statistic.keySet().toArray()[i] + "," + statistic.get(statistic.keySet().toArray()[i]) + "," + countPercent(statistic, i, allWords) + "%" + "\n");
            }
        }
        catch (IOException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error while writing file:\n" + e.getLocalizedMessage());
        }
        finally {
            if (writer != null) {
                try {
                    writer.close();
                }
                catch (IOException | ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    public static void main(String[] args) {
        Reader reader = null;
        try
        {
            reader = new InputStreamReader(new FileInputStream(args[0]));
            StringBuilder builder = new StringBuilder();
            Map<String, Integer> statistic = new HashMap<>();
            while (reader.ready()) {
                int currentChar = reader.read();
                if (currentChar >= (int)'a' && currentChar <= (int)'z' || currentChar >= (int)'A' && currentChar <= (int)'Z' || currentChar >= (int)'0' && currentChar <= (int)'9' || currentChar >= (int)'а' && currentChar <= (int)'я' || currentChar >= (int)'А' && currentChar <= (int)'Я') {
                    builder.append((char)currentChar);
                }
                else {
                    if (!builder.isEmpty()) {
                        statistic.merge(builder.toString(), 1, Integer::sum);
                    }
                    builder.setLength(0);
                }
            }
            if (!builder.isEmpty()) {
                statistic.merge(builder.toString(), 1, Integer::sum);
            }

            Map<String, Integer> sortedStatistic = sort(statistic);
            writeToCsv(sortedStatistic);
        }
        catch (IOException | ArrayIndexOutOfBoundsException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            if (null != reader)
            {
                try
                {
                    reader.close();
                }
                catch (IOException | ArrayIndexOutOfBoundsException e)
                {
                    e.printStackTrace(System.err);
                }
            }
        }
    }
}