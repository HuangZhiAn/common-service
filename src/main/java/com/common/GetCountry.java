package com.common;

import com.dto.Entry;
import com.service.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * Created by JoeHuang on 2017/7/19.
 */
@Path("get_country")
public class GetCountry {
    @Autowired
    private JedisPool jedisPool;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entry> getCountry(@QueryParam("capital") String capital, @Context HttpServletRequest request){
        List<Entry> list = new ArrayList<Entry>();
        //限制每个IP地址的访问次数
        RateLimiter rateLimiter = new RateLimiter();
        if(!rateLimiter.access(request.getRemoteAddr())){
            list.add(new Entry("error","Access too frequently"));
            return list;
        }
        if(capital!=null&&!capital.matches("^[A-Za-z]")){
            list.add(new Entry("error","parameter (capital)'s value invalid,expect (str[regexp:^[A-Za-z]]), but get ("+capital+"), see doc for more info."));
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
                    s="";
                }
                String s1 = jedis.get("jersey_"+s+ i);
                if(s1!=null&&!s1.equals("null")&&!s1.equals("")&&(capital==null||capital.equals("")||s1.startsWith(capital))){
                    list.add(new Entry(s + i,s1));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
