import annotations.After;
import annotations.Before;
import annotations.ExpectedException;
import annotations.Test;

public class AnnotatedTestClass {

    @Before
    public void beforeTest() {
        System.out.println("before test");
    }

    @After
    public void afterTest() {
        System.out.println("after test");
    }

    @Test
    public void test1() {
        System.out.println("test 1");
    }

    @Test
    public void test2() {
        System.out.println("test 2");
    }

    @Test
    public void test3() {
        System.out.println("test 3");
    }

    @Test
    @ExpectedException(RuntimeException.class)
    public void test5() {
        throw new NullPointerException();
    }

    @Test
    public void test6() {
        System.out.println("test 6");
    }

    @Test
    public void test4() {
        System.out.println("test 4");
    }
}
