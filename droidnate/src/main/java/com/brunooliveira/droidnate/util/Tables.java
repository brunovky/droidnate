package com.brunooliveira.droidnate.util;

import com.brunooliveira.droidnate.annotations.Column;
import com.brunooliveira.droidnate.annotations.Decimal;
import com.brunooliveira.droidnate.annotations.Entity;
import com.brunooliveira.droidnate.annotations.Except;
import com.brunooliveira.droidnate.annotations.ForeignKey;
import com.brunooliveira.droidnate.annotations.NotNull;
import com.brunooliveira.droidnate.annotations.PrimaryKey;
import com.brunooliveira.droidnate.annotations.Temporal;
import com.brunooliveira.droidnate.exception.DroidnateException;
import com.brunooliveira.droidnate.helper.DatabaseHelper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bruno on 20/03/2015.
 */
public final class Tables {

    public static int ON_CREATE = 1;
    public static int ON_UPDATE = 2;

    public static void create(Class clazz, int type) throws DroidnateException {
        if (type > ON_UPDATE || type < ON_CREATE) throw new DroidnateException(Tables.class.getSimpleName(), "The type is invalid.");
        StringBuilder sqlBuilder = createSQL(clazz);
        if (type == ON_CREATE) DatabaseHelper.addSqlToBeExecuted(sqlBuilder.toString());
        else DatabaseHelper.addSqlToBeUpdated(sqlBuilder.toString());
    }

    public static void alter(Class clazz, String sql) throws DroidnateException {
        if (!clazz.isAnnotationPresent(Entity.class)) throw new DroidnateException(Tables.class.getSimpleName(), "The class [" + clazz.getSimpleName() + "] isn't an entity. Please, use @Entity annotation in this class.");
        StringBuilder sqlBuilder = new StringBuilder("ALTER TABLE " + getEntityName(clazz) + " " + sql);
        DatabaseHelper.addSqlToBeUpdated(sqlBuilder.toString());
    }

    private static StringBuilder createSQL(Class clazz) throws DroidnateException {
        if (!clazz.isAnnotationPresent(Entity.class)) throw new DroidnateException(Tables.class.getSimpleName(), "The class [" + clazz.getSimpleName() + "] isn't an entity. Please, use @Entity annotation in this class.");
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE " + getEntityName(clazz) + "(\n");
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Except.class)) {
                field.setAccessible(true);
                sqlBuilder.append(getColumnName(field)).append(" ").append(getColumnType(field));
                if (field.isAnnotationPresent(PrimaryKey.class)) {
                    sqlBuilder.append(" PRIMARY KEY");
                    if (field.getAnnotation(PrimaryKey.class).autoIncrement()) sqlBuilder.append(" AUTOINCREMENT");
                    sqlBuilder.append(",\n");
                }
                else if (field.isAnnotationPresent(ForeignKey.class)) {
                    sqlBuilder.append(" ");
                    if (field.isAnnotationPresent(NotNull.class)) sqlBuilder.append("NOT NULL ");
                    sqlBuilder.append("REFERENCES ").append(getFKTableName(field)).append("(").append(getFKName(field)).append("),\n");
                } else if (field.isAnnotationPresent(NotNull.class)) sqlBuilder.append(" NOT NULL,\n");
                if (!sqlBuilder.toString().endsWith(",\n")) sqlBuilder.append(",\n");
            }
        }
        sqlBuilder = new StringBuilder(sqlBuilder.toString().substring(0, sqlBuilder.toString().lastIndexOf(",\n")));
        sqlBuilder.append("\n)");
        return sqlBuilder;
    }

    private static String getEntityName(Class clazz) {
        String name = ((Entity) clazz.getAnnotation(Entity.class)).name();
        return name.equals("") ? clazz.getSimpleName() : name;
    }

    private static String getColumnName(Field field) {
        if (!field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(ForeignKey.class)) return field.getName();
        if (field.isAnnotationPresent(ForeignKey.class)) return field.getAnnotation(ForeignKey.class).fieldName();
        Column column = field.getAnnotation(Column.class);
        if (column.name().equals("")) return field.getName();
        return column.name();
    }

    private static String getColumnType(Field field) throws DroidnateException {
        if (field.isAnnotationPresent(ForeignKey.class)) {
            Field fkField = getFKField(field);
            return getColumnType(fkField);
        }
        if (field.getType().equals(String.class)) {
            String type = "VARCHAR(";
            if (!field.isAnnotationPresent(Column.class)) return type + "200" + ")";
            Column column = field.getAnnotation(Column.class);
            return type + column.length() + ")";
        }
        if (field.getType().equals(int.class) || field.getType().equals(Integer.class) ||
                field.getType().equals(Long.class) || field.getType().equals(long.class)) return "INTEGER";
        if (field.getType().equals(double.class) || field.getType().equals(Double.class) || field.getType().equals(BigDecimal.class)) {
            String type = "DECIMAL(";
            if (!field.isAnnotationPresent(Decimal.class)) return type + "8,2" + ")";
            Decimal decimal = field.getAnnotation(Decimal.class);
            return type + decimal.precision() + "," + decimal.scale() + ")";
        }
        if (field.getType().equals(char.class) || field.getType().equals(Character.class)) {
            String type = "CHAR(";
            if (!field.isAnnotationPresent(Column.class)) return type + "200" + ")";
            Column column = field.getAnnotation(Column.class);
            return type + column.length() + ")";
        }
        if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) return "BOOLEAN";
        if (field.getType().equals(byte[].class)) return "BLOB";
        if (field.getType().equals(Date.class) || field.getType().equals(Calendar.class)) {
            if (!field.isAnnotationPresent(Temporal.class)) return "DATE";
            Temporal temporal = field.getAnnotation(Temporal.class);
            return temporal.type().toString();
        }
        throw new DroidnateException(Tables.class.getSimpleName(), "The field [" + field.getName() + "] don't have correct type.");
    }

    private static String getFKTableName(Field field) throws DroidnateException {
        Class fkClass = field.getType();
        if (!fkClass.isAnnotationPresent(Entity.class)) throw new DroidnateException(Tables.class.getSimpleName(), "The class [" + fkClass.getSimpleName() + "] isn't an entity. Please, use @Entity annotation in this class.");
        Entity entity = (Entity) fkClass.getAnnotation(Entity.class);
        return entity.name().equals("") ? fkClass.getName() : entity.name();
    }

    private static String getFKName(Field field) throws DroidnateException {
        return getColumnName(getFKField(field));
    }

    private static Field getFKField(Field field) throws DroidnateException {
        Class fkClass = field.getType();
        if (!fkClass.isAnnotationPresent(Entity.class)) throw new DroidnateException(Tables.class.getSimpleName(), "The class [" + fkClass.getSimpleName() + "] isn't an entity. Please, use @Entity annotation in this class.");
        for (Field fkField : fkClass.getDeclaredFields()) {
            if (fkField.getName().equals(field.getAnnotation(ForeignKey.class).objectField())) return fkField;
        }
        throw new DroidnateException(Tables.class.getSimpleName(), "The object field [" + field.getAnnotation(ForeignKey.class).objectField() + "] don't exist in the class [" + fkClass.getSimpleName() + "].");
    }

}