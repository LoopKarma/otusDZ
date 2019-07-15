package otus.homework;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DIYArrayListTest {
    @Test
    void addAll() {
        int max = 40;
        DIYArrayList<Integer> first = new DIYArrayList<>();
        Integer[] second = new Integer[max];


        for (int i = 0; i < max; i++) {
            first.add(i);
            second[i] = i;
        }

        assertEquals(max, first.size());
        assertEquals(max, second.length);

        Collections.addAll(first, second);
        assertEquals(max * 2, first.size());
    }

    @Test
    void copy() {
        int max = 40;

        DIYArrayList<Integer> even = new DIYArrayList<>();
        DIYArrayList<Integer> odd = new DIYArrayList<>(max);

        for (int i = 0; i < max; i++) {
            if (i%2==0) {
                even.add(i);
            } else {
                odd.add(i);
            }
        }
        Collections.copy(odd, even);

        assertEquals(0, odd.get(0));
        assertEquals(2, odd.get(1));
        assertEquals(4, odd.get(2));
        assertEquals(6, odd.get(3));
    }

    @Test
    void sort() {
        int max = 40;

        DIYArrayList<Integer> list = new DIYArrayList<>();
        ArrayList<Integer> list1 = new ArrayList<>();

        for (int i = max;i >= 0; i--) {
            list.add(i);
        }

        Collections.sort(list);
        Collections.sort(list1);

        assertEquals(1, list.get(1));
    }
}
