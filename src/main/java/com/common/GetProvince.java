package com.common;

import com.dto.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * Created by JoeHuang on 2017/7/19.
 */
@Path("get_province")
public class GetProvince {
    @Autowired
    private JedisPool jedisPool;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entry> getProvince(@QueryParam("country") String country){
        List<Entry> list = new ArrayList<Entry>();
        if(country==null||!country.matches("^[0-9]{3,3}")){
            list.add(new Entry("error","parameter (country)'s value invalid,expect (str[regexp:^[0-9]{3,3}]), but get ("+country+"), see doc for more info."));
            return list;
        }
        try(Jedis jedis = jedisPool.getResource()){
            String s = "";
            for(int i=0;i<1000;i++){
                if(i<10){
                    s = "00";
                }else if(i<100){
                    s = "0";
                }else{
                    s = "";
                }
                String s1 = jedis.get("jersey_"+country+ s + i);
                if(s1!=null&&!s1.equals("null")&&!s1.equals("")){
                    list.add(new Entry(country+s + i,s1));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
