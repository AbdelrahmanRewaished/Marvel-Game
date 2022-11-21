package engine;

public class PriorityQueue {

    @SuppressWarnings("rawtypes")
private final Comparable[] elements;
   private int nItems;

    public PriorityQueue(int size){
        elements = new Comparable[size];
      nItems=0;
   }
    
   @SuppressWarnings({ "rawtypes", "unchecked" })
public void insert(Comparable item) {
      
      int i;
      for (i = nItems - 1; i >= 0 && item.compareTo(elements[i]) > 0; i--)
         elements[i + 1] = elements[i];

      elements[i + 1] = item;
      nItems++;
   }
    
   @SuppressWarnings("rawtypes")
public Comparable remove() {
      nItems--;
      return elements[nItems];
   }
    
   public boolean isEmpty() {
      return (nItems == 0);
   }

    @SuppressWarnings("rawtypes")
public Comparable peekMin() {
      return elements[nItems-1];
   }
     
   public int size() {
      return nItems;
   }
}
