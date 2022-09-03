package com.atguigu.gmall.search.repository;

import com.atguigu.gmall.search.bean.Person;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends PagingAndSortingRepository<Person,Long> {

    // 查询指定地区的
    List<Person> findAllByAddressLike(String address);

    // 查询小于等于指定年龄的
    List<Person> findAllByAgeLessThanEqual(Integer age);

    // 查询大于指定年龄并且属于指定地区的
    List<Person> findAllByAgeGreaterThanAndAddress(Integer age,String address);

    @Query("{\n" +
            "    \"bool\": {\n" +
            "      \"must\": [\n" +
            "        {\n" +
            "          \"match\": {\n" +
            "            \"address\": \"西安市\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"range\": {\n" +
            "            \"age\": {\n" +
            "              \"gt\": 19\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      ],\n" +
            "      \"should\": [\n" +
            "        {\n" +
            "          \"match\": {\n" +
            "            \"id\": \"1\"\n" +
            "          }\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  }")
    List<Person> findAgeGreaterThanAndAddressOrIDThan(Integer age,String address,Long id);

}
