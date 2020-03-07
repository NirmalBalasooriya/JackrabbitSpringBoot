package com.nirmal.JackrabbitSpringBoot.app;

/**
 *
 * @author Nirmal Balasooriya
 *
 * @param <T>
 */
public class Response<T> {
    private String code;
    private String desc;
    private T t;
    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }
    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
    /**
     * @return the t
     */
    public T getT() {
        return t;
    }
    /**
     * @param t the t to set
     */
    public void setT(T t) {
        this.t = t;
    }
    /**
     * @param code
     * @param desc
     * @param t
     */
    public Response(String code, String desc, T t) {
        super();
        this.code = code;
        this.desc = desc;
        this.t = t;
    }

}
