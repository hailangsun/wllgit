package com.wll.demo.testplus.mapper;

import com.wll.demo.testplus.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {


    @Resource
    private UserMapper mapper;

    @Test
    public void aInsert() {
        User user = new User();
        user.setName("小羊");
        user.setId(1232);
        Assert.assertTrue(mapper.insert(user) > 0);
        // 成功直接拿会写的 ID
        System.err.println("\n插入成功 ID 为：" + user.getId());
    }

    //
//    @Test
//    public void bDelete() {
//        Assert.assertTrue(mapper.deleteById(3L) > 0);
//        Assert.assertTrue(mapper.delete(new QueryWrapper<User>()
//                .lambda().eq(User::getUsername, "Sandy")) > 0);
//    }
//
//
    @Test
    public void cUpdate() {
        Assert.assertTrue(mapper.updateById(new User().setId(1232).setName("云鹏").setAge(22).setEmail("hhhhhh@163")) > 0);
//        Assert.assertTrue(mapper.update(new User().setUsername("云鹏小吉吉"),
//                new UpdateWrapper<User>().lambda()
//                        .set(User::getAge, 3)
//                        .eq(User::getId, 2)) > 0);
    }


//    @Test
//    public void dSelect() {
//        Assert.assertEquals("ab@c.c", mapper.selectById(1232).getUsername());
////        User user = mapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getId, 2));
////        Assert.assertEquals("mp", user.getName());
////        Assert.assertTrue(3 == user.getAge());
//    }


}