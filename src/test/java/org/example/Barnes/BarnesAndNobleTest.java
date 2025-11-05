package org.example.Barnes;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;

class BarnesAndNobleTest {

    /**
     *         <Specification-Based Testing>
     *
     *         1.) Understand the Requirements
     *
     *             The getPriceForCart() method is responsible for calculating the total
     *             price of books in a shopping cart at Barnes & Noble.
     *
     *             Input/Output Types:
     *
     *             Input: The method takes a Map<String, Integer> called order where the
     *             keys represent the ISBN of a books, and the value is the quantity
     *             of that book ordered.
     *
     *             INPUT - getPriceForCart() : Map<String, Integer> order
     *
     *             Output: The method returns a PurchaseSummary object called purchasesummary.
     *             This object has an int called totalPrice which holds the entire price of
     *             the cart. The second parameter is a Map<Book, Integer> called unavailable that
     *             has a Book object as a key, and the value is the quantity of unavailable copies.
     *             Example: If the user wants 3 books, but we only have 2, then we order them
     *             the 2 books, and the other 1 is flagged as unavailable
     *             (which the purchase summary records)
     *
     *             OUTPUT - getPriceForCart() --> PurchaseSummary purchasesummary
     *                                            PurchaseSummary : int totalPrice;
     *                                                              Map<Book, Integer> unavailable
     *
     *         2.) Partitioning
     *
     *         a. Single Input Partitions
     *
     *         order - String : null | empty | char | multiple characters | Valid ISBN
     *         order - Integer: null | negative | 0 | positive
     *
     *         b. Combination Partitions
     *         String is null and Integer is null, negative, 0, or positive
     *         String is empty and Integer is null, negative, 0, or positive
     *         String is a char and Integer is null, negative, 0, or positive
     *         String is multiple characters and Integer is null, negative, 0, or positive
     *         String is a valid ISBN and Integer is null, negative, 0, or positive
     *
     *         String is null, empty, a char, multi-char, or a valid ISBN and Integer is null
     *         String is null, empty, a char, multi-char, or a valid ISBN and Integer is negative
     *         String is null, empty, a char, multi-char, or a valid ISBN and Integer is 0
     *         String is null, empty, a char, multi-char, or a valid ISBN and Integer is positive
     *
     *         Order is null
     *
     *         c. Output Partitions
     *         getPriceForCart() --> PurchaseSummary object --> Map<String, Integer> unavailable
     *                                                          int totalPrice
     *
     *         PurchaseSummary has >0 totalPrice and no unavailable books
     *         PurchaseSummary has >0 totalPrice and 1 or more unavailable books
     *         PurchaseSummary has 0 totalPrice and has no unavailable books
     *         PurchaseSummary has 0 totalPrice and has 1 or more unavailable books
     *         The function returns null
     *
     *         3.) Create Test Cases
     *         - In Map<String, Integer> order, if order is null --> return null
     *
     *         - In Map<String, Integer> order, if order is empty -->
     *           return PurchaseSummary : totalPrice = 0, unavailable = {}
     *
     *         - In Map<String, Integer> order, if String, Integer, or both are null --> NPE
     *
     *         - In Map<String, Integer> order, if String is valid ISBN and Integer is 0 -->
     *           return PurchaseSummary : totalPrice = 0, unavailable = {}
     *
     *         - In Map<String, Integer> order, if String is valid ISBN and Integer is >0 -->
     *           return PurchaseSummary : totalPrice = 20, unavailable = {}
     *
     *         - In Map<String, Integer> order, if String is valid ISBN and Integer is less
     *          than the amount of books available --> return PurchaseSummary : totalPrice = 20, unavailable = {}
     *          return PurchaseSummary : totalPrice = 58, unavailable = {"HarryPotter": 1, "PercyJackson": 5}
     *
     *          - If the books have the same ISBN --> return True
     *
     *          - If the book that is being compared is null or a different class --> return false
     *
     *          - If the books point to the same reference --> return True
     */
    @Test
    @DisplayName("Specification-Based")
    /**
     *
     *  In Map<String, Integer> order, if order is null --> return null
     */
    void orderNullCheck() {
        Map<String, Integer> orderTest = new HashMap<>();

        orderTest = null;

        BookDatabase bd = mock(BookDatabase.class);
        BuyBookProcess bbp = mock(BuyBookProcess.class);

        BarnesAndNoble bn = new BarnesAndNoble(bd, bbp);

        assertNull(bn.getPriceForCart(null));
    }

    @Test
    @DisplayName("Specification-Based")
    /**
     *
     *  In Map<String, Integer> order, if order is empty -->
     *  return PurchaseSummary : totalPrice = 0, unavailable = {}
     *
     */
    void emptyOrderCheck() {
        Map<String, Integer> order = new HashMap<>();

        BookDatabase bd = mock(BookDatabase.class);
        BuyBookProcess bbp = mock(BuyBookProcess.class);

        BarnesAndNoble bn = new BarnesAndNoble(bd, bbp);

        PurchaseSummary ps = bn.getPriceForCart(order);

        assertTrue(ps.getUnavailable().isEmpty());
        assertEquals(0, ps.getTotalPrice());
    }

