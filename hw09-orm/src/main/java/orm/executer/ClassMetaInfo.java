package orm.executer;

import lombok.SneakyThrows;
import org.springframework.util.ReflectionUtils;
import orm.annotation.Id;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class ClassMetaInfo {
    private final Connection connection;
    private final List<Field> fields;
    private Field idField;
    private final String updateQuery;
    private final String insertQuery;
    private final String selectQuery;

    @SneakyThrows
    <T> ClassMetaInfo(T objectData, Connection connection) {
        this.connection = connection;
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
        this.updateQuery = createUpdateQuery(tableName, this.idField.getName(), this.fields);
        this.selectQuery = createSelectQuery(tableName, this.idField.getName());
        this.insertQuery = createInsertQuery(tableName, this.fields);
    }

    private String createUpdateQuery(String tableName, String idField, List<Field> fields) {
        String changedFields = fields.stream()
                .map(f -> String.format("%s = ?", f.getName()))
                .collect(Collectors.joining(","));

        return String.format("UPDATE %s SET %s WHERE %s = ?", tableName, changedFields, idField);
    }

    private String createInsertQuery(String tableName, List<Field> fields) {
        String set = fields.stream().map(Field::getName).collect(Collectors.joining(","));
        String values = fields.stream().map(f -> "?").collect(Collectors.joining(","));

        return String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, set, values);
    }

    private String createSelectQuery(String tableName, String idField) {
        return String.format("SELECT * FROM %s WHERE %s = ?", tableName, idField);
    }

    @SneakyThrows
    PreparedStatement buildSelectStatement() {
        return connection.prepareStatement(this.selectQuery);
    }

    @SneakyThrows
    PreparedStatement buildUpdateStatement(Object data, int id) {
        PreparedStatement preparedStatement = connection.prepareStatement(this.updateQuery);
        int i;
        for (i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            field.setAccessible(true);
            preparedStatement.setObject(i + 1, field.get(data));
        }
        preparedStatement.setInt(i + 1, id);

        return preparedStatement;
    }

    @SneakyThrows
    PreparedStatement buildInsertStatement(Object data) {
        PreparedStatement preparedStatement = connection.prepareStatement(this.insertQuery);
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            field.setAccessible(true);
            preparedStatement.setObject(i + 1, field.get(data));
        }

        return preparedStatement;
    }

    Field getIdField() {
        return idField;
    }

    List<Field> getFields() {
        return fields;
    }
}
