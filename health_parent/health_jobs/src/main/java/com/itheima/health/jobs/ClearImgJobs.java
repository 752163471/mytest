package com.itheima.health.jobs;

import com.itheima.health.constant.RedisConstant;
import com.itheima.health.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * 定时任务：清理垃圾图片
 */
public class ClearImgJobs {

   @Autowired
   private JedisPool jedisPool;
   //清理垃圾图片
   public void ClearImg(){
      //计算redis中两个集合的差值，获取垃圾图片名称集合
      Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);

      Iterator<String> iterator = set.iterator();

      while (iterator.hasNext()) {
         String pic = iterator.next();
         System.out.println("垃圾文件：" + pic);
         //清理图片服务器的垃圾文件
         QiniuUtils.deleteFileFromQiniu(pic);
         //清理redis中的垃圾文件
         jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,pic);
      }
   }
}
