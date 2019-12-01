package orm.executer;

import org.springframework.util.ReflectionUtils;
import orm.annotation.Id;
import orm.repository.Repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class DbExecuter implements Repository {
    private Connection connection;
    private HashMap<String, Optional<ClassMetaInfo>> metaInfoCache = new HashMap<>();

    public DbExecuter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <T> void create(T objectData) {
        ClassMetaInfo metaInfo = getMetaInfo(objectData);
        String sql = buildInsert(metaInfo, objectData);

        try (CallableStatement insert = connection.prepareCall(sql)) {
            Savepoint savePoint = this.connection.setSavepoint("beforeInsert");
            try {
                insert.executeUpdate();
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback(savePoint);
                System.out.println(ex.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> void update(T objectData) {
        ClassMetaInfo metaInfo = new ClassMetaInfo(objectData);
        Object o = loadById(metaInfo.idValue, objectData.getClass());

        StringBuilder updateBuilder = buildUpdate(objectData, metaInfo, o);

        try (PreparedStatement pst = connection.prepareStatement(updateBuilder.toString())) {
            pst.setInt(1, (int) metaInfo.idValue);
            Savepoint savePoint = this.connection.setSavepoint("beforeUpdate");
            try {
                pst.executeUpdate();
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback(savePoint);
                System.out.println(ex.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T loadById(long id, Class<T> clazz) {
        ClassMetaInfo classMetaInfo = metaInfoCache.get(clazz.getName()).get();

        StringBuilder selectBuilder = buildSelect(clazz, classMetaInfo);

        try (PreparedStatement pst = connection.prepareStatement(selectBuilder.toString())) {
            pst.setInt(1, (int) id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    try {
                        final Constructor<T> constructor = clazz.getDeclaredConstructor();
                        T newInstance = constructor.newInstance();
                        for (Field field: classMetaInfo.fields) {
                            field.setAccessible(true);
                            Object object = rs.getObject(field.getName());
                            ReflectionUtils.setField(field, newInstance, object);
                        }
                        return newInstance;
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        throw new RuntimeException("Cannot create instance of " + clazz.getName());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private <T> ClassMetaInfo getMetaInfo(T objectData) {
        String className = objectData.getClass().getName();
        if (!metaInfoCache.containsKey(className)) {
            ClassMetaInfo newMetaInfo = new ClassMetaInfo(objectData);
            metaInfoCache.put(className, Optional.of(newMetaInfo));

            return newMetaInfo;
        }

        return metaInfoCache.get(className).get();
    }

    private static class ClassMetaInfo {
        private final String name;
        private final List<Field> fields;
        private String idField;
        private long idValue;

        <T> ClassMetaInfo(T objectData) {
            Class<?> clazz = objectData.getClass();
            ArrayList<Field> fields = new ArrayList<>();
            ReflectionUtils.doWithFields(
                    clazz,
                    f ->  {
                        if (f.isAnnotationPresent(Id.class)) {
                            idField = f.getName();
                            f.setAccessible(true);
                            idValue = (long) f.get(objectData);
                        }

                        fields.add(f);
                    }
            );
            this.name = clazz.getSimpleName().toLowerCase();
            this.fields = fields;
            if (idField.isEmpty()) {
                throw new RuntimeException("Class should have Id annotation");
            }
        }
    }

    private <T> String buildInsert(ClassMetaInfo metaInfo, T objectData) {
        StringBuilder sqlBuilder = new StringBuilder("insert into ").append(metaInfo.name).append("(");
        sqlBuilder.append(metaInfo.fields.stream().map(Field::getName).collect(Collectors.joining(",")));
        sqlBuilder.append(") values (");
        for (Field field: metaInfo.fields) {
            field.setAccessible(true);
            sqlBuilder.append("'").append(ReflectionUtils.getField(field, objectData).toString()).append("',");
        }
        sqlBuilder.setLength(sqlBuilder.length() - 1);
        sqlBuilder.append(")");

        return sqlBuilder.toString();
    }

    private <T> StringBuilder buildSelect(Class<T> clazz, ClassMetaInfo classMetaInfo) {
        String selectFrom = clazz.getSimpleName().toLowerCase();
        StringBuilder selectBuilder = new StringBuilder("select * from ");
        selectBuilder.append(selectFrom).append(" where ").append(classMetaInfo.idField).append(" = ?");
        return selectBuilder;
    }

    private <T> StringBuilder buildUpdate(T objectData, ClassMetaInfo metaInfo, Object o) {
        String changedFields = metaInfo.fields.stream()
                .filter(f -> ReflectionUtils.getField(f, objectData) != ReflectionUtils.getField(f, o))
                .map(f -> f.getName() + "= '" + ReflectionUtils.getField(f, objectData) + "'")
                .collect(Collectors.joining(","));


        var updateBuilder = new StringBuilder("update ");
        updateBuilder
                .append(metaInfo.name)
                .append(" ")
                .append("SET ").append(changedFields)
                .append(" where ").append(metaInfo.idField).append(" = ?");
        return updateBuilder;
    }
}
