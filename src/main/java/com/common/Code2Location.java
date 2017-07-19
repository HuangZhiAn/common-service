package com.common;

import com.dto.Entry;
import org.jvnet.hk2.annotations.Service;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JoeHuang on 2017/7/18.
 */
@Path("code_to_location")
public class Code2Location {

    @Autowired
    private JedisPool jedisPool;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entry> code2Location(@QueryParam("codes") String codes){
        System.out.println(codes);
        String[] split = codes.split(",");
        if(jedisPool==null){
            System.out.println("true");
        }
        Jedis jedis = jedisPool.getResource();
        List<Entry> list = new ArrayList<Entry>();
        for (String code:split) {
            String s = jedis.get("jersey_" + code);
            list.add(new Entry(code,s));
        }
        return list;
    }
}
