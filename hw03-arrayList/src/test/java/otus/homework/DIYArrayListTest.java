package otus.homework;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DIYArrayListTest {
    @Test
    void addAll() {
        DIYArrayList<Integer> first = new DIYArrayList<>();
        DIYArrayList<Integer> second = new DIYArrayList<>();

        int i = 0;
        int max = 40;

        for (;i < max; i++) {
            first.add(i);
            second.add(i);
        }

        assertEquals(max, first.size());
        assertEquals(max, second.size());

        Collections.addAll(first, second.toArray(Integer[]::new));
        assertEquals(max * 2, first.size());
    }

    @Test
    void copy() {
        int i = 0;
        int max = 40;

        DIYArrayList<Integer> even = new DIYArrayList<>();
        DIYArrayList<Integer> odd = new DIYArrayList<>(max);

        for (;i < max; i++) {
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
