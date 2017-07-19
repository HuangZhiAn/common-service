package com.common;

import com.dto.City;
import com.dto.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * Created by JoeHuang on 2017/7/18.
 */
@Path("get_city")
public class GetCity {
    @Autowired
    private JedisPool jedisPool;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entry> getCity(@QueryParam("province") String province){
        List<Entry> list = new ArrayList<Entry>();
        if(!province.matches("^[0-9]{6,6}")){
            Entry entry = new Entry();
            entry.setKey("error");
            entry.setValue("parameter (province)'s value invalid,expect (str[regexp:^[0-9]{6,6}]), but get ("+province+"), see doc for more info.");
            list.add(entry);
            return list;
        }
        Jedis jedis = jedisPool.getResource();
        String s = "";
        for(int i=0;i<1000;i++){
            if(i<10){
                s = "00";
            }else if(i<100){
                s = "0";
            }else{
                s = "";
            }
            String s1 = jedis.get("jersey_"+province+ s + i);
            if(s1!=null&&!s1.equals("null")&&!s1.equals("")){
                Entry entry = new Entry();
                entry.setKey(province+s+i);
                entry.setValue(s1);
                list.add(entry);
            }
        }
        return list;
    }
}
