package com.brunooliveira.droidnate.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.brunooliveira.droidnate.annotations.Column;
import com.brunooliveira.droidnate.annotations.Entity;
import com.brunooliveira.droidnate.annotations.ForeignKey;
import com.brunooliveira.droidnate.annotations.NotNull;
import com.brunooliveira.droidnate.exception.DroidnateException;
import com.brunooliveira.droidnate.helper.DatabaseHelper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Class responsible for performing basic database operations (insert, update, delete and select).
 * 
 * <p>Use the parameterization class to identify the type of information that will be managed by the class. For example: if you want to manage information of a {@code User} class, use {@code DAO<User>}.
 * <br><br>
 * 
 * @author	Bruno Oliveira
 * @see		DatabaseHelper
 * @since	v.1
 *
 */
public class DAO<T> {

	private static SQLiteDatabase db;
	private Context context;
	private Class<T> clazz;

	/**
	 * Constructs a new data management using the Data Access Object design pattern.
	 * @param context to use to open or create the database
	 * @param clazz to use to identify the parameterization type class
	 */
	public DAO(Context context, Class<T> clazz) {
		if (db == null) db = new DatabaseHelper(context).getDatabase();
		this.context = context;
		this.clazz = clazz;
	}

	/**
	 * 
	 * @param entity
	 * @return
	 * @throws DroidnateException
	 */
	public long insert(T entity) throws DroidnateException {
		ContentValues values = new ContentValues();
		for (Field field : entity.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if (!field.isAnnotationPresent(Column.class) || !field.getAnnotation(Column.class).generatedValue()) {
				try {
					putValues(entity, field, values);
				} catch (IllegalAccessException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (IllegalArgumentException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (NoSuchFieldException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				}
			}
		}
		String tableName;
		if (entity.getClass().isAnnotationPresent(Entity.class)) {
			tableName = entity.getClass().getAnnotation(Entity.class).name().equals("") ? entity.getClass().getSimpleName() : entity.getClass().getAnnotation(Entity.class).name();
			return db.insert(tableName, null, values);
		}
		return -1;
	}

	/**
	 * 
	 * @param entity
	 * @param where
	 * @param whereArgs
	 * @return
	 * @throws DroidnateException
	 */
	public int update(T entity, String where, String[] whereArgs) throws DroidnateException {
		ContentValues values = new ContentValues();
		for (Field field : entity.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if (!field.isAnnotationPresent(Column.class) || !field.getAnnotation(Column.class).generatedValue()) {
				try {
					putValues(entity, field, values);
				} catch (IllegalAccessException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (IllegalArgumentException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (NoSuchFieldException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				}
			}
		}
		String tableName;
		if (entity.getClass().isAnnotationPresent(Entity.class)) {
			tableName = entity.getClass().getAnnotation(Entity.class).name().equals("") ? entity.getClass().getSimpleName() : entity.getClass().getAnnotation(Entity.class).name();
			return db.update(tableName, values, where, whereArgs);
		}
		return -1;
	}

	/**
	 * 
	 * @param where
	 * @param whereArgs
	 * @return
	 */
	public int delete(String where, String[] whereArgs) {
		String tableName;
		Class<T> entityClass = clazz;
		if (entityClass.isAnnotationPresent(Entity.class)) {
			tableName = entityClass.getAnnotation(Entity.class).name().equals("") ? entityClass.getSimpleName() : entityClass.getAnnotation(Entity.class).name();
			return db.delete(tableName, where, whereArgs);
		}
		return -1;
	}

	/**
	 * 
	 * @param where
	 * @param whereArgs
	 * @return
	 * @throws DroidnateException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T select(String where, String[] whereArgs) throws DroidnateException {
		String tableName;
		Class<T> entityClass = clazz;
		if (entityClass.isAnnotationPresent(Entity.class)) {
			tableName = entityClass.getAnnotation(Entity.class).name().equals("") ? entityClass.getSimpleName() : entityClass.getAnnotation(Entity.class).name();
			Cursor c = db.query(tableName, getColumns(entityClass), where, whereArgs, null, null, null);
			c.moveToFirst();
			if (c.getCount() > 0) {
				Object obj;
				try {
					obj = entityClass.newInstance();
					int count = 0;
					for (Field field : obj.getClass().getDeclaredFields()) {
						field.setAccessible(true);
						if (field.isAnnotationPresent(ForeignKey.class)) {
							DAO<?> dao = new DAO(context, field.getType());
							field.set(obj, dao.select((field.getAnnotation(ForeignKey.class).objectField().equals("") ? "id" : field.getAnnotation(ForeignKey.class).objectField()) + "=?",
									new String[] { String.valueOf(c.getLong(count)) }));
						} else setField(obj, c, count, field);
						count++;
					}
					c.close();
					return (T) obj;
				} catch (InstantiationException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (IllegalAccessException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (IllegalArgumentException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (ParseException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				}
			}
			c.close();
			return null;
		}
		return null;
	}

	/**
	 * 
	 * @param where
	 * @param whereArgs
	 * @param orderBy
	 * @return
	 * @throws DroidnateException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> select(String where, String[] whereArgs, String orderBy) throws DroidnateException {
		String tableName;
		Class<T> entityClass = clazz;
		if (entityClass.isAnnotationPresent(Entity.class)) {
			tableName = entityClass.getAnnotation(Entity.class).name().equals("") ? entityClass.getSimpleName() : entityClass.getAnnotation(Entity.class).name();
			Cursor c = db.query(tableName, getColumns(entityClass), where, whereArgs, null, null, null);
			c.moveToFirst();
			if (c.getCount() > 0) {
				try {
					List<T> objs = new ArrayList<T>();
					do {
						Object obj = entityClass.newInstance();
						int count = 0;
						for (Field field : obj.getClass().getDeclaredFields()) {
							field.setAccessible(true);
							if (field.isAnnotationPresent(ForeignKey.class)) {
								DAO<?> dao = new DAO(context, field.getType());
								field.set(obj, dao.select((field.getAnnotation(ForeignKey.class).objectField().equals("") ? "id" : field.getAnnotation(ForeignKey.class).objectField()) + "=?",
										new String[] { String.valueOf(c.getLong(count)) }));
							} else setField(obj, c, count, field);
							count++;
						}
						objs.add((T) obj);
					} while (c.moveToNext());
					c.close();
					return objs;
				} catch (InstantiationException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (IllegalAccessException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (IllegalArgumentException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (ParseException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				}
			}
			c.close();
			return null;
		}
		return null;
	}

	/**
	 * 
	 * @param where
	 * @param whereArgs
	 * @param orderBy
	 * @param offset
	 * @param limit
	 * @return
	 * @throws DroidnateException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> select(String where, String[] whereArgs, String orderBy, int offset, int limit) throws DroidnateException {
		String tableName;
		Class<T> entityClass = clazz;
		if (entityClass.isAnnotationPresent(Entity.class)) {
			tableName = entityClass.getAnnotation(Entity.class).name().equals("") ? entityClass.getSimpleName() : entityClass.getAnnotation(Entity.class).name();
			Cursor c = db.query(tableName, getColumns(entityClass), where, whereArgs, null, null, null);
			c.moveToPosition(offset);
			if (c.getCount() > 0) {
				try {
					List<T> objs = new ArrayList<T>();
					int i = 1;
					do {
						Object obj = entityClass.newInstance();
						int count = 0;
						for (Field field : obj.getClass().getDeclaredFields()) {
							field.setAccessible(true);
							if (field.isAnnotationPresent(ForeignKey.class)) {
								DAO<?> dao = new DAO(context, field.getType());
								field.set(obj, dao.select((field.getAnnotation(ForeignKey.class).objectField().equals("") ? "id" : field.getAnnotation(ForeignKey.class).objectField()) + "=?",
										new String[] { String.valueOf(c.getLong(count)) }));
							} else setField(obj, c, count, field);
							count++;
						}
						objs.add((T) obj);
						i++;
					} while (c.moveToNext() && i <= limit);
					c.close();
					return objs;
				} catch (InstantiationException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (IllegalAccessException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (IllegalArgumentException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (ParseException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				}
			}
			c.close();
			return null;
		}
		return null;
	}

	/**
	 * 
	 * @param orderBy
	 * @return
	 * @throws DroidnateException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> selectAll(String orderBy) throws DroidnateException {
		String tableName;
		Class<T> entityClass = clazz;
		if (entityClass.isAnnotationPresent(Entity.class)) {
			tableName = entityClass.getAnnotation(Entity.class).name().equals("") ? entityClass.getSimpleName() : entityClass.getAnnotation(Entity.class).name();
			Cursor c = db.query(tableName, getColumns(entityClass), null, null, null, null, null);
			c.moveToFirst();
			if (c.getCount() > 0) {
				try {
					List<T> objs = new ArrayList<T>();
					do {
						Object obj = entityClass.newInstance();
						int count = 0;
						for (Field field : obj.getClass().getDeclaredFields()) {
							field.setAccessible(true);
							if (field.isAnnotationPresent(ForeignKey.class)) {
								DAO<?> dao = new DAO(context, field.getType());
								field.set(obj, dao.select((field.getAnnotation(ForeignKey.class).objectField().equals("") ? "id" : field.getAnnotation(ForeignKey.class).objectField()) + "=?",
										new String[] { String.valueOf(c.getLong(count)) }));
							} else setField(obj, c, count, field);
							count++;
						}
						objs.add((T) obj);
					} while (c.moveToNext());
					c.close();
					return objs;
				} catch (InstantiationException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (IllegalAccessException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (IllegalArgumentException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (ParseException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				}
			}
			c.close();
			return null;
		}
		return null;
	}

	/**
	 * 
	 * @param orderBy
	 * @param offset
	 * @param limit
	 * @return
	 * @throws DroidnateException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> selectAll(String orderBy, int offset, int limit) throws DroidnateException {
		String tableName;
		Class<T> entityClass = clazz;
		if (entityClass.isAnnotationPresent(Entity.class)) {
			tableName = entityClass.getAnnotation(Entity.class).name().equals("") ? entityClass.getSimpleName() : entityClass.getAnnotation(Entity.class).name();
			Cursor c = db.query(tableName, getColumns(entityClass), null, null, null, null, null);
			c.moveToPosition(offset);
			if (c.getCount() > 0) {
				try {
					List<T> objs = new ArrayList<T>();
					int i = 1;
					do {
						Object obj = entityClass.newInstance();
						int count = 0;
						for (Field field : obj.getClass().getDeclaredFields()) {
							field.setAccessible(true);
							if (field.isAnnotationPresent(ForeignKey.class)) {
								DAO<?> dao = new DAO(context, field.getType());
								field.set(obj, dao.select((field.getAnnotation(ForeignKey.class).objectField().equals("") ? "id" : field.getAnnotation(ForeignKey.class).objectField()) + "=?",
										new String[] { String.valueOf(c.getLong(count)) }));
							} else setField(obj, c, count, field);
							count++;
						}
						objs.add((T) obj);
						i++;
					} while (c.moveToNext() && i <= limit);
					c.close();
					return objs;
				} catch (InstantiationException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (IllegalAccessException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (IllegalArgumentException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				} catch (ParseException e) {
					throw new DroidnateException("Droidnate Error :: DAO", e);
				}
			}
			c.close();
			return null;
		}
		return null;
	}

	private String[] getColumns(Class<T> entityClass) {
		String[] columns = new String[entityClass.getDeclaredFields().length];
		int count = 0;
		for (Field field : entityClass.getDeclaredFields()) {
			field.setAccessible(true);
			if (field.isAnnotationPresent(ForeignKey.class)) columns[count] = field.getAnnotation(ForeignKey.class).fieldName().equals("") ? field.getName() + "_id" : field.getAnnotation(
					ForeignKey.class).fieldName();
			else if (field.isAnnotationPresent(Column.class)) columns[count] = !field.getAnnotation(Column.class).name().equals("") ? field.getAnnotation(Column.class).name() : field.getName();
			else columns[count] = field.getName();
			count++;
		}
		return columns;
	}

	private void putValues(T entity, Field field, ContentValues values) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException, DroidnateException {
		if (field.isAnnotationPresent(NotNull.class) && field.get(entity) == null) { throw new DroidnateException("Droidnate Error :: DAO", new Throwable(field.getAnnotation(NotNull.class)
				.errorMessage().equals("{0} can't be null") ? field.getAnnotation(NotNull.class).errorMessage().replace("{0}", field.getName()) : field.getAnnotation(NotNull.class).errorMessage())); }
		if (field.isAnnotationPresent(ForeignKey.class)) {
			ForeignKey fk = field.getAnnotation(ForeignKey.class);
			if (field.get(entity) != null) {
				Field childField = field.get(entity).getClass().getDeclaredField(fk.objectField());
				childField.setAccessible(true);
				values.put(fk.fieldName().equals("") ? field.getName() : fk.fieldName(), childField.get(field.get(entity)).toString());
			} else {
				values.putNull(fk.fieldName().equals("") ? field.getName() : fk.fieldName());
			}
		} else if (field.getType().equals(boolean.class)) {
			values.put(getFieldName(field), Boolean.parseBoolean(field.get(entity).toString()));
		} else if (field.getType().equals(byte.class)) {
			values.put(getFieldName(field), Byte.parseByte(field.get(entity).toString()));
		} else if (field.getType().equals(double.class)) {
			values.put(getFieldName(field), Double.parseDouble(field.get(entity).toString()));
		} else if (field.getType().equals(int.class)) {
			values.put(getFieldName(field), Integer.parseInt(field.get(entity).toString()));
		} else if (field.getType().equals(BigDecimal.class)) {
			values.put(getFieldName(field), String.valueOf(new BigDecimal(Double.parseDouble(field.get(entity).toString())).setScale(2, BigDecimal.ROUND_HALF_UP)));
		} else if (field.getType().equals(Calendar.class)) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
			sdf.applyPattern("yyyy-MM-dd");
			Date d = new Date(((Calendar) field.get(entity)).getTimeInMillis());
			values.put(getFieldName(field), sdf.format(d));
		} else if (field.getType().equals(Date.class)) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
			sdf.applyPattern("yyyy-MM-dd");
			values.put(getFieldName(field), sdf.format(field.get(entity)));
		} else {
			values.put(getFieldName(field), field.get(entity).toString());
		}
	}

	private void setField(Object object, Cursor c, int count, Field field) throws IllegalAccessException, IllegalArgumentException, ParseException {
		if (field.getType().equals(boolean.class)) {
			field.set(object, c.getInt(count) == 1);
		} else if (field.getType().equals(byte.class)) {
			field.set(object, Byte.parseByte(c.getString(count)));
		} else if (field.getType().equals(double.class)) {
			field.set(object, c.getDouble(count));
		} else if (field.getType().equals(BigDecimal.class)) {
			field.set(object, new BigDecimal(c.getDouble(count)).setScale(2, BigDecimal.ROUND_HALF_UP));
		} else if (field.getType().equals(int.class)) {
			field.set(object, c.getInt(count));
		} else if (field.getType().equals(Long.class)) {
			field.set(object, c.getLong(count));
		} else if (field.getType().equals(Calendar.class)) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
			sdf.applyPattern("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(c.getString(count)));
			field.set(object, cal);
		} else if (field.getType().equals(Date.class)) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
			sdf.applyPattern("yyyy-MM-dd");
			field.set(object, sdf.parse(c.getString(count)));
		} else {
			field.set(object, c.getString(count));
		}
	}

	private String getFieldName(Field field) {
		return field.isAnnotationPresent(Column.class) && !field.getAnnotation(Column.class).name().equals("") ? field.getAnnotation(Column.class).name() : field.getName();
	}

}