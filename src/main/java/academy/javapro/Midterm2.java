package academy.javapro;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiFunction;

public class Midterm2 {

    /**
     * Simple Person class used for demonstration.
     */
    static class Person {
        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String toString() {
            return "Person{name='" + name + "', age=" + age + '}';
        }
    }

    /**
     * Merges two collections of different types into a new collection using a BiFunction.
     *
     * @param <T>              The type of elements in the first collection
     * @param <S>              The type of elements in the second collection
     * @param <R>              The type of elements in the resulting collection
     * @param firstCollection  The first collection containing elements of type T
     * @param secondCollection The second collection containing elements of type S
     * @param mergeFunction    The function that defines how to merge elements from both collections
     * @return A new collection containing the merged elements
     * @throws IllegalArgumentException if the collections have different sizes
     */
    public static <T, S, R> List<R> mergeCollections(
            Collection<T> firstCollection,
            Collection<S> secondCollection,
            BiFunction<T, S, R> mergeFunction) {

        // Check if the collections are of the same size
        if (firstCollection.size() != secondCollection.size()) {
            throw new IllegalArgumentException(
                    "Collections must have the same size for element-wise merging. " +
                            "First collection size: " + firstCollection.size() +
                            ", Second collection size: " + secondCollection.size());
        }

        // Create a new list to hold the merged results
        
        List<R> result = new ArrayList<>(firstCollection.size());

        // Convert collections to arrays for easier access
        T[] firstArray = (T[]) firstCollection.toArray();
        S[] secondArray = (S[]) secondCollection.toArray();

        // Iterate through both arrays and apply the merge function
        for(int i = 0; i < firstArray.length; i++) {
            R mergedResult = mergeFunction.apply(firstArray[i], secondArray[i]);
            result.add(mergedResult);
        }

        return result;
        
    }

    /**
     * Alternative implementation that doesn't require equal-sized collections.
     * Instead, it pairs elements until one collection is exhausted.
     */
    public static <T, S, R> List<R> zipCollections(
            Collection<T> firstCollection,
            Collection<S> secondCollection,
            BiFunction<T, S, R> mergeFunction) {

        
        List<R> result = new ArrayList<>(Math.min(firstCollection.size(), secondCollection.size()));

        // Use iterators to traverse both collections simultaneously
        
        Iterator<T> firstIterator = firstCollection.iterator();
        Iterator<S> secondIterator = secondCollection.iterator();

        // Iterate until one of the collections is exhausted
        while(firstIterator.hasNext() && secondIterator.hasNext()) {
            T firstElement = firstIterator.next();
            S secondElement = secondIterator.next();
            R mergedResult = mergeFunction.apply(firstElement, secondElement);
            result.add(mergedResult);
        }

        return result;
        
    }

    public static void main(String[] args) {
        // Example 1: Merging numbers and strings
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<String> words = Arrays.asList("one", "two", "three", "four", "five");

        // Define a merge function that combines an integer and a string
        BiFunction<Integer, String, String> numberWordMerger =
                (num, word) -> num + " = " + word;

        List<String> combinedNumberWords = mergeCollections(numbers, words, numberWordMerger);

        System.out.println("Example 1: Merging numbers with their word representations");
        combinedNumberWords.forEach(System.out::println);

        // Example 2: Creating Person objects from names and ages
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        List<Integer> ages = Arrays.asList(25, 30, 22);

        // Define a merge function that creates a Person from name and age
        BiFunction<String, Integer, Person> personCreator = Person::new;

        List<Person> people = mergeCollections(names, ages, personCreator);

        System.out.println("\nExample 2: Creating Person objects from names and ages");
        people.forEach(System.out::println);

        // Example 3: Merging using mathematical operations
        List<Double> firstNumbers = Arrays.asList(1.5, 2.5, 3.5);
        List<Double> secondNumbers = Arrays.asList(0.5, 1.0, 1.5);

        // Several different merge functions
        BiFunction<Double, Double, Double> sum = Double::sum;
        BiFunction<Double, Double, Double> product = (a, b) -> a * b;
        BiFunction<Double, Double, String> comparisonResult =
                (a, b) -> a + " vs " + b + ": " + (a > b ? "First is larger" :
                        a < b ? "Second is larger" :
                                "Both are equal");

        List<Double> sums = zipCollections(firstNumbers, secondNumbers, sum);
        List<Double> products = zipCollections(firstNumbers, secondNumbers, product);
        List<String> comparisons = zipCollections(firstNumbers, secondNumbers, comparisonResult);

        System.out.println("\nExample 3: Mathematical operations");
        System.out.println("Sums: " + sums);
        System.out.println("Products: " + products);
        System.out.println("Comparisons:");
        comparisons.forEach(System.out::println);

        // Example 4: Using different sized collections with zipCollections
        List<Character> letters = Arrays.asList('A', 'B', 'C', 'D', 'E');
        List<Integer> positions = Arrays.asList(1, 2, 3); // Shorter list

        BiFunction<Character, Integer, String> positionedLetter =
                (letter, position) -> position + ". " + letter;

        List<String> lettersWithPositions = zipCollections(letters, positions, positionedLetter);

        System.out.println("\nExample 4: Using different sized collections");
        lettersWithPositions.forEach(System.out::println);
    }
}

