package com.service;

import com.dto.City;
import com.dto.Country;
import com.dto.Province;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import org.jvnet.hk2.annotations.Service;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * Created by JoeHuang on 2017/7/18.
 * TUDO 提供英文服务
 */
public class InitData {

    public static void main(String[] args){
        new InitData();
    }

    public InitData(){
        try {
            saveToRedis(getDatas(Country.class,"country.txt"),Country.class);
            saveToRedis(getDatas(Province.class,"province.txt"),Province.class);
            saveToRedis(getDatas(City.class,"city.txt"),City.class);
            System.out.println("数据初始化成功");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> getDatas(Class<T> c,String fileName) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Gson gson = new Gson();
        List list = null;
        try {
            URL url = InitData.class.getClassLoader().getResource(fileName);
            //URL路径有空格会变成%20，需要替换回去
            File file = new File(url.getFile().replaceAll("%20"," "));
            list = gson.fromJson(new JsonReader(new FileReader(file)), List.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<T> list1 = new ArrayList<T>();
        for (Object o: list) {
            LinkedTreeMap map1 = (LinkedTreeMap) o;
            Iterator iterator = map1.keySet().iterator();
            while (iterator.hasNext()){
                String key =(String) iterator.next();
                String value = (String) map1.get(key);
                T t = null;
                Constructor[] ctor = c.getDeclaredConstructors();
                //找到我们需要的构造方法
               for(int i=0;i<ctor.length;i++ ){
                         Class[] cl = ctor[i].getParameterTypes();
                       if(cl.length == 2){
                                //实例化对象
                               t = c.getConstructor(cl).newInstance(new Object[]{key,value});
                            }
                    }
                list1.add(t);
            }
        }
        return list1;
    }

    public <T> void saveToRedis(List<T> list,Class<T> c) throws InvocationTargetException, IllegalAccessException {
        Jedis jedis = new Jedis();
        for (T t:list) {
            Method[] declaredMethods = c.getDeclaredMethods();
            String code = null;
            String name = null;
            for (Method m:declaredMethods) {
                String methodName = m.getName();
                if(methodName.startsWith("get")&&methodName.endsWith("Code")){
                    code =(String) m.invoke(t);
                }else if(methodName.startsWith("get")&&methodName.endsWith("Name")){
                    name =(String) m.invoke(t);
                }
            }
            String s = jedis.get("jersey_"+code);
            if(s==null||!s.equals(name)){
                jedis.set("jersey_"+code,name);
                System.out.println("data change");
            }
        }
    }
}
