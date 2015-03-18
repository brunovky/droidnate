package com.brunooliveira.droidnate.ws.request;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.brunooliveira.droidnate.ws.annotations.Consumes;
import com.brunooliveira.droidnate.ws.annotations.DELETE;
import com.brunooliveira.droidnate.ws.annotations.GET;
import com.brunooliveira.droidnate.ws.annotations.POST;
import com.brunooliveira.droidnate.ws.annotations.PUT;
import com.brunooliveira.droidnate.ws.annotations.Produces;
import com.brunooliveira.droidnate.ws.annotations.ResourcePath;
import com.brunooliveira.droidnate.ws.enums.MediaType;
import com.brunooliveira.droidnate.ws.enums.RequestType;
import com.brunooliveira.droidnate.ws.response.IResponseHandler;
import com.brunooliveira.droidnate.ws.response.JSONResponseHandler;
import com.brunooliveira.droidnate.ws.response.ListModelResponseHandler;
import com.brunooliveira.droidnate.ws.response.ModelResponseHandler;
import com.brunooliveira.droidnate.ws.response.SimpleResponseHandler;
import com.brunooliveira.droidnate.ws.response.StringResponseHandler;
import com.brunooliveira.droidnate.ws.response.ToastResponseHandler;
import com.google.gson.JsonObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

	private Context context;
	private Object entity;
	private Map<String, Object> params;
	private boolean waitingDialog;
	private String url;
	private MediaType produces;
	private MediaType consumes;
	private RequestType type;
	private Class<?> returnType;
    private String resourcePath;
    private boolean parallel;

	public Request(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public boolean isWaitingDialog() {
		return waitingDialog;
	}

	public void setWaitingDialog(boolean waitingDialog) {
		this.waitingDialog = waitingDialog;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public void addParam(String key, Object value) {
		if (params == null) params = new HashMap<String, Object>();
		this.params.put(key, value);
	}

    public void setParallel(boolean parallel) {
        this.parallel = parallel;
    }

    public String getUrl() {
		return url;
	}

	public MediaType getProduces() {
		return produces;
	}

	public MediaType getConsumes() {
		return consumes;
	}

	public RequestType getType() {
		return type;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	@SuppressWarnings("unchecked")
	public <T> void execute(IResponseHandler<T> handler) {
		try {
			initVariables();
			switch (type) {
			case GET:
				if (handler instanceof ModelResponseHandler) {
					ModelRequestTask<T> task = new ModelRequestTask<T>(this);
					task.setModelHandler((ModelResponseHandler<T>) handler);
					task.execute();
				} else if (handler instanceof ListModelResponseHandler) {
					ListModelRequestTask<T> task = new ListModelRequestTask<T>(this);
					task.setModelHandler((ListModelResponseHandler<T>) handler);
					task.execute();
				} else if (handler instanceof JSONResponseHandler) {
					JSONRequestTask<T> task = new JSONRequestTask<T>(this);
					task.setJsonHandler((JSONResponseHandler) handler);
					task.execute();
				} else if (handler instanceof ToastResponseHandler) {
					ToastNoEntityRequestTask task = new ToastNoEntityRequestTask(this);
					task.setToastHandler((ToastResponseHandler) handler);
					task.execute();
				} else if (handler instanceof StringResponseHandler) {
                    StringRequestTask task = new StringRequestTask(this);
                    task.setStringHandler((StringResponseHandler) handler);
                    task.execute();
                } else if (handler instanceof SimpleResponseHandler) {
                    NoReturnsRequestTask<T> task = new NoReturnsRequestTask<T>(this);
                    task.setSimpleHandler((SimpleResponseHandler) handler);
                    task.execute();
                }
				break;
			case POST:
			case PUT:
				if (handler instanceof ModelResponseHandler) {
					ModelRequestTask<T> task = new ModelRequestTask<T>(this);
					task.setModelHandler((ModelResponseHandler<T>) handler);
					task.execute((T) entity);
				} else if (handler instanceof JSONResponseHandler) {
					JSONRequestTask<T> task = new JSONRequestTask<T>(this);
					task.setJsonHandler((JSONResponseHandler) handler);
					task.execute((T) entity);
				} else if (handler instanceof ToastResponseHandler) {
					ToastWithEntityRequestTask<T> task = new ToastWithEntityRequestTask<T>(this);
					task.setToastHandler((ToastResponseHandler) handler);
					task.execute((T) entity);
				} else if (handler instanceof SimpleResponseHandler) {
					NoReturnsRequestTask<T> task = new NoReturnsRequestTask<T>(this);
					task.setSimpleHandler((SimpleResponseHandler) handler);
					task.execute((T) entity);
				} else if (handler instanceof StringResponseHandler) {
                    StringRequestTask task = new StringRequestTask(this);
                    task.setStringHandler((StringResponseHandler) handler);
                    task.execute((T) entity);
                }
				break;
			case DELETE:
				if (handler instanceof JSONResponseHandler) {
					JSONRequestTask<T> task = new JSONRequestTask<T>(this);
					task.setJsonHandler((JSONResponseHandler) handler);
					task.execute();
				} else if (handler instanceof ToastResponseHandler) {
					ToastNoEntityRequestTask task = new ToastNoEntityRequestTask(this);
					task.setToastHandler((ToastResponseHandler) handler);
					task.execute();
				} else if (handler instanceof SimpleResponseHandler) {
					DeleteNoReturnsRequestTask task = new DeleteNoReturnsRequestTask(this);
					task.setSimpleHandler((SimpleResponseHandler) handler);
					task.execute();
				}
			}
		} catch (Exception e) {
			Log.e("Droidnate Error", Log.getStackTraceString(e));
            //LogUtils.writeLine(context, "download", "ERROR", Log.getStackTraceString(e));
        }
	}

    public <T> void waitForResponse(IResponseHandler<T> handler) {
        try {
            initVariables();
            switch (type) {
                case GET:
                    if (handler instanceof ModelResponseHandler) {
                        ModelRequestTask<T> task = new ModelRequestTask<T>(this);
                        T response = parallel ? task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get() : task.execute().get();
                        if (response != null) handler.onSuccess(response);
                        else handler.onFailure("");
                    } else if (handler instanceof ListModelResponseHandler) {
                        ListModelRequestTask<T> task = new ListModelRequestTask<T>(this);
                        List<T> response = task.execute().get();
                        if (response != null) ((ListModelResponseHandler) handler).onSuccess(response);
                        else handler.onFailure("");
                    } else if (handler instanceof JSONResponseHandler) {
                        JSONRequestTask<T> task = new JSONRequestTask<T>(this);
                        JsonObject response = task.execute().get();
                        if (response != null) ((JSONResponseHandler) handler).onSuccess(response);
                        else handler.onFailure("");
                    } else if (handler instanceof ToastResponseHandler) {
                        ToastNoEntityRequestTask task = new ToastNoEntityRequestTask(this);
                        Void response = task.execute().get();
                        ((ToastResponseHandler) handler).onSuccess(response);
                    } else if (handler instanceof StringResponseHandler) {
                        StringRequestTask task = new StringRequestTask(this);
                        String response = (String) task.execute().get();
                        if (response != null) ((StringResponseHandler) handler).onSuccess(response);
                        else handler.onFailure("");
                    } else if (handler instanceof SimpleResponseHandler) {
                        NoReturnsRequestTask<T> task = new NoReturnsRequestTask<T>(this);
                        Void response = task.execute().get();
                        ((SimpleResponseHandler) handler).onSuccess(response);
                    }
                    break;
                case POST:
                case PUT:
                    if (handler instanceof ModelResponseHandler) {
                        ModelRequestTask<T> task = new ModelRequestTask<T>(this);
                        T response = task.execute((T) entity).get();
                        if (response != null) handler.onSuccess(response);
                        else handler.onFailure("");
                    } else if (handler instanceof JSONResponseHandler) {
                        JSONRequestTask<T> task = new JSONRequestTask<T>(this);
                        JsonObject response = task.execute((T) entity).get();
                        if (response != null) ((JSONResponseHandler) handler).onSuccess(response);
                        else handler.onFailure("");
                    } else if (handler instanceof ToastResponseHandler) {
                        ToastWithEntityRequestTask<T> task = new ToastWithEntityRequestTask<T>(this);
                        Void response = task.execute((T) entity).get();
                        ((ToastResponseHandler) handler).onSuccess(response);
                    } else if (handler instanceof SimpleResponseHandler) {
                        NoReturnsRequestTask<T> task = new NoReturnsRequestTask<T>(this);
                        Void response = task.execute((T) entity).get();
                        ((SimpleResponseHandler) handler).onSuccess(response);
                    } else if (handler instanceof StringResponseHandler) {
                        StringRequestTask task = new StringRequestTask(this);
                        String response = (String) task.execute((T) entity).get();
                        if (response != null) ((StringResponseHandler) handler).onSuccess(response);
                        else handler.onFailure("");
                    }
                    break;
                case DELETE:
                    if (handler instanceof JSONResponseHandler) {
                        JSONRequestTask<T> task = new JSONRequestTask<T>(this);
                        JsonObject response = task.execute().get();
                        if (response != null) ((JSONResponseHandler) handler).onSuccess(response);
                        else handler.onFailure("");
                    } else if (handler instanceof ToastResponseHandler) {
                        ToastNoEntityRequestTask task = new ToastNoEntityRequestTask(this);
                        Void response = task.execute().get();
                        ((ToastResponseHandler) handler).onSuccess(response);
                    } else if (handler instanceof SimpleResponseHandler) {
                        DeleteNoReturnsRequestTask task = new DeleteNoReturnsRequestTask(this);
                        Void response = task.execute().get();
                        ((SimpleResponseHandler) handler).onSuccess(response);
                    }
            }
        } catch (Exception e) {
            Log.e("Droidnate Error", Log.getStackTraceString(e));
            //LogUtils.writeLine(context, "download", "ERROR", Log.getStackTraceString(e));
        }
    }

	private void initVariables() throws ClassNotFoundException, NoSuchMethodException {
		Method method = getCallerMethod();
		Annotation[] annotations = method.getDeclaredAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().equals(GET.class)) {
				this.url = ((GET) annotation).value().equals("") ? method.getName() : ((GET) annotation).value();
				this.returnType = ((GET) annotation).returnType();
				this.type = RequestType.GET;
			} else if (annotation.annotationType().equals(POST.class)) {
				this.url = ((POST) annotation).value().equals("") ? method.getName() : ((POST) annotation).value();
				this.type = RequestType.POST;
			} else if (annotation.annotationType().equals(PUT.class)) {
				this.url = ((PUT) annotation).value().equals("") ? method.getName() : ((PUT) annotation).value();
				this.type = RequestType.PUT;
			} else if (annotation.annotationType().equals(DELETE.class)) {
				this.url = ((DELETE) annotation).value().equals("") ? method.getName() : ((DELETE) annotation).value();
				this.type = RequestType.DELETE;
			}
			if (annotation.annotationType().equals(Consumes.class)) {
				this.consumes = ((Consumes) annotation).value();
			}
			if (annotation.annotationType().equals(Produces.class)) {
				this.produces = ((Produces) annotation).value();
			}
		}
		if (this.consumes == null) this.consumes = MediaType.JSON;
		if (this.produces == null) this.produces = MediaType.JSON;
	}

	private Method getCallerMethod() throws ClassNotFoundException {
		StackTraceElement[] stackTraces = new Throwable().fillInStackTrace().getStackTrace();
        int index = -1;
        for (int i = 0 ; i < stackTraces.length ; i++) {
            if ((stackTraces[i].getMethodName().equals("execute") || stackTraces[i].getMethodName().equals("waitForResponse")) && stackTraces[i].getClassName().equals(Request.class.getName())) {
                index = i;
                break;
            }
        }
        if (index == -1) return null;
        StackTraceElement stackTrace = stackTraces[index + 1];
		Class<?> clazz = Class.forName(stackTrace.getClassName());
		for (Method m : clazz.getDeclaredMethods()) {
			if (m.getName().equals(stackTrace.getMethodName())) { return m; }
		}
		return null;
	}

	public String getResourcePath() {
        if (resourcePath == null) {
            try {
                Method method = getCallerMethod();
                Class<?> clazz = method.getDeclaringClass();
                if (clazz != null && clazz.isAnnotationPresent(ResourcePath.class)) resourcePath = clazz.getAnnotation(ResourcePath.class).value();
            } catch (Exception e) {
                e.printStackTrace();
                //LogUtils.writeLine(context, "download", "ERROR", Log.getStackTraceString(e));
            }
        }
        return resourcePath;
	}

}