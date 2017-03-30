package com.gosun.core.utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.Assert;

import ognl.ObjectNullHandler;
import ognl.Ognl;
import ognl.OgnlRuntime;

/**
 * 列表工具类
 * @author Abe
 *
 */
public class ListUtils extends org.apache.commons.collections.ListUtils {
    
    /**
     * 把两个列表合并，也就是对两个列表做并集算且减掉重复的元素
     * @param <T> 类对象
     * @param srcList 源列表
     * @param addedList 需要添加的列表
     * @throws Exception
     * @return 合并后的列表
     */
    public static <T> List<T> merge(List<T> srcList, List<T> addedList) throws Exception {
        List<T> returnList = new ArrayList<T>();
        if(srcList == null || srcList.size() == 0){
            returnList = new ArrayList<T>();
            returnList.addAll(addedList);
        }else{
            returnList.addAll(srcList);
            if (addedList == null) {
                returnList.clear();
                return null;
            }
            if(returnList != null){
                Iterator<T> srcIterator = returnList.iterator();

                while (srcIterator.hasNext()) {
                    T element = srcIterator.next();
                    if (addedList.contains(element)) {
                        //srcIterator.remove();
                        addedList.remove(element);
                    } else {
                        //addedList.remove(element);
                        srcIterator.remove();
                    }
                }
            }
            returnList.addAll(addedList);
        }
        return returnList;
    }
    
    /**
     * 把<S>类型列表根据<S>类的属性转化为对应的<T>类的列表
     * @param <S> 源类型
     * @param <T> 目标类型
     * @param fromList 源列表
     * @param targetClass 目标类
     * @param fromPropertyName 源类型属性
     * @throws Exception
     * @return 目标类型列表
     */
    @SuppressWarnings("unchecked")
    public static <S,T> List<T>  listTypeConvert(final List<S> fromList,final Class<T> targetClass, final String fromPropertyName) throws Exception{
        return listTypeConvert(fromList, targetClass, fromPropertyName, null);
    }
    
    /**
     * 把<S>类型列表根据<S>类的属性转化为对应的<T>类的列表
     * @param <S> 源类型
     * @param <T> 目标类型
     * @param fromList 源列表
     * @param targetClass 目标类
     * @param fromPropertyName 源类型属性
     * @param toPropertyName 目标类型属性
     * @throws Exception
     * @return 目标类型列表
     */
    @SuppressWarnings("unchecked")
    public static <S,T> List<T>  listTypeConvert(final List<S> fromList,final Class<T> targetClass, final String fromPropertyName,String toPropertyName) throws Exception{
        List<T> returnList = new ArrayList<T>();
        T obj = null;
        if(fromList == null || fromList.isEmpty())return null;
        for (S s : fromList) {
            if(targetClass == Long.class){
                returnList.add((T)new Long(Ognl.getValue(fromPropertyName, s).toString()));
            }else if(toPropertyName == null){
                if(s.getClass() == Long.class){
                    obj = targetClass.newInstance();
                    Ognl.setValue("id", obj,s);
                    returnList.add(obj);
                }else
                    returnList.add((T)Ognl.getValue(fromPropertyName, s));
            }else{
                obj = targetClass.newInstance();
                Ognl.setValue(fromPropertyName, obj,Ognl.getValue(fromPropertyName, s));
                returnList.add(obj);
            }
                
        }
        return returnList;
    }
    
    /**
     * 把以,为token字符串如1,2,3,4转化为<T>类型属性为id的列表
     * @param <T> 目标类
     * @param srcValue 字符串,如1,2,3,4
     * @return 目标类型列表
     * @throws Exception
     */
    public static <T> List<T> stringListConvert(final Class<T> targetClass,String srcValue) throws Exception{
        return stringListConvert(targetClass,"id",srcValue,",");
    }
    
    /**
     * 把以,为token字符串如1,2,3,4转化为<T>类型的列表
     * @param <T> 目标类
     * @param targetClass 目标类
     * @param propertyName 目标类属性
     * @param srcValue 字符串,如1,2,3,4
     * @return 目标类型列表
     * @throws Exception
     */
    public static <T> List<T> stringListConvert(final Class<T> targetClass,String propertyName,String srcValue) throws Exception{
        return stringListConvert(targetClass,propertyName,srcValue,",");
    }
    
    /**
     * 把以,为token字符串如1,2,3,4转化为<T>类型的列表
     * @param <T> 目标类
     * @param targetClass 目标类
     * @param propertyName 目标类属性
     * @param srcValue 字符串,如1,2,3,4
     * @param delimiter token符号
     * @return 目标类型列表
     * @throws Exception
     */
    public static <T> List<T> stringListConvert(final Class<T> targetClass,final String propertyName,String srcValue,String delimiter) throws Exception{
        StringTokenizer stringTokenizer = new StringTokenizer(srcValue,delimiter);
        List<T> targetList = new ArrayList<T>();
        OgnlRuntime.setNullHandler(targetClass, new ObjectNullHandler());
        while(stringTokenizer.hasMoreTokens()){
            String srcObj = stringTokenizer.nextToken();
            T targetObj = targetClass.newInstance();
            if(!targetClass.equals(String.class))
                Ognl.setValue(propertyName, targetObj, srcObj);
            else
                targetObj = (T) srcObj;
            targetList.add(targetObj);
        }
        return targetList;
    }
    
    /**
     * 把以,为token字符串如1,2,3,4转化为<T>类型的列表
     * @param targetClass 目标类
     * @param srcString 字符串,如1,2,3,4
     * @param propertyName 目标类属性
     * @param otherProperties 其它的属性列表
     * @param delimiter token符号
     * @return 目标类型列表
     * @throws Exception
     */
    public static <T> List<T> stringListConvert(final Class<T> targetClass,String srcString,String propertyName,Map<String,?> otherProperties,String delimiter) throws Exception{
        StringTokenizer stringTokenizer = new StringTokenizer(srcString,delimiter);
        List<T> targetList = new ArrayList<T>();
        OgnlRuntime.setNullHandler(Object.class, new ObjectNullHandler());
        while(stringTokenizer.hasMoreTokens()){
            String srcObj = stringTokenizer.nextToken();
            T targetObj = targetClass.newInstance();
            Ognl.setValue(propertyName, targetObj, srcObj);
            for (Map.Entry<String, ?> entry : otherProperties.entrySet()) {
                    String expression = entry.getKey();
                    Ognl.setValue(expression,targetObj, entry.getValue());
            }
            targetList.add(targetObj);
        }
        return targetList;
    }
    
    /**
     * 把集合转为,号的字符串
     * @param collection
     * @param fieldName
     * @return 转化后的字符串
     * @throws Exception
     */
    public static <T> String getInString(final Collection<T> collection,String fieldName) throws Exception{
        Assert.notNull(collection, "collection不能为空");
//        Assert.notNull(fieldName, "fieldName不能为空");
        String inString = "";
        int i=1;
        for (T item : collection) {
            
            if(i == collection.size()){
                if(fieldName == null){
                    inString += item;
                }else
                    inString += PropertyUtils.getProperty(item, fieldName);
            }else{
                if(fieldName == null){
                    inString += item+",";
                }else
                    inString += PropertyUtils.getProperty(item, fieldName)+",";
            }
            i++;
        }
        return inString;
    }
}