    @Test
    @DisplayName("Specification-Based")
    /**
     *
     *  In Map<String, Integer> order, if String, Integer, or both are null -->
     *  throw NullPointerException
     *
     */
    void NPECheck() {
        Map<String, Integer> order1 = new HashMap<>();
        Map<String, Integer> order2 = new HashMap<>();
        Map<String, Integer> order3 = new HashMap<>();

        BookDatabase bd = mock(BookDatabase.class);
        BuyBookProcess bbp = mock(BuyBookProcess.class);

        order1.put(null, null);
        order2.put(null, 3);
        order3.put("exampleISBN", null);

        BarnesAndNoble bn = new BarnesAndNoble(bd, bbp);

        //PurchaseSummary ps = bn.getPriceForCart(orderTest1);

        assertThrows(NullPointerException.class, () -> {
            bn.getPriceForCart(order1);
            bn.getPriceForCart(order2);
            bn.getPriceForCart(order3);

        });
    }

    @Test
    @DisplayName("Specification-Based")
    /**
     *
     *  In Map<String, Integer> order, if String is valid ISBN and Integer is 0 -->
     *  return PurchaseSummary : totalPrice = 0, unavailable = {}
     *
     */
    void validBookZeroIntCheck() {
        Map<String, Integer> order = new HashMap<>();

        BookDatabase bd = mock(BookDatabase.class);
        BuyBookProcess bbp = mock(BuyBookProcess.class);

        order.put("validISBN", 0);
        order.put("validISBN2", 0);

        Book HarryPotter = new Book("validISBN", 5, 3);
        Book PercyJackson = new Book("validISBN2", 10, 8);

        //when we try and find the book validISBN, we get a HarryPotter book object
        when(bd.findByISBN("validISBN")).thenReturn(HarryPotter);
        when(bd.findByISBN("validISBN2")).thenReturn(PercyJackson);

        BarnesAndNoble bn = new BarnesAndNoble(bd, bbp);
        PurchaseSummary ps = bn.getPriceForCart(order);

        assertEquals(0, ps.getTotalPrice());

    }

    @Test
    @DisplayName("Specification-Based")
    /**
     *
     *  In Map<String, Integer> order, if String is valid ISBN and Integer is >0 -->
     *  return PurchaseSummary : totalPrice = 20, unavailable = {}
     *
     */
    void validBookIntCheck() {
        Map<String, Integer> order = new HashMap<>();

        BookDatabase bd = mock(BookDatabase.class);
        BuyBookProcess bbp = mock(BuyBookProcess.class);

        order.put("validISBN", 5);
        order.put("validISBN2", 2);

        Book HarryPotter = new Book("validISBN", 2, 6);
        Book PercyJackson = new Book("validISBN2", 10, 3);

        //when we try and find the book validISBN, we get a HarryPotter book object
        when(bd.findByISBN("validISBN")).thenReturn(HarryPotter);
        when(bd.findByISBN("validISBN2")).thenReturn(PercyJackson);

        BarnesAndNoble bn = new BarnesAndNoble(bd, bbp);
        PurchaseSummary ps = bn.getPriceForCart(order);

        assertEquals(30, ps.getTotalPrice());

    }

    @Test
    @DisplayName("Specification-Based")
    /**
     *
     *  In Map<String, Integer> order, if String is valid ISBN and Integer is less
     *  than the amount of books available --> return PurchaseSummary : totalPrice = 20, unavailable = {}
     *  return PurchaseSummary : totalPrice = 58, unavailable = {"HarryPotter": 1, "PercyJackson": 5}
     *
     */
    void unavailableBooksCheck() {
        Map<String, Integer> order = new HashMap<>();

        BookDatabase bd = mock(BookDatabase.class);
        BuyBookProcess bbp = mock(BuyBookProcess.class);

        order.put("validISBN", 5);
        order.put("validISBN2", 10);

        Book HarryPotter = new Book("validISBN", 2, 4);
        Book PercyJackson = new Book("validISBN2", 10, 5);

        //when we try and find the book validISBN, we get a HarryPotter book object
        when(bd.findByISBN("validISBN")).thenReturn(HarryPotter);
        when(bd.findByISBN("validISBN2")).thenReturn(PercyJackson);

        BarnesAndNoble bn = new BarnesAndNoble(bd, bbp);
        PurchaseSummary ps = bn.getPriceForCart(order);
        Map<Book, Integer> unavailableBooks = ps.getUnavailable();

        assertEquals(1, unavailableBooks.get(HarryPotter));
        assertEquals(5, unavailableBooks.get(PercyJackson));
        assertEquals(58, ps.getTotalPrice());

    }

    @Test
    @DisplayName("Structural-Based")
    /**
     *
     *  If two books are equal --> return true
     *
     */
    void booksEqualCheck() {
        Book HarryPotter = new Book("validISBN", 2, 4);
        Book HarryPotter2 = new Book("validISBN", 2, 4);


        assertTrue(HarryPotter.equals(HarryPotter2));

    }

    @Test
    @DisplayName("Structural-Based")
    /**
     *
     * If the book that is being compared is null or a different class --> return false
     *- If the books point to the same reference --> return True
     */
    void booksNotEqualCheck() {
        BookDatabase bd = mock(BookDatabase.class);
        BuyBookProcess bbp = mock(BuyBookProcess.class);

        Book HarryPotter = new Book("validISBN1", 2, 4);
        Book PercyJackson = null;
        BarnesAndNoble bn = new BarnesAndNoble(bd,bbp);


        assertFalse(HarryPotter.equals(PercyJackson));
        assertFalse(HarryPotter.equals(bn));
    }

    @Test
    @DisplayName("Structural-Based")
    /**
     *
     *
     *   If the books point to the same reference --> return True
     */
    void booksSameReferenceCheck() {
        Book HarryPotter = new Book("validISBN", 2, 4);

        Book x = HarryPotter;
        Book y = HarryPotter;

        assertTrue(x.equals(y));


    }

}