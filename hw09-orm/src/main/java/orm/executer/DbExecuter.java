package orm.executer;

import lombok.SneakyThrows;
import org.springframework.util.ReflectionUtils;
import orm.repository.Repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.HashMap;

public class DbExecuter implements Repository {
    private Connection connection;
    private HashMap<String, ClassMetaInfo> metaInfoCache = new HashMap<>();

    public DbExecuter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <T> void create(T objectData) {
        ClassMetaInfo metaInfo = getMetaInfo(objectData);
        try (PreparedStatement pst = metaInfo.buildInsertStatement(objectData)) {
            Savepoint savePoint = this.connection.setSavepoint("beforeInsert");
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

    @SneakyThrows
    @Override
    public <T> void update(T objectData) {
        ClassMetaInfo metaInfo = getMetaInfo(objectData);
        Long id = (long) metaInfo.getIdField().get(objectData);


        try (PreparedStatement pst = metaInfo.buildUpdateStatement(objectData, id.intValue())) {
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
        try (PreparedStatement pst = classMetaInfo.buildSelectStatement()) {
            pst.setInt(1, (int) id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    try {
                        final Constructor<T> constructor = clazz.getDeclaredConstructor();
                        T newInstance = constructor.newInstance();
                        for (Field field: classMetaInfo.getFields()) {
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
            ClassMetaInfo newMetaInfo = new ClassMetaInfo(objectData, connection);
            metaInfoCache.put(className, newMetaInfo);

            return newMetaInfo;
        }

        return metaInfoCache.get(className);
    }


}
