package orm.repository;

public interface Repository {
    <T> void create(T objectData);
    <T> void update(T objectData) throws IllegalAccessException;
    <T> T loadById(long id, Class<T> clazz);
}
