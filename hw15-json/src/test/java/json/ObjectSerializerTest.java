package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import json.object.Address;
import json.object.Status;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectSerializerTest {
    private final Gson gson = new Gson();

    @Test
    void testPrimitives() {
        assertEqualResult(null);
        assertEqualResult(1);
        assertEqualResult(1L);
        assertEqualResult(new Short((short)2));
        assertEqualResult(new Byte((byte) 1));
        assertEqualResult(2.3);
        assertEqualResult(new Character('a'));
        assertEqualResult(true);
        assertEqualResult(false);
        assertEqualResult("abc");
    }

    @Test
    void testArrays() {
        var size = 10;

        var arInt = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++){
            arInt[i] = random.nextInt();
        }
        assertEqualResult(arInt);

        var arStr = new String[size];
        for (int i = 0; i < size; i++){
            arStr[i] = "test";
        }
        assertEqualResult(arStr);
    }

    @Test
    void testMap() {
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put("one", 1);
        stringIntegerHashMap.put("two", 2);
        stringIntegerHashMap.put("three", 3);
        stringIntegerHashMap.put("four", 212389223);
        assertEqualResult(stringIntegerHashMap);

    }

    @Test
    void testCollection() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("abc");
        strings.add("def");
        strings.add("ghi");
        strings.add("jkl");
        strings.add("mno");
        assertEqualResult(strings);
    }

    @Test
    void testObjectExcludeNull() {
        Status status1 = Status.builder().statusId(1).changedBy("Admin").build();
        Status status2 = Status.builder().statusId(2).changedBy("User").build();
        Status status3 = Status.builder().statusId(3).changedBy("QA").build();

        ArrayList<Status> statuses = new ArrayList<>(Arrays.asList(status1, status2, status3));

        Address addr = Address.builder()
                .id(123)
                .type(45)
                .name("default address")
                .company("private")
                .country("Russia")
                .city("Ukhta")
                .zipCode("123123")
                .street("Lenina 1")
                .phone("123123123123")
                .email("bWhite@gmail.com")
                .createdBy(1)
                .statuses(statuses)
                .subStatuses(new ArrayList<Integer>(Arrays.asList(1,2,3,4)))
                .build();

        assertEqualResult(addr);
    }

    @Test
    void testObjectIncludeNull() {
        Status status1 = Status.builder().statusId(1).changedBy("Admin").build();
        Status status2 = Status.builder().statusId(2).changedBy("User").build();
        Status status3 = Status.builder().statusId(3).changedBy("QA").build();

        ArrayList<Status> statuses = new ArrayList<>(Arrays.asList(status1, status2, status3));

        Address addr = Address.builder()
                .id(123)
                .type(45)
                .name("default address")
                .company("private")
                .country("Russia")
                .city("Ukhta")
                .zipCode("123123")
                .email("bWhite@gmail.com")
                .createdBy(1)
                .statuses(statuses)
                .subStatuses(new ArrayList<Integer>(Arrays.asList(1,2,3,4)))
                .build();

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();

        assertThat(ObjectSerializer.serialize(addr, new ObjectSerializer.Config(false)))
                .isEqualTo(gson.toJson(addr));
    }



    private void assertEqualResult(Object object) {
        assertThat(ObjectSerializer.serialize(object, null)).isEqualTo(gson.toJson(object));
    }
}