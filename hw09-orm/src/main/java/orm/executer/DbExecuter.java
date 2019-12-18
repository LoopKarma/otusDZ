package orm.executer;

import org.springframework.util.ReflectionUtils;
import orm.annotation.Id;
import orm.repository.Repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DbExecuter implements Repository {
    private Connection connection;
    private HashMap<String, ClassMetaInfo> metaInfoCache = new HashMap<>();

    public DbExecuter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <T> void create(T objectData) {
        ClassMetaInfo metaInfo = getMetaInfo(objectData);
        String insertSql = metaInfo.insertQuery.apply(objectData);

        try (CallableStatement insert = connection.prepareCall(insertSql)) {
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
    public <T> void update(T objectData) throws IllegalAccessException {
        ClassMetaInfo metaInfo = getMetaInfo(objectData);
        long id = (long) metaInfo.idField.get(objectData);

        String updateQuery = metaInfo.updateQuery.apply(objectData);

        try (PreparedStatement pst = connection.prepareStatement(updateQuery)) {
            pst.setInt(1, (int) id);
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
        ClassMetaInfo classMetaInfo = getMetaInfo(clazz);
        try (PreparedStatement pst = connection.prepareStatement(classMetaInfo.selectQuery)) {
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
                        throw new RuntimeException("Cannot create instance of " + clazz.getName() + " reason:" + e.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private <T> ClassMetaInfo getMetaInfo(T objectData) {
        String className;
        if (objectData instanceof Class) {
            className = ((Class) objectData).getName();
        } else {
            className = objectData.getClass().getName();
        }

        if (!metaInfoCache.containsKey(className)) {
            ClassMetaInfo newMetaInfo = new ClassMetaInfo(objectData);
            metaInfoCache.put(className, newMetaInfo);

            return newMetaInfo;
        }

        return metaInfoCache.get(className);
    }

    private static class ClassMetaInfo {
        private final List<Field> fields;
        private Field idField;
        private final Function<Object, String> updateQuery;
        private final Function<Object, String> insertQuery;
        private final String selectQuery;

        <T> ClassMetaInfo(T objectData) {
            Class<?> clazz = objectData.getClass();
            ArrayList<Field> fields = new ArrayList<>();
            ReflectionUtils.doWithFields(
                    clazz,
                    f ->  {
                        if (f.isAnnotationPresent(Id.class)) {
                            idField = f;
                            f.setAccessible(true);
                        }

                        fields.add(f);
                    }
            );
            String tableName = clazz.getSimpleName().toLowerCase();
            this.fields = fields;
            if (idField == null) {
                throw new RuntimeException("Class should have Id annotation");
            }
            this.updateQuery = createUpdateFunction(tableName, this.idField.getName(), this.fields);
            this.selectQuery = buildSelect(tableName, this.idField.getName());
            this.insertQuery = createInsertFunction(tableName, this.fields);
        }

        private Function<Object, String> createUpdateFunction(String tableName, String idField, List<Field> fields) {
            return objectData -> {
                String changedFields = fields.stream()
                        .map(f -> f.getName() + "= '" + ReflectionUtils.getField(f, objectData) + "'")
                        .collect(Collectors.joining(","));

                return "update " +
                        tableName +
                        " " +
                        "SET " +
                        changedFields +
                        " where " + idField + " = ?";
            };
        }

        private Function<Object, String> createInsertFunction(String tableName, List<Field> fields) {
            return objectData -> {
                StringBuilder sqlBuilder = new StringBuilder("insert into ").append(tableName).append("(");
                sqlBuilder.append(fields.stream().map(Field::getName).collect(Collectors.joining(",")));
                sqlBuilder.append(") values (");
                for (Field field: fields) {
                    field.setAccessible(true);
                    sqlBuilder.append("'").append(ReflectionUtils.getField(field, objectData).toString()).append("',");
                }
                sqlBuilder.setLength(sqlBuilder.length() - 1);
                sqlBuilder.append(")");

                return sqlBuilder.toString();
            };
        }

        private String buildSelect(String tableName, String idField) {
            return "select * from " + tableName + " where " + idField + " = ?";
        }
    }
}
