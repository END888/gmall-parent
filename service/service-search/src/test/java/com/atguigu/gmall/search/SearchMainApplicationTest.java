package com.atguigu.gmall.search;

import com.atguigu.gmall.search.bean.Person;
import com.atguigu.gmall.search.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class SearchMainApplicationTest {

    @Autowired
    PersonRepository personRepository;

    @Test
    void test01(){

        Person person1 = new Person();
        person1.setId(1L);
        person1.setFirstName("张");
        person1.setLastName("三");
        person1.setAge(18);
        person1.setAddress("西安市雁塔区");
        personRepository.save(person1);

        Person person2 = new Person();
        person2.setId(2L);
        person2.setFirstName("李");
        person2.setLastName("四");
        person2.setAge(19);
        person2.setAddress("西安市雁塔区");
        personRepository.save(person2);

        Person person3 = new Person();
        person3.setId(3L);
        person3.setFirstName("王");
        person3.setLastName("五");
        person3.setAge(20);
        person3.setAddress("西安市高新区");
        personRepository.save(person3);


        Person person4 = new Person();
        person4.setId(4L);
        person4.setFirstName("王");
        person4.setLastName("子");
        person4.setAge(21);
        person4.setAddress("西安市未央区");
        personRepository.save(person4);

        Person person5 = new Person();
        person5.setId(5L);
        person5.setFirstName("赵");
        person5.setLastName("云");
        person5.setAge(19);
        person5.setAddress("商洛市洛南县");
        personRepository.save(person5);



    }

    @Test
    void test02(){
        Optional<Person> p2 = personRepository.findById(2L);
        System.out.println("p2 = " + p2);
        System.out.println("-----------------");

        // 1、查询 address 在西安市的人
        List<Person> list1 = personRepository.findAllByAddressLike("西安市");
        list1.forEach(System.out::println);

        // 2、查询年龄小于等于 19 的人
        List<Person> list2 = personRepository.findAllByAgeLessThanEqual(19);
        list2.forEach(System.out::println);

        // 3、查询年龄大于 18 且在西安市的人
        List<Person> list3 = personRepository.findAllByAgeGreaterThanAndAddress(18, "西安市");
        list3.forEach(System.out::println);

        System.out.println("---------------------------------");
        // 4、查询年龄大于 20 且在西安市的人或 id = 1 的人
        List<Person> list4 = personRepository.findAgeGreaterThanAndAddressOrIDThan(20, "西安", 1L);
        list4.forEach(System.out::println);
    }
}
